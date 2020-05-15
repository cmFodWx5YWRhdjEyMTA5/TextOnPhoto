package com.dev.signatureonphoto.data.model;

import java.io.Serializable;

public class ImageTemplate implements Serializable {
    private int image;
    private boolean isChecked;
    private boolean checkAds;

    public ImageTemplate(int image, boolean isChecked, boolean checkAds) {
        this.image = image;
        this.isChecked = isChecked;
        this.checkAds = checkAds;
    }

    public ImageTemplate(int image, boolean checkAds) {
        this.image = image;
        this.checkAds = checkAds;
    }

    public ImageTemplate(int image) {
        this.image = image;
    }

    public boolean isCheckAds() {
        return checkAds;
    }

    public void setCheckAds(boolean checkAds) {
        this.checkAds = checkAds;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public boolean getChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

