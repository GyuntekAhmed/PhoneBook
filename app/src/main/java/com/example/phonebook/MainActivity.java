package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ContactManager contactManager;
    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber;
    private Button btnAddContact, btnSearchContact, btnDeleteContact;
    private ListView listViewContacts;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализирай ContactManager
        contactManager = new ContactManager();
//        listViewContacts = findViewById(R.id.listViewContacts);
        // Създайте адаптер
        contactAdapter = new ContactAdapter(MainActivity.this, R.layout.list_item_contact, contacts);

        listViewContacts = findViewById(R.id.listViewContacts);
        listViewContacts.setAdapter(contactAdapter);


        // Инициализирай EditText компонентите от layout файла
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        // Инициализирай Button компонентите от layout файла
        btnAddContact = findViewById(R.id.btnAddContact);
        btnSearchContact = findViewById(R.id.btnSearchContact);
        btnDeleteContact = findViewById(R.id.btnDeleteContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                if (firstName.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Моля попълнете празните полета", Toast.LENGTH_SHORT).show();
                } else {
                    Contact newContact = new Contact(firstName, lastName, phoneNumber);

                    contactManager.addContact(newContact);
                    contacts.add(newContact);
                    contactAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Контакта е добавен успешно.", Toast.LENGTH_SHORT).show();
                }

                editTextFirstName.setText("");
                editTextLastName.setText("");
                editTextPhoneNumber.setText("");
            }
        });

        // Логика за търсене на контакт
        btnSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    // Търсене на контакт по ключовата дума
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
                        Toast.makeText(MainActivity.this, "Контакт с такова име не съществува", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Ако всички полета са празни
                    Toast.makeText(MainActivity.this, "Моля въведете име или телефонен номер за търсене", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Логика за изтриване на контакт
        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty()) {
                    Contact contactToDelete = contactManager.searchContact(firstName);

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
            }
        });
    }
}
