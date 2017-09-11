package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by Nakul Sheth on 01-09-2016.
 */
public class Loginbean implements Serializable {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isGetloginDetails() {
        return isGetloginDetails;
    }

    public void setGetloginDetails(boolean getloginDetails) {
        isGetloginDetails = getloginDetails;
    }

    boolean isGetloginDetails=false;

    String name;
    String password;
    String email;
    String birthday;
    String mobileNumber;
    String socialType;
    String username;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    String isSocial;
    String firebaseId;
    String profileImage;


    String errorMessage;

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    boolean isServiceSuccess=false;

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    String referalCode;

}
