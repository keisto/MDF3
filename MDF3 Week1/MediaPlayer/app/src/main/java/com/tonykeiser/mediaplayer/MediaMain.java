package com.tonykeiser.mediaplayer;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1601   ######
// ##########################################################
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.media.IMediaBrowserServiceCompatCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import com.tonykeiser.mediaplayer.MediaService.ServiceBinder;
import java.net.URI;

public class MediaMain extends AppCompatActivity implements ServiceConnection {
    private static final String ACTI = "ACTION";
    private static final String PLAY = "PLAY";
    private static final String PAUS = "PAUSE";
    private static final String STOP = "STOP";
    private static final String REWI = "REWIND";
    private static final String FORW = "FORWARD";
    private boolean isBound = false;
    private Intent bindIntent;
    MediaService mediaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindIntent = new Intent(this, MediaService.class);
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }

    public void onClick(View v) {
        if (isBound) {
            switch (v.getId()) {
                case R.id.rewind:
                    bindIntent.putExtra(ACTI, REWI);
                    startService(bindIntent);
                    mediaDetails();
                    break;
                case R.id.stop:
                    bindIntent.putExtra(ACTI, STOP);
                    startService(bindIntent);
                    break;
                case R.id.play:
                    bindIntent.putExtra(ACTI, PLAY);
                    startService(bindIntent);
                    mediaDetails();
                    break;
                case R.id.pause:
                    bindIntent.putExtra(ACTI, PAUS);
                    startService(bindIntent);
                    mediaDetails();
                    break;
                case R.id.forward:
                    bindIntent.putExtra(ACTI, FORW);
                    startService(bindIntent);
                    mediaDetails();
                    break;
            }
        }
    }
    public void mediaDetails() {
        JSONObject object = mediaService.currentMedia();
        String artist   = null;
        String track    = null;
        String album    = null;
        String artwork  = null;
        try {
            artist  = (String) object.get("Artist");
            track   = (String) object.get("Track");
            album   = (String) object.get("Album");
            artwork = (String) object.get("Image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Pass Details to Fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MediaDetails detailFrag = new MediaDetails();
        detailFrag.setArtistString(artist);
        detailFrag.setTrackString(track);
        detailFrag.setAlbumString(album);
        detailFrag.setImageString(artwork);
        fragmentTransaction.replace(R.id.mediaDetails, detailFrag);
        fragmentTransaction.commit();
    }

    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        isBound = true;
        ServiceBinder binder = (ServiceBinder) iBinder;
        mediaService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mediaService.onUnbind(bindIntent);
    }
}
