// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
package xyz.y_not.mediaplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

public class MediaActivity extends AppCompatActivity {

    private ArrayList<Uri> trackUris = new ArrayList<>();
    private ArrayList<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);// Stopping in the activity:

        // Load MediaPlay Fragment
        MediaControls frag = new MediaControls();
        // Load Raw Files
        trackUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.tonight));
        trackUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.battlefield));
        trackUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.youstupidgirl));
        trackUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.downfall));
        trackUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.blackout));
        imageUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.fmstatic));
        imageUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.jordansparks));
        imageUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.framinghanley));
        imageUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.trustcompany));
        imageUris.add(Uri.parse
                ("android.resource://" + getPackageName() + "/" + R.raw.breathecarolina));
        // Pass Files to Fragment
        frag.setTrackUris(trackUris);
        frag.setImageUris(imageUris);
        // Display Fragment
        getFragmentManager().beginTransaction().add(R.id.mediaPlayer, frag).commit();
    }
}
