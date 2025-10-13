package com.example.whatsappdemo.service;

import com.example.whatsappdemo.entity.Contact;
import com.example.whatsappdemo.repo.ContactRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepo contactRepo;

    public List<Contact> getAllContacts(int page, int size) {
        return contactRepo.findAll(PageRequest.of(page, size,Sort.by("lastMessage").descending())).toList();
    }

    public Contact addContact(String phoneNumber, String name) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (contactRepo.existsByPhoneNumber(phoneNumber)) {
            Contact existing = contactRepo.findByPhoneNumber(phoneNumber).orElse(null);
            if (existing != null && !existing.getName().equals(name)) {
                throw new RuntimeException("Contact already exists with a different name");
            }
        }
        return contactRepo.save(Contact.builder()
                .phoneNumber(phoneNumber)
                .name(name)
                .build());
    }

    public void deleteContact(Long id) {
        if (!contactRepo.existsById(id)) {
            throw new RuntimeException("Contact not found");
        }
        contactRepo.deleteById(id);
    }
}