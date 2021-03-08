package com.example.android.homework3;

public class Location {
    private String location;
    private String type;
    private String dimension;


    public Location(String location, String type, String dimension) {
        this.location = location;
        this.type = type;
        this.dimension = dimension;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
