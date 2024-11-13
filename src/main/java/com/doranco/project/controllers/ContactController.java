package com.doranco.project.controllers;

import com.doranco.project.entities.Contact;
import com.doranco.project.services.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Contact> submitContactForm(@RequestBody Contact contact) {
        Contact savedContact = contactService.saveContact(contact);
        return ResponseEntity.ok(savedContact);
    }
    @GetMapping("/all")
public ResponseEntity<List<Contact>> fetchContacts() {
        List<Contact> contactList = contactService.getAllContacts();
        return ResponseEntity.ok(contactList);
    }

}
