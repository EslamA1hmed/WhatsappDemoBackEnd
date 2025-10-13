package com.example.whatsappdemo.controller;

import com.example.whatsappdemo.entity.Contact;
import com.example.whatsappdemo.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts(int page,int size) {
        return contactService.getAllContacts(page,size);
    }

    @PostMapping
    public ResponseEntity<?> addContact(@RequestBody Map<String, String> body) {
        try {
            Contact contact = contactService.addContact(
                    body.get("phoneNumber"),
                    body.get("name")
            );
            return ResponseEntity.ok(contact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok(Map.of("message", "Contact deleted"));
    }
}