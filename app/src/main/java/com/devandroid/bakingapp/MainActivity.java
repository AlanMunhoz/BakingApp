package com.devandroid.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Util.JSON;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ArrayList<Recipe> lstRecipe = JSON.ParseRecipe(JSON.JSON_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
