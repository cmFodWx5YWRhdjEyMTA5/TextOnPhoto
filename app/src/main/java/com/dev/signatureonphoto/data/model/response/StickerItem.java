package com.dev.signatureonphoto.data.model.response;

public class StickerItem {
    private int imageSticker;
    private boolean newSticker=false;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StickerItem(int imageSticker, boolean newSticker, String path) {

        this.imageSticker = imageSticker;
        this.newSticker = newSticker;
        this.path = path;
    }

    public StickerItem(String path) {
        this.path = path;
    }

    public StickerItem(int imageSticker) {
        this.imageSticker = imageSticker;
    }


    public void setNewSticker(boolean newSticker) {
        this.newSticker = newSticker;
    }

    public boolean isNewSticker() {
        return newSticker;
    }

    public int getImageSticker() {
        return imageSticker;
    }

    public void setImageSticker(int imageSticker) {
        this.imageSticker = imageSticker;
    }
}
