package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;

    /**
     * Dosya yükleme endpoint'i
     * Kullanım: POST /api/files/upload
     * Form-data: file = [seçilen dosya]
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Dosya boş mu kontrol et
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Dosya seçilmedi"));
            }

            // Dosya türü validasyonu
            if (!fileUploadService.isValidFileType(file)) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Desteklenmeyen dosya türü. Sadece resim, PDF ve text dosyaları yüklenebilir."));
            }

            // Dosyayı kaydet
            String savedFileName = fileUploadService.saveFile(file);
            
            log.info("Dosya başarıyla yüklendi: {} -> {}", file.getOriginalFilename(), savedFileName);

            // Başarılı response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dosya başarıyla yüklendi");
            response.put("fileName", savedFileName);
            response.put("originalFileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("downloadUrl", "/api/files/download/" + savedFileName);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Dosya yüklenirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Dosya yüklenemedi: " + e.getMessage()));
        }
    }

    /**
     * Dosya indirme endpoint'i
     * Kullanım: GET /api/files/download/{fileName}
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // Dosya var mı kontrol et
            if (!fileUploadService.fileExists(fileName)) {
                return ResponseEntity.notFound().build();
            }

            // Dosya yolunu al
            Path filePath = fileUploadService.getFilePath(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            // Resource'u kontrol et
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Content-Type'ı belirle
            String contentType = determineContentType(fileName);

            log.info("Dosya indiriliyor: {}", fileName);

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);

        } catch (MalformedURLException e) {
            log.error("Dosya indirme hatası: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Dosya silme endpoint'i (Admin veya dosya sahibi)
     * Kullanım: DELETE /api/files/{fileName}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileName) {
        try {
            if (!fileUploadService.fileExists(fileName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Dosya bulunamadı"));
            }

            fileUploadService.deleteFile(fileName);
            
            log.info("Dosya silindi: {}", fileName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dosya başarıyla silindi");
            response.put("fileName", fileName);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Dosya silinirken hata: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Dosya silinemedi: " + e.getMessage()));
        }
    }

    /**
     * Dosya bilgisi endpoint'i
     * Kullanım: GET /api/files/info/{fileName}
     */
    @GetMapping("/info/{fileName}")
    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable String fileName) {
        if (!fileUploadService.fileExists(fileName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("Dosya bulunamadı"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("fileName", fileName);
        response.put("exists", true);
        response.put("downloadUrl", "/api/files/download/" + fileName);

        return ResponseEntity.ok(response);
    }

    /**
     * Error response helper
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }

    /**
     * Dosya uzantısına göre Content-Type belirle
     */
    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
            return "image/" + fileName.substring(fileName.lastIndexOf(".") + 1);
        } else if (fileName.toLowerCase().endsWith(".txt")) {
            return "text/plain";
        }
        return "application/octet-stream"; // Default
    }
}