package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class MessageBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    String onlyMessageID;
    public String getOnlyMessageID() {
        return onlyMessageID;
    }


    public void setOnlyMessageID(String onlyMessageID) {
        this.onlyMessageID = onlyMessageID;
    }


    String originalPhoto;
    String senderName;
    String smallPhoto;
    String postDate;
    String fromUserID;

    public String getFromUserID() {
        return fromUserID;
    }


    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }


    String subject;
    String isReadOrUnreadMail="0";




    public String getIsReadOrUnreadMail() {
        return isReadOrUnreadMail;
    }


    public void setIsReadOrUnreadMail(String isReadOrUnreadMail) {
        this.isReadOrUnreadMail = isReadOrUnreadMail;
    }





    boolean isServiceSuccess=false;
    boolean mailCheckedStatus=false;


    String message;
    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getOriginalPhoto() {
        return originalPhoto;
    }


    public void setOriginalPhoto(String originalPhoto) {
        this.originalPhoto = originalPhoto;
    }


    public String getSenderName() {
        return senderName;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public String getSmallPhoto() {
        return smallPhoto;
    }


    public void setSmallPhoto(String smallPhoto) {
        this.smallPhoto = smallPhoto;
    }


    public String getPostDate() {
        return postDate;
    }


    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }








    public boolean isMailCheckedStatus() {
        return mailCheckedStatus;
    }


    public void setMailCheckedStatus(boolean mailCheckedStatus) {
        this.mailCheckedStatus = mailCheckedStatus;
    }


    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }


    public void setServiceSuccess(boolean isServiceSuccess) {
        this.isServiceSuccess = isServiceSuccess;
    }



}
