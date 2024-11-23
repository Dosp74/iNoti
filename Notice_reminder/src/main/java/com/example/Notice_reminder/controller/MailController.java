package com.example.Notice_reminder.controller;

import com.example.Notice_reminder.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.MailException;
import org.springframework.http.ResponseEntity;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send-mail")
    public ResponseEntity<String> sendMail() {
        try {
            mailService.sendMail("pantene74@naver.com", "테스트", "테스트");
            return ResponseEntity.ok("메일 발송 완료!");
        } catch (MailException e) {
            return ResponseEntity.status(500).body("메일 발송에 실패했습니다. 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("예기치 못한 오류가 발생했습니다.");
        }
    }
}