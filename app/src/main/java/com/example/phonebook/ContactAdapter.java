package com.example.phonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context mContext;
    private int mResource;

    public ContactAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        TextView textViewContactFirstName = convertView.findViewById(R.id.textFirstName);
        TextView textViewContactLastName = convertView.findViewById(R.id.textLastName);
        TextView textViewContactPhoneNumber = convertView.findViewById(R.id.textPhoneNumber);

        Contact contact = getItem(position);
        if (contact != null) {
            textViewContactFirstName.setText(contact.getFirstName());
            textViewContactLastName.setText(contact.getLastName());
            textViewContactPhoneNumber.setText(contact.getPhoneNumber());
        }

        return convertView;
    }
}

