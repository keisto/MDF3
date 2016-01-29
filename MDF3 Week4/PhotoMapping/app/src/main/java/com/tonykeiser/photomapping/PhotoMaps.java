package com.tonykeiser.photomapping;

import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class PhotoMaps implements Serializable {

    private static final long serialVersionUID = 1234567890987654321L;

    // Variables
    private String  title;
    private String  photo;
    private Boolean favorite;
    private Double  latitude;
    private Double  longitude;

    // Getters & Setters
    public String  getTitle()     { return title;     }
    public String  getPhoto()     { return photo;     }
    public Boolean getFavorite()  { return favorite;  }
    public Double  getLatitude()  { return latitude;  }
    public Double  getLongitude() { return longitude; }

    // Creating a Location on Map with Photo
    public PhotoMaps(String _title, String _photo, Boolean _favorite,
                     Double _latitude, Double _longitude) {
        this.title     = _title;
        this.photo     = _photo;
        this.favorite  = _favorite;
        this.latitude  = _latitude;
        this.longitude = _longitude;
    }

    //	Output Data
    @Override
    public String toString() {
        return title;
    }

    public PhotoMaps(Bundle bundle) {
        if (bundle != null) {
            this.title     = bundle.getString  ("title");
            this.photo     = bundle.getString  ("photo");
            this.favorite  = bundle.getBoolean ("favorite");
            this.latitude  = bundle.getDouble  ("latitude");
            this.longitude = bundle.getDouble  ("longitude");
        }
    }
}
