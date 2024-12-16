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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/member/signup")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "redirect:/member/login";
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/member/login";
    }

    @GetMapping("/member/admin")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "admin";
    }

    @GetMapping("/member/main")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/member/info")
    public String getMemberInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String memberEmail = "";
        String memberName = "";
        Long memberId = null;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            memberEmail = userDetails.getUsername();

            MemberDTO memberDTO = memberService.findByEmail(memberEmail);
            if (memberDTO != null) {
                memberId = memberDTO.getId();
                memberName = memberDTO.getMemberName();
            }
        }

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberEmail", memberEmail);
        model.addAttribute("memberName", memberName);

        return "info";
    }

    @PostMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteByid(id);
        return "redirect:/member/admin";
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