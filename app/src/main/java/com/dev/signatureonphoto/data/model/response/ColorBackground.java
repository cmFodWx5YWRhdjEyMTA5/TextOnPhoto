package com.dev.signatureonphoto.data.model.response;


public class ColorBackground {
    private int image;
    private boolean isChecked;


    public ColorBackground(int image) {
        this.image = image;
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
