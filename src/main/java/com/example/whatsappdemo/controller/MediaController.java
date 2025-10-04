package com.example.whatsappdemo.controller;

import com.example.whatsappdemo.service.MediaService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Add CORS support
public class MediaController {
    
    @Autowired
    private MediaService mediaService;

    /**
     * Get media URL by mediaId
     */
    @GetMapping("/url/{mediaId}")
    public ResponseEntity<Map<String, String>> getMediaUrl(@PathVariable String mediaId) {
        try {
            String url = mediaService.getMediaUrl(mediaId);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to retrieve media URL: " + e.getMessage()));
        }
    }

    /**
     * Upload media
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {

        Map<String, Object> result = mediaService.uploadMedia(file, type);
        return ResponseEntity.ok(result);
    }

    /**
     * Download media by mediaId
     */
    @GetMapping("/download/{mediaId}")
    public ResponseEntity<byte[]> download(@PathVariable String mediaId) throws Exception {
        byte[] data = mediaService.downloadMedia(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}