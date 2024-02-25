package com.example.phonebook;

import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private List<Contact> contacts;

    public ContactManager() {
        this.contacts = new ArrayList<>();
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }

    public void editContact(Contact oldContact, Contact newContact){
        if (contacts.contains(oldContact)){
            int index = contacts.indexOf(oldContact);
            contacts.set(index, newContact);
        }
    }

    public Contact searchContact(String keyword) {

        for (Contact contact : contacts) {
            if (contact.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                    contact.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                    contact.getPhoneNumber().contains(keyword)) {

                return contact;
            }
        }
        return null;
    }

    public void deleteContact(Contact contact){
        contacts.remove(contact);
    }
}
