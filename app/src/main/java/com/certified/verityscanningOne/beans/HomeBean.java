package com.certified.verityscanningOne.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class HomeBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    String homeParentItemID;
    String homeParentTitle;
    String homeParentLink;
    String homeParentParams;
    String homeParentServiceName;
    String homeParentArticleID;
    String homeParentIconLink;

    public String getHomeParentIconLink() {
        return homeParentIconLink;
    }

    public void setHomeParentIconLink(String homeParentIconLink) {
        this.homeParentIconLink = homeParentIconLink;
    }

    String homeChildItemID;
    String homeChildTitle;
    String homeChildLink;

    String homeChildParams;
    String homeChildServiceName;
    String homeChildArticleID;

    boolean isHomeServiceSuccess;

    public boolean isHomeServiceSuccess() {
        return isHomeServiceSuccess;
    }

    public void setHomeServiceSuccess(boolean isHomeServiceSuccess) {
        this.isHomeServiceSuccess = isHomeServiceSuccess;
    }

    ArrayList<HomeBean> arrayListHomeChildItems;

    public String getHomeParentItemID() {
        return homeParentItemID;
    }

    public void setHomeParentItemID(String homeParentItemID) {
        this.homeParentItemID = homeParentItemID;
    }

    public String getHomeParentTitle() {
        return homeParentTitle;
    }

    public void setHomeParentTitle(String homeParentTitle) {
        this.homeParentTitle = homeParentTitle;
    }

    public String getHomeParentLink() {
        return homeParentLink;
    }

    public void setHomeParentLink(String homeParentLink) {
        this.homeParentLink = homeParentLink;
    }

    public String getHomeParentParams() {
        return homeParentParams;
    }

    public void setHomeParentParams(String homeParentParams) {
        this.homeParentParams = homeParentParams;
    }

    public String getHomeParentServiceName() {
        return homeParentServiceName;
    }

    public void setHomeParentServiceName(String homeParentServiceName) {
        this.homeParentServiceName = homeParentServiceName;
    }

    public String getHomeParentArticleID() {
        return homeParentArticleID;
    }

    public void setHomeParentArticleID(String homeParentArticleID) {
        this.homeParentArticleID = homeParentArticleID;
    }

    public String getHomeChildItemID() {
        return homeChildItemID;
    }

    public void setHomeChildItemID(String homeChildItemID) {
        this.homeChildItemID = homeChildItemID;
    }

    public String getHomeChildTitle() {
        return homeChildTitle;
    }

    public void setHomeChildTitle(String homeChildTitle) {
        this.homeChildTitle = homeChildTitle;
    }

    public String getHomeChildLink() {
        return homeChildLink;
    }

    public void setHomeChildLink(String homeChildLink) {
        this.homeChildLink = homeChildLink;
    }

    public String getHomeChildParams() {
        return homeChildParams;
    }

    public void setHomeChildParams(String homeChildParams) {
        this.homeChildParams = homeChildParams;
    }

    public String getHomeChildServiceName() {
        return homeChildServiceName;
    }

    public void setHomeChildServiceName(String homeChildServiceName) {
        this.homeChildServiceName = homeChildServiceName;
    }

    public String getHomeChildArticleID() {
        return homeChildArticleID;
    }

    public void setHomeChildArticleID(String homeChildArticleID) {
        this.homeChildArticleID = homeChildArticleID;
    }

    public ArrayList<HomeBean> getArrayListHomeChildItems() {
        return arrayListHomeChildItems;
    }

    public void setArrayListHomeChildItems(
            ArrayList<HomeBean> arrayListHomeChildItems) {
        this.arrayListHomeChildItems = arrayListHomeChildItems;
    }

}
