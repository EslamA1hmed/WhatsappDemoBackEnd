package com.example.whatsappdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.api.version:v20.0}")
    private String apiVersion;

    @Value("${whatsapp.api.url}")
    private String apiUrl; // https://graph.facebook.com/v20.0/{phone_number_id}

    @Value("${whatsapp.api.token}")
    private String apiToken;

    /**
     * Upload any media file (image/video/audio/document) to WhatsApp Cloud API
     */
    public Map<String, Object> uploadMedia(MultipartFile file, String type) {
        String url = apiUrl + "/media";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("messaging_product", "whatsapp");
        body.add("type", type);
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    /**
     * Public method: Retrieve media URL from WhatsApp using media ID
     */
    public String getMediaUrl(String mediaId) {
        String url = "https://graph.facebook.com/" + apiVersion + "/" + mediaId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("url")) {
            return (String) responseBody.get("url");
        }
        
        throw new RuntimeException("Media URL not found in response");
    }

    /**
     * Download media file using the retrieved URL
     */
    public byte[] downloadMedia(String mediaId) {
        String downloadUrl = getMediaUrl(mediaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );

        return response.getBody();
    }

    /**
     * Save media locally (optional)
     */
    public void saveMediaLocally(byte[] data, String filename) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }
}