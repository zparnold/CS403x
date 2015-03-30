package com.bignerdranch.android.criminalintent;

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
    private List<Photo> mPhoto;
    
    public Crime() {
        mId = UUID.randomUUID();
        mPhoto = new ArrayList<Photo>();
        mDate = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        mPhoto = new ArrayList<Photo>();

        if (json.has(JSON_PHOTO)) {
            for (int i = 0; i < json.getJSONArray(JSON_PHOTO).length(); i++) {
                mPhoto.add((Photo) json.getJSONArray(JSON_PHOTO).get(i));
            }
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null)
            json.put(JSON_PHOTO, new JSONArray(mPhoto));
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

	public List<Photo> getPhotoList() {
		return mPhoto;
	}

	public void setPhotoList(List<Photo> photoList) {
		mPhoto = photoList;
	}
    
}
