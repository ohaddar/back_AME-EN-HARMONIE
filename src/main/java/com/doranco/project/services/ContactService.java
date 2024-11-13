package com.doranco.project.services;

import com.doranco.project.entities.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ContactService {
    public Contact saveContact(Contact contact);
    public List<Contact> getAllContacts();
}
