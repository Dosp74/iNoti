package com.example.Notice_reminder;

import com.example.Notice_reminder.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.boot.CommandLineRunner;
=======
>>>>>>> origin/feature_js
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class NoticeReminderApplication implements CommandLineRunner {
<<<<<<< HEAD
=======

>>>>>>> origin/feature_js
	private final CrawlerService crawlerService;

	@Autowired
	public NoticeReminderApplication(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

	@Override
	public void run(String... args) throws Exception {
		crawlerService.startCrawl();  // 크롤링 서비스 실행
	}

	public static void main(String[] args) {
		SpringApplication.run(NoticeReminderApplication.class, args);
	}
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/feature_js
