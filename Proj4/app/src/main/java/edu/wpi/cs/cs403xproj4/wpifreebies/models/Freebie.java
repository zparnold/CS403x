package edu.wpi.cs.cs403xproj4.wpifreebies.models;

import java.util.Date;

/**
 * Created by Tyler on 4/24/2015.
 */
public class Freebie {
    String name;
    String description;
    Date postDate;
    double latitude;
    double longitude;
    int upVotes;
    int downVotes;
    Category category;

    public Freebie() { }

    public Freebie(String name, String description, Date postDate, double latitude, double longitude,
                   int upVotes, int downVotes, Category category) {
        this.name = name;
        this.description = description;
        this.postDate = postDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.category = category;
    }

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

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toJSON(){
        String retString = "";
        retString += "{\"name\":\""+this.name+"\",";
        retString += "\"description\":\""+this.description+"\",";
        retString += "\"postDate\":\""+this.postDate.toString()+"\",";;
        retString += "\"latitude\":\""+Double.toString(this.latitude)+"\",";
        retString += "\"longitude\":\""+Double.toString(this.longitude)+"\",";
        retString += "\"upVotes\":\""+Integer.toString(this.upVotes)+"\",";
        retString += "\"downVotes\":\""+Integer.toString(this.downVotes)+"\",";
        retString += "\"category\":"+this.category.toJSON()+"}";
        return retString;
    }
}
