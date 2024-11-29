package com.example.Notice_reminder.service;

import com.example.Notice_reminder.dto.NoticeDTO;
import com.example.Notice_reminder.entity.NoticeEntity;
import com.example.Notice_reminder.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    // 크롤링 데이터 -> NoticeDTO -> Notice Entity
    public void saveNotice(NoticeDTO noticeDTO) {
        // 중복된 제목 확인
        boolean exists = noticeRepository.existsByTitle(noticeDTO.getTitle());

        if (!exists) {
            // 중복되지 않은 공지사항만 저장
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTO);
            noticeRepository.save(noticeEntity);
        } else {
            System.out.println("중복된 공지사항 : " + noticeDTO.getTitle());
        }
    }

    // 키워드가 포함된 공지사항을 데이터베이스에서 검색
    public List<NoticeEntity> getNoticesByKeyword(String keyword) {
        return noticeRepository.findByTitleContaining(keyword);  // 'title' 필드에서 키워드가 포함된 공지사항 검색
    }

    // 전체 공지사항을 가져옴
    public List<NoticeEntity> getAllNotices() {
        return noticeRepository.findAll();
    }
}
