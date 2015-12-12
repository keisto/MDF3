package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Remote_Factory implements RemoteViewsService.RemoteViewsFactory {

    private static final int STATIC_ID = 0x000011;

    private List<Contact> contacts;
    private Context remoteContext;
    ArrayList<JSONObject> contactJson = new ArrayList<JSONObject>();
    int contactData = 100;
    String jsonData = "";

    public Remote_Factory(Context context) {
        remoteContext = context;
        contacts      = new Contact_Data().getContacts();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        // Load in New Contacts From JSON File
        File external = remoteContext.getExternalFilesDir(null);
        File file = new File(external, "contacts.txt");
        try {
            FileInputStream fin= new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fin);
            char[] data = new char[contactData];
            int size;
            try {
                while ((size = isr.read(data))>0){
                    String readData = String.copyValueOf(data,0,size);
                    jsonData += readData;
                    data = new char[contactData];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] breakup = jsonData.split("\n");
        for (int i = 0; i <breakup.length; i++) {
            try {
                JSONObject converter = new JSONObject(breakup[i]);
                contactJson.add(converter);
                contacts.add(new Contact(converter.getString("Name"), converter.getString("Age"),
                        converter.getString("Gender"), converter.getString("Phone")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Contact contact = contacts.get(position);
        RemoteViews listItem = new RemoteViews(remoteContext.getPackageName(), R.layout.list_item);
        listItem.setTextViewText(R.id.name,   contact.getName());
        listItem.setTextViewText(R.id.age,    contact.getAge());
        listItem.setTextViewText(R.id.gender, contact.getGender());
        listItem.setTextViewText(R.id.phone,  contact.getPhone());

        Intent intent = new Intent();
        intent.putExtra(Contact_Provider.EXTRA_ITEM, contact);
        listItem.setOnClickFillInIntent(R.id.list_item, intent);

        return listItem;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return STATIC_ID + position;
    }

    @Override
    public boolean hasStableIds() { return true; }

}
