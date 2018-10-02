package com.devandroid.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.devandroid.bakingapp.R;


public class RecipeService extends IntentService {


    public static final String ACTION_UPDATE_INGREDIENTS_ = "com.devandroid.bakingapp.action.update_ingredients";

    public RecipeService(String name) {
        super(name);
    }

    public static void startUpdateIngredients(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_);
        context.startService(intent);

        handleActionUpdateIngredients(intent, context);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_.equals(action)) {
                handleActionUpdateIngredients(intent, this);
            }
        }
    }

    private static void handleActionUpdateIngredients(@Nullable Intent intent, Context context) {

        if (intent != null)
        {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

            BakingAppProvider.updateIngredientWidgets(context, appWidgetManager, appWidgetIds);
        }
    }

}
