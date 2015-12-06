// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
package xyz.y_not.mediaplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MediaService extends Service {
    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001 ;
    public static final String PREV = "Prev"; // previous track
    public static final String STOP = "Stop"; // stop track
    public static final String PLAY = "Play"; // start track
    public static final String PAUS = "Paus"; // pause track
    public static final String NEXT = "Next"; // next track

    BroadcastReceiver receiver;
    Bitmap cover;
    private int position;
    private int counter;
    private boolean stopped = false;

    // ArrayLists
    private ArrayList<String> tracks    = new ArrayList<>();
    private ArrayList<String> artists   = new ArrayList<>();
    private ArrayList<String> albums    = new ArrayList<>();
    private ArrayList<String> sources   = new ArrayList<>();
    private ArrayList<String> images    = new ArrayList<>();
    private ArrayList<Uri>    trackUris = new ArrayList<>();
    private ArrayList<Uri>    imageUris = new ArrayList<>();
    // Create Media Player, Idle
    MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();

//        IntentFilter fIntent = new IntentFilter();
//        fIntent.addAction(PREV); // Previous
//        fIntent.addAction(STOP); // Stop
//        fIntent.addAction(PLAY); // Play
//        fIntent.addAction(PAUS); // Pause
//        fIntent.addAction(NEXT); // Next
//        receiver = new castReceiver();
//        registerReceiver(receiver, fIntent);
//        getData();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        switch (intent.getAction()){
//            case PREV : prevAction();
//                break;
//            case STOP : stopAction();
//                break;
//            case PLAY : playAction();
//                break;
//            case PAUS : pausAction();
//                break;
//            case NEXT : nextAction();
//                break;
//        }
        // Create Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //Uri imageUri = Uri.parse(intent.getExtras().getString("Image"));
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            builder.setLargeIcon(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        builder.setSmallIcon(R.drawable.ic_notifications_active_white_24dp);
        builder.setContentTitle(intent.getExtras().getString("Track"));
        builder.setContentText(intent.getExtras().getString("Artist") + " on " +
                intent.getExtras().getString("Album"));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        Intent nIntent = new Intent(this, MediaActivity.class);
        PendingIntent pIntent =
                PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, nIntent, 0);
        builder.setContentIntent(pIntent);
        startForeground(FOREGROUND_NOTIFICATION, builder.build());

        return Service.START_NOT_STICKY;
    }
    public class BoundServiceBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new BoundServiceBinder();
    }

//    private void prevAction() {
//        if (player != null) {
//            player.stop();
//            if (counter != 0) {
//                counter--;
//               // mediaInfo();
//            } else {
//                // Loop to the End of the List
//                counter = trackUris.size() - 1;
//               // mediaInfo();
//            }
//            mediaPlay();
//        }
//    }
//
//    private void stopAction() {
//        // Stop Current & Play Previous Track
//        if (player != null) {
//            player.stop();
//            if (counter != 0) {
//                counter--;
//               // mediaInfo();
//            } else {
//                // Loop to the End of the List
//                counter = trackUris.size() - 1;
//               // mediaInfo();
//            }
//            mediaPlay();
//        }
//    }
//
//    private void playAction() {
//        // Play Track
//        if (player == null) {
//            //getActivity().startService(intent);
//            mediaPlay();
//        } else {
//            if (player.isPlaying()) {
//                // Ignore Play Button
//            } else {
//                if (stopped){
//                    mediaPlay();
//                    stopped = false;
//                } else {
//                    player.start();
//                }
//            }
//        }
//    }
//
//    private void pausAction() {
//        if (player != null) {
//            if (player.isPlaying()) {
//                // Pause Track
//                player.pause();
//                position = player.getCurrentPosition();
//            } else {
//                // Resume Track
//                player.seekTo(position);
//                player.start();
//            }
//        }
//    }
//
//    private void nextAction() {
//        // Stop Current & Play Next Track
//        if (player != null) {
//            player.stop();
//            if (counter != trackUris.size() - 1) {
//                counter++;
//               // mediaInfo();
//            } else {
//                // Loop to the Beginning of the List
//                counter = 0;
//              //  mediaInfo();
//            }
//            mediaPlay();
//        }
//    }
//    private void mediaPlay() {
//        // Create Media Player, Idle
//        player = new MediaPlayer();
//        player.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
//        // Initialized, Set Source
//        try {
//            player.setDataSource(this, trackUris.get(counter));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // Prepare Media
//        player.prepareAsync();
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer media) {
//                // Start Media
//                media.start();
//                MediaControls.track = tracks.get(counter);
//                MediaControls.artist = tracks.get(counter);
//                MediaControls.album = tracks.get(counter);
//                MediaControls.image = images.get(counter);
//
//            }
//        });
//
//    }
//
//    private class castReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (player != null) player.release();
//    }
//
//    private void mediaInfo () {
////        MediaControl.set
////                (tracks.get(counter));
////        artistText.setText(artists.get(counter));
////        albumText.setText(albums.get(counter));
////        imageView.setImageURI(imageUris.get(counter));
//    }

//    private void getData(){
//        // Create Track Data
//        createTrack("Tonight",
//                "tonight", "FM Static", "Critically Ashamed", "fmstatic");
//        createTrack("Battlefield",
//                "battlefield", "Jordan Sparks", "Battlefield (Single)", "jordansparks");
//        createTrack("You Stupid Girl",
//                "youStupidGirl", "Framing Hanley", "You Stupid Girl (Single)", "framinghanley");
//        createTrack("Downfall",
//                "downfall", "Trust Company", "The Lonely Position of Neutral", "trustcompany");
//        createTrack("Blackout",
//                "blackout", "Breathe Carolina", "Hell Is What You Make It", "breathecarolina");
//        trackUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.tonight));
//        trackUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.battlefield));
//        trackUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.youstupidgirl));
//        trackUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.downfall));
//        trackUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.blackout));
//        imageUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.fmstatic));
//        imageUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.jordansparks));
//        imageUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.framinghanley));
//        imageUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.trustcompany));
//        imageUris.add(Uri.parse
//                ("android.resource://" + getPackageName() + "/" + R.raw.breathecarolina));
//    }
//    private void createTrack(String track, String source,
//                             String artist, String album, String image){
//        // Add Track to Service
//        tracks.add(track);
//        sources.add(source);
//        artists.add(artist);
//        albums.add(album);
//        images.add(image);
//    }

}

