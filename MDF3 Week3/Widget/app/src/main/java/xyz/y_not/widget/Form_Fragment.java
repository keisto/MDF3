package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################

import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Form_Fragment extends Fragment {
    private EditText name;
    private EditText age;
    private EditText gender;
    private EditText phone;
    Contact_Data contacts = new Contact_Data();
    List<Contact> contactList = contacts.getContacts();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_fragment, container, false);

        // Get UI Form
        name = (EditText) view.findViewById(R.id.name);
        age = (EditText) view.findViewById(R.id.age);
        gender = (EditText) view.findViewById(R.id.gender);
        phone = (EditText) view.findViewById(R.id.phone);

        Button addContact = (Button) view.findViewById(R.id.addContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.createContact(
                        name.getText().toString(), age.getText().toString(),
                        gender.getText().toString(), phone.getText().toString());
                saveData(); // Save Data as JSON
                Remote_Factory factory = new Remote_Factory(getActivity());
                factory.onDataSetChanged(); // Load new Data to Factory
                getActivity().finish();
            }
        });

        return view;
    }

    private void saveData() {
        // Save Data to Recall Later
        File external = getActivity().getExternalFilesDir(null);
        File file = new File(external, "contacts.txt");
        // Convert to JSON Object
        ArrayList<JSONObject> contactData = new ArrayList<JSONObject>();
        for (int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            JSONObject contactItem = new JSONObject();
            try {
                contactItem.put("Name", contact.getName());
                contactItem.put("Age", contact.getAge());
                contactItem.put("Gender", contact.getGender());
                contactItem.put("Phone", contact.getPhone());

                contactData.add(contactItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            // Write bytes to the stream
            for (int i = 0; i < contactData.size(); i++) {
                osw.write(contactData.get(i).toString() + "\n");
            }
            // Close the stream to save the file.
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


