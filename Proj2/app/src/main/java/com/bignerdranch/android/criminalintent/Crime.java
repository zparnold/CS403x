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
    private LinkedList<Photo> mPhoto;
    
    public Crime() {
        mId = UUID.randomUUID();
        mPhoto = new LinkedList<Photo>();
        mDate = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        mPhoto = new LinkedList<Photo>();

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
        if (mPhoto.size() > 0)
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
		mPhoto = new LinkedList<Photo>(photoList);
	}

    public void addPhoto(Photo photo) {
        if (mPhoto.size() >= 5)
            mPhoto.removeLast();

        mPhoto.addFirst(photo);
    }

    public Photo getPhoto() {
        if (mPhoto.size() >= 1)
            return mPhoto.getFirst();
        else
            return null;
    }

    public List<Photo> getMinorPhotoList() {
        LinkedList<Photo> minorPhotos = new LinkedList<Photo>(mPhoto);
        if (minorPhotos.size() >= 2) {
            minorPhotos.removeFirst();
            return minorPhotos;
        } else {
            return new LinkedList<Photo>();
        }
    }

    public void deletePhotoList() {
        mPhoto = new LinkedList<Photo>();
    }
    
}
