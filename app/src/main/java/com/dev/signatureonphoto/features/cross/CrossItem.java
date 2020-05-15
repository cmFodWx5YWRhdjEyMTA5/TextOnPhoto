package com.dev.signatureonphoto.features.cross;

public class CrossItem {
    private String title,content,packagename;

    public CrossItem() {
    }

    public CrossItem(String title, String content, String packagename) {
        this.title = title;
        this.content = content;
        this.packagename = packagename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
}
