package com.example.roparmarketplace;

public class resultModel {
    private String name;
    private String photo;
    private String score;
    private String time;

    public resultModel(String name, String photo, String score, String time) {
        this.name = name;
        this.photo = photo;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
