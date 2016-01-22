package com.tonykeiser.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_VIEW_DETAILS = "com.tonykeiser.widget.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "com.tonykeiser.widget.WidgetProvider.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            rv.setRemoteAdapter(R.id.widgetList, intent);
            rv.setEmptyView(R.id.widgetList, R.id.empty);

            // Load Selected Asset
            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent dIntent = PendingIntent.getBroadcast(context, 0, detailIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widgetList, dIntent);

            // Create New Asset
            Intent addIntent = new Intent(context, AddActivity.class);
            PendingIntent aIntent = PendingIntent.getActivity(context, 0, addIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.addAsset, aIntent);

            appWidgetManager.updateAppWidget(widgetId, rv);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            Asset asset = (Asset) intent.getSerializableExtra(EXTRA_ITEM);
            if (asset != null) {
                Intent details = new Intent(context, DetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DetailsActivity.EXTRA_ITEM, asset);
                context.startActivity(details);
            }
        }
        super.onReceive(context, intent);
    }
}
