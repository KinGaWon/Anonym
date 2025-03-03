# Anonym : 🧑‍💻
## 구인구직 익명 커뮤니티
### 개요
- 프로젝트 기간 : 2024년 10월 14일 ~ 11월 13일
- 프로젝트 주제 : 사용자 참여형 오픈 커뮤니티 시스템
- 프로젝트 목적 : JAVA, JSP, HTML, DB 구축, AJAX, API 등 웹개발에 필요한 기술을 연습하고, 팀 프로젝트를 통해 협업 능력과 개인의 개발 역량을 향상시키는 것을 목적
- 프로젝트 참여자 : 김가원(팀장), 유다현(팀원), 전동훈(팀원)
- 내가 맡은 역할 : 회원가입, 로그인, 마이페이지 구현
---
### 프로젝트 소개
_**‘ 직장인 뿐만 아니라 다양한 그룹에 속한 사용자들이 자유롭게 소통할 수 있는 익명 커뮤니티를 제공하는 것을 목표로 하는 구인구직 사이트 ’**_
- 모든 유저가 자유롭게 소통할 수 있는 오픈형 댓글 기반 자유 게시판
- 기업 정보 조회 및 사용자 리뷰 기능을 갖춘 게시판과 기업 소속 직원 전용 사내 커뮤니티 공간
- 기업 전용 채용 공고 및 지원자 관리 시스템을 제공하는 기업 서비스 게시판

  
_**‘ 관련핵심 기술 ’**_
  1. 시스템 요구사항 분석 및 설계 기술
  2. JDBC를 활용한 데이터 CRUD처리 기술
  3. JSP를 활용한 웹 애플리케이션 구현 기술
  4. 시스템 요구사항 반영 데이터베이스 모델링 기술
  5. MVC 패턴 활용을 통한 디자인패턴 기술
---
### 주요 기능
**1. 회원가입**
- 기업 회원과 개인 회원 구분 가입
- AJAX 기반으로 ID, 닉네임 중복 검사
- 카카오 주소 API를 활용한 주소 입력 기능
- 재직 중인 회원은 소속 회사 선택
  
**2. 로그인**

**3. 자유게시판 CURD**
- 모든 유저의 게시글 및 댓글 조회 가능
- 검색어 포함 게시글 제목 검색 기능
- 로그인 후 게시글 작성, 수정, 삭제 가능
- 스마트 에디터를 이용한 글 작성 지원
- 로그인 후 신고, 좋아요 기능 수행 가능

**4. 기업리뷰 CRUD**
- jQuery와 AJAX를 활용하여 실시간 회사 검색 기능과 서버와의 비동기 데이터 통신을 구현
- 인기 회사 추천 목록 제공
- 해당 회사 소속 회원만 리뷰 작성 가능
- 비회원은 리뷰 1개만 조회 가능(회원 유저는 모든 리뷰 조회 가능)
- 사내 커뮤니티는 해당 회사 소속 회원만 이용 가능
- 사내 커뮤니티 CRUD 기능, 스마트 에디터를 이용한 글 작성 지원 및 좋아요/신고 기능 제공

**5. 채용공고**
- 검색어를 포함한 공고 제목 및 내용 검색 기능
- 개인 회원이 해당 회사 공고에 이력서 지원 기능

**6. 기업서비스 CURD**
- 기업 전용 공간
- 채용 공고 작성, 수정, 마감 처리 기능
- 채용 공고에 지원한 지원자 관리 (이력서 조회, 합격/불합격 처리)

**7. 마이페이지**
- 개인회원: 로그인 후 내 정보와 이력서를 관리
- 기업회원: 내 정보 관리
- 관리자: 기업 승인 관리, 신고된 게시물 관리, 블랙리스트 회원 조회 기능

**8. 관리자 권한**
- 모든 게시글·댓글 삭제 가능
