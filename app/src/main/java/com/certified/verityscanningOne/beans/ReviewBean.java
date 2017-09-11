package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by harmis on 25/2/17.
 */

public class ReviewBean implements Serializable {


    String _id = null;
    String _productId = null;
    String _userName = null;
    String _reviewTitle = null;
    String _reviewText = null;
    String _reviewDate = null;
    String _rating= null;
    private boolean isServiceSuccess;


    public String get_rating() {
        return _rating;
    }

    public void set_rating(String _rating) {
        this._rating = _rating;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_productId() {
        return _productId;
    }

    public void set_productId(String _productId) {
        this._productId = _productId;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_reviewTitle() {
        return _reviewTitle;
    }

    public void set_reviewTitle(String _reviewTitle) {
        this._reviewTitle = _reviewTitle;
    }

    public String get_reviewText() {
        return _reviewText;
    }

    public void set_reviewText(String _reviewText) {
        this._reviewText = _reviewText;
    }

    public String get_reviewDate() {
        return _reviewDate;
    }

    public void set_reviewDate(String _reviewDate) {
        this._reviewDate = _reviewDate;
    }

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }
}
