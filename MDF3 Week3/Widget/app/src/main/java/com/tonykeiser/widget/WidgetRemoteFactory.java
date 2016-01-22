package com.tonykeiser.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class WidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x12345;

    public ArrayList<Asset> companyAssets;
    private Context remoteContext;

    public WidgetRemoteFactory(Context context){
        remoteContext = context;
        companyAssets = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        // Load Data
        companyAssets = readData();
    }

    @Override
    public void onDataSetChanged() {
        // Reload Data
        companyAssets = readData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return companyAssets.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Asset asset = companyAssets.get(position);
        RemoteViews itemView = new RemoteViews(remoteContext.getPackageName(), R.layout.asset_item);
        itemView.setTextViewText(R.id.nameText,  asset.getName());
        itemView.setTextViewText(R.id.titleText, asset.getTitle());
        itemView.setTextViewText(R.id.rateText,  asset.getRate());
        Intent intent = new Intent();
        intent.putExtra(WidgetProvider.EXTRA_ITEM, asset);
        itemView.setOnClickFillInIntent(R.id.asset_item, intent);
        return itemView;
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
        return ID_CONSTANT + position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public ArrayList readData() {
        ArrayList<Asset> assetArray = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(
                    new File(remoteContext.getFilesDir(),"")+File.separator+"assets.ser");
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
