package com.devandroid.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.devandroid.bakingapp.MainActivity;
import com.devandroid.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppProvider extends AppWidgetProvider {

     static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_provider);
        //views.setTextViewText(R.id.appwidget_text, "teste");
         appWidgetManager.updateAppWidget(appWidgetId, views);


         /**
          * Starting a intent
          */
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

         /**
          * Starting a service
          */
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

}

