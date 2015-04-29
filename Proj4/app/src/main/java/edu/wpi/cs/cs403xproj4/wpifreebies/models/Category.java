package edu.wpi.cs.cs403xproj4.wpifreebies.models;

/**
 * Created by Tyler on 4/24/2015.
 *
 * FOOD = RED
 * CLOTHING = GREEN
 * OTHER = BLUE
 */
public class Category {
    String name;
    String color;

    public Category() { }

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toJSON(){
        String retString = "";
        retString += "{\"name\":\""+this.name+"\",";
        retString += "\"color\":\""+this.color+"\"}";
        return retString;
    }
}
