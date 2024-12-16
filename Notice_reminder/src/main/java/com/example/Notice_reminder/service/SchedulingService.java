package com.example.Notice_reminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingService {
    private final MailService mailService;
    private final CrawlerService crawlerService;

    @Scheduled(fixedRate=1000*60*2)
    public void run() throws Exception {

        if(true) return;    //크롤링, 메일 서비스 차단. 실제 테스트 시 해당 줄 삭제

        crawlerService.startCrawl();  // 크롤링 서비스 실행
        mailService.sendMailForAllMembers();
    }
}
