package com.devandroid.bakingapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preferences {

    private static String PREFS_FILE = "prefs_file";
    private static String PREFS_RECIPE_NAME = "prefs_recipe_name";
    private static String PREFS_LIST = "prefs_list";

    public static void saveStringList(Context context, ArrayList<String> lstStrings) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>(lstStrings);
        editor.putStringSet(PREFS_LIST, set);
        editor.commit();
    }

    public static ArrayList<String> restoreStringList(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        Set<String> set = sharedPreferences.getStringSet(PREFS_LIST, null);

        if(set==null)
            return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public static void saveStringRecipe(Context context, String strRecipe) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREFS_RECIPE_NAME, strRecipe);
        editor.commit();
    }

    public static String restoreStringRecipe(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        String strName = sharedPreferences.getString(PREFS_RECIPE_NAME, "");

        return strName;
    }


}
