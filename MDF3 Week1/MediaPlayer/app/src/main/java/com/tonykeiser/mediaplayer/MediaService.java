package com.tonykeiser.mediaplayer;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1601   ######
// ##########################################################
public class MediaService extends Service {
    private static final String TAG = "FUNDAMENTALS";
    private static final String ACTION_CONTROLLER = "ACTION";
    private String controller;
    private int counter = 0;
    private boolean stopped = false;
    private boolean playing = false;
    private int position;
    // Create Media Player, Idle
    private MediaPlayer player;
    private JSONArray playlist = new JSONArray();
    ResultReceiver resultReceiver;
    // Shuffle Variables
    private boolean isShuffle = false;
    private int randomNumber;

    public class ServiceBinder extends Binder {
        public MediaService getService() { return MediaService.this; }
    }

    ServiceBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new ServiceBinder();

        // onCreate Get Media Files
        trackSetup("Trapt", "Black Rose", "Only Through the Pain", "black_rose", "trapt");
        trackSetup("Disciple", "Dear X, You Don't Own Me", "Horseshoes & Handgrenades",
                "dear_x", "disciple");
        trackSetup("Orianthi", "According to You", "Believe", "according_to_you", "orianthi");
        trackSetup("Thousand Foot Krutch", "Already Home", "Welcome To The Masquerade",
                "already_home", "thousand_foot_krutch");
        trackSetup("Third Eye Blind", "Jumper", "A Collection", "jumper", "third_eye_blind");
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        if (i != null) {
            if (i.getParcelableExtra("receiver")!=null){
                resultReceiver = i.getParcelableExtra("receiver");
                return START_STICKY;
            }
            if (i.hasExtra(ACTION_CONTROLLER)) {
                controller = i.getStringExtra(ACTION_CONTROLLER);
                switch (controller) {
                    case "REWIND":
                        rewindAction();
                        break;
                    case "PLAY":
                        playAction();
                        break;
                    case "STOP":
                        stopAction();
                        break;
                    case "PAUSE":
                        pauseAction();
                        break;
                    case "FORWARD":
                        forwardAction();
                        break;
                    case "SHUFFLE":
                        shuffler();
                        break;
                }
            }
        }
        return super.onStartCommand(i, flags, startId);
    }

    private void stopAction() {
        // Stop Track
        if (player != null) {
            if(!stopped)
            player.stop();
            player.release();
            playing = false;
            stopped = true;
        }
    }

    public void seekbarChanged(int progress){
        // Called from seekbar change from user
        player.seekTo(progress);
    }
    public long seekbarDuration(){
        // Called from seekbar change from user
        return player.getDuration();
    }
    public long seekbarPosition(){
        // Called from seekbar change from user
        if(!playing) {
            return 0;
        } else {
            return player.getCurrentPosition();
        }
    }

    public void getDetails() {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("Artist", String.valueOf(playlist.getJSONObject(counter)
                    .get("Artist")));
            bundle.putString("Track", String.valueOf(playlist.getJSONObject(counter)
                    .get("Track")));
            bundle.putString("Album", String.valueOf(playlist.getJSONObject(counter)
                    .get("Album")));
            bundle.putString("Image", String.valueOf(playlist.getJSONObject(counter)
                    .get("Image")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.putLong("Duration", player.getDuration());
        bundle.putLong("Position", player.getCurrentPosition());
        resultReceiver.send(3, bundle);
    }

    private void shuffler(){
        if(isShuffle) {
            isShuffle = false;
        } else {
            isShuffle = true;
        }
    }
    private void shufflePlay(){
        // Create randomNumber based off number of trackUris
        randomNumber = (int) (Math.random() * (playlist.length() - 1));
        counter = randomNumber;
    }

    private void playAction() {
        String track = null;
        if (isShuffle) shufflePlay();
        // Create Player if player = null;
        player = new MediaPlayer();
        // If player is NOT playing ... Start playing
        if (!playing) {
            try {
                track = (String) playlist.getJSONObject(counter).get("Song");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                player.setDataSource(this, Uri.parse(track));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Prepare Media
            try {
                if (player != null) {
                    player.prepare();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer media) {
                    // Start Media
                    media.start();
                    stopped = false;
                    playing = true;
                    // Set up Notification to Receiver
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("Artist", String.valueOf(playlist.getJSONObject(counter)
                                .get("Artist")));
                        bundle.putString("Track", String.valueOf(playlist.getJSONObject(counter)
                                .get("Track")));
                        bundle.putString("Album", String.valueOf(playlist.getJSONObject(counter)
                                .get("Album")));
                        bundle.putString("Image", String.valueOf(playlist.getJSONObject(counter)
                                .get("Image")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultReceiver.send(1, bundle);
                    // Set up Seeker to Receiver
                    Bundle seeker = new Bundle();
                    bundle.putLong("Duration", player.getDuration());
                    bundle.putLong("Position", player.getCurrentPosition());
                    resultReceiver.send(2, seeker);

                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    // Go Play Next Track
                    forwardAction();
                }
            });
        }
    }

    private void rewindAction() {
        // Rewind Track
        if (player != null) {
            player.stop();
            player.release();
            playing = false;
            if (counter != 0) {
                counter--;
            } else {
                // Loop to the End of the List
                counter = playlist.length() - 1;
            }
            playAction();
        }
    }

    private void pauseAction() {
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

    private void forwardAction() {
        // Forward Track
        if (player != null) {
            player.stop();
            player.release();
            playing = false;
            if (counter != playlist.length()-1) {
                counter++;
            } else {
                // Loop to the Beginning of the List
                counter = 0;
            }
            playAction();
        }
    }

    public void trackSetup(String artist, String track, String album, String song, String image){
        JSONObject media = new JSONObject();
        try {
            media.put("Artist", artist);
            media.put("Track", track);
            media.put("Album", album);
            //Format song && image files
            String formatSong = "android.resource://" + getPackageName()
                    + "/raw/" + song;
            String formatImage = "android.resource://" + getPackageName()
                    + "/raw/" + image;
            media.put("Song", formatSong);
            media.put("Image", formatImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Add Track to Playlist
        playlist.put(media);
    }

    @Override
    public IBinder onBind(Intent i) {
        return mBinder = new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent i) {
        // Stop Track
        if (player != null) {
            if(!stopped)
                player.stop();
            player.release();
            stopped = true;
        }
        return super.onUnbind(i);
    }
}
