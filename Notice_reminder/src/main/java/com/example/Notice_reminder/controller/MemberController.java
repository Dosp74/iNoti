package com.example.Notice_reminder.controller;

import com.example.Notice_reminder.dto.PasswordDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import com.example.Notice_reminder.dto.MemberDTO;
import com.example.Notice_reminder.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
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
    public String adminForm(Model model) {
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

    @GetMapping("/member/password")
    public String passwordForm() {
        return "password";
    }

    @PostMapping("/member/password")
    public String updatePassword(@ModelAttribute PasswordDTO passwordDTO, Principal principal, RedirectAttributes redirectAttributes){
        if(!passwordDTO.getConfirmPassword().equals(passwordDTO.getNewPassword())){
            redirectAttributes.addFlashAttribute("message", "새 비밀번호가 일치하지 않습니다. 다시 시도해 주세요.");
            return "redirect:/member/password";
        }

        String email=principal.getName();
        boolean isSuccess=memberService.updatePassword(email, passwordDTO.getCurrentPassword(),passwordDTO.getNewPassword());

        if (isSuccess) {
            // 성공 시 메시지와 함께 login.html로 리다이렉트
            redirectAttributes.addFlashAttribute("message", "패스워드가 변경되었습니다.");
            return "redirect:/member/main";
        } else {
            // 실패 시 메시지와 함께 password.html로 리다이렉트
            redirectAttributes.addFlashAttribute("message", "현재 비밀번호가 일치하지 않습니다. 다시 시도해 주세요.");
            return "redirect:/member/password";
        }
    }

    @PostMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id, Principal principal){
        memberService.deleteByid(id);
        if(principal.getName().equals("root"))
            return "redirect:/member/admin";
        return "redirect:/member/login";
    }
}
//MemberController.class