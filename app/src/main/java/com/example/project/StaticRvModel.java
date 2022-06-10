package com.example.project;

public class StaticRvModel {
    private String image;
    private String text,id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StaticRvModel(String image, String text, String id) {
        this.image = image;
        this.text = text;
        this.id = id;
    }
}
