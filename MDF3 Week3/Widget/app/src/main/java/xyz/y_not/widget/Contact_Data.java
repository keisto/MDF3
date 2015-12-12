package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact_Data implements Serializable {
    private List<Contact> contacts = new ArrayList<>();
    public List<Contact> getContacts() { return contacts; }
    private static final long serialVersionUID = 0L; // Data Not Serialized

    public void createContact(String name, String age, String gender, String phone) {
        contacts.add(new Contact(name, age, gender, phone));
    }
    public Contact_Data() {

    }
}
