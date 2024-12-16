package com.example.Notice_reminder.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import com.example.Notice_reminder.dto.MemberDTO;
import com.example.Notice_reminder.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor //MemberService에 대한 멤버를 사용 가능
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/member/signup")    // name값을 requestparam에 담아온다
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.signup");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);

        return "redirect:/member/login";
    }

    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/member/login";
    }

    @GetMapping("/member/admin")
    public String findAll(Model model) {
        // 어떠한 html로 가져갈 데이터가 있다면 model 사용
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "admin";
    }

    @GetMapping("/member/main")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/member/info")
    public String getMemberInfo(Model model, Principal principal) {
        String memberEmail = principal.getName(); // 이메일 (username 필드)

        // 사용자 객체에서 추가 정보를 가져오는 로직 (서비스 호출)
        MemberDTO memberDTO = memberService.findByEmail(memberEmail); // 이메일로 조회
        Long memberId = memberDTO.getId();
        String memberName = memberDTO.getMemberName();

        // 모델에 데이터 추가
        model.addAttribute("memberId", memberId);
        model.addAttribute("memberEmail", memberEmail);
        model.addAttribute("memberName", memberName);

        return "info";
    }

    @PostMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id, Principal principal){
        memberService.deleteByid(id);
        if(principal.getName().equals("root"))
            return "redirect:/member/admin";
        return "redirect:/member/login";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 비밀번호 변경 페이지 반환 (GET 요청)
    @GetMapping("/member/password")
    public String showPasswordChangePage() {
        return "password"; // password.html 반환
    }

    // 비밀번호 변경 처리 (POST 요청)
    @PostMapping("/member/password/change")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        String memberEmail = principal.getName();

        boolean isChanged = memberService.changePassword(memberEmail, currentPassword, newPassword);

        if (isChanged) {
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/member/main";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "기존 비밀번호와 동일합니다. 변경할 비밀번호를 입력해주세요!");
            return "redirect:/member/password";
        }
    }
}
//MemberController.class