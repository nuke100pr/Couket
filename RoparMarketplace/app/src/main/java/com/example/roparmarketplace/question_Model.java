package com.example.roparmarketplace;

public class question_Model {

    private  String photoName;
    private  String questionNo;
    private  String correctOption;

    public question_Model(String photoName, String questionNo, String correctOption) {
        this.photoName = photoName;
        this.questionNo = questionNo;
        this.correctOption = correctOption;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
}
