package com.devandroid.bakingapp.Model;

import java.util.ArrayList;

public class Recipe {

    private int mId;
    private String mName;
    private ArrayList<Ingredient> lstIngredients;
    private ArrayList<Step> lstSteps;

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {

        mId = id;
        mName = name;
        lstIngredients = ingredients;
        lstSteps = steps;
    }

}
