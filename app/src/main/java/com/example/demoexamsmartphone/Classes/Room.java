package com.example.demoexamsmartphone.Classes;

import android.graphics.drawable.Drawable;

public class Room {
    private Drawable image;
    private String type;
    private String name;
    private int id;
    public Drawable getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
