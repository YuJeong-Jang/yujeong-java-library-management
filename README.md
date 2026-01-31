# 도서관리 시스템 (MSA)

### Java / Spring Boot 기반 도서 관리 시스템으로, MSA(Microservices Architecture) 구조로 구현된 프로젝트

## 📋 프로젝트 개요
- 회원, 도서, 대여 도메인을 분리한 멀티 모듈 구조
- Spring Boot + Spring Data JPA + MySQL
- 프로파일(local, prod) 기반 환경 분리
- WebClient를 통한 MSA 간 통신
- Thymeleaf 기반 프론트엔드

## 🛠 기술 스택
- **Backend**: Java 17, Spring Boot 3.3.5
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Build**: Gradle (멀티 모듈)
- **Communication**: Spring WebFlux WebClient

## 🏗 MSA 구조

```
yujeong-java-library-management/
├── library-common/          # 공통 모듈 (엔티티, DTO, 예외처리)
├── library-book/           # 도서 관리 서비스 (Port: 8080)
├── library-member/         # 회원 관리 서비스 (Port: 7070)
├── library-rental/         # 대여 관리 서비스 (Port: 9090)
├── library-frontend/       # 프론트엔드 서비스 (Port: 3000)
```

### 서비스별 역할

#### 🔹 Library-Common
- 공통 엔티티 (Member, Book, Rental)
- 공통 DTO 및 응답 클래스
- 예외 처리 및 유틸리티

#### 🔹 Library-Book (Port: 8080)
- 도서 등록, 수정, 삭제
- 도서 검색 및 조회
- 재고 관리 (대여/반납 시 수량 조정)

#### 🔹 Library-Member (Port: 7070)
- 회원 가입, 정보 수정
- 회원 상태 관리 (활성/비활성)
- 회원 권한 관리 (일반/관리자)

#### 🔹 Library-Rental (Port: 9090)
- 도서 대여/반납 처리
- 연체 상태 관리
- 대여 이력 조회

#### 🔹 Library-Frontend (Port: 3000)
- 통합 웹 인터페이스
- MSA 서비스 간 통신 및 데이터 통합
- 사용자 친화적 UI/UX

## 🚀 실행 방법

### 1. 사전 준비
```bash
# MySQL 설치 및 실행 (54.180.241.63:3306)
# 데이터베이스 생성
CREATE DATABASE library;
```

### 2. 전체 서비스 실행
```bash
gradlew :library-book:bootRun
gradlew :library-member:bootRun
gradlew :library-rental:bootRun
gradlew :library-frontend:bootRun
```

### 3. 서비스 접속
- **프론트엔드**: http://localhost:3000
- **도서 API**: http://localhost:8080/api/books
- **회원 API**: http://localhost:7070/api/members
- **대여 API**: http://localhost:9090/api/rentals

## 📊 데이터베이스 설계

### Member (회원)
```sql
- member_id (PK)
- login_id (Unique)
- password
- name
- email
- phone
- status (ACTIVE/INACTIVE)
- role (USER/ADMIN)
- created_at, updated_at
```

### Book (도서)
```sql
- book_id (PK)
- isbn
- title
- author
- publisher
- publish_date
- category
- total_quantity
- available_qty
- status (AVAILABLE/BORROWED)
- created_at, updated_at
```

### Rental (대여)
```sql
- rental_id (PK)
- member_id (FK)
- book_id (FK)
- rental_status (RENTED/RETURNED/OVERDUE)
- rental_date
- due_date
- return_date
- remarks
```

## 🔄 MSA 통신 구조

```
Frontend (3000)
    ↓ WebClient
┌─────────────────────────────────┐
│  Book Service (8080)            │
│  Member Service (7070)          │  
│  Rental Service (9090)          │
└─────────────────────────────────┘
    ↓ JPA
┌─────────────────────────────────┐
│  MySQL Database (3306)          │
│  - library                      │
└─────────────────────────────────┘
```

## 🎯 주요 기능

### 📚 도서 관리
- ✅ 도서 등록/수정/삭제
- ✅ 도서 검색 (제목, 저자)
- ✅ 재고 관리
- ✅ 카테고리별 분류

### 👥 회원 관리  
- ✅ 회원 가입/정보 수정
- ✅ 회원 상태 관리
- ✅ 권한 관리 (일반/관리자)
- ✅ 중복 검증 (ID, 이메일)

### 🔄 대여 관리
- ✅ 도서 대여/반납
- ✅ 연체 관리
- ✅ 대여 이력 조회
- ✅ 실시간 재고 연동

### 🎨 프론트엔드
- ✅ 반응형 웹 디자인
- ✅ Bootstrap 5 기반 UI
- ✅ 실시간 데이터 연동
- ✅ 모달 기반 CRUD

## 🔧 개발 환경 설정

### application.properties 설정
각 서비스별로 포트와 데이터베이스 설정이 구성되어 있습니다.

```properties
# 공통 설정 예시
server.port=8080
spring.application.name=library-book

# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://database_ip:3306/database_name
spring.datasource.username=database_username
spring.datasource.password=database_password

# JPA 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 📝 API 문서

### 도서 API (Port: 8080)
```
GET    /api/books              # 전체 도서 조회
GET    /api/books/{id}         # 도서 상세 조회
GET    /api/books/search       # 도서 검색
POST   /api/books              # 도서 등록
PUT    /api/books/{id}         # 도서 수정
DELETE /api/books/{id}         # 도서 삭제
```

### 회원 API (Port: 7070)
```
GET    /api/members            # 전체 회원 조회
GET    /api/members/{id}       # 회원 상세 조회
POST   /api/members            # 회원 등록
PUT    /api/members/{id}       # 회원 정보 수정
DELETE /api/members/{id}       # 회원 삭제
PATCH  /api/members/{id}/activate   # 회원 활성화
PATCH  /api/members/{id}/deactivate # 회원 비활성화
```

### 대여 API (Port: 9090)
```
GET    /api/rentals            # 전체 대여 조회
GET    /api/rentals/{id}       # 대여 상세 조회
GET    /api/rentals/member/{memberId}  # 회원별 대여 조회
GET    /api/rentals/overdue    # 연체 도서 조회
POST   /api/rentals            # 도서 대여
PATCH  /api/rentals/{id}/return # 도서 반납
```

## 🎨 UI/UX 특징

- **모던한 디자인**: 그라데이션과 그림자 효과
- **반응형 레이아웃**: 모바일/태블릿/데스크톱 지원
- **직관적인 네비게이션**: 아이콘과 색상으로 구분
- **실시간 피드백**: 성공/오류 메시지 표시
- **카드 기반 레이아웃**: 정보의 시각적 구분

## 🔮 향후 계획

- [ ] Docker 컨테이너화
- [ ] Kubernetes 배포
- [ ] API Gateway 도입
- [ ] 서비스 디스커버리 (Eureka)
- [ ] 분산 추적 (Zipkin)
- [ ] 로그 집중화 (ELK Stack)
- [ ] 보안 강화 (Spring Security + JWT)

## 📄 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.

---

**개발자**: 유정  
**개발 기간**: 2025년 
**기술 스택**: Java 17, Spring Boot 3.3.5, MySQL, Thymeleaf, Bootstrap 5