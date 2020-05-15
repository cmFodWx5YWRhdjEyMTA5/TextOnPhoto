package com.dev.signatureonphoto.data.model.response;

public class FontText {
    private int image;
    private boolean isNew=false;

    public FontText(int image) {
        this.image = image;
    }

    public FontText(int image, boolean isNew) {
        this.image = image;
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
