package com.devandroid.bakingapp.Model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Step {

    @SerializedName("id")
    int mId;

    @SerializedName("shortDescription")
    String mShortDescription;

    @SerializedName("description")
    String mDescription;

    @SerializedName("videoUrl")
    String mVideoUrl;

    @SerializedName("thumbnailUrl")
    String mThumbnailUrl;

    public Step() {}

    public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {

        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    public int getmId() { return mId; }
    public String getmShortDescription() { return mShortDescription; }
    public String getmVideoUrl() { return mVideoUrl; }

}
