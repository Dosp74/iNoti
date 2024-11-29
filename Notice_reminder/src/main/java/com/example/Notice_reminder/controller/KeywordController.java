package com.example.Notice_reminder.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.service.KeywordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class KeywordController {

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @PostMapping("/keyword/add")
    public String addKeyword(@RequestParam("keyword") String keyword, Principal principal, Model model) {
        // 현재 로그인된 사용자의 이메일 가져오기
        String memberEmail = principal.getName();

        // 키워드 저장 시도
        boolean isSaved = keywordService.saveKeyword(keyword, memberEmail);

        if (isSaved) {
            model.addAttribute("successMessage", keyword + " 키워드가 성공적으로 등록되었습니다!");
            return "keyword_success"; // 성공 시 keyword_success.html로 이동
        } else {
            model.addAttribute("errorMessage", "중복 키워드를 등록하셨네요! 다른 키워드를 입력해주세요 :)");
            return "main"; // 실패 시 main.html에 머무르며 메시지를 표시
        }
    }

    @GetMapping("/keyword/list")
    public String viewKeywords(Principal principal, Model model) {
        // 현재 로그인된 사용자의 이메일 가져오기
        String memberEmail = principal.getName();

        // 사용자가 등록한 키워드 목록 조회
        List<KeywordEntity> keywords = keywordService.findKeywordsByMemberEmail(memberEmail);

        // 모델에 키워드 목록 추가
        model.addAttribute("keywords", keywords);

        return "keyword_list";
    }

    @PostMapping("/keyword/delete")
    public String deleteKeyword(@RequestParam("id") Long keywordId) {
        keywordService.deleteKeyword(keywordId);
        return "redirect:/keyword/list";
    }
}



