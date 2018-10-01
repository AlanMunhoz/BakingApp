package com.devandroid.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.devandroid.bakingapp.MainActivity;
import com.devandroid.bakingapp.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppProvider extends AppWidgetProvider {

    ArrayList<String> lstAdapter;

    /*
     static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_provider);
        //views.setTextViewText(R.id.appwidget_text, "teste");
         appWidgetManager.updateAppWidget(appWidgetId, views);



        // Starting a intent

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);


        // Starting a service
        //views.setOnClickPendingIntent(R.id.appwidget_text, startService(context));

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }

    */



    /*
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d("01102018", "BakingAppProvider -> onUpdate");

        final int N = appWidgetIds.length;
        /*int[] appWidgetIds holds ids of multiple instance of your widget
         * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public void setListAdapter(ArrayList<String> lstAdapter) {
        this.lstAdapter = lstAdapter;
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        Log.d("01102018", "BakingAppProvider -> updateWidgetListView");

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.baking_app_provider);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, RecipeService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //svcIntent.putStringArrayListExtra("ADAPTER_LIST", lstAdapter);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent);
        //setting an empty view in case of no data
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.appwidget_text);
        return remoteViews;
    }



}

