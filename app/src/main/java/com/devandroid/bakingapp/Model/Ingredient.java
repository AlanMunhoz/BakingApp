package com.devandroid.bakingapp.Model;

import org.parceler.Parcel;

@Parcel
public class Ingredient {

    double mQuantity;
    String mMeasure;
    String mDescription;

    public Ingredient() {}

    public Ingredient(double quantity, String measure, String description) {

        mQuantity = quantity;
        mMeasure = measure;
        mDescription = description;
    }

    public double getmQuantity() { return mQuantity; }
    public String getmMeasure() { return mMeasure; }
    public String getmDescription() { return mDescription; }

}
