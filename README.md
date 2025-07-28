# 📚 Kütüphane Randevu Sistemi

> Modern kütüphane yönetimi için geliştirilmiş, kapsamlı randevu sistemi

Bu proje, üniversite ve kurumsal kütüphanelerde **oda rezervasyonu** ve **randevu yönetimi** için geliştirilmiş, tam özellikli bir REST API sistemidir. JWT tabanlı güvenlik, gerçek zamanlı bildirimler, analytics dashboard ve dosya yükleme özellikleri ile modern kütüphane ihtiyaçlarını karşılar.

## ✨ Temel Özellikler

### 🔐 Güvenlik & Kimlik Doğrulama
- **JWT tabanlı kimlik doğrulama** - Güvenli token sistemi
- **Rol bazlı yetkilendirme** - USER ve ADMIN rolleri
- **Spring Security entegrasyonu** - Kapsamlı güvenlik yapılandırması
- **Şifre değiştirme** ve **hesap yönetimi**

### 📅 Gelişmiş Randevu Yönetimi
- **Akıllı çakışma kontrolü** - Aynı oda/saat çakışması önleme
- **Zaman validasyonu** - Geçmiş tarih/saat kontrolü
- **Randevu durumu yönetimi** - ACTIVE, CANCELLED, COMPLETED
- **Toplu randevu işlemleri** - Çoklu seçim ve işlem
- **Dinamik filtreleme** - Tarih, saat, oda, kullanıcı bazlı

### 🏢 Oda Yönetimi
- **Oda CRUD işlemleri** - Tam yönetim sistemi
- **Kapasite kontrolü** ve **özellik yönetimi**
- **Oda durumu takibi** - Aktif/pasif yönetimi
- **Popülerlik analizi** - En çok tercih edilen odalar

### 🔔 Gerçek Zamanlı Bildirim Sistemi
- **WebSocket entegrasyonu** - Anlık bildirimler
- **Otomatik bildirimler** - Randevu değişikliklerinde
- **Bildirim geçmişi** - Okundu/okunmadı durumu
- **Admin duyuruları** - Toplu bildirim gönderimi
- **Zamanlanmış hatırlatmalar** - Cron job ile otomatik

### 📊 Analytics & Dashboard
- **Admin dashboard** - Kapsamlı istatistikler
- **Kullanım analizi** - Günlük/haftalık raporlar
- **Oda doluluk oranları** - Haftalık pattern analizi
- **Popülerlik metrikleri** - En çok kullanılan odalar
- **Gerçek zamanlı istatistikler** - Canlı veri takibi

### 📎 Dosya Yönetimi
- **Dosya yükleme sistemi** - Güvenli upload
- **Çoklu format desteği** - Resim, PDF, döküman
- **Boyut kontrolü** - Maksimum 10MB limit
- **Dosya validasyonu** - MIME type kontrolü

### 🕐 Zamanlanmış İşler
- **Otomatik hatırlatmalar** - Günlük cron job
- **Eski randevu temizliği** - Periyodik bakım
- **Bildirim gönderimi** - Zamanlanmış duyurular
- **Sistem bakımı** - Otomatik temizlik işleri

## 🛠️ Teknoloji Stack'i

### Backend
| Teknoloji | Versiyon | Açıklama |
|-----------|----------|----------|
| **Java** | 17       | Modern Java özellikleri ile geliştirildi |
| **Spring Boot** | 3.2.5    | Enterprise seviye framework |
| **Spring Security** | 6.x      | JWT tabanlı güvenlik sistemi |
| **Spring Data JPA** | 3.x      | ORM ve veritabanı erişimi |
| **Spring WebSocket** | -        | Gerçek zamanlı bildirimler |
| **MySQL** | 8.x      | İlişkisel veritabanı |
| **JWT** | 0.11.5   | Token tabanlı kimlik doğrulama |
| **Lombok** | 1.18.32  | Kod sadeleştirme |
| **Maven** | 3.x      | Dependency yönetimi |
| **SpringDoc OpenAPI** | 2.3.0    | API dokümantasyonu |

### Frontend
| Teknoloji | Versiyon | Açıklama |
|-----------|----------|----------|
| **React** | 19.1.0 | Modern UI kütüphanesi |
| **React Router** | 7.7.1 | SPA routing sistemi |
| **Bootstrap** | 5.3.7 | Responsive UI framework |
| **Axios** | 1.11.0 | HTTP client |
| **Formik** | 2.4.6 | Form yönetimi |

### Özellikler
- 🏗️ **RESTful API** tasarımı
- 🔒 **JWT Authentication** & **Authorization**
- 📱 **Responsive Design** - Mobil uyumlu
- 🚀 **Real-time Updates** - WebSocket ile
- 📈 **Analytics Dashboard** - Detaylı raporlama
- 🗂️ **File Upload** - Güvenli dosya yönetimi
- ⏰ **Scheduled Tasks** - Cron job entegrasyonu
- 🎯 **Role-based Access** - Kullanıcı yetkilendirme

## 🚀 Kurulum ve Çalıştırma

### Ön Gereksinimler
- ☕ **Java 17** veya üzeri
- 🗄️ **MySQL 8.0** veya üzeri
- 📦 **Node.js 18+** (Frontend için)
- 🔧 **Maven 3.6+** (veya wrapper kullanın)

### 1️⃣ Backend Kurulumu

```bash
# Repository'yi klonla
git clone https://github.com/berkuyar/kutuphane-randevu.git
cd kutuphane-randevu/kutuphane-randevu

# MySQL veritabanını oluştur
mysql -u root -p
CREATE DATABASE kutuphane;
EXIT;

# application.properties'i düzenle
# Veritabanı bilgilerini güncelle:
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# Projeyi derle ve çalıştır
./mvnw clean install
./mvnw spring-boot:run
```

**Backend çalışacağı adres:** `http://localhost:8080`

### 2️⃣ Frontend Kurulumu

```bash
# Frontend dizinine geç
cd ../kutuphane-ui

# Bağımlılıkları yükle
npm install

# Development server'ı başlat
npm start
```

**Frontend çalışacağı adres:** `http://localhost:3000`

### 3️⃣ Varsayılan Kullanıcılar

Sistem ilk çalıştırıldığında otomatik olarak admin kullanıcısı oluşturulur:

```
👤 Admin Kullanıcı:
Email: admin@kutuphane.com
Şifre: admin123

👤 Test Kullanıcı:
Email: user@kutuphane.com
Şifre: user123
```

## 📖 API Dokümantasyonu

Uygulama çalıştırıldıktan sonra Swagger UI'ya şu adresten erişebilirsiniz:

🔗 **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

### Temel API Endpoint'leri

| Method | Endpoint | Açıklama | Yetkilendirme |
|--------|----------|----------|---------------|
| POST | `/api/auth/register` | Kullanıcı kaydı | Herkese açık |
| POST | `/api/auth/authenticate` | Giriş yap | Herkese açık |
| GET | `/api/appointments` | Randevuları listele | JWT Token |
| POST | `/api/appointments` | Randevu oluştur | JWT Token |
| GET | `/api/rooms` | Odaları listele | JWT Token |
| POST | `/api/rooms` | Oda oluştur | Admin yetkisi |
| GET | `/api/notifications` | Bildirimleri getir | JWT Token |
| GET | `/api/admin/dashboard` | Admin dashboard | Admin yetkisi |

## 🏗️ Proje Yapısı

```
kutuphane-randevu/
├── kutuphane-randevu/          # Backend (Spring Boot)
│   ├── src/main/java/
│   │   └── com/uyarberk/kutuphane_randevu/
│   │       ├── config/         # Security & WebSocket config
│   │       ├── controller/     # REST Controllers
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── exception/     # Custom Exceptions
│   │       ├── model/         # JPA Entities
│   │       ├── repository/    # Data Access Layer
│   │       ├── security/      # JWT & Security
│   │       ├── service/       # Business Logic
│   │       └── Scheduler/     # Cron Jobs
│   └── src/main/resources/
│       ├── application.properties
│       └── static/            # File uploads
└── kutuphane-ui/              # Frontend (React)
    ├── src/
    │   ├── components/        # React Components
    │   ├── context/          # Context API
    │   ├── pages/            # Page Components
    │   ├── services/         # API Services
    │   └── utils/            # Utility Functions
    └── public/
```

## 🔧 Yapılandırma

### Environment Variables

```bash
# .env dosyası oluştur (opsiyonel)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=kutuphane
DB_USERNAME=root
DB_PASSWORD=your_password

JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

FILE_UPLOAD_DIR=uploads/
```

### Application Properties

```properties
# Veritabanı yapılandırması
spring.datasource.url=jdbc:mysql://localhost:3306/kutuphane
spring.datasource.username=root
spring.datasource.password=1234

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.dir=uploads/
```

## 🧪 Test

```bash
# Backend testleri
cd kutuphane-randevu
./mvnw test

# Frontend testleri
cd kutuphane-ui
npm test
```

## 📱 Screenshots

**(Not: UI geliştirildikten sonra ekran görüntüleri eklenecek)**

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📝 License

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakınız.

## 📧 İletişim

Sorularınız için:
- 📧 Email: berkuyar@email.com
- 💼 LinkedIn: [Berk Uyar](https://linkedin.com/in/berkuyar)
- 🐙 GitHub: [@berkuyar](https://github.com/berkuyar)

---

<div align="center">
  <h3>🎯 Modern Kütüphane Yönetimi için Geliştirildi</h3>
  <p><strong>👨‍💻 Developed with ❤️ by Berk Uyar</strong></p>
  <p><em>"Teknoloji ile eğitimi buluşturmak"</em></p>
</div>