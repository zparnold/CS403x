package edu.wpi.cs.cs403xproj4.wpifreebies.models;

import java.util.Date;

/**
 * Created by Tyler on 4/24/2015.
 */
public class Freebie {
    String _id;
    String name;
    String description;
    Date postDate;
    double latitude;
    double longitude;
    int upVotes;
    int downVotes;
    String color;

    public Freebie() {
        _id = "";
        name = "";
        description = "";
        Date postDate = new Date();
        latitude = 0.0;
        longitude = 0.0;
        int upVotes = 0;
        int downVotes = 0;
        color = "RED";
    }

    public Freebie(String name, String description, Double latitude, Double longitude, String color) {
        this._id = "";
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
    }

    public Freebie(String _id, String name, String description, Date postDate, Double latitude,
                   Double longitude, int upVotes, int downVotes, String color) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.postDate = postDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String toJSON(){
        String retString = "";
        retString += "{\"name\":\""+this.name+"\",";
        retString += "\"description\":\""+this.description+"\",";
        retString += "\"postDate\":\""+this.postDate.toString()+"\",";;
        retString += "\"latitude\":\""+Double.toString(this.latitude)+"\",";
        retString += "\"longitude\":\""+Double.toString(this.longitude)+"\",";
        retString += "\"upVotes\":\""+Integer.toString(this.upVotes)+"\",";
        retString += "\"downVotes\":\""+Integer.toString(this.downVotes)+"\",";
        return retString;
    }
}
