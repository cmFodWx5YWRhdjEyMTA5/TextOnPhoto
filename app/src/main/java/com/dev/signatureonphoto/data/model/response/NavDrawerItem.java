package com.dev.signatureonphoto.data.model.response;


public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int icMenu;


    public NavDrawerItem() {

    }

    public NavDrawerItem(String title, int icMenu) {
        this.title = title;
        this.icMenu = icMenu;
    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcMenu() {
        return icMenu;
    }

    public void setIcMenu(int icMenu) {
        this.icMenu = icMenu;
    }
}
