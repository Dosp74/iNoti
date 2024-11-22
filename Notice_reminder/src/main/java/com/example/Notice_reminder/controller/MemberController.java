package com.example.Notice_reminder.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import com.example.Notice_reminder.dto.MemberDTO;
import com.example.Notice_reminder.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor //MemberService에 대한 멤버를 사용 가능
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
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
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html로 가져갈 데이터가 있다면 model 사용
        model.addAttribute("memberList", memberDTOList);
        return "admin";
    }

    @GetMapping("/member/main")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/member/info")
    public String getMemberInfo(Model model) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Object principal=authentication.getPrincipal();

        String memberEmail="";
        String memberName="";
        Long memberId=null;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            memberEmail = userDetails.getUsername(); // 이메일 (username 필드)

            // 사용자 객체에서 추가 정보를 가져오는 로직 (서비스 호출)
            MemberDTO memberDTO = memberService.findByEmail(memberEmail); // 이메일로 조회
            if (memberDTO != null) {
                memberId = memberDTO.getId();
                memberName = memberDTO.getMemberName();
            }
        }

        // 모델에 데이터 추가
        model.addAttribute("memberId", memberId);
        model.addAttribute("memberEmail", memberEmail);
        model.addAttribute("memberName", memberName);

        return "info";
    }


    @PostMapping("/member/delete/{id}") // /member/{id}로 할 수 있도록 공부
    public String deleteById(@PathVariable Long id){
        memberService.deleteByid(id);

        return "redirect:/member/login";
    }
}
//MemberController.class