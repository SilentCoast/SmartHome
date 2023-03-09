package com.example.demoexamsmartphone.Classes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class Device {
    public Device(){

    }
    private int textColor;
    private Drawable image;
    private String type;
    private int id;
    private Integer speed_fan;
    private Integer temperature;
    private Integer light_lm;
    private boolean workState;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Integer getLight_lm() {
        return light_lm;
    }

    public Integer getSpeed_fan() {
        return speed_fan;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setLight_lm(Integer light_lm) {
        this.light_lm = light_lm;
    }

    public void setSpeed_fan(Integer speed_fan) {
        this.speed_fan = speed_fan;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Drawable getImage() {
        return image;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isWorkState() {
        return workState;
    }

    public void setWorkState(boolean workState) {
        this.workState = workState;
    }
}
