package edu.wpi.cs.cs403xproj4.wpifreebies.models;

/**
 * Created by Tyler on 4/24/2015.
 */
public class Category {
    String _id;
    String name;
    String color;

    public Category() { }

    public Category(String _id, String name, String color) {
        this._id = _id;
        this.name = name;
        this.color = color;
    }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

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
}
