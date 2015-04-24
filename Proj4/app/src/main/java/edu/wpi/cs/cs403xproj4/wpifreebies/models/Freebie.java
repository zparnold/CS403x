package edu.wpi.cs.cs403xproj4.wpifreebies.models;

import java.util.Date;

/**
 * Created by Tyler on 4/24/2015.
 */
public class Freebie {
    String name;
    String description;
    Date postDate;
    Long latitude;
    Long longitude;
    int upVotes;
    int downVotes;
    Category category;

    public Freebie() { }

    public Freebie(String name, String description, Date postDate, Long latitude, Long longitude,
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

}
