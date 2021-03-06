package com.dev.signatureonphoto.database;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "QUOTE".
 */
@Entity
public class Quote {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private String categore;

    @NotNull
    private String source;

    @NotNull
    private String upload;

    @Generated
    public Quote() {
    }

    public Quote(Long id) {
        this.id = id;
    }

    @Generated
    public Quote(Long id, String content, String categore, String source, String upload) {
        this.id = id;
        this.content = content;
        this.categore = categore;
        this.source = source;
        this.upload = upload;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContent(@NotNull String content) {
        this.content = content;
    }

    @NotNull
    public String getCategore() {
        return categore;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCategore(@NotNull String categore) {
        this.categore = categore;
    }

    @NotNull
    public String getSource() {
        return source;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSource(@NotNull String source) {
        this.source = source;
    }

    @NotNull
    public String getUpload() {
        return upload;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUpload(@NotNull String upload) {
        this.upload = upload;
    }

}
