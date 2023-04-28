package com.padme.emaone;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Eventcard_Modal_Class {
    private String Docid;

    private String CUID;
    private String Title;
    private String SubTitle;
    private String TextContent;
    private  String DisplayImageUri;
    private  String ParticipantsCount;
    private String UpvoteCount;
    private String DepartmentName;
    private String CommiteeName;
    private String EventDateTime;
    private Timestamp PostDateTime;
    private String Category;
    public Eventcard_Modal_Class(){};
    public Eventcard_Modal_Class(String docid, String title, String subTitle, String textContent, String displayImageUri, String participantsCount, String upvoteCount, String departmentName, String commiteeName, String eventDateTime, Timestamp postDateTime, String category,String cuid) {
        Docid = docid;
        Title = title;
        SubTitle = subTitle;
        TextContent = textContent;
        DisplayImageUri = displayImageUri;
        ParticipantsCount = participantsCount;
        UpvoteCount = upvoteCount;
        DepartmentName = departmentName;
        CommiteeName = commiteeName;
        EventDateTime = eventDateTime;
        PostDateTime = postDateTime;
        Category = category;
        CUID=cuid;
    }

    public String getDocid() {
        return Docid;
    }
public  String getCUID(){return CUID;}
    public String getTitle() {
        return Title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public String getTextContent() {
        return TextContent;
    }

    public String getDisplayImageUri() {
        return DisplayImageUri;
    }

    public String getParticipantsCount() {
        return ParticipantsCount;
    }

    public String getUpvoteCount() {
        return UpvoteCount;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public String getCommiteeName() {
        return CommiteeName;
    }

    public String getEventDateTime() {
        return EventDateTime;
    }

    public Timestamp getPostDateTime() {
        return PostDateTime;
    }

    public String getCategory() {
        return Category;
    }

    public void setDocid(String docid) {
        Docid = docid;
    }

    public void setCUID(String cuid) {
        CUID = cuid;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public void setTextContent(String textContent) {
        TextContent = textContent;
    }

    public void setDisplayImageUri(String displayImageUri) {
        DisplayImageUri = displayImageUri;
    }

    public void setParticipantsCount(String participantsCount) {
        ParticipantsCount = participantsCount;
    }

    public void setUpvoteCount(String upvoteCount) {
        UpvoteCount = upvoteCount;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public void setCommiteeName(String commiteeName) {
        CommiteeName = commiteeName;
    }

    public void setEventDateTime(String eventDateTime) {
        EventDateTime = eventDateTime;
    }

    public void setPostDateTime(Timestamp postDateTime) {
        PostDateTime = postDateTime;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
