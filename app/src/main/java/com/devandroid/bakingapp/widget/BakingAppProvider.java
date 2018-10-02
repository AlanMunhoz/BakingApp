package com.devandroid.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.devandroid.bakingapp.MainActivity;
import com.devandroid.bakingapp.R;
import com.devandroid.bakingapp.Util.Preferences;


/**
 * Implementation of App Widget functionality.
 */
public class BakingAppProvider extends AppWidgetProvider {

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews rv;
        rv = getGardenGridRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getGardenGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_provider);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.listViewWidget, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listViewWidget, appPendingIntent);
        //Nome da receita
        views.setTextViewText(R.id.appwidget_text, Preferences.restoreStringRecipe(context));
        // Handle empty gardens
        //views.setEmptyView(R.id.listViewWidget, R.id.appwidget_text);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

       RecipeService.startUpdateIngredients(context);
    }

}

