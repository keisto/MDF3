package com.tonykeiser.photomapping;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get Values from Bundle
        String  markerTitle = getArguments().getString("title");
        Boolean markerFavor = getArguments().getBoolean("favorite");
        String  markerImage = getArguments().getString("photo");
        Double  latitude    = getArguments().getDouble("latitude");
        Double  longitude   = getArguments().getDouble("longitude");

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Get UI Elements from Layout
        TextView  title    = (TextView)  view.findViewById(R.id.titleText);
        TextView  favorite = (TextView)  view.findViewById(R.id.favoriteText);
        ImageView photo    = (ImageView) view.findViewById(R.id.imageView);
        TextView  lat      = (TextView)  view.findViewById(R.id.latitudeText);
        TextView  lng      = (TextView)  view.findViewById(R.id.longitudeText);

        // Set Values retrieved
        title.setText(markerTitle);
        if(markerFavor){
            favorite.setText("YES");
        } else {
            favorite.setText("NO");
        }
        photo.setImageURI(Uri.parse(markerImage));
        lat.setText(latitude.toString());
        lng.setText(longitude.toString());

        // Display View
        return view;
    }
}
