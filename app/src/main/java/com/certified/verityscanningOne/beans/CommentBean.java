package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class CommentBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String commentID = null;
    String commentContent = null;
    String commentPostedDate = null;
    String commentPostedBy= null;



    public String getCommentPostedDate() {
        return commentPostedDate;
    }

    public void setCommentPostedDate(String commentPostedDate) {
        this.commentPostedDate = commentPostedDate;
    }

    public String getCommentPostedBy() {
        return commentPostedBy;
    }

    public void setCommentPostedBy(String commentPostedBy) {
        this.commentPostedBy = commentPostedBy;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
