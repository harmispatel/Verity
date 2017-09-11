package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class CountryBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    String country_id = null;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    String country_name = null;

}
