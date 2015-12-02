// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
package xyz.y_not.mediaplayer;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MediaControls extends Fragment /* implements ServiceConnection */{
    public MediaControls() {}
    // Variables
    public static String track;
    public static String artist;
    public static String album;
    private String source;
    public static String image;
    private int position;
    private int counter;
    private boolean stopped = false;
    private boolean isBound;
    // Create Media Player, Idle
    MediaPlayer player;
    // Service Intent
    Intent sIntent;
    // ArrayLists
    private ArrayList<String> tracks    = new ArrayList<>();
    private ArrayList<String> artists   = new ArrayList<>();
    private ArrayList<String> albums    = new ArrayList<>();
    private ArrayList<String> sources   = new ArrayList<>();
    private ArrayList<String> images    = new ArrayList<>();
    private ArrayList<Uri>    trackUris = new ArrayList<>();
    private ArrayList<Uri>    imageUris = new ArrayList<>();
    // UI Display
    private TextView  trackText;
    private TextView  artistText;
    private TextView  albumText;
    private ImageView imageView;

    private void getData(){
        // Create Track Data
        createTrack("Tonight",
                "tonight", "FM Static", "Critically Ashamed", "fmstatic");
        createTrack("Battlefield",
                "battlefield", "Jordan Sparks", "Battlefield (Single)", "jordansparks");
        createTrack("You Stupid Girl",
                "youStupidGirl", "Framing Hanley", "You Stupid Girl (Single)", "framinghanley");
        createTrack("Downfall",
                "downfall", "Trust Company", "The Lonely Position of Neutral", "trustcompany");
        createTrack("Blackout",
                "blackout", "Breathe Carolina", "Hell Is What You Make It", "breathecarolina");
    }


     public static MediaControls newInstance(String track, String artist, String album, String Image){
         MediaControls frag = new MediaControls();
         Bundle bundle = new Bundle();
         bundle.putString("Track",  track);
         bundle.putString("Artist", artist);
         bundle.putString("Album",  album);
         bundle.putString("Image",  image);
         frag.setArguments(bundle);
         return frag;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_controls, container, false);
        trackText   = (TextView)    view.findViewById(R.id.track);
        artistText  = (TextView)    view.findViewById(R.id.artist);
        albumText   = (TextView)    view.findViewById(R.id.album);
        imageView   = (ImageView)   view.findViewById(R.id.image);
        ImageButton forward = (ImageButton) view.findViewById(R.id.forward);
        ImageButton rewind = (ImageButton) view.findViewById(R.id.rewind);
        ImageButton pause = (ImageButton) view.findViewById(R.id.pause);
        ImageButton play = (ImageButton) view.findViewById(R.id.play);
        ImageButton stop = (ImageButton) view.findViewById(R.id.stop);
        getData(); // Setup Data
        mediaInfo(); // Show Data

        // Assign onClick Listeners
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop Current & Play Next Track
                if (player != null) {
                    player.stop();
                    if (counter != trackUris.size() - 1) {
                        counter++;
                        mediaInfo();
                    } else {
                        // Loop to the Beginning of the List
                        counter = 0;
                        mediaInfo();
                    }
                    mediaPlay();
                }
            }
        });
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop Current & Play Previous Track
                if (player != null) {
                    player.stop();
                    if (counter != 0) {
                        counter--;
                        mediaInfo();
                    } else {
                        // Loop to the End of the List
                        counter = trackUris.size() - 1;
                        mediaInfo();
                    }
                    mediaPlay();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    if (player.isPlaying()) {
                        // Pause Track
                        player.pause();
                        position = player.getCurrentPosition();
                    } else {
                        // Resume Track
                        player.seekTo(position);
                        player.start();
                    }
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop Track
                if (player != null) {
                    player.stop();
                    stopped = true;
                    getActivity().stopService(sIntent);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play Track
                if (player == null) {
                    //getActivity().startService(intent);
                    mediaPlay();
                } else {
                    if (player.isPlaying()) {
                        // Ignore Play Button
                    } else {
                        if (stopped) {
                            mediaPlay();
                            stopped = false;
                        } else {
                            player.start();
                        }
                    }
                }
//                if (isBound){
//                    Intent intent = new Intent(getActivity(), MediaService.class);
//                    intent.setAction(MediaService.PLAY);
//                    getActivity().startService(intent);
//                }
            }
        });
        return view;
    }

    private void mediaInfo (){
        trackText.setText(tracks.get(counter));
        artistText.setText(artists.get(counter));
        albumText.setText(albums.get(counter));
        imageView.setImageURI(imageUris.get(counter));
        sIntent = new Intent(getActivity(), MediaService.class);
        sIntent.putExtra("Track", tracks.get(counter));
        sIntent.putExtra("Artist", artists.get(counter));
        sIntent.putExtra("Album", albums.get(counter));
    }
    private void mediaPlay(){
        // Create Media Player, Idle
        player = new MediaPlayer();
        // Initialized, Set Source
        try {
            player.setDataSource(getActivity(), trackUris.get(counter));
        } catch (IOException e) {
            e.printStackTrace();
            player.release();
            player = null;
        }
        // Prepare Media
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer media) {
                // Start Media
                media.start();
                getActivity().startService(sIntent);
            }
        });
    }
    private void createTrack(String track, String source, String artist, String album, String image){
        // Add Track to Service
        tracks.add(track);
        sources.add(source);
        artists.add(artist);
        albums.add(album);
        images.add(image);
    }
    public void setTrackUris(ArrayList<Uri> trackUris) {this.trackUris = trackUris;}
    public void setImageUris(ArrayList<Uri> imageUris) {this.imageUris = imageUris;}

//    Binding not working correctly ::
//    @Override
//    public void onStart() {
//        super.onStart();
//        Intent intent = new Intent(getActivity(), MediaService.class);
//        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        getActivity().unbindService(this);
//        isBound = false;
//    }
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        MediaService.BoundServiceBinder binder =
//                (MediaService.BoundServiceBinder) service;
//        binder.getService();
//        isBound = true;
//        Log.e("Service: ", "Connected!");
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        isBound = false;
//    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        // Assign UI Widgets
//
////
////        if (getArguments() != null){
////            if(!track.equals("")) trackText.setText(track);
////            if(!artist.equals("")) artistText.setText(artist);
////            if(!album.equals(""))albumText.setText(album);
////            if(!image.equals(""))imageView.setImageURI(Uri.parse(image));
////        }
//
//    }
}