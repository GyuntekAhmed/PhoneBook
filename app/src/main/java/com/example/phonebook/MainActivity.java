package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phonebook.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber;
    private Button btnAddContact, btnSearchContact, btnDeleteContact;
    private ListView listViewContacts;
    private ContactAdapter contactAdapter;
    private final List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this);
        contactAdapter = new ContactAdapter(MainActivity.this, R.layout.list_item_contact, contacts);

        listViewContacts = findViewById(R.id.listViewContacts);
        listViewContacts.setAdapter(contactAdapter);

        loadContacts();

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

            if (firstName.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(MainActivity.this, "Моля попълнете празните полета", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = dbHelper.searchContact(phoneNumber);
                if (cursor != null && cursor.moveToFirst()) {
                    Toast.makeText(MainActivity.this, "Телефонният номер вече съществува", Toast.LENGTH_SHORT).show();
                    cursor.close();
                } else {
                    dbHelper.addContact(firstName, lastName, phoneNumber);
                    Contact newContact = new Contact(firstName, lastName, phoneNumber);
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
                Cursor foundContact = dbHelper.searchContact(keyword);

                if (foundContact != null && foundContact.moveToFirst()) {
                    int firstNameIndex = foundContact.getColumnIndex(MyDatabaseHelper.COLUMN_FIRST_NAME);
                    int lastNameIndex = foundContact.getColumnIndex(MyDatabaseHelper.COLUMN_LAST_NAME);
                    int phoneNumberIndex = foundContact.getColumnIndex(MyDatabaseHelper.COLUMN_PHONE_NUMBER);

                    editTextFirstName.setText(foundContact.getString(firstNameIndex));
                    editTextLastName.setText(foundContact.getString(lastNameIndex));
                    editTextPhoneNumber.setText(foundContact.getString(phoneNumberIndex));
                    Toast.makeText(MainActivity.this, "Контактът е намерен", Toast.LENGTH_SHORT).show();

                } else {
                    editTextFirstName.setText("");
                    editTextLastName.setText("");
                    editTextPhoneNumber.setText("");
                    Toast.makeText(MainActivity.this, "Контактът не съществува", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Моля въведете име за търсене", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteContact.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            Cursor cursor = dbHelper.searchContact(phoneNumber);

            if (cursor != null && cursor.moveToFirst()) {
                int contactIdIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ID);

                if (contactIdIndex != -1) {
                    int contactId = cursor.getInt(contactIdIndex);
                    dbHelper.deleteContact(contactId);

                    Contact deletedContact = new Contact(firstName, lastName, phoneNumber);
                    contacts.remove(deletedContact);

                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Контактът е изтрит успешно!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Грешка при изтриване на контакта", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Контактът не съществува", Toast.LENGTH_SHORT).show();
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

    private void loadContacts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {MyDatabaseHelper.COLUMN_FIRST_NAME, MyDatabaseHelper.COLUMN_LAST_NAME, MyDatabaseHelper.COLUMN_PHONE_NUMBER};

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int firstNameIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_LAST_NAME);
                int phoneNumberIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_PHONE_NUMBER);

                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);

                Contact contact = new Contact(firstName, lastName, phoneNumber);
                contacts.add(contact);
            } while (cursor.moveToNext());

            cursor.close();

            contactAdapter.notifyDataSetChanged();
        }
    }
}
