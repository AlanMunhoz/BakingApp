package com.devandroid.bakingapp.Model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Ingredient {

    @SerializedName("quantity")
    double mQuantity;

    @SerializedName("measure")
    String mMeasure;

    @SerializedName("ingredient")
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
