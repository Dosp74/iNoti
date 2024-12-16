package com.example.Notice_reminder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String email;

    private LocalDateTime sentTime;  // 발송 시간

    public EmailRecordEntity(String url, String email, LocalDateTime sentTime) {
        this.url = url;
        this.email = email;
        this.sentTime = sentTime;
    }
}