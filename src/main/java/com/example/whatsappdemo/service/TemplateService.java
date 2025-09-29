package com.example.whatsappdemo.service;

import com.example.whatsappdemo.dto.WhatsAppTemplateCreateDTO;
import com.example.whatsappdemo.dto.WhatsAppTemplateCreateResponseDTO;
import com.example.whatsappdemo.dto.WhatsAppTemplatesResponseDTO;
import com.example.whatsappdemo.entity.Template;
import com.example.whatsappdemo.mapper.TemplateMapper;
import com.example.whatsappdemo.repo.TemplateRepo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TemplateService {
    @Value("${whatsapp.api.url_tem}")
    private String apiUrl;

     @Value("${whatsapp.api.url}")
    private String apiUrl2;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Autowired
    private TemplateRepo templateRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public WhatsAppTemplateCreateResponseDTO createTemplate(WhatsAppTemplateCreateDTO whatsAppTemplateCreateDTO) {
        String url = apiUrl + "/message_templates";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<WhatsAppTemplateCreateDTO> request = new HttpEntity<>(whatsAppTemplateCreateDTO, headers);

        ResponseEntity<WhatsAppTemplateCreateResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                WhatsAppTemplateCreateResponseDTO.class);
        whatsAppTemplateCreateDTO.setStatus(response.getBody().getStatus());
        templateRepo.save(TemplateMapper.toEntity(whatsAppTemplateCreateDTO));

        return response.getBody();
    }

    public WhatsAppTemplatesResponseDTO getAllTemplates() {
        String url = apiUrl + "/message_templates";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<WhatsAppTemplatesResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                WhatsAppTemplatesResponseDTO.class);

        return response.getBody(); // ده بيرجع Object فيه data + paging
    }
public WhatsAppTemplatesResponseDTO.TemplateDTO getTemplateByName(String templateName) {
    String url = apiUrl + "/message_templates?name=" + URLEncoder.encode(templateName, StandardCharsets.UTF_8);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    ResponseEntity<WhatsAppTemplatesResponseDTO> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            WhatsAppTemplatesResponseDTO.class);

    WhatsAppTemplatesResponseDTO body = response.getBody();
    if (body != null && body.getData() != null && !body.getData().isEmpty()) {
        return body.getData().get(0);
    }
    return null;
}
public WhatsAppTemplatesResponseDTO.TemplateDTO getTemplateByNameV2(String templateName) {
    List<Template> l = new ArrayList<>();
    l.add(templateRepo.findByName(templateName));
    return TemplateMapper.toResponse(l).getData().get(0);
}
 public Page<String> getTemplateNames(Pageable pageable) {
        return templateRepo.findAllTemplateNames(pageable);
    }

    public Map<String, String> uploadMedia(MultipartFile file, String type) {
        String url = apiUrl2 + "/media";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("messaging_product", "whatsapp");
        body.add("type", type);
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            Map.class
        );

        return response.getBody();
    }
}