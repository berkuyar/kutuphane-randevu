package com.uyarberk.kutuphane_randevu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    // application.properties'den dosya klasörünü okur
    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * Dosyayı belirtilen klasöre kaydeder
     * @param file Kaydedilecek dosya
     * @return Kaydedilen dosyanın unique ismi
     */
    public String saveFile(MultipartFile file) {
        try {
            // Klasör yoksa oluştur
            createUploadDirectoryIfNotExists();
            
            // Unique dosya ismi oluştur (UUID + orijinal uzantı)
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
            
            // Dosya yolu oluştur
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            
            // Dosyayı kaydet
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Dosya başarıyla kaydedildi: {} -> {}", file.getOriginalFilename(), uniqueFileName);
            
            return uniqueFileName;
            
        } catch (IOException e) {
            log.error("Dosya kaydedilirken hata: {}", e.getMessage());
            throw new RuntimeException("Dosya kaydedilemedi: " + e.getMessage());
        }
    }

    /**
     * Dosyayı siler
     * @param fileName Silinecek dosya ismi
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(filePath);
            log.info("Dosya silindi: {}", fileName);
        } catch (IOException e) {
            log.error("Dosya silinirken hata: {}", e.getMessage());
            throw new RuntimeException("Dosya silinemedi: " + e.getMessage());
        }
    }

    /**
     * Dosya var mı kontrol eder
     * @param fileName Kontrol edilecek dosya ismi
     * @return true eğer dosya varsa
     */
    public boolean fileExists(String fileName) {
        Path filePath = Paths.get(uploadDir, fileName);
        return Files.exists(filePath);
    }

    /**
     * Dosyanın tam yolunu döner
     * @param fileName Dosya ismi
     * @return Dosyanın tam yolu
     */
    public Path getFilePath(String fileName) {
        return Paths.get(uploadDir, fileName);
    }

    /**
     * Upload klasörü yoksa oluşturur
     */
    private void createUploadDirectoryIfNotExists() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Upload klasörü oluşturuldu: {}", uploadDir);
        }
    }

    /**
     * Unique dosya ismi oluşturur (çakışmaları önler)
     * @param originalFileName Orijinal dosya ismi
     * @return UUID + orijinal uzantı
     */
    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + fileExtension;
    }

    /**
     * Dosya türü validasyonu
     * @param file Kontrol edilecek dosya
     * @return true eğer izin verilen türdeyse
     */
    public boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.startsWith("image/") ||          // Resim dosyaları
            contentType.equals("application/pdf") ||     // PDF
            contentType.startsWith("text/")              // Text dosyaları
        );
    }
}