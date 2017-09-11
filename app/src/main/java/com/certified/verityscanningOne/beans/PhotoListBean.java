package com.certified.verityscanningOne.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class PhotoListBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    Boolean PhotolistserviceSuccess = false;
    private String photo_album_id = null;
    private String photo_user_id = null;
    private String photo_album_name = null;
    private String photo_album_image_count = null;
    private String photo_album_post_on = null;
    private String photo_album_post_by = null;
    private String photo_album_post_visibility = null;
    private String photo_album_image = null;
    private String photo_album_big_image = null;
    private String photo_album_like_count = null;
    private String photo_album_dislike_count = null;
    private String photo_album_is_liked = null;
    private String photo_album_comment_count = null;

    private List<String> photoes_original = new ArrayList<String>();
    private List<String> photoes_small = new ArrayList<String>();

    private List<String> comment_title = new ArrayList<String>();
    private List<String> comment_post_by = new ArrayList<String>();
    private List<String> comment_post_on = new ArrayList<String>();

    public List<String> getComment_title() {
        return comment_title;
    }

    public void setComment_title(List<String> comment_title) {
        this.comment_title = comment_title;
    }

    public List<String> getComment_post_by() {
        return comment_post_by;
    }

    public void setComment_post_by(List<String> comment_post_by) {
        this.comment_post_by = comment_post_by;
    }

    public List<String> getComment_post_on() {
        return comment_post_on;
    }

    public void setComment_post_on(List<String> comment_post_on) {
        this.comment_post_on = comment_post_on;
    }

    public String getPhoto_album_comment_count() {
        return photo_album_comment_count;
    }

    public void setPhoto_album_comment_count(String photo_album_comment_count) {
        this.photo_album_comment_count = photo_album_comment_count;
    }

    public String getPhoto_album_like_count() {
        return photo_album_like_count;
    }

    public void setPhoto_album_like_count(String photo_album_like_count) {
        this.photo_album_like_count = photo_album_like_count;
    }

    public String getPhoto_album_dislike_count() {
        return photo_album_dislike_count;
    }

    public void setPhoto_album_dislike_count(String photo_album_dislike_count) {
        this.photo_album_dislike_count = photo_album_dislike_count;
    }

    public String getPhoto_album_is_liked() {
        return photo_album_is_liked;
    }

    public void setPhoto_album_is_liked(String photo_album_is_liked) {
        this.photo_album_is_liked = photo_album_is_liked;
    }

    public String getPhoto_user_id() {
        return photo_user_id;
    }

    public void setPhoto_user_id(String photo_user_id) {
        this.photo_user_id = photo_user_id;
    }

    public String getPhoto_album_post_by() {
        return photo_album_post_by;
    }

    public void setPhoto_album_post_by(String photo_album_post_by) {
        this.photo_album_post_by = photo_album_post_by;
    }

    public String getPhoto_album_post_visibility() {
        return photo_album_post_visibility;
    }

    public void setPhoto_album_post_visibility(
            String photo_album_post_visibility) {
        this.photo_album_post_visibility = photo_album_post_visibility;
    }

    public List<String> getPhotoes_original() {
        return photoes_original;
    }

    public void setPhotoes_original(List<String> photoes_original) {
        this.photoes_original = photoes_original;
    }

    public List<String> getPhotoes_small() {
        return photoes_small;
    }

    public void setPhotoes_small(List<String> photoes_small) {
        this.photoes_small = photoes_small;
    }

    public String getPhoto_album_big_image() {
        return photo_album_big_image;
    }

    public void setPhoto_album_big_image(String photo_album_big_image) {
        this.photo_album_big_image = photo_album_big_image;
    }

    public Boolean getPhotolistserviceSuccess() {
        return PhotolistserviceSuccess;
    }

    public void setPhotolistserviceSuccess(Boolean photolistserviceSuccess) {
        PhotolistserviceSuccess = photolistserviceSuccess;
    }

    public String getPhoto_album_id() {
        return photo_album_id;
    }

    public void setPhoto_album_id(String photo_album_id) {
        this.photo_album_id = photo_album_id;
    }

    public String getPhoto_album_name() {
        return photo_album_name;
    }

    public void setPhoto_album_name(String photo_album_name) {
        this.photo_album_name = photo_album_name;
    }

    public String getPhoto_album_image_count() {
        return photo_album_image_count;
    }

    public void setPhoto_album_image_count(String photo_album_image_count) {
        this.photo_album_image_count = photo_album_image_count;
    }

    public String getPhoto_album_post_on() {
        return photo_album_post_on;
    }

    public void setPhoto_album_post_on(String photo_album_post_on) {
        this.photo_album_post_on = photo_album_post_on;
    }

    public String getPhoto_album_image() {
        return photo_album_image;
    }

    public void setPhoto_album_image(String photo_album_image) {
        this.photo_album_image = photo_album_image;
    }

}
