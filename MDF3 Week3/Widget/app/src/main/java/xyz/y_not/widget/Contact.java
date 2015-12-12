package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################

import android.os.Bundle;

import java.io.Serializable;

public class Contact implements Serializable {

    // Constants
    public static final String CONTACT_NAME   = "contactName";
    public static final String CONTACT_AGE    = "contactAge";
    public static final String CONTACT_GENDER = "contactGender";
    public static final String CONTACT_PHONE  = "contactPhone";

    // Variables
    private String name;
    private String age;
    private String gender;
    private String phone;

    // Getters & Setters
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getAge() { return age; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public void setAge(String age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }

    // Creating Object
    public Contact(String _name, String _age, String _gender, String _phone){
        this.name   = _name;
        this.age    = _age;
        this.gender = _gender;
        this.phone  = _phone;
    }

    // Create Bundle
    public Contact(Bundle bundle) {
        if (bundle != null) {
            this.name   = bundle.getString(CONTACT_NAME);
            this.age    = bundle.getString(CONTACT_AGE);
            this.gender = bundle.getString(CONTACT_GENDER);
            this.phone  = bundle.getString(CONTACT_PHONE);
        }
    }

    // Package Bundle
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(CONTACT_NAME,   this.name);
        bundle.putString(CONTACT_AGE,    this.age);
        bundle.putString(CONTACT_GENDER, this.gender);
        bundle.putString(CONTACT_PHONE,  this.phone);
        return bundle;
    }

    //	Output Data
    @Override
    public String toString() {
        return name;
    }

}
