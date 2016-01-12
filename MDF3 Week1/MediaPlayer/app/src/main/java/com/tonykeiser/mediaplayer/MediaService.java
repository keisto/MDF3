package com.tonykeiser.mediaplayer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;

// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1601   ######
// ##########################################################
public class MediaService extends Service {
    private static final String TAG = "FUNDAMENTALS";
    private static final String ACTION_CONTROLLER = "ACTION";
    private static final int FOREGROUND_NOTIFICATION = 0x1001 ;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x1002;
    private String controller;
    private int counter = 0;
    private int position;
    // Create Media Player, Idle
    private MediaPlayer player;
    private JSONArray playlist = new JSONArray();

    public class ServiceBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
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
    public IBinder onBind(Intent i) {
        return mBinder;
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
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        return super.onUnbind(i);
    }

    public JSONObject currentMedia(){
        // Pass current track to Details
        JSONObject object = null;
        try {
            object = playlist.getJSONObject(counter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        if (i != null) {
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
                }
            }
        }
        return super.onStartCommand(i, flags, startId);
    }

    private void stopAction() {
        // Stop Track
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    private void playAction() {
        String track = null;
        // Create Player if player = null;
        player = new MediaPlayer();
        // If player is NOT playing ... Start playing
        if (!player.isPlaying()) {
            try {
                track = (String) playlist.getJSONObject(counter).get("Song");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                player.setDataSource(this, Uri.parse(track));
            } catch (IOException e) {
                e.printStackTrace();
                player.stop();
                player.release();
                player = null;
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
                    // Set up Notification
                    mediaNotification(counter);
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    // Go Play Next Track
                    forwardAction();
                    Log.d(TAG, "onCompletion: Loading Next Song to play");
                }
            });
        }
    }

    private void rewindAction() {
        // Rewind Track
        if (player != null) {
            player.stop();
            player.release();
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
            //player.release();
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

    public void mediaNotification(int count){
        NotificationManager mgr = (NotificationManager)
                this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        try {
            builder.setContentTitle("Playing: " + playlist.getJSONObject(count).get("Track"));
            builder.setContentText("Aritst: " + playlist.getJSONObject(count).get("Artist") +
            " on  Album: " + playlist.getJSONObject(count).get("Album"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        Intent nIntent = new Intent(this, MediaMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(
                this, REQUEST_NOTIFY_LAUNCH, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);
        mgr.notify(FOREGROUND_NOTIFICATION, builder.build());
    }
}
