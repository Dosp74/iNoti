package com.example.Notice_reminder.service;

import com.example.Notice_reminder.dto.NoticeDTO;
import com.example.Notice_reminder.entity.NoticeEntity;
import com.example.Notice_reminder.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    // 크롤링 데이터 -> NoticeDTO -> Notice Entity
    public void saveNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTO);
        noticeRepository.save(noticeEntity);
    }
}