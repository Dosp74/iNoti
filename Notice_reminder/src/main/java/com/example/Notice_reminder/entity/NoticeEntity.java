package com.example.Notice_reminder.entity;

import com.example.Notice_reminder.dto.NoticeDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "notice_table") // 공지사항 정보를 저장하는 테이블
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title; // 제목

    @Column
    private String createdDate; // 작성일

    @Column
    private String updatedDate; // 수정일

    @Column(length = 1000)
    private String url; // 링크

    // NoticeDTO를 Notice로 변환하는 메서드
    public static NoticeEntity toNoticeEntity(NoticeDTO noticeDTO) {
        return NoticeEntity.builder()
                .id(noticeDTO.getId())
                .title(noticeDTO.getTitle())
                .createdDate(noticeDTO.getCreatedDate())
                .updatedDate(noticeDTO.getUpdatedDate())
                .url(noticeDTO.getUrl())
                .build();
    }
}
