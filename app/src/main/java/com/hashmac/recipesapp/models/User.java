package com.hashmac.recipesapp.models;

public class User {
    private String id, name, email, image, cover;

    public User() {
    }

    public User(String id, String name, String email, String image, String cover) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
