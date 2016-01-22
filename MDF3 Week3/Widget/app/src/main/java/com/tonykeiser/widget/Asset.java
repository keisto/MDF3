package com.tonykeiser.widget;

import android.os.Bundle;

import java.io.Serializable;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class Asset implements Serializable {

    private static final long serialVersionUID = 1234567890987654321L;

    // Variables
    private String name;
    private String title;
    private String rate;

    // Getters & Setters
    public String getRate()  { return rate; }
    public String getTitle() { return title; }
    public String getName()  { return name; }

    // Creating Object
    public Asset(String _name, String _title, String _rate) {
        this.name = _name;
        this.title = _title;
        this.rate = _rate;
    }

    //	Output Data
    @Override
    public String toString() {
        return "Asset [name=" + name +", title=" + title +", rate=" + rate + "]";
    }

    public Asset(Bundle bundle) {
        if (bundle != null) {
            this.name  = bundle.getString("name");
            this.title = bundle.getString("title");
            this.rate  = bundle.getString("rate");
        }
    }
}
