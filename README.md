# 📚 Kütüphane Randevu Sistemi

Bu proje, üniversite ve benzeri kurumlarda öğrencilerin **kütüphane odaları** için online randevu almasını sağlayan, güvenli ve özelleştirilebilir bir **Spring Boot REST API** sistemidir.  
JWT ile kimlik doğrulama, rol bazlı yetkilendirme, bildirim sistemi, cron ile zamanlanmış işler ve admin paneli gibi birçok gelişmiş özellik içerir.

---

## 🚀 Özellikler

### 🔐 Kimlik Doğrulama ve Roller
- Kullanıcı kayıt ve giriş (JWT ile)
- Rol bazlı yetkilendirme: `USER` ve `ADMIN`
- Token ile güvenli erişim, sadece kendi verini görme

### 📅 Randevu Yönetimi
- Randevu oluşturma, görüntüleme, güncelleme, silme
- Çakışma kontrolü: Aynı odada aynı saatte birden fazla randevu alınamaz
- Geçmiş tarih ve saat kontrolü
- Aktif / silinmiş randevu ayrımı (opsiyonel soft-delete)

### 🔎 Filtreleme
- Randevuları **tarih**, **saat**, **oda ID’si** gibi parametrelere göre filtreleme
- Kullanıcı sadece kendi randevularını görüntüleyebilir
- Admin tüm randevuları görebilir

### 🔔 Bildirim Sistemi
- Randevu oluşturma, güncelleme, silme sonrası otomatik bildirim
- Kullanıcı, sadece kendine ait bildirimleri görüntüler
- Bildirimler okundu/okunmadı olarak işaretlenebilir

### 🕰️ Cron Tabanlı Hatırlatma
- Her gün otomatik çalışan cron görevi
- Ertesi gün randevusu olan kullanıcıya bildirim gönderimi

### 📊 Admin Dashboard
- Toplam kullanıcı sayısı
- Bugünkü randevu sayısı
- Müsait oda sayısı
- En yoğun saat aralığı analizi

---

## 🛠️ Kullanılan Teknolojiler

| Teknoloji       | Açıklama                            |
|-----------------|-------------------------------------|
| Java 17         | Ana yazılım dili                    |
| Spring Boot     | Uygulama çatısı                     |
| Spring Security | JWT ile güvenli kimlik doğrulama    |
| JPA & Hibernate | ORM (veri erişim katmanı)           |
| MySQL           | Veritabanı                          |
| Lombok          | Kod sadeleştirme ve otomatik getter |
| Maven           | Proje yapılandırma                  |
| Cron Scheduler  | Zamanlanmış görevler (bildirimler)  |

---

## ⚙️ Kurulum

```bash
# 1. Projeyi klonla
git clone https://github.com/kullaniciadi/kutuphane-randevu.git
cd kutuphane-randevu

# 2. application.properties dosyasını yapılandır
src/main/resources/application.properties dosyasında veritabanı bilgilerini güncelle

# 3. Maven ile projeyi derle
./mvnw clean install

# 4. Uygulamayı başlat
./mvnw spring-boot:run

``` 
<p align="center"><b>👨‍💻 Developed by Berk Uyar</b></p> 
