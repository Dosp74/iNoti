package com.example.Notice_reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NoticeReminderApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoticeReminderApplication.class, args);
	}
}