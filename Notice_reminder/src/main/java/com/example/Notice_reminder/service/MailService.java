package com.example.Notice_reminder.service;

import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.entity.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {
    private final KeywordService keywordService;
    private final NoticeService noticeService;
    private final MemberService memberService;

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("emailservice6@naver.com"); // 이메일을 보낼 네이버 계정

            mailSender.send(message);

            logger.info("메일 발송 완료! to: {}", to);
        } catch (MailException e) {
            logger.error("메일 발송 실패", e);
        }
    }

    public void sendMailForAllMembers() {
        try {
            // 데이터베이스에서 모든 사용자의 이메일을 가져옴
            List<String> memberEmails = memberService.findAllEmails();

            if (!memberEmails.isEmpty()) {
                for (String email : memberEmails) {
                    // 사용자가 설정한 키워드를 가져옴
                    List<KeywordEntity> keywords = keywordService.findKeywordsByMemberEmail(email);

                    if (keywords.isEmpty()) {
                        continue; // 키워드가 없는 사용자는 건너뜀
                    }

                    for (KeywordEntity keyword : keywords) {
                        String keywordText = keyword.getKeyword();

                        // 키워드가 포함된 공지사항 검색
                        List<NoticeEntity> matchingNotices = noticeService.getNoticesByKeyword(keywordText);

                        if (!matchingNotices.isEmpty()) {
                            // 키워드가 포함된 공지사항이 있으면 이메일을 발송함
                            for (NoticeEntity notice : matchingNotices) {
                                String subject = "[iNoti] 설정한 키워드와 관련된 공지사항이 업데이트되었습니다.";
                                String text = "설정한 키워드와 관련된 공지사항이 업데이트되었습니다.\n\n" + notice.getTitle() + "\n" + notice.getUrl() + "\n\n지금 바로 확인해보세요!";
                                sendMail(email, subject, text); // 메일 발송
                            }
                        }
                    }
                }
            }
            System.out.println("모든 사용자에 대한 메일 발송 완료");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메일 발송 중 오류 발생");
        }
    }
}