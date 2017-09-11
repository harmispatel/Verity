package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by harmis on 9/9/16.
 */
public class Parameterbean implements Serializable {

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
    String birthDay;
    String mobileNumber;
    String socialType;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    String confirmPassword;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String isSocial;
    String firebaseId;
    String profileImage;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    public String getReceiverReferralCode() {
        return receiverReferralCode;
    }

    public void setReceiverReferralCode(String receiverReferralCode) {
        this.receiverReferralCode = receiverReferralCode;
    }

    String receiverReferralCode=null;

}
