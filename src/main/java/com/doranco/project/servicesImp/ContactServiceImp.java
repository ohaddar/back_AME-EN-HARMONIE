package com.doranco.project.servicesImp;

import com.doranco.project.entities.Contact;
import com.doranco.project.repositories.IContactRepository;
import com.doranco.project.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ContactServiceImp implements ContactService {
    @Autowired
    IContactRepository contactRepository;
    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);

    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
}
