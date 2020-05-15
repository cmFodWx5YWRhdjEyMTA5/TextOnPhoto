package com.dev.signatureonphoto.features.saved;


import java.util.Date;

public class GalleryItem {
    private String path;
    private Date date;

    public GalleryItem() {
    }

    public GalleryItem(String path) {
        this.path = path;
    }

    public GalleryItem(String path, Date date) {
        this.path = path;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
