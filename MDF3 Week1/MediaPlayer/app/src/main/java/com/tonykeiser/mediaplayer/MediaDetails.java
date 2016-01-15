package com.tonykeiser.mediaplayer;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1601   ######
// ##########################################################

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MediaDetails extends Fragment {

    public MediaDetails(){}

    private boolean rotation;
    private String artistString;
    private String trackString;
    private String albumString;
    private String imageString;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        if (rotation){
            View view = inflater.inflate(R.layout.fragment_landscape, container, false);
            // Set UI Elements
            TextView artist = (TextView) view.findViewById(R.id.artist);
            TextView track = (TextView) view.findViewById(R.id.track);
            TextView album = (TextView) view.findViewById(R.id.album);
            ImageView artwork = (ImageView) view.findViewById(R.id.artwork);

            if(artistString!=null) {
                artist.setText(artistString);
                track.setText(trackString);
                album.setText(albumString);
                artwork.setImageURI(Uri.parse(imageString));
            }
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_controls, container, false);
            // Set UI Elements
            TextView artist = (TextView) view.findViewById(R.id.artist);
            TextView track = (TextView) view.findViewById(R.id.track);
            TextView album = (TextView) view.findViewById(R.id.album);
            ImageView artwork = (ImageView) view.findViewById(R.id.artwork);

            if(artistString!=null) {
                artist.setText(artistString);
                track.setText(trackString);
                album.setText(albumString);
                artwork.setImageURI(Uri.parse(imageString));
            }
            return view;
        }
    }
    public void setArtistString(String artistString) {
        this.artistString = artistString;
    }

    public void setTrackString(String trackString) {
        this.trackString = trackString;
    }

    public void setAlbumString(String albumString) {
        this.albumString = albumString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public void setRotation(boolean rotation) { this.rotation = rotation;
    }
}
