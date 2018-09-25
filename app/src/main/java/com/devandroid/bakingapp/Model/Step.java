package com.devandroid.bakingapp.Model;

import org.parceler.Parcel;

@Parcel
public class Step {

    int mId;
    String mShortDescription;
    String mDescription;
    String mVideoUrl;
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

}
