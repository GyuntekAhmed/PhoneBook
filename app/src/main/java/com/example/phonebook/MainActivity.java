package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactManager contactManager;
    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber;
    private Button btnAddContact, btnSearchContact, btnDeleteContact;
    private ListView listViewContacts;
    private ContactAdapter contactAdapter;
    private final List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactManager = new ContactManager();
        contactAdapter = new ContactAdapter(MainActivity.this, R.layout.list_item_contact, contacts);

        listViewContacts = findViewById(R.id.listViewContacts);
        listViewContacts.setAdapter(contactAdapter);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        btnAddContact = findViewById(R.id.btnAddContact);
        btnSearchContact = findViewById(R.id.btnSearchContact);
        btnDeleteContact = findViewById(R.id.btnDeleteContact);

        btnAddContact.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            if (firstName.isEmpty() && phoneNumber.isEmpty()) {
                Toast.makeText(MainActivity.this, "Моля попълнете празните полета", Toast.LENGTH_SHORT).show();
            } else {

                if (contactManager.searchContact(phoneNumber) != null){
                    Toast.makeText(MainActivity.this, "Телефонният номер вече съществува", Toast.LENGTH_SHORT).show();
                } else {
                    Contact newContact = new Contact(firstName, lastName, phoneNumber);

                    contactManager.addContact(newContact);
                    contacts.add(newContact);

                    Collections.sort(contacts, (contact1, contact2) ->
                            contact1.getFirstName().compareToIgnoreCase(contact2.getFirstName()));

                    contactAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Контакта е добавен успешно.", Toast.LENGTH_SHORT).show();

                    editTextFirstName.setText("");
                    editTextLastName.setText("");
                    editTextPhoneNumber.setText("");
                }
            }
        });

        btnSearchContact.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String phone = editTextPhoneNumber.getText().toString().trim();

            String keyword = "";

            if (!firstName.isEmpty()) {
                keyword = firstName;
            } else if (!lastName.isEmpty()) {
                keyword = lastName;
            } else if (!phone.isEmpty()) {
                keyword = phone;
            }

            if (!keyword.isEmpty()) {
                Contact foundContact = contactManager.searchContact(keyword);

                if (foundContact != null) {
                    editTextFirstName.setText(foundContact.getFirstName());
                    editTextLastName.setText(foundContact.getLastName());
                    editTextPhoneNumber.setText(foundContact.getPhoneNumber());
                    Toast.makeText(MainActivity.this, "Контактът е намерен", Toast.LENGTH_SHORT).show();
                } else {
                    editTextFirstName.setText("");
                    editTextLastName.setText("");
                    editTextPhoneNumber.setText("");
                    Toast.makeText(MainActivity.this, "Контактът не съществува", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Моля въведете име или телефонен номер за търсене", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteContact.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            String keyword = "";

            if (!firstName.isEmpty()) {
                keyword = firstName;
            } else if (!lastName.isEmpty()) {
                keyword = lastName;
            } else if (!phoneNumber.isEmpty()) {
                keyword = phoneNumber;
            }

            if (!keyword.isEmpty()) {

                Contact contactToDelete = contactManager.searchContact(keyword);

                if (contactToDelete != null) {
                    contactManager.deleteContact(contactToDelete);
                    contacts.remove(contactToDelete);
                    contactAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Контактът е изтрит успешно!", Toast.LENGTH_SHORT).show();
                }
            }

            editTextFirstName.setText("");
            editTextLastName.setText("");
            editTextPhoneNumber.setText("");
        });

        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = (Contact) parent.getItemAtPosition(position);
            if (selectedContact != null) {
                editTextFirstName.setText(selectedContact.getFirstName());
                editTextLastName.setText(selectedContact.getLastName());
                editTextPhoneNumber.setText(selectedContact.getPhoneNumber());
            }
        });
    }
}
