# 도서관 관리 시스템 API 명세서

## 개요
도서관 관리 시스템의 REST API 명세서입니다. 이 시스템은 도서, 회원, 대여 관리 기능을 제공합니다.

## 기본 정보
- **Base URL**: 각 서비스별로 다름
  - Book Service: `http://localhost:8081`
  - Member Service: `http://localhost:8082`
  - Rental Service: `http://localhost:8083`
- **Content-Type**: `application/json`
- **Character Encoding**: UTF-8

## 공통 응답 형식

### 성공 응답
```json
{
  "success": true,
  "message": "성공 메시지",
  "data": { /* 응답 데이터 */ }
}
```

### 에러 응답
```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null
}
```

## Enum 타입

### BookStatus
- `AVAILABLE` (0): 대여 가능
- `BORROWED` (1): 대여 중

### MemberRole
- `USER` (0): 일반 사용자
- `ADMIN` (1): 관리자

### MemberStatus
- `ACTIVE` (0): 활성
- `INACTIVE` (1): 비활성

### RentalStatus
- `RENTED` (0): 대여 중
- `RETURNED` (1): 반납 완료
- `OVERDUE` (2): 연체

---

## 1. 도서 관리 API (Book Service)

### 1.1 전체 도서 조회
**GET** `/api/books`

도서 목록을 조회합니다.

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "title": "자바의 정석",
      "author": "남궁성",
      "isbn": "978-8994492032",
      "publisher": "도우출판",
      "category": "프로그래밍",
      "totalQuantity": 5,
      "availableQty": 3,
      "status": "AVAILABLE",
      "createdAt": "2026-01-01T00:00:00Z",
      "updatedAt": "2026-01-01T00:00:00Z"
    }
  ]
}
```

### 1.2 도서 상세 조회
**GET** `/api/books/{id}`

특정 도서의 상세 정보를 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 도서 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 1,
    "title": "자바의 정석",
    "author": "남궁성",
    "isbn": "978-8994492032",
    "publisher": "도우출판",
    "category": "프로그래밍",
    "totalQuantity": 5,
    "availableQty": 3,
    "status": "AVAILABLE",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

### 1.3 도서 검색
**GET** `/api/books/search?keyword={keyword}`

키워드로 도서를 검색합니다.

#### 쿼리 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| keyword | String | O | 검색 키워드 (제목, 저자, ISBN 등) |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "title": "자바의 정석",
      "author": "남궁성",
      "isbn": "978-8994492032",
      "publisher": "도우출판",
      "category": "프로그래밍",
      "totalQuantity": 5,
      "availableQty": 3,
      "status": "AVAILABLE",
      "createdAt": "2026-01-01T00:00:00Z",
      "updatedAt": "2026-01-01T00:00:00Z"
    }
  ]
}
```

### 1.4 도서 등록
**POST** `/api/books`

새로운 도서를 등록합니다.

#### 요청 본문
```json
{
  "title": "자바의 정석",
  "author": "남궁성",
  "isbn": "978-8994492032",
  "publisher": "도우출판",
  "publishDate": "2016-01-27",
  "category": "프로그래밍",
  "totalQuantity": 5,
  "availableQty": 5
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| title | String | O | 도서 제목 |
| author | String | O | 저자 |
| isbn | String | O | ISBN |
| publisher | String | O | 출판사 |
| publishDate | LocalDate | X | 출판일 (YYYY-MM-DD) |
| category | String | X | 카테고리 |
| totalQuantity | Integer | O | 총 수량 |
| availableQty | Integer | O | 대여 가능 수량 |

#### 응답
```json
{
  "success": true,
  "message": "도서가 성공적으로 등록되었습니다.",
  "data": {
    "id": 1,
    "title": "자바의 정석",
    "author": "남궁성",
    "isbn": "978-8994492032",
    "publisher": "도우출판",
    "category": "프로그래밍",
    "totalQuantity": 5,
    "availableQty": 5,
    "status": "AVAILABLE",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

### 1.5 도서 수정
**PUT** `/api/books/{id}`

도서 정보를 수정합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 도서 ID |

#### 요청 본문
```json
{
  "title": "자바의 정석 (개정판)",
  "author": "남궁성",
  "isbn": "978-8994492032",
  "publisher": "도우출판",
  "publishDate": "2016-01-27",
  "category": "프로그래밍",
  "totalQuantity": 10,
  "availableQty": 8
}
```

#### 응답
```json
{
  "success": true,
  "message": "도서가 성공적으로 수정되었습니다.",
  "data": {
    "id": 1,
    "title": "자바의 정석 (개정판)",
    "author": "남궁성",
    "isbn": "978-8994492032",
    "publisher": "도우출판",
    "category": "프로그래밍",
    "totalQuantity": 10,
    "availableQty": 8,
    "status": "AVAILABLE",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-15T00:00:00Z"
  }
}
```

### 1.6 도서 삭제
**DELETE** `/api/books/{id}`

도서를 삭제합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 도서 ID |

#### 응답
```json
{
  "success": true,
  "message": "도서가 성공적으로 삭제되었습니다.",
  "data": null
}
```

### 1.7 헬스 체크
**GET** `/api/books/health`

서비스 상태를 확인합니다.

#### 응답
```
OK
```

---

## 2. 회원 관리 API (Member Service)

### 2.1 전체 회원 조회
**GET** `/api/members`

회원 목록을 조회합니다.

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "loginId": "user01",
      "name": "홍길동",
      "email": "user01@example.com",
      "phone": "010-1234-5678",
      "status": "ACTIVE",
      "role": "USER",
      "createdAt": "2026-01-01T00:00:00Z",
      "updatedAt": "2026-01-01T00:00:00Z"
    }
  ]
}
```

### 2.2 회원 상세 조회
**GET** `/api/members/{id}`

특정 회원의 상세 정보를 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 1,
    "loginId": "user01",
    "name": "홍길동",
    "email": "user01@example.com",
    "phone": "010-1234-5678",
    "status": "ACTIVE",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

### 2.3 로그인 ID로 회원 조회
**GET** `/api/members/login/{loginId}`

로그인 ID로 회원 정보를 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| loginId | String | O | 로그인 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 1,
    "loginId": "user01",
    "name": "홍길동",
    "email": "user01@example.com",
    "phone": "010-1234-5678",
    "status": "ACTIVE",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

### 2.4 회원 가입
**POST** `/api/members`

새로운 회원을 등록합니다.

#### 요청 본문
```json
{
  "loginId": "user01",
  "password": "password123",
  "name": "홍길동",
  "email": "user01@example.com",
  "phone": "010-1234-5678",
  "role": 0
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| loginId | String | O | 로그인 ID |
| password | String | O | 비밀번호 |
| name | String | O | 이름 |
| email | String | O | 이메일 |
| phone | String | O | 전화번호 |
| role | Integer | X | 권한 (0: USER, 1: ADMIN, 기본값: 0) |

#### 응답
```json
{
  "success": true,
  "message": "회원이 성공적으로 등록되었습니다.",
  "data": {
    "id": 1,
    "loginId": "user01",
    "name": "홍길동",
    "email": "user01@example.com",
    "phone": "010-1234-5678",
    "status": "ACTIVE",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

### 2.5 회원 정보 수정
**PUT** `/api/members/{id}`

회원 정보를 수정합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 요청 본문
```json
{
  "name": "홍길동",
  "email": "newemail@example.com",
  "phone": "010-9876-5432",
  "password": "newpassword123",
  "role": 0
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | X | 이름 |
| email | String | X | 이메일 |
| phone | String | X | 전화번호 |
| password | String | X | 비밀번호 |
| role | Integer | X | 권한 (0: USER, 1: ADMIN) |

#### 응답
```json
{
  "success": true,
  "message": "회원 정보가 성공적으로 수정되었습니다.",
  "data": {
    "id": 1,
    "loginId": "user01",
    "name": "홍길동",
    "email": "newemail@example.com",
    "phone": "010-9876-5432",
    "status": "ACTIVE",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-15T00:00:00Z"
  }
}
```

### 2.6 회원 삭제
**DELETE** `/api/members/{id}`

회원을 삭제합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 응답
```json
{
  "success": true,
  "message": "회원이 성공적으로 삭제되었습니다.",
  "data": null
}
```

### 2.7 회원 비활성화
**PATCH** `/api/members/{id}/deactivate`

회원을 비활성화합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 응답
```json
{
  "success": true,
  "message": "회원이 비활성화되었습니다.",
  "data": null
}
```

### 2.8 회원 활성화
**PATCH** `/api/members/{id}/activate`

회원을 활성화합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 응답
```json
{
  "success": true,
  "message": "회원이 활성화되었습니다.",
  "data": null
}
```

### 2.9 회원 권한 변경
**PATCH** `/api/members/{id}/role`

회원의 권한을 변경합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 회원 ID |

#### 요청 본문
```json
{
  "role": 1
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| role | Integer | O | 권한 (0: USER, 1: ADMIN) |

#### 응답
```json
{
  "success": true,
  "message": "회원 역할이 변경되었습니다.",
  "data": null
}
```

### 2.10 로그인
**POST** `/api/members/login`

회원 로그인을 처리합니다.

#### 요청 본문
```json
{
  "loginId": "user01",
  "password": "password123"
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| loginId | String | O | 로그인 ID |
| password | String | O | 비밀번호 |

#### 응답 (성공)
```json
{
  "success": true,
  "message": "로그인에 성공했습니다.",
  "data": {
    "id": 1,
    "loginId": "user01",
    "name": "홍길동",
    "email": "user01@example.com",
    "phone": "010-1234-5678",
    "status": "ACTIVE",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
}
```

#### 응답 (실패)
```json
{
  "success": false,
  "message": "존재하지 않는 아이디입니다.",
  "data": null
}
```

### 2.11 헬스 체크
**GET** `/api/members/health`

서비스 상태를 확인합니다.

#### 응답
```
OK
```

---

## 3. 대여 관리 API (Rental Service)

### 3.1 전체 대여 조회
**GET** `/api/rentals`

대여 목록을 조회합니다.

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "memberId": 1,
      "memberName": "홍길동",
      "bookId": 1,
      "bookTitle": "자바의 정석",
      "bookAuthor": "남궁성",
      "rentalStatus": "RENTED",
      "rentalDate": "2026-01-01T00:00:00Z",
      "dueDate": "2026-01-15T00:00:00Z",
      "returnDate": null,
      "remarks": "대여 메모"
    }
  ]
}
```

### 3.2 대여 상세 조회
**GET** `/api/rentals/{id}`

특정 대여의 상세 정보를 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 대여 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": 1,
    "memberId": 1,
    "memberName": "홍길동",
    "bookId": 1,
    "bookTitle": "자바의 정석",
    "bookAuthor": "남궁성",
    "rentalStatus": "RENTED",
    "rentalDate": "2026-01-01T00:00:00Z",
    "dueDate": "2026-01-15T00:00:00Z",
    "returnDate": null,
    "remarks": "대여 메모"
  }
}
```

### 3.3 회원별 대여 조회
**GET** `/api/rentals/member/{memberId}`

특정 회원의 대여 목록을 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| memberId | Long | O | 회원 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "memberId": 1,
      "memberName": "홍길동",
      "bookId": 1,
      "bookTitle": "자바의 정석",
      "bookAuthor": "남궁성",
      "rentalStatus": "RENTED",
      "rentalDate": "2026-01-01T00:00:00Z",
      "dueDate": "2026-01-15T00:00:00Z",
      "returnDate": null,
      "remarks": "대여 메모"
    }
  ]
}
```

### 3.4 도서별 대여 조회
**GET** `/api/rentals/book/{bookId}`

특정 도서의 대여 목록을 조회합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| bookId | Long | O | 도서 ID |

#### 응답
```json
{
  "success": true,
  "message": null,
  "data": [
    {
      "id": 1,
      "memberId": 1,
      "memberName": "홍길동",
      "bookId": 1,
      "bookTitle": "자바의 정석",
      "bookAuthor": "남궁성",
      "rentalStatus": "RETURNED",
      "rentalDate": "2026-01-01T00:00:00Z",
      "dueDate": "2026-01-15T00:00:00Z",
      "returnDate": "2026-01-14T00:00:00Z",
      "remarks": "대여 메모"
    }
  ]
}
```

### 3.5 도서 대여
**POST** `/api/rentals`

도서를 대여합니다.

#### 요청 본문
```json
{
  "memberId": 1,
  "bookId": 1,
  "dueDate": "2026-01-15",
  "remarks": "대여 메모"
}
```

#### 요청 필드
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| memberId | Long | O | 회원 ID |
| bookId | Long | O | 도서 ID |
| dueDate | LocalDate | O | 반납 예정일 (YYYY-MM-DD) |
| remarks | String | X | 비고 |

#### 응답
```json
{
  "success": true,
  "message": "도서가 성공적으로 대여되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "memberName": "홍길동",
    "bookId": 1,
    "bookTitle": "자바의 정석",
    "bookAuthor": "남궁성",
    "rentalStatus": "RENTED",
    "rentalDate": "2026-01-01T00:00:00Z",
    "dueDate": "2026-01-15T00:00:00Z",
    "returnDate": null,
    "remarks": "대여 메모"
  }
}
```

### 3.6 도서 반납
**PATCH** `/api/rentals/{id}/return`

대여한 도서를 반납합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 대여 ID |

#### 응답
```json
{
  "success": true,
  "message": "도서가 성공적으로 반납되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "memberName": "홍길동",
    "bookId": 1,
    "bookTitle": "자바의 정석",
    "bookAuthor": "남궁성",
    "rentalStatus": "RETURNED",
    "rentalDate": "2026-01-01T00:00:00Z",
    "dueDate": "2026-01-15T00:00:00Z",
    "returnDate": "2026-01-14T00:00:00Z",
    "remarks": "대여 메모"
  }
}
```

### 3.7 대여 기록 삭제
**DELETE** `/api/rentals/{id}`

대여 기록을 삭제합니다.

#### 경로 파라미터
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| id | Long | O | 대여 ID |

#### 응답
```json
{
  "success": true,
  "message": "대여 기록이 성공적으로 삭제되었습니다.",
  "data": null
}
```

### 3.8 헬스 체크
**GET** `/api/rentals/health`

서비스 상태를 확인합니다.

#### 응답
```
OK
```

---

## 에러 코드

### HTTP 상태 코드
| 코드 | 설명 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 |
| 404 | 리소스를 찾을 수 없음 |
| 500 | 서버 내부 오류 |

### 비즈니스 에러 메시지
- "존재하지 않는 아이디입니다."
- "비밀번호가 일치하지 않습니다."
- "비활성화된 계정입니다."
- "이미 존재하는 로그인 ID입니다."
- "도서를 찾을 수 없습니다."
- "회원을 찾을 수 없습니다."
- "대여 기록을 찾을 수 없습니다."
- "대여 가능한 도서가 없습니다."
- "이미 반납된 도서입니다."

---

## 참고 사항

1. 모든 날짜/시간은 ISO 8601 형식을 사용합니다.
2. 요청 본문의 날짜는 `YYYY-MM-DD` 형식을 사용합니다.
3. 응답의 날짜/시간은 UTC 기준 ISO 8601 형식(`YYYY-MM-DDTHH:mm:ssZ`)을 사용합니다.
4. 비밀번호는 평문으로 저장되므로 실제 운영 환경에서는 암호화가 필요합니다.
5. 인증/인가 기능은 현재 구현되어 있지 않습니다.
