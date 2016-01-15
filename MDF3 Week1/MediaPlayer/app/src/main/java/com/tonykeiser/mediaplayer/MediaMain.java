package com.tonykeiser.mediaplayer;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1601   ######
// ##########################################################
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tonykeiser.mediaplayer.MediaService.ServiceBinder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MediaMain extends AppCompatActivity implements ServiceConnection {
    private static final int FOREGROUND_NOTIFICATION = 0x1001 ;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x1002;

    private static final String ACTI = "ACTION";
    private static final String PLAY = "PLAY";
    private static final String PAUS = "PAUSE";
    private static final String STOP = "STOP";
    private static final String REWI = "REWIND";
    private static final String FORW = "FORWARD";
    private static final String SHUF = "SHUFFLE";

    private String artist;
    private String track;
    private String album;
    private String image;

    private boolean isBound = false;
    private Intent bindIntent;
    private Intent receiverIntent;
    MediaService mediaService;
    MediaReceiver mediaReceiver;
    public boolean isRotated = false;
    private Switch shuffle;

    // SeekBar Variables
    private static SeekBar mediaSeekBar;
    private static TextView mediaStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaReceiver = new MediaReceiver(null);
        receiverIntent = new Intent(this, MediaService.class);
        receiverIntent.putExtra("receiver", mediaReceiver);

        mediaSeekBar = (SeekBar)  findViewById(R.id.seekBar);
        mediaStatus  = (TextView) findViewById(R.id.status);
        shuffle      = (Switch)   findViewById(R.id.shuffle);

        shuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Shuffle Media
                bindIntent.putExtra(ACTI, SHUF);
                startService(bindIntent);
                startService(receiverIntent);
            }
        });

    }

    Bundle savedBundle = new Bundle();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            isRotated = true;
            startService(receiverIntent);
            mediaService.getDetails();
            setContentView(R.layout.activity_landscape);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            isRotated = false;
            startService(receiverIntent);
            mediaService.getDetails();
            setContentView(R.layout.activity_main);
        }
    }
    @SuppressLint("ParcelCreator")
    public class MediaReceiver extends ResultReceiver
    {
        public MediaReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // If bundle received from service
            if(resultCode == 1) {
                artist = resultData.getString("Artist");
                track  = resultData.getString("Track");
                album  = resultData.getString("Album");
                image  = resultData.getString("Image");

                mediaDetails();
                mediaNotification();
            }
            if(resultCode == 2){
                long duration = mediaService.seekbarDuration();
                long position = mediaService.seekbarPosition();
                mediaSeeker(duration, position);
                Log.d("KEISERTONY", "onReceiveResult: " + duration + " || " + position);
            }
            if(resultCode == 3){
                artist = resultData.getString("Artist");
                track  = resultData.getString("Track");
                album  = resultData.getString("Album");
                image  = resultData.getString("Image");
                long duration = mediaService.seekbarDuration();
                long position = mediaService.seekbarPosition();
                mediaSeeker(duration, position);
                mediaDetails();
            }
        }
    }

    public void onClick(View v) {
        if (isBound) {
            switch (v.getId()) {
                case R.id.rewind:
                    bindIntent.putExtra(ACTI, REWI);
                    startService(bindIntent);
                    startService(receiverIntent);
                    break;
                case R.id.stop:
                    bindIntent.putExtra(ACTI, STOP);
                    startService(bindIntent);
                    startService(receiverIntent);
                    break;
                case R.id.play:
                    bindIntent.putExtra(ACTI, PLAY);
                    startService(bindIntent);
                    startService(receiverIntent);
                    break;
                case R.id.pause:
                    bindIntent.putExtra(ACTI, PAUS);
                    startService(bindIntent);
                    startService(receiverIntent);
                    break;
                case R.id.forward:
                    bindIntent.putExtra(ACTI, FORW);
                    startService(bindIntent);
                    startService(receiverIntent);
                    break;
            }
        }
    }


    public void mediaSeeker(final long duration, final long position){
        mediaSeekBar.setEnabled(true);
        mediaSeekBar.setMax((int) duration);
        mediaSeekBar.setProgress(0);
        mediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Thread() {
                    public void run() {
                        mediaSeekBar.post(new Runnable() {
                            public void run() {
                                mediaSeekBar.setProgress((int) mediaService.seekbarPosition());
                            }
                        });
                    }
                }.start();
                mediaStatus.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(
                        progress) + " / " + TimeUnit.MILLISECONDS.toSeconds(
                        duration)) + " Seconds");
                if(fromUser) {
                    // If user changes bar
                    mediaService.seekbarChanged(progress);
                    mediaSeekBar.setProgress(progress);
                    mediaStatus.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(
                            progress) + " / " + TimeUnit.MILLISECONDS.toSeconds(
                            duration)) + " Seconds");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void mediaDetails() {
        // Pass Details to Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MediaDetails detailFrag = new MediaDetails();
        detailFrag.setArtistString(artist);
        detailFrag.setTrackString(track);
        detailFrag.setAlbumString(album);
        detailFrag.setImageString(image);
        detailFrag.setRotation(isRotated);
        fragmentTransaction.replace(R.id.mediaDetails, detailFrag);
        fragmentTransaction.commit();
    }

    public void mediaNotification(){
        NotificationManager mgr = (NotificationManager)
                this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setContentTitle("Playing: " + track);
        builder.setContentText("Artist: " + artist + " on  Album: " + album);
        Uri imageUri = Uri.parse(image);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        Intent nIntent = new Intent(this, MediaMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(
                this, REQUEST_NOTIFY_LAUNCH, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);
        mgr.notify(FOREGROUND_NOTIFICATION, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBound = false;
        bindIntent = new Intent(this, MediaService.class);
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }

    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        isBound = true;
        ServiceBinder binder = (MediaService.ServiceBinder) iBinder;
        mediaService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mediaService.onUnbind(bindIntent);
    }
}
