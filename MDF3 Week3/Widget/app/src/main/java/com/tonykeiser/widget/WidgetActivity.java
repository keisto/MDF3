package com.tonykeiser.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class WidgetActivity extends AppCompatActivity {

    public static final String ASSET_BUNDLE = "asset";
    private static WidgetActivity app;

    ArrayList<Asset> assetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        app = this;
        onUpdate();
    }

    public void onNewAsset(View v){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    // Update ListView
    public static void onUpdate(){
        app.assetList = readData();
        ItemAdapter adapter = new ItemAdapter(app, app.assetList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) app.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent details = new Intent(app, DetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DetailsActivity.EXTRA_ITEM, app.assetList.get(position));
                app.startActivity(details);
            }
        });
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
    }

    // Save Serializable Data
    public static void saveData(String name, String title, String rate) {
        ArrayList<Asset> arrayList = new ArrayList<>();
        // Load Data and Append New Data
        arrayList = readData();
        arrayList.add(new Asset(name, title, "$" + rate + ".00/hr"));
        try {
            FileOutputStream fos = new FileOutputStream(
                    new File(app.getFilesDir(),"")+File.separator+"assets.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Data Saved... Update Widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(app);
        ComponentName myWidget = new ComponentName(app, WidgetProvider.class);
        int[] id = appWidgetManager.getAppWidgetIds(myWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widgetList);
    }

    // Load Serializable Data
    public static ArrayList readData() {
        ArrayList<Asset> assetArray = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(
                    new File(app.getFilesDir(),"")+File.separator+"assets.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            assetArray = (ArrayList<Asset>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetArray;
    }
}
