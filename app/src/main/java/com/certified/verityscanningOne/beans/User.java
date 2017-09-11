package com.certified.verityscanningOne.beans;

/**
 * Created by harmis on 28/9/16.
 */
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}