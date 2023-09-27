package com.example.roparmarketplace;

public class news_Model {

    private String photoName;
    private String newsContent;
    private String newsTitle;
    private String newsTime;

    public news_Model(String photoName, String newsContent, String newsTitle, String newsTime) {
        this.photoName = photoName;
        this.newsContent = newsContent;
        this.newsTitle = newsTitle;
        this.newsTime = newsTime;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }
}
