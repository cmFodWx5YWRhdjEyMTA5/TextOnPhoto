package com.dev.signatureonphoto.data.model.response;

public class Image {
    private String path;
    private String day;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Image(String path, String day) {
        this.path = path;
        this.day = day;
    }

    public Image() {
    }
}
