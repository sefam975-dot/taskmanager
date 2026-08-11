# Task Manager Backend

Task Manager Backend, gorevlerin olusturulmasi, atanmasi, takip edilmesi, kullanici ve grup operasyonlarinin yonetilmesi icin gelistirilmis Spring Boot tabanli REST API servisidir. Uygulama JWT tabanli kimlik dogrulama kullanir ve admin/kullanici rollerine gore yetkilendirme saglar.

## Ozellikler

- Kullanici girisi ve JWT token uretimi
- Rol bazli endpoint yetkilendirmesi
- Admin kullanicisinin otomatik olusturulmasi
- Kullanici olusturma, listeleme, rol guncelleme ve pasif hale getirme
- Grup olusturma, grup listeleme ve gruba kullanici ekleme/cikarma
- Task olusturma, listeleme ve silme
- Task atama akislari: bireysel atama, gruptan herhangi bir kullaniciye atama, gruptaki herkese atama
- Task durum guncelleme ve durum gecmisi takibi
- Kullanici bildirimlerini listeleme ve okundu olarak isaretleme
- Geciken gorevler icin saatlik ceza kontrolu
- Global hata yonetimi ve validasyon kontrolleri
- Swagger/OpenAPI dokumantasyonu

## Kullanilan Teknolojiler

- Java 21
- Spring Boot 3.2.5
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2 Database
- JWT
- Lombok
- MapStruct
- Spring Validation
- Springdoc OpenAPI / Swagger
- Maven
- Docker

## Proje Yapisi

```text
src/
  main/
    java/com/project/taskmanager/
      application/        DTO'lar, servis arayuzleri, servis implementasyonlari ve uygulama hatalari
      domain/             Domain modelleri, enum'lar ve repository arayuzleri
      infrastructure/     Persistence, mapper, security, scheduler, swagger ve bootstrap katmani
      presentation/       REST controller'lar ve global exception handler
      TaskmanagerApplication.java
    resources/
      application.properties
      application-dev.properties
  test/
    java/                 Test siniflari
    resources/            Test ortam ayarlari
```

## Kurulum

Projeyi yerel ortamda calistirmak icin:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Varsayilan olarak backend su adreste baslar:

```text
http://localhost:8080
```

Windows ortaminda Maven Wrapper icin:

```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

## Gelistirme Profili

Yerel gelistirme icin H2 veritabani kullanmak isterseniz dev profilini calistirin:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Dev profilinde H2 memory database kullanilir ve uygulama kapaninca veri sifirlanir.

## Ortam Degiskenleri

Production/PostgreSQL ortami icin varsayilan ayarlar `application.properties` dosyasinda bulunur:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_manager
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:}
spring.jpa.hibernate.ddl-auto=validate
```

Yerel PostgreSQL kullanirken gerekli ortam degiskeni:

```bash
DB_PASSWORD=postgres_sifreniz
```

Render veya farkli bir deployment ortami kullaniliyorsa datasource URL, username ve password degerleri ilgili ortama gore ayarlanmalidir.

## Swagger

API dokumantasyonuna uygulama calisirken su adresten ulasilabilir:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON ciktisi:

```text
http://localhost:8080/v3/api-docs
```

JWT korumali endpointleri Swagger uzerinden test etmek icin login endpointinden alinan token `Bearer Token` olarak eklenmelidir.

## Varsayilan Admin Kullanicisi

Uygulama basladiginda admin kullanicisi otomatik olarak olusturulur veya guncellenir:

```text
username: admin
password: 123
role: ROLE_ADMIN
```

Production ortaminda bu bilgilerin guvenli sekilde degistirilmesi onerilir.

## Temel Endpointler

Auth:

```text
POST /api/v1/auth/login
```

Users:

```text
POST   /api/v1/users
GET    /api/v1/users
GET    /api/v1/users/{id}
GET    /api/v1/users/username/{username}
PATCH  /api/v1/users/{id}/role
DELETE /api/v1/users/{id}
```

Groups:

```text
POST   /api/v1/groups
GET    /api/v1/groups
GET    /api/v1/groups/{id}
GET    /api/v1/groups/user/{userId}
POST   /api/v1/groups/users
DELETE /api/v1/groups/{groupId}/users/{userId}
DELETE /api/v1/groups/{id}
```

Tasks:

```text
POST   /api/v1/tasks
GET    /api/v1/tasks/{id}
GET    /api/v1/tasks/active
DELETE /api/v1/tasks/{id}
```

Task Assignments:

```text
POST  /api/v1/task-assignments/assign
PATCH /api/v1/task-assignments/state
GET   /api/v1/task-assignments
GET   /api/v1/task-assignments/user/{userId}
GET   /api/v1/task-assignments/task/{taskId}
GET   /api/v1/task-assignments/group/{groupId}
```

Notifications:

```text
GET   /api/v1/notifications/user/{userId}
GET   /api/v1/notifications/user/{userId}/unread
PATCH /api/v1/notifications/{notificationId}/read
```

## Komutlar

```bash
./mvnw spring-boot:run
```

Gelistirme sunucusunu baslatir.

```bash
./mvnw test
```

Testleri calistirir.

```bash
./mvnw clean package
```

Projeyi derler ve jar dosyasi olusturur.

```bash
./mvnw clean package -DskipTests
```

Testleri calistirmadan production build olusturur.

## Docker

Projeyi Docker ile build etmek icin:

```bash
docker build -t taskmanager-backend .
```

Container calistirmak icin:

```bash
docker run -p 8080:8080 -e DB_PASSWORD=postgres_sifreniz taskmanager-backend
```

Dockerfile uygulamayi Java 21 ile build eder ve `PORT` ortam degiskeni varsa uygulamayi o porttan baslatir. `PORT` verilmezse varsayilan port `8080` kullanilir.

## Deployment

Backend servis Render veya benzeri bir platformda Dockerfile ya da Maven build ciktisi ile yayinlanabilir.

Production build icin:

```bash
./mvnw clean package -DskipTests
```

Frontend tarafinda API adresi asagidaki degisken ile backend servis adresine yonlendirilmelidir:

```text
VITE_API_BASE_URL=https://backend-servis-adresi
```
