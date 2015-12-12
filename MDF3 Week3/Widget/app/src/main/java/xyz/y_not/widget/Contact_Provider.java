package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Contact_Provider extends AppWidgetProvider {
    public static final String ACTION_VIEW_DETAILS = "xyz.y_not.widget.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "xyz.y_not.widget.Contact_Provider.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, Contact_Service.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.contact_widget);
            widgetView.setRemoteAdapter(android.R.id.list, intent);
            widgetView.setEmptyView(android.R.id.widget_frame, R.id.empty);

            // Jump to Contact
            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent dIntent = PendingIntent.getBroadcast(context, 0, detailIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(android.R.id.list, dIntent);

            // Add New Contact
            Intent addIntent = new Intent(context, Form_View.class);
            PendingIntent aIntent = PendingIntent.getActivity(context, 0, addIntent, 0);
            widgetView.setOnClickPendingIntent(R.id.addContact, aIntent);


            appWidgetManager.updateAppWidget(widgetId, widgetView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            Contact contact = (Contact) intent.getSerializableExtra(EXTRA_ITEM);
            if (contact != null) {
                Intent details = new Intent(context, Detail_View.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(Detail_View.EXTRA_ITEM, contact);
                context.startActivity(details);
            }
        }
        super.onReceive(context, intent);
    }
}
