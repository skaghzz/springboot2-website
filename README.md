# springboot2-website

## 개요
이동욱님이 쓰신 "스프링 부트와 AWS로 혼자 구현하는 웹 서비스" 책을 따라 개발을 진행합니다.

## 변경사항
도서의 개발환경 버전이 옛날입니다.(개발에는 1년도 옛날..)   
해당 프로젝트에서는 옛날 버전에 맞추지 않고 최신버전을 그대로 이용하겠습니다.   
변경된 버전은 다음과 같이 기재합니다.
- spring-boot 2.5.2
- gradle4.x -> gradle-7.1.1
- jdk8 -> jdk11
- JUnit4 -> JUnit5

## note
### chapter 3. 스프링 부트에서 JPA로 데이터베이스 다뤄보자
- Entity는 setter를 만들지 않는다. 목적과 의도를 확실히 구분한 메소드를 만든다.
- @Entity 어노테이션은 테이블과 연결되는 클래스를 나타낸다. 카멜케이스이름을 언더스코어 네이밍으로 테이블 이름을 매칭한다.
- lombok annotation은 필수가 아니라서 클래스명과 멀리 둔다. 그러면 kotlin으로 변경해도 쉽게 삭제가 가능하다.
- @ID == PK
- Entity 클래스와 Repository 클래스는 함께 움직여야하므로 도메인 패키지에서 함께 관리한다.
- applicatoin.properites 파일에 spring.jpa.show_sql=true 을 작성하면 실행되는 쿼리로그를 확인할 수 있다.
- h2 db를 MySQL 쿼리 형식으로 사용하려면 application.properties 파일에 아래처럼 추가해야한다. 버전이 달라서 아래 내용을 적용해야한다.
```PROPERTIES
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.datasource.hikari.jdbc-url=jdbc:h2:mem://localhost/~/testdb;MODE=MYSQL
```

- spring web 계층
- ![spring web layer](spring-web-layer.png)
  - Web Layer
    - 외부 요청과 응답에 대한 전반적인 영역
    - 컨트롤러, JSP/Freemaker 등의 뷰 템플릿 영역
    - 필터(@Filter), 인터셉터, 컨트롤러 어드바이스(@ControllerAdvice) 등
  - Service Layer
    - @Service에 사용되는 서비스 영역
    - controller와 Dao의 중간 영역
    - @Transaction이 사용되어야 하는 영역
  - Repository Layer
    - database에 접근하는 영역
  - Dtos
    - Dto(Data Transfer Object)는 계층 간에 데이터 교환을 위한 객체
  - Domain Model
    - 도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화시킨 것을 도메인 모델이라고 함
- 비즈니스 처리는 Domain에서 담당해야한다.
- 서비스 메소드는 트랜젝션과 도메인 간의 순서만 보장
- 로직은 도메인 내부에서 처리 됨

- Date/Calendar 보다는 LocalDate, LocalDateTime을 사용을 권장
- JPA Auditing : Audit는 감독하고 검사하다는 뜻으로, 해당 데이터를 보고 있다가 생성 또는 수정이 발생하면 자동으로 값을 넣어주는 기능
- @MappedSuperclass 는 enitiy가 해당 클래스를 상속할 경우 필드들도 column으로 인식하도록 함
- @EntityListeners(AuditingEntityListener.class) 는 해당 클래스에 Auditing 기능을 포함 시킨다

### chapter 4. 머스테치로 화면 구성하기
- @Transactional 어노테이션에 readOnly = true 옵션을 주면 트랜잭션 범위는 유지하되 조회 기능만 남겨두기 때문에 속도가 빨라진다.

### chapter 5. 스프링 시큐리티와 OAuth2.0으로 로그인 기능 구현하기
- scope를 profile, email로 지정한다. 기본값은 openid, profile, email 인데 open id provider가 아닌 서비스를 위해서 별도로 OAuth2Service를 만들어야한다.<br>
하나의 OAuth2Service로 사용하기 위해서 일부러 openid scope를 빼고 등록한다.
```PROPERTIES
spring.security.oauth2.client.registration.google.scope=profile,email
```
- spring boot에서는 properties의 이름을 application-xxx.properties 로 만들면 xxx라는 이름의 profile이 생성되어 이를 통해 관리할 수 있다.
- spring security에서는 권한 코드에 항상 ROLE_이 앞에 있어야 한다.
- 세션 저장소로 톰캣, DB, 메모리 DB 가 있다.