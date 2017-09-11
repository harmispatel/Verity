package com.certified.verityscanningOne.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class VideoListBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    Boolean videolistserviceSuccess = false;
    private String video_id = null;
    private String video_name = null;
    private String video_link = null;
    private String video_image = null;
    private String video_post_by = null;
    private String video_post_on = null;
    private String video_category = null;
    private String video_visible = null;
    private String video_user_id = null;
    private String video_desc = null;
    private String video_seen = null;
    private String video_like_count = null;
    private String video_is_liked = null;
    private String video_comment_count = null;

    private List<String> comment_title = new ArrayList<String>();
    private List<String> comment_post_by = new ArrayList<String>();
    private List<String> comment_post_on = new ArrayList<String>();

    public String getVideo_like_count() {
        return video_like_count;
    }

    public void setVideo_like_count(String video_like_count) {
        this.video_like_count = video_like_count;
    }

    public String getVideo_is_liked() {
        return video_is_liked;
    }

    public void setVideo_is_liked(String video_is_liked) {
        this.video_is_liked = video_is_liked;
    }

    public String getVideo_comment_count() {
        return video_comment_count;
    }

    public void setVideo_comment_count(String video_comment_count) {
        this.video_comment_count = video_comment_count;
    }

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

    public String getVideo_seen() {
        return video_seen;
    }

    public void setVideo_seen(String video_seen) {
        this.video_seen = video_seen;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getVideo_user_id() {
        return video_user_id;
    }

    public void setVideo_user_id(String video_user_id) {
        this.video_user_id = video_user_id;
    }

    public String getVideo_category() {
        return video_category;
    }

    public void setVideo_category(String video_category) {
        this.video_category = video_category;
    }

    public String getVideo_visible() {
        return video_visible;
    }

    public void setVideo_visible(String video_visible) {
        this.video_visible = video_visible;
    }

    public String getVideo_post_by() {
        return video_post_by;
    }

    public void setVideo_post_by(String video_post_by) {
        this.video_post_by = video_post_by;
    }

    public String getVideo_post_on() {
        return video_post_on;
    }

    public void setVideo_post_on(String video_post_on) {
        this.video_post_on = video_post_on;
    }

    public Boolean getVideolistserviceSuccess() {
        return videolistserviceSuccess;
    }

    public void setVideolistserviceSuccess(Boolean videolistserviceSuccess) {
        this.videolistserviceSuccess = videolistserviceSuccess;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getVideo_image() {
        return video_image;
    }

    public void setVideo_image(String video_image) {
        this.video_image = video_image;
    }

}
