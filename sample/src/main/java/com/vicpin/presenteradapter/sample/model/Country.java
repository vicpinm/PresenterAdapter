package com.vicpin.presenteradapter.sample.model;

/**
 * Created by Victor on 25/06/2016.
 */
public class Country {

    private String name;

    private int imageResourceId;

    public Country(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
