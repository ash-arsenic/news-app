package com.example.newsapp;

public class NewsModal {
    private String title;
    private String author;
    private String source;
    private String description;
    private String image;
    private String redirect;
    private String published;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public NewsModal(String title, String author, String source, String description, String image, String redirect, String published) {
        this.title = title;
        this.author = author;
        this.source = source;
        this.description = description;
        this.image = image;
        this.redirect = redirect;
        published = published.replace('T', ' ');
        published = published.replace('Z', ' ');
        this.published = published;
    }
}
