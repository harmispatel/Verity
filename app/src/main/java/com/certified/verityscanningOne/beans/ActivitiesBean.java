package com.certified.verityscanningOne.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class ActivitiesBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String activity_ID = null;
    private String activity_user_profile = null;
    private String activity_username = null;
    private String isliked = null;
    private String likeDislikeFlag = "1";
    private String app_action_type = null;

    public String getApp_action_type() {
        return app_action_type;
    }

    public void setApp_action_type(String app_action_type) {
        this.app_action_type = app_action_type;
    }

    public String getLikeDislikeFlag() {
        return likeDislikeFlag;
    }

    public void setLikeDislikeFlag(String likeDislikeFlag) {
        this.likeDislikeFlag = likeDislikeFlag;
    }

    public String getIsliked() {
        return isliked;
    }

    public void setIsliked(String isliked) {
        this.isliked = isliked;
    }

    int count = 0;

    private String loggedUserAvtar = null;
    private String postCreated = null;
    private String videoURL = null;
    private String videoThumnail = null;
    private String videoDescription = null;
    private String postedContentText = null;
    private String like = null;
    private String dislike = null;
    private String postedImageThumbBig = null;

    public String getPostedImageThumbBig() {
        return postedImageThumbBig;
    }

    public void setPostedImageThumbBig(String postedImageThumbBig) {
        this.postedImageThumbBig = postedImageThumbBig;
    }

    public String getPostedImageThumbSmall() {
        return postedImageThumbSmall;
    }

    public void setPostedImageThumbSmall(String postedImageThumbSmall) {
        this.postedImageThumbSmall = postedImageThumbSmall;
    }

    private String postedImageThumbSmall = null;

    private String postType = null;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    ArrayList<CommentBean> arrayListCommentList;
    int commentCount = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLoggedUserAvtar() {
        return loggedUserAvtar;
    }

    public void setLoggedUserAvtar(String loggedUserAvtar) {
        this.loggedUserAvtar = loggedUserAvtar;
    }

    public String getPostCreated() {
        return postCreated;
    }

    public void setPostCreated(String postCreated) {
        this.postCreated = postCreated;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoThumnail() {
        return videoThumnail;
    }

    public void setVideoThumnail(String videoThumnail) {
        this.videoThumnail = videoThumnail;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getPostedContentText() {
        return postedContentText;
    }

    public void setPostedContentText(String postedContentText) {
        this.postedContentText = postedContentText;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public ArrayList<CommentBean> getArrayListCommentList() {
        return arrayListCommentList;
    }

    public void setArrayListCommentList(
            ArrayList<CommentBean> arrayListCommentList) {
        this.arrayListCommentList = arrayListCommentList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isServiceSuccess = false;

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setServiceSuccess(boolean isServiceSuccess) {
        this.isServiceSuccess = isServiceSuccess;
    }

    public String getActivity_ID() {
        return activity_ID;
    }

    public void setActivity_ID(String activity_ID) {
        this.activity_ID = activity_ID;
    }

    public String getActivity_user_profile() {
        return activity_user_profile;
    }

    public void setActivity_user_profile(String activity_user_profile) {
        this.activity_user_profile = activity_user_profile;
    }

    public String getActivity_username() {
        return activity_username;
    }

    public void setActivity_username(String activity_username) {
        this.activity_username = activity_username;
    }

}
