package com.example.Notice_reminder.controller;

import com.example.Notice_reminder.service.KeywordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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

        // 키워드 저장
        keywordService.saveKeyword(keyword, memberEmail);

        // 모델에 메시지 추가
        model.addAttribute("message", keyword + " 키워드가 등록되었습니다!");

        // 성공 페이지로 이동
        return "keyword_success";
    }
}


