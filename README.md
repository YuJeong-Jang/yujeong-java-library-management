# 도서관리 시스템

### Java / Spring Boot 기반 도서 관리 시스템으로, Docker와 Kubernetes 환경에 배포하기 위한 연습용 MSA(monorepo) 프로젝트

##### 프로젝트 개요
- 회원, 도서, 대여 도메인을 분리한 멀티 모듈 구조
- Spring Boot + Spring Data JPA + MySQL
- 프로파일(local, prod) 기반 환경 분리
- 추후 Kubernetes 배포를 위한 도커라이징 / 매니페스트 작성을 목표

##### 기술 스택
- Java 17
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Thymeleaf
- Database : MySQL
- Build : Gradle (멀티 모듈 / 모노레포)
- IDE : IntelliJ IDEA

##### 모듈 구조
```
yujeong-java-library-management 
├── build.gradle
├── settings.gradle****
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── library-common
│   ├── build.gradle
│   └── src
│       └── main
│           ├── java
│           └── resources
├── library-member
│   ├── build.gradle
│   └── src
│       └── main
│           ├── java
│           └── resources
├── library-rental
│   ├── build.gradle
│   └── src
│       └── main
│           ├── java
│           └── resources
└── library-management
    ├── build.gradle
    └── src
        └── main
            ├── java
            └── resources
```
- library-common
  - 공통 엔티티, DTO, 예외, 유틸성 코드
- library-member (port : 7070)
  - 회원 가입, 로그인, 회원 조회/수정
- library-management (port : 8080)
  - 도서 등록/수정/삭제, 도서 검색
- library-rental (port : 9090)
  - 도서 대여/반납, 연체 상태 관리

##### 도메인 설계 (간단 요약)
- Member
  - member_id, login_id, password, name, email, status, role etc
- Book (management)
  - book_id, isbn, title, author. publisher, total_quantity, available_qty etc
- Rental
  - rental_id, member_id, book_id, rental_status, rental_date, due_date, return_date etc

##### 커밋 컨벤션
- [feat] : 새로운 기능 추가
- [fix] : 버그 수정
- [refactor] : 리팩토링
- [chore] : 빌드/설정 변경
- [docs] : 문서 수정