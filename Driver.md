## 📍 크롬 드라이버 관련 안내

- 크롤링 작업 시 크롬 드라이버 설치 및 프로젝트 내 경로 설정이 필요합니다.
- 프로그램 실행 전 아래 내용을 참고해서 진행해 주세요!

---

### 1. 참고 사이트
- [크롬 드라이버 설치](https://m.blog.naver.com/cjs0308cjs/223259465805)
- [공식 크롬 드라이버 다운로드](https://sites.google.com/chromium.org/driver/downloads)

---

### 2. 설치 및 설정 방법 (Windows & Mac)
#### 1) **크롬 버전 확인**
- Chrome 주소창에 `chrome://settings/help`를 입력하여 현재 크롬 버전을 확인합니다.
- ex) `119.0.6045.105`

#### 2) **드라이버 다운로드**
- 참고 사이트에서 본인의 크롬 버전에 맞는 드라이버를 찾습니다.
- ex) 크롬 버전 `119.0.6045.105` → 드라이버 `119.x.x.x` 다운로드

#### 3) **드라이버 설치**
- **Windows :** zip 파일의 압축을 풀고 `chromedriver.exe` 경로를 확인합니다.  
  ex) `C:\chromedriver\chromedriver.exe`
- **Mac :** zip 파일의 압축을 풀고 `chromedriver` 파일을 확인합니다.  
  ex) `/Users/yourname/chromedriver/chromedriver`

#### 4) **드라이버 실행 권한 부여 (Mac만 해당)**
- 압축 해제 후, 드라이버에 실행 권한을 부여합니다.
  ```bash
  chmod +x /Users/yourname/chromedriver/chromedriver
  ```

#### 5) **경로 설정**
- 프로젝트의 `CrawlerService` 파일에서 아래 코드를 찾아 수정합니다.
- **Windows :**
  ```java
  System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
  ```
- **Mac :**
  ```java
  System.setProperty("webdriver.chrome.driver", "/Users/yourname/chromedriver/chromedriver");
  ```

#### 6) **프로그램 실행**
- 설정이 완료되면 프로그램을 실행합니다.

---

### 3. 주의사항
- **드라이버 버전과 크롬 버전이 반드시 일치해야 합니다.**
    - 버전 불일치 시 크롤링 작업을 실행할 수 없습니다.
- **드라이버 경로는 로컬 시스템에 맞게 설정해 주세요.**
- 크롬 업데이트 시 드라이버도 동일하게 업데이트해야 합니다.