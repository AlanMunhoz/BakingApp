package com.devandroid.bakingapp.Util;

import android.content.Context;
import android.util.Log;

import com.devandroid.bakingapp.Model.Ingredient;
import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class JSON {

    private static final String LOG_TAG = JSON.class.getSimpleName();
    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_STEPS = "steps";
    private static final String JSON_INGREDIENTS = "ingredients";
    private static final String JSON_QUANTITY = "quantity";
    private static final String JSON_MEASURE = "measure";
    private static final String JSON_INGREDIENT = "ingredient";
    private static final String JSON_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_VIDEO_URL = "videoURL";
    private static final String JSON_THUMBNAIL = "thumbnailURL";

    /**
     * ParseRecipe - Convert JSON string to Recipe list
     * @param ctx
     * @return ArrayList<Recipe>
     * @throws JSONException
     */
    public static ArrayList<Recipe> ParseRecipe(Context ctx) throws JSONException {

        /**
         * parse json file to string
         */
        String JSonString;
        try {
            InputStream is = ctx.getAssets().open("recipe_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        /**
         * convert json string to object
         */
        ArrayList<Recipe> lstRecipe = new ArrayList<>();
        JSONArray lestJRecipe = new JSONArray(JSonString);

        for(int i=0; i<lestJRecipe.length(); i++) {

            ArrayList<Ingredient> lstIngredients = new ArrayList();
            ArrayList<Step> lstSteps = new ArrayList();

            JSONObject jRecipe = lestJRecipe.getJSONObject(i);
            int recipeId = jRecipe.getInt(JSON_ID);
            String recipeName = jRecipe.getString(JSON_NAME);

            JSONArray jIngredients = jRecipe.getJSONArray(JSON_INGREDIENTS);
            for(int j=0; j<jIngredients.length(); j++) {
                double quantity = jIngredients.getJSONObject(j).getDouble(JSON_QUANTITY);
                String measure = jIngredients.getJSONObject(j).getString(JSON_MEASURE);
                String description = jIngredients.getJSONObject(j).getString(JSON_INGREDIENT);
                lstIngredients.add(new Ingredient(quantity, measure, description));
            }

            JSONArray jSteps = jRecipe.getJSONArray(JSON_STEPS);
            for(int k=0; k<jSteps.length(); k++) {
                int id = jSteps.getJSONObject(k).getInt(JSON_ID);
                String shortDescription = jSteps.getJSONObject(k).getString(JSON_SHORT_DESCRIPTION);
                String description = jSteps.getJSONObject(k).getString(JSON_DESCRIPTION);
                String videoURL = jSteps.getJSONObject(k).getString(JSON_VIDEO_URL);
                String thumbnailURL = jSteps.getJSONObject(k).getString(JSON_THUMBNAIL);
                lstSteps.add(new Step(id, shortDescription, description, videoURL, thumbnailURL));
            }

            lstRecipe.add(new Recipe(recipeId, recipeName, lstIngredients, lstSteps));
            Log.d(LOG_TAG, "Name: " + recipeName + "id: " + recipeId);
        }
        return lstRecipe;
    }

}


