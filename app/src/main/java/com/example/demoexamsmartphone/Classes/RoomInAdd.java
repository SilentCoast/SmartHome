package com.example.demoexamsmartphone.Classes;

import android.graphics.drawable.Drawable;

public class RoomInAdd {
    String type;
    int textColor;
    Drawable image;

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTextColor() {
        return textColor;
    }

}
