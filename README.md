아직쓰는중

# 📝 SOBI - 내돈내산 리뷰 플랫폼
> 신뢰 기반 후기 문화 형성을 위한 리뷰 중심 블로그 플랫폼
>
> 
-----------------------------------------------------

## 👥 팀원 소개  4℃
| 이름     | 역할                          | GitHub ID       |
|----------|-------------------------------|------------------|
|오창진|멤버, 후기|[dengo25](https://github.com/dengo-dev)|
|최완빈|관리자|[wanbinchoi](https://github.com/wanbinchoi)|
|왕시은|커뮤니티|[alo-wang](https://github.com/alo-wang)|
|엄지원|마이페이지|[alo-wang](https://github.com/alo-wang)|


## 📆 프로젝트 기간

**2025.06.27 ~ 2025.07.18 ** 


## 🧾 프로젝트 개요


**SOBI**는 신뢰할 수 있는 "내돈내산" 리뷰를 중심으로  
블로그처럼 자유롭게 작성하고 공유할 수 있는 후기 플랫폼입니다.  
리뷰 기반 SNS 형태로 리뷰, 댓글, 채팅, 쪽지 등  
사용자 간의 교류를 중심으로 한 후기 문화를 형성합니다.


## 🔧 사용 기술 스택

### Backend
- Java 21, JSP/Servlet
- MySQL (AWS RDS)
- Spring 6
- Spring Security
- JPA

- ### Frontend
- MUI, Quill
- React, React-Router
- Axios
- JSP, HTML/CSS, JavaScript

- ### 협업 & 배포
- Git & GitHub
- Notion (기획 및 설계)
- ERDCloud, Google Docs
- Eclipse IDE, IntelliJ
- GithubAction, Docker
- AWS S3
- Postman



## 🖥️ 주요 기능

### 👤 일반 사용자
- 회원가입 / 로그인 (소셜 연동 포함)
- 리뷰 게시판 (검색, 페이징, 신고, 카테고리)
- 쪽지함 (보내기, 받기, 읽음 처리, 페이징)
- 마이페이지
  - 내 정보 확인 및 수정
  - 쪽지 관리

    
## 🚀 향후 확장 예정 기능

| 기능명           | 설명 |
|------------------|------|
| 💬 댓글 작성 기능 | 리뷰에 댓글을 남기고 의견을 나눌 수 있는 기능 |
| 🚨 신고 기능       | 리뷰 및 댓글 신고 기능을 통해 커뮤니티 품질 유지 |
| 💎 포인트 시스템   | 활동(리뷰, 댓글, 좋아요 등)에 따라 포인트 지급, 랭킹 시스템 도입 예정 |
| 🛒 쇼핑몰 연동     | 리뷰 기반 상품 추천 및 실제 구매 연동 기능 계획 중 |

---

## 📁 프로젝트 폴더 구조

```
sobi_backend/
├─.gradle
│  ├─8.14.2
│  │  ├─checksums
│  │  ├─executionHistory
│  │  ├─expanded
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.settings
├─bin
│  ├─default
│  │  └─com
│  │      └─kosta
│  │          └─domain
│  │              ├─blacklist
│  │              ├─blacklisthistory
│  │              ├─community
│  │              ├─member
│  │              ├─message
│  │              ├─report
│  │              └─review
│  ├─main
│  │  └─com
│  │      └─kosta
│  │          ├─config
│  │          ├─controller
│  │          │  ├─admin
│  │          │  ├─blacklist
│  │          │  ├─community
│  │          │  ├─member
│  │          │  ├─message
│  │          │  ├─mypage
│  │          │  ├─report
│  │          │  └─review
│  │          ├─domain
│  │          │  ├─blacklist
│  │          │  ├─blacklisthistory
│  │          │  ├─community
│  │          │  ├─member
│  │          │  ├─message
│  │          │  ├─report
│  │          │  └─review
│  │          ├─dto
│  │          │  ├─admin
│  │          │  ├─blacklist
│  │          │  ├─common
│  │          │  ├─community
│  │          │  ├─member
│  │          │  ├─message
│  │          │  ├─report
│  │          │  └─review
│  │          ├─mapper
│  │          │  └─community
│  │          ├─repository
│  │          │  ├─admin
│  │          │  ├─blacklist
│  │          │  ├─blacklisthistory
│  │          │  ├─community
│  │          │  ├─member
│  │          │  ├─message
│  │          │  ├─report
│  │          │  └─review
│  │          ├─security
│  │          │  ├─dto
│  │          │  ├─service
│  │          │  └─vo
│  │          ├─service
│  │          │  ├─admin
│  │          │  ├─blacklist
│  │          │  ├─common
│  │          │  ├─community
│  │          │  ├─member
│  │          │  ├─message
│  │          │  ├─report
│  │          │  ├─review
│  │          │  └─search
│  │          └─util
│  └─test
│      └─com
│          └─kosta
├─build
│  ├─classes
│  │  └─java
│  │      └─main
│  │          └─com
│  │              └─kosta
│  │                  ├─config
│  │                  ├─controller
│  │                  │  ├─admin
│  │                  │  ├─blacklist
│  │                  │  ├─community
│  │                  │  ├─member
│  │                  │  ├─message
│  │                  │  ├─mypage
│  │                  │  ├─report
│  │                  │  └─review
│  │                  ├─domain
│  │                  │  ├─blacklist
│  │                  │  ├─blacklisthistory
│  │                  │  ├─community
│  │                  │  ├─member
│  │                  │  ├─message
│  │                  │  ├─report
│  │                  │  └─review
│  │                  ├─dto
│  │                  │  ├─admin
│  │                  │  ├─blacklist
│  │                  │  ├─common
│  │                  │  ├─community
│  │                  │  ├─member
│  │                  │  ├─message
│  │                  │  ├─report
│  │                  │  └─review
│  │                  ├─mapper
│  │                  │  └─community
│  │                  ├─repository
│  │                  │  ├─admin
│  │                  │  ├─blacklist
│  │                  │  ├─blacklisthistory
│  │                  │  ├─community
│  │                  │  ├─member
│  │                  │  ├─message
│  │                  │  ├─report
│  │                  │  └─review
│  │                  ├─security
│  │                  │  ├─dto
│  │                  │  ├─service
│  │                  │  └─vo
│  │                  ├─service
│  │                  │  ├─admin
│  │                  │  ├─blacklist
│  │                  │  ├─common
│  │                  │  ├─community
│  │                  │  ├─member
│  │                  │  ├─message
│  │                  │  ├─report
│  │                  │  ├─review
│  │                  │  └─search
│  │                  └─util
│  ├─generated
│  │  └─sources
│  │      ├─annotationProcessor
│  │      │  └─java
│  │      │      └─main
│  │      │          └─com
│  │      │              └─kosta
│  │      │                  └─domain
│  │      │                      ├─blacklist
│  │      │                      ├─blacklisthistory
│  │      │                      ├─community
│  │      │                      ├─member
│  │      │                      ├─message
│  │      │                      ├─report
│  │      │                      └─review
│  │      └─headers
│  │          └─java
│  │              └─main
│  ├─reports
│  │  └─problems
│  └─tmp
│      └─compileJava
├─gradle
│  └─wrapper
└─src
    ├─main
    │  ├─generated
    │  │  └─querydsl
    │  │      └─com
    │  │          └─kosta
    │  │              └─domain
    │  │                  ├─blacklist
    │  │                  ├─blacklisthistory
    │  │                  ├─member
    │  │                  ├─memberlog
    │  │                  ├─message
    │  │                  ├─reivew
    │  │                  └─report
    │  ├─java
    │  │  └─com
    │  │      └─kosta
    │  │          ├─config
    │  │          ├─controller
    │  │          │  ├─admin
    │  │          │  ├─blacklist
    │  │          │  ├─community
    │  │          │  ├─member
    │  │          │  ├─message
    │  │          │  ├─mypage
    │  │          │  ├─report
    │  │          │  └─review
    │  │          ├─domain
    │  │          │  ├─blacklist
    │  │          │  ├─blacklisthistory
    │  │          │  ├─community
    │  │          │  ├─member
    │  │          │  ├─message
    │  │          │  ├─report
    │  │          │  └─review
    │  │          ├─dto
    │  │          │  ├─admin
    │  │          │  ├─blacklist
    │  │          │  ├─common
    │  │          │  ├─community
    │  │          │  ├─member
    │  │          │  ├─message
    │  │          │  ├─report
    │  │          │  └─review
    │  │          ├─mapper
    │  │          │  └─community
    │  │          ├─repository
    │  │          │  ├─admin
    │  │          │  ├─blacklist
    │  │          │  ├─blacklisthistory
    │  │          │  ├─community
    │  │          │  ├─member
    │  │          │  ├─message
    │  │          │  ├─report
    │  │          │  └─review
    │  │          ├─security
    │  │          │  ├─dto
    │  │          │  ├─service
    │  │          │  └─vo
    │  │          ├─service
    │  │          │  ├─admin
    │  │          │  ├─blacklist
    │  │          │  ├─common
    │  │          │  ├─community
    │  │          │  ├─member
    │  │          │  ├─message
    │  │          │  ├─report
    │  │          │  ├─review
    │  │          │  └─search
    │  │          └─util
    │  └─resources
    └─test
        └─java
            └─com
                └─kosta
```


## 📷 서비스 화면
<img width="1920" height="3288" alt="image" src="https://github.com/user-attachments/assets/8d407f21-8816-4534-8892-2b001df6a3fa" />

## 📁 DB 설계
 **ERDcloud로 작성**
 
[초안]

<img width="1057" alt="image" src="https://github.com/user-attachments/assets/9c3927e4-3b4e-4e06-9a4a-e1ac31d12382" />

[최종]

<img width="1093" alt="image" src="https://github.com/user-attachments/assets/58a68405-e8ed-4af5-a496-fbd9854dc0ab" />



## 📊 데이터베이스 테이블 구조

> SOBI 프로젝트는 총 **17개 이상의 테이블**로 구성되어 있으며,  
후기 공유, 사용자 관리, 쪽지, 신고, 좋아요, 관리자 기능 등을 모두 데이터베이스 구조로 지원합니다.  
아래는 주요 테이블에 대한 간단한 설명입니다.

---

### 👤 사용자 및 인증 관련

| 테이블명         | 설명                           |
|------------------|--------------------------------|
| `MEMBER`         | 사용자(회원) 기본 정보 저장         |
| `BLACK_LIST`     | 차단된 사용자 정보                 |
| `BLACKLIST_HISTORY` | 차단 이력 기록                   |
| `MEMBER_LOG`     | 사용자 활동 로그 기록              |

---

### 📬 쪽지 기능

| 테이블명     | 설명                                 |
|--------------|--------------------------------------|
| `MESSAGE`     | 사용자 간 1:1 쪽지 내용                 |
| `MESSAGE_ID_DELETED_BY_SENDER/RECEIVER` | 쪽지 삭제 여부 상태 저장 필드 포함 |

---

### 📝 후기 및 댓글

| 테이블명         | 설명                           |
|------------------|--------------------------------|
| `REVIEW`         | 후기 본문 및 작성 정보             |
| `REVIEW_IMAGE`   | 후기 이미지 파일 정보              |
| `REVIEW_COMMENT` | 후기 댓글 정보                   |
| `CATEGORY`       | 후기 카테고리 관리                |

---

### ❤️ 좋아요 기능

| 테이블명         | 설명                           |
|------------------|--------------------------------|
| `REVIEW_LIKE`    | 후기 좋아요 내역                 |
| `COMMENT_LIKE`   | 댓글 좋아요 내역                 |

---

### 🚨 신고 기능

| 테이블명           | 설명                           |
|--------------------|--------------------------------|
| `REVIEW_REPORT`     | 후기 신고 정보                   |
| `COMMENT_REPORT`    | 댓글 신고 정보                   |

---

### 📢 공지 및 FAQ

| 테이블명         | 설명                           |
|------------------|--------------------------------|
| `NOTICE`         | 공지사항 본문                   |
| `NOTICE_IMAGE`   | 공지 이미지                     |
| `FAQ`            | 자주 묻는 질문 정보              |

---

> 전체 테이블 구조는 기능별로 명확하게 분리되어 있어 유지보수와 확장에 용이하며,  
추후 쇼핑몰 연동, 포인트 시스템, 추천 알고리즘 등의 기능 확장을 위한 기반이 마련되어 있습니다.

---
## 📘 SOBI 커뮤니티 유저 가이드라인

> SOBI는 'Save, Or Buy Immediately'를 모토로, 내돈내산 기반의 **진짜 소비 경험을 나누는 리뷰 커뮤니티**입니다.  
여러분 모두가 신뢰할 수 있는 후기와 소통을 나누는 공간이 되도록 아래의 가이드라인을 꼭 지켜주세요.

### 1. 운영 철학
- 객관적이고 진실된 후기를 통해 올바른 소비 선택을 돕는 것을 목표로 합니다.
- 서로를 존중하고, 건전한 피드백을 주고받는 커뮤니티 문화를 지향합니다.

### 2. 금지되는 콘텐츠
- **가짜/조작 리뷰**: 금전 보상 후기, 도용, 사용 경험 없는 후기
- **부적절한 표현**: 욕설, 혐오, 성적 발언 등
- **스팸/광고**: 반복성 콘텐츠, 링크 홍보 등
- **민감한 주제**: 자해, 선정성, 폭력 등 미화 또는 조장 콘텐츠

### 3. 이미지 업로드 가이드
- 실사용 제품의 사진만 허용
- 편집/합성으로 소비를 왜곡하는 이미지 금지
- 음란/폭력 이미지 업로드 시 즉시 제재

### 4. 커뮤니케이션 예절
- 존중하는 언어 사용, 불쾌감 유발 표현 금지
- 신고 누적 시 댓글은 블라인드 처리

### 5. 제재 정책
- 위반 시 콘텐츠 삭제, 경고, 활동 제한 또는 계정 정지

### 6. 예외 사항
- 분석/비교/풍자 목적은 사전 신고 시 검토하여 일부 허용될 수 있음

---

## 📝 SOBI 사용자 메뉴얼

### 1. 회원가입 및 로그인
- `회원가입` → 정보 입력 후 로그인
- **구글, 깃허브 소셜 로그인 지원**

### 2. 리뷰 둘러보기
- 최신순, 카테고리별 정렬 가능
- 리뷰 카드를 클릭하여 상세내용 확인

### 3. 리뷰 작성
- 제목, 제품명, 카테고리, 이미지 포함 작성 가능

### 4. 마이페이지
- 내가 작성한 리뷰를 목록으로 관리
- 받은 쪽지, 보낸 쪽지를 확인하고 답장 가능

### 5. 쪽지 기능
- 마이페이지 클릭 → `쪽지 보내기`
- 쪽지는 1:1, 텍스트 전용 (파일/이미지 불가)
- 읽은 쪽지는 자동 읽음 처리 / 개별 삭제 가능

---


