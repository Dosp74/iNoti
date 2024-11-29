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
        crawlerService.startCrawl();  // 크롤링 서비스 실행
        mailService.sendMailForAllMembers();
    }
}
