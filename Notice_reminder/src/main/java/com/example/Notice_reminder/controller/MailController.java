package com.example.Notice_reminder.controller;

import com.example.Notice_reminder.service.KeywordService;
import com.example.Notice_reminder.service.MailService;
import com.example.Notice_reminder.service.MemberService;
import com.example.Notice_reminder.service.NoticeService;
import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.entity.NoticeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;
    private final KeywordService keywordService;
    private final NoticeService noticeService;
    private final MemberService memberService;

    @Autowired
    public MailController(MailService mailService, KeywordService keywordService, NoticeService noticeService, MemberService memberService) {
        this.mailService = mailService;
        this.keywordService = keywordService;
        this.noticeService = noticeService;
        this.memberService = memberService;
    }

    @GetMapping("/send")
    public String sendMailForAllMembers() {
        try {
            // 데이터베이스에서 모든 사용자의 이메일을 가져옴
            List<String> memberEmails = memberService.findAllEmails();

            if (memberEmails.isEmpty()) {
                return "등록된 사용자가 없습니다.";
            }

            for (String email : memberEmails) {
                // 사용자가 설정한 키워드를 가져옴
                List<KeywordEntity> keywords = keywordService.findKeywordsByMemberEmail(email);

                if (keywords.isEmpty()) {
                    continue; // 키워드가 없는 사용자는 건너뜀
                }

                for (KeywordEntity keyword : keywords) {
                    String keywordText = keyword.getKeyword();

                    // 키워드가 포함된 공지사항 검색
                    List<NoticeEntity> matchingNotices = findNoticesByKeyword(keywordText);

                    if (!matchingNotices.isEmpty()) {
                        // 키워드가 포함된 공지사항이 있으면 이메일을 발송함
                        for (NoticeEntity notice : matchingNotices) {
                            String subject = "[iNoti] 설정한 키워드와 관련된 공지사항이 업데이트되었습니다.";
                            String text = "설정한 키워드와 관련된 공지사항이 업데이트되었습니다.\n\n" + notice.getTitle() + "\n" + notice.getUrl() + "\n\n지금 바로 확인해보세요!";
                            mailService.sendMail(email, subject, text); // 메일 발송
                        }
                    }
                }
            }

            return "모든 사용자에 대한 메일 발송 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "메일 발송 중 오류 발생";
        }
    }

    // 키워드에 맞는 공지사항을 검색하는 메서드
    private List<NoticeEntity> findNoticesByKeyword(String keyword) {
        // 데이터베이스에서 키워드가 포함된 공지사항이 있는지 검색
        return noticeService.getNoticesByKeyword(keyword);
    }
}