# SpringBoot 백엔드 과제

SpringBoot 기반으로 JWT 인증 및 인가, 역할 기반 접근 제어, Swagger UI 문서화, JUnit 테스트,
GitHub Actions 기반 CI/CD, AWS EC2 배포까지 구현한 백엔드 과제입니다.

## 구현 사항

-[x] JWT 기반 회원가입 / 로그인 / 권한 부여 API 개발
-[x] 역할(Role) 기반 관리자 접근 제어
-[x] Swagger UI를 통한 API 문서화
-[x] JUnit 기반 테스트 작성
-[x] AWS EC2 배포 및 Nginx 리버스 프록시 구성

### ➕ 추가 기능

-[x] GitHub Actions 기반 CI/CD 자동화 파이프라인 구성

## 🛠 기술 스택

- Java17
- Spring Boot 3.5.x
- Spring Security + JWT
- Swagger (springdoc-openapi)
- JUnit5
- Gradle
- H2 (개발용 인메모리 DB)
- AWS EC2 (Ubuntu 24.04 LTS)
- Nginx (리버스 프록시)

## 📋주요 APIs

### 1. 회원가입 `POST /signup`

`요청`

```
{
  "username": "user123",
  "password": "1234Abcd!@",
  "nickname": "토마토"
}
```

`응답`

```
{
  "username": "user123",
  "nickname": "토마토",
  "roles": [
    {
      "role": "USER"
    }
  ]
}
```

### 2. 로그인 `POST /login`

`요청`

```
{
  "username": "user123",
  "password": "1234Abcd!@"
}
```

`응답`

```
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3. 관리자 권한 부여 `PATCH /admin/users/{userId}/roles`

`응답`

```
{
  "username": "admin1234",
  "nickname": "관리자12",
  "roles": [
    {
      "role": "ADMIN"
    }
  ]
}
```

## 🏁테스트

JUnit 기반 테스트가 작성되어 있으며, 다음 명령어로 실행할 수 있습니다.

```
./gradlew test
```

## 📘 Swagger 문서

### Swagger UI:

http://13.125.248.110:8080/swagger-ui/index.html

### ☁ 배포 정보

- EC2 주소: http://13.125.248.110
- 서버 환경:
    - 운영체제: Ubuntu 24.04 LTS
    - JDK: OpenJDK 17 (Temurin)
    - 웹 서버: Nginx (리버스 프록시 구성)

## 📁 프로젝트 구조

```
src
├── main
│   ├── java/com/server/baro
│   │   ├── common/exception       # 예외 처리
│   │   ├── config                 # 설정
│   │   ├── security/jwt           # JWT 보안
│   │   └── user
│   │       ├── controller         # API 컨트롤러
│   │       ├── dto
│   │       │   ├── request        # 요청 DTO
│   │       │   └── response       # 응답 DTO
│   │       ├── entity             # 엔티티
│   │       ├── repository         # DB 접근
│   │       ├── service            # 비즈니스 로직
│   │       └── util               # 유틸리티
│   └── resources
│       ├── static                 # 정적 리소스
│       └── templates              # 템플릿
└── test
    ├── java/com/server/baro/user/controller  # 컨트롤러 테스트
    └── resources                            # 테스트 리소스
```
