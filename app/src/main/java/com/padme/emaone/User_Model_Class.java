package com.padme.emaone;



public class User_Model_Class {
private String UID;
private String DocID;
private String UCID;
private String Name;
private String Email;
private String UserLevel;
private String ProfilePhotoUri;

public User_Model_Class(){}
    public User_Model_Class(String UID, String docID, String UCID, String name, String email, String userLevel, String profilePhotoUri) {
        this.UID = UID;
        DocID = docID;
        this.UCID = UCID;
        Name = name;
        Email = email;
        UserLevel = userLevel;
        ProfilePhotoUri = profilePhotoUri;
    }

    public String getUID() {
        return UID;
    }

    public String getDocID() {
        return DocID;
    }

    public String getUCID() {
        return UCID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public String getProfilePhotoUri() {
        return ProfilePhotoUri;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public void setUCID(String UCID) {
        this.UCID = UCID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        ProfilePhotoUri = profilePhotoUri;
    }
}
