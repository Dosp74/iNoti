package com.example.Notice_reminder.service;

import com.example.Notice_reminder.dto.NoticeDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlerService {
    @Autowired
    private NoticeService noticeService;

    public void startCrawl() {
//        // 크롬 드라이버 자동 다운로드 및 설정
//        WebDriverManager.chromedriver().clearResolutionCache().setup();

        // 크롬 드라이버 경로
        // 개인 로컬에서 실행 시 경로 변경 필요 (Driver.md 파일 참고)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver-win64\\chromedriver.exe");

        // 크롬 옵션
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Headless 모드
        options.addArguments("--disable-gpu"); // GPU 비활성화
        options.addArguments("--window-size=1920x1080"); // 기본 화면 크기
        options.addArguments("--no-sandbox"); // 안정화
        options.addArguments("--disable-dev-shm-usage"); // 안정화

        WebDriver driver = new ChromeDriver(options);

        try {
            String noticeUrl = "https://www.kw.ac.kr/ko/life/notice.jsp#zoom-in"; // 광운대학교 공지사항 - 전체
            driver.get(noticeUrl);

            // 엘리먼트 검색
            List<WebElement> elements = driver.findElements(By.cssSelector("#jwxe_main_content .list-box ul li"));
            int limit = Math.min(10, elements.size()); // 상위 10개

            for (int i = 0; i < limit; i++) {
                WebElement element = elements.get(i);

                // 제목 추출
                WebElement titleElement = element.findElement(By.cssSelector("a")); // a 태그
                String title = titleElement.getText();
                String url = titleElement.getAttribute("href");

                // 특정 단어 제거
                title = removeWords(title);

                // 상세정보 추출
                WebElement infoElement = element.findElement(By.cssSelector("div > p.info")); // p 태그
                String info = infoElement.getText();

                // 작성일 추출
                String createdDate = dateInfo(info, "작성일", "|");
                // 수정일 추출
                String updatedDate = dateInfo(info, "수정일", "|");

                // DTO 변환
                NoticeDTO noticeDTO = new NoticeDTO();
                noticeDTO.setTitle(title);
                noticeDTO.setUrl(url);
                noticeDTO.setCreatedDate(createdDate);
                noticeDTO.setUpdatedDate(updatedDate);

                // DB 저장
                noticeService.saveNotice(noticeDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /**
     * 텍스트에서 특정 정보를 추출하는 메서드
     * @param text 전체 텍스트
     * @param startKey 시작 키워드 ("작성일" || "수정일")
     * @param endKey 끝 키워드 ("|")
     * @return 추출한 값
     */
    private static String dateInfo(String text, String startKey, String endKey) {
        int startIndex = text.indexOf(startKey) + startKey.length() + 1;
        int endIndex = text.indexOf(endKey, startIndex);
        if (startIndex > 0 && endIndex > 0) {
            return text.substring(startIndex, endIndex).trim();
        }
        return "";
    }

    /**
     * 제목에서 불필요한 단어를 제거하는 메서드
     * @param text 전체 텍스트
     * @return 추출한 제목
     */
    private static String removeWords(String text) {
        String[] words = {"신규게시글", "Attachment"};
        for (String word : words) {
            text = text.replace(word, "");
        }
        return text.trim();
    }
}