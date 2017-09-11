package com.certified.verityscanningOne.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 6/9/16.
 */
public class Searchbean implements Serializable {


    String errorMessage = null;
    boolean isServiceSuccess = false;


    String productId = null;
    String productName = null;
    String productDesc = null;
    String category = null;
    String lat = null;
    String lng = null;
    String companyUniqueId = null;
    String sealPercentage = null;
    String upcCode = null;
    String website = null;
    String thumbImage = null;

    String isRate = null;
    String isReview = null;
    String avgRating = null;
    String rateuserCount = null;
    ReviewBean reviewbean = null;


    public String getIsRate() {
        return isRate;
    }

    public void setIsRate(String isRate) {
        this.isRate = isRate;
    }

    public String getIsReview() {
        return isReview;
    }

    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getRateuserCount() {
        return rateuserCount;
    }

    public void setRateuserCount(String rateuserCount) {
        this.rateuserCount = rateuserCount;
    }

    public ReviewBean getReviewbean() {
        return reviewbean;
    }

    public void setReviewbean(ReviewBean reviewbean) {
        this.reviewbean = reviewbean;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public String getSealPercentage() {
        return sealPercentage;
    }

    public void setSealPercentage(String sealPercentage) {
        this.sealPercentage = sealPercentage;
    }

    public String getCompanyUniqueId() {
        return companyUniqueId;
    }

    public void setCompanyUniqueId(String companyUniqueId) {
        this.companyUniqueId = companyUniqueId;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    String productImage = null;


    ArrayList<Searchbean> searchbeanArrayList_extra_feilds_typa_a = new ArrayList<>();

    public ArrayList<Searchbean> getSearchbeanArrayList_extra_feilds_typa_b() {
        return searchbeanArrayList_extra_feilds_typa_b;
    }

    public void setSearchbeanArrayList_extra_feilds_typa_b(ArrayList<Searchbean> searchbeanArrayList_extra_feilds_typa_b) {
        this.searchbeanArrayList_extra_feilds_typa_b = searchbeanArrayList_extra_feilds_typa_b;
    }

    public ArrayList<Searchbean> getSearchbeanArrayList_extra_feilds_typa_a() {
        return searchbeanArrayList_extra_feilds_typa_a;
    }

    public void setSearchbeanArrayList_extra_feilds_typa_a(ArrayList<Searchbean> searchbeanArrayList_extra_feilds_typa_a) {
        this.searchbeanArrayList_extra_feilds_typa_a = searchbeanArrayList_extra_feilds_typa_a;
    }

    ArrayList<Searchbean> searchbeanArrayList_extra_feilds_typa_b = new ArrayList<>();


    public boolean isExistExtraFeildsA() {
        return isExistExtraFeildsA;
    }

    public void setExistExtraFeildsA(boolean existExtraFeildsA) {
        isExistExtraFeildsA = existExtraFeildsA;
    }

    public boolean isExistExtraFeildsB() {
        return isExistExtraFeildsB;
    }

    public void setExistExtraFeildsB(boolean existExtraFeildsB) {
        isExistExtraFeildsB = existExtraFeildsB;
    }

    boolean isExistExtraFeildsA = false;
    boolean isExistExtraFeildsB = false;


    String placeholder = null;
    String type = null;
    String value = null;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    String redirectUrl = null;


}
