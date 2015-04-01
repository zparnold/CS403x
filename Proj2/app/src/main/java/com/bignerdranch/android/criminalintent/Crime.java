package com.bignerdranch.android.criminalintent;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO = "photo";
    
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo[] mPhoto;

    private int counter;
    
    public Crime() {
        mId = UUID.randomUUID();
        mPhoto = new Photo[4];
        mDate = new Date();
        counter = 0;
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        mPhoto = new Photo[4];

        if (json.has(JSON_PHOTO)) {
            for (int i = 0; i < json.getJSONArray(JSON_PHOTO).length(); i++) {
                mPhoto[i] = ((Photo) json.getJSONArray(JSON_PHOTO).get(i));
            }
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        LinkedList<Photo> aListOfPhotos = new LinkedList<Photo>();
        for (int i = 0; i < mPhoto.length; i++)
            aListOfPhotos.add(mPhoto[i]);
        if (mPhoto.length > 0)
            json.put(JSON_PHOTO, new JSONArray(aListOfPhotos));
        return json;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

	public Photo[] getPhotoList() {
		return mPhoto;
	}

	public void setPhotoList(Photo[] photoList) {
		mPhoto = photoList;
	}

    public void addPhoto(Photo photo) {
        if (counter >= 4) {
            mPhoto[counter % 4] = photo;
        }

        else {//counter < 4
            mPhoto[3] = mPhoto[2];
            mPhoto[2] = mPhoto[1];
            mPhoto[1] = mPhoto[0];
            mPhoto[0] = photo;
        }
        counter++;
    }

    public Photo getPhoto() {
        if (mPhoto.length >= 1)
            return mPhoto[0];
        else
            return null;
    }

    public Photo[] getMinorPhotoList() {
        Photo[] minorPhotoList = new Photo[3];
        minorPhotoList[0] = mPhoto[1];
        minorPhotoList[1] = mPhoto[2];
        minorPhotoList[2] = mPhoto[3];
        return minorPhotoList;
    }

    public void deletePhotoList() {
        for (int i = 0; i<mPhoto.length; i++)
            mPhoto[i] = null;
        counter = 0;
    }
    
}
