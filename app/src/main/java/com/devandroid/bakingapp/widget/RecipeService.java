package com.devandroid.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RecipeService extends IntentService {

    public static final String MY_ACTION_ = "com.devandroid.bakingapp.action.my_action";
    public static final String ACTION_UPDATE_RECIPE_STEP_ = "com.devandroid.bakingapp.action.update_recipe_step";

    public RecipeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (MY_ACTION_.equals(action)) {
                handleMyAction();
            } else if(ACTION_UPDATE_RECIPE_STEP_.equals(action)) {
                handleActionUpdateRecipeStep(intent);
            }
        }
    }

    private void handleMyAction() { }

    private void handleActionUpdateRecipeStep(@Nullable Intent intent) { }

}
