package com.devandroid.bakingapp.Model;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * (1) Annotate the class with the @Parcel decorator.
 * (2) Use only public fields (private fields cannot be detected during annotation) that need to be serialized.
 * (3) Create a public constructor with no arguments for the annotation library.
 */

@Parcel
public class Recipe {

    int mId;
    String mName;
    ArrayList<Ingredient> lstIngredients;
    ArrayList<Step> lstSteps;

    public Recipe() { }

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {

        mId = id;
        mName = name;
        lstIngredients = ingredients;
        lstSteps = steps;
    }

    public String getName() { return mName; }

    public ArrayList<Ingredient> getLstIngredients() { return lstIngredients; }

    public ArrayList<Step> getLstSteps() { return lstSteps; }

}
