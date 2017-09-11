package com.certified.verityscanningOne.Model;

/**
 * Created by harmis on 19/5/17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("RssFeeds")
    @Expose
    private List<RssFeed> rssFeeds = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<RssFeed> getRssFeeds() {
        return rssFeeds;
    }

    public void setRssFeeds(List<RssFeed> rssFeeds) {
        this.rssFeeds = rssFeeds;
    }

}

