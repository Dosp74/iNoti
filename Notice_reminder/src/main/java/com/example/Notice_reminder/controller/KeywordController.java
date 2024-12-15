package com.example.Notice_reminder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.service.KeywordService;

import java.security.Principal;
import java.util.List;

@Controller
public class KeywordController {

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    // 키워드 등록 페이지 반환 (GET 요청)
    @GetMapping("/keyword/add")
    public String showKeywordPage(Model model) {
        return "keyword"; // keyword.html 반환
    }

    // 키워드 등록 처리 (POST 요청)
    @PostMapping("/keyword/add")
    public String addKeyword(@RequestParam("keyword") String keyword, Principal principal, RedirectAttributes redirectAttributes) {
        String memberEmail = principal.getName();
        boolean isSaved = keywordService.saveKeyword(keyword, memberEmail);

        if (isSaved) {
            redirectAttributes.addFlashAttribute("successMessage", "등록 성공하였습니다!");
            return "redirect:/member/main"; // 성공 시 메인 페이지로 이동
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "등록에 실패하였습니다.");
            return "redirect:/keyword/add"; // 실패 시 다시 키워드 등록 페이지
        }
    }

    // 키워드 목록 확인 (GET 요청)
    @GetMapping("/keyword/list")
    public String viewKeywords(Principal principal, Model model) {
        // 현재 로그인된 사용자의 이메일 가져오기
        String memberEmail = principal.getName();

        // 사용자가 등록한 키워드 목록 조회
        List<KeywordEntity> keywords = keywordService.findKeywordsByMemberEmail(memberEmail);

        // 모델에 키워드 목록 추가
        model.addAttribute("keywords", keywords);

        return "keyword_list"; // keyword_list.html 반환
    }

    // 키워드 삭제 처리 (POST 요청)
    @PostMapping("/keyword/delete")
    public String deleteKeyword(@RequestParam("id") Long keywordId, RedirectAttributes redirectAttributes) {
        keywordService.deleteKeyword(keywordId);
        redirectAttributes.addFlashAttribute("successMessage", "키워드 삭제 성공!");
        return "redirect:/keyword/list"; // 삭제 후 키워드 목록으로 이동
    }
}



