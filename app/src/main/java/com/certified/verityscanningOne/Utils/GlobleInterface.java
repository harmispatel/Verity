package com.certified.verityscanningOne.Utils;

import android.app.Activity;

import com.certified.verityscanningOne.beans.HomeBean;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public interface GlobleInterface<T> {
    void loadNewIntent(Activity activity, Class<T> to_screen);

    void customTostDialog(Activity mActivity, String message);


    boolean emailValidator(String email);

    JSONObject getJSONFromUrl(String url, List<NameValuePair> params);

    JSONObject getJSONFromUrlWithoutParams(String url);


    ArrayList<HomeBean> getDynamicalHomeItems();


}
