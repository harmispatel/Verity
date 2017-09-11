package com.certified.verityscanningOne.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Model.Example;
import com.certified.verityscanningOne.ProductDetailsActivity;
import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.beans.Parameterbean;
import com.certified.verityscanningOne.beans.Responcebean;
import com.certified.verityscanningOne.beans.ReviewBean;
import com.certified.verityscanningOne.beans.Searchbean;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class CommonMethods {
    static Activity mActivity = null;
    static JSONParser mJsonParser = null;
    LayoutInflater gLayoutInflater = null;
    CommonSession mCommonSession = null;
    String TAG = "CommnoMethods";

    public CommonMethods(Activity myActivity) {
        // TODO Auto-generated constructor stub
        try {
            mActivity = myActivity;
            gLayoutInflater = myActivity.getLayoutInflater();
            mJsonParser = new JSONParser();
            mCommonSession = new CommonSession(myActivity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean isImageUrlValid(String stringurl) {
        boolean isValidUrl = false;
        isValidUrl = !(stringurl == null || stringurl.equals("") || stringurl.isEmpty() || stringurl.equals("null"));
        return isValidUrl;
    }

    public static void printFirebaseLogcat(String tag, Exception e, String message) {
        if (message == null) {

        } else {
            message = "NPE caught";
        }
        FirebaseCrash.logcat(Log.ERROR, tag, message);
        FirebaseCrash.report(e);
    }

    public static boolean isNetworkConnected(Activity mActivity) {
        ConnectivityManager cm = (ConnectivityManager)  mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void mToast(String message) {

        try {
            View layout = gLayoutInflater.inflate(
                    R.layout.custome_toast_dialog_screen, null);

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    public String login(String str_userName, String str_password) {

        JSONObject jsonObject_parent = null;
        String success_value = null;

        try {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", str_userName));
            params.add(new BasicNameValuePair("password", str_password));

            System.out.println("login Params ======>>>>  " + params);
            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.LOGIN, params);

            // {"success":1,"username":"rbpatel","profile":{"userId":"352","name":"rahul","username":"rbpatel","email":"harmistest@gmail.com"}}

            if (jsonObject_parent == null) {

                success_value = CommonKeyword.FALSE;

            } else {
                success_value = jsonObject_parent
                        .getString(JsonKeyword.COMMON_JSON_SUCCESS_FIELD_KEYWORD);

                if (success_value == null || success_value.equals("")) {
                    success_value = CommonKeyword.FALSE;

                } else {

                    if (success_value.equals("1")) {

                        JSONObject mjsonObject = jsonObject_parent
                                .getJSONObject("profile");

                        success_value = CommonKeyword.TRUE;

                        mCommonSession.storeLoggedUserFName(mjsonObject
                                .getString("name"));

                        mCommonSession.storeLoggedUserID(mjsonObject
                                .getString("userId"));

                    } else {
                        success_value = CommonKeyword.FALSE;
                        success_value = jsonObject_parent.getString("message");
                    }
                }

            }

        } catch (Exception exception) {
            success_value = mActivity.getResources().getString(
                    R.string.no_internet_connection);
        }
        return success_value;

    }


    public ArrayList<Searchbean> getSeachListing(String searchKeyword, int startLimit) {

        JSONObject jsonObject_parent = null;
        String success_value = null;
        ArrayList<Searchbean> searchbeanArrayList = new ArrayList<>();
        Searchbean searchbean_globle = new Searchbean();
        int total = 0;

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("searchword", searchKeyword));
            params.add(new BasicNameValuePair("userId", mCommonSession.getLoggedUserID()));
            params.add(new BasicNameValuePair("offset", String.valueOf(startLimit)));
            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.search, params);

            if (jsonObject_parent == null) {

                searchbean_globle.setServiceSuccess(false);
                searchbeanArrayList.add(searchbean_globle);

            } else {
                success_value = jsonObject_parent
                        .getString(JSONConstants.COMMON_JSON_SUCCESS_FIELD_KEYWORD);

                if (success_value == null || success_value.equals("")) {
                    searchbean_globle.setServiceSuccess(false);
                    searchbeanArrayList.add(searchbean_globle);
                } else {
                    if (success_value.equals("1")) {

                        if (jsonObject_parent.has("result")) {
                            JSONArray jsonArray_results = jsonObject_parent.getJSONArray("result");
                            if (jsonArray_results == null) {
                                searchbean_globle.setServiceSuccess(false);
                                searchbeanArrayList.add(searchbean_globle);
                            } else {
                                if (jsonArray_results.length() == 0) {
                                    searchbean_globle.setServiceSuccess(false);
                                    searchbeanArrayList.add(searchbean_globle);
                                } else {
                                    for (int i = 0; i < jsonArray_results.length(); i++) {
                                        JSONObject jsonObject_single_search = jsonArray_results.getJSONObject(i);
                                        Searchbean searchbean_single = new Searchbean();
                                        searchbean_single.setServiceSuccess(true);


                                        if (jsonObject_single_search.has("productId")) {
                                            searchbean_single.setProductId(jsonObject_single_search.getString("productId"));

                                        }

                                        if (jsonObject_single_search.has("productName")) {
                                            searchbean_single.setProductName(jsonObject_single_search.getString("productName"));

                                        }
                                        if (jsonObject_single_search.has("productDesc")) {
                                            searchbean_single.setProductDesc(jsonObject_single_search.getString("productDesc"));

                                        }
                                        if (jsonObject_single_search.has("category")) {
                                            searchbean_single.setCategory(jsonObject_single_search.getString("category"));

                                        }
                                        if (jsonObject_single_search.has("lat")) {
                                            searchbean_single.setLat(jsonObject_single_search.getString("lat"));

                                        }
                                        if (jsonObject_single_search.has("lng")) {
                                            searchbean_single.setLng(jsonObject_single_search.getString("lng"));

                                        }
                                        if (jsonObject_single_search.has("companyUniqueId")) {
                                            searchbean_single.setCompanyUniqueId(jsonObject_single_search.getString("companyUniqueId"));

                                        }
                                        if (jsonObject_single_search.has("sealPercentage")) {
                                            searchbean_single.setSealPercentage(jsonObject_single_search.getString("sealPercentage"));

                                        }


                                        if (jsonObject_single_search.has("upcCode")) {
                                            searchbean_single.setUpcCode(jsonObject_single_search.getString("upcCode"));

                                        }
                                        if (jsonObject_single_search.has("website")) {
                                            searchbean_single.setWebsite(jsonObject_single_search.getString("website"));

                                        }
                                        if (jsonObject_single_search.has("thumbImage")) {
                                            searchbean_single.setThumbImage(jsonObject_single_search.getString("thumbImage"));

                                        }

                                        if (jsonObject_single_search.has("productImage")) {
                                            searchbean_single.setProductImage(jsonObject_single_search.getString("productImage"));

                                        }

                                        // code by Rahul
                                        if (jsonObject_single_search.has("isRate")) {
                                            searchbean_single.setIsRate(jsonObject_single_search.getString("isRate"));

                                        }

                                        if (jsonObject_single_search.has("isReview")) {
                                            searchbean_single.setIsReview(jsonObject_single_search.getString("isReview"));

                                        }

                                        if (jsonObject_single_search.has("avgRating")) {
                                            searchbean_single.setAvgRating(jsonObject_single_search.getString("avgRating"));

                                        }

                                        if (jsonObject_single_search.has("rateUserCount")) {
                                            searchbean_single.setRateuserCount(jsonObject_single_search.getString("rateUserCount"));

                                        }

                                        if (jsonObject_single_search.has("review")) {

                                            JSONObject jsonObjReview = jsonObject_single_search.getJSONObject("review");
                                            ReviewBean reviewbean = new ReviewBean();

                                            reviewbean.set_id(jsonObjReview.getString("id"));
                                            reviewbean.set_productId(jsonObjReview.getString("productId"));
                                            reviewbean.set_reviewDate(jsonObjReview.getString("reviewDate"));
                                            reviewbean.set_reviewText(jsonObjReview.getString("reviewText"));
                                            reviewbean.set_reviewTitle(jsonObjReview.getString("reviewTitle"));
                                            reviewbean.set_userName(jsonObjReview.getString("userName"));
                                            reviewbean.set_rating(jsonObjReview.getString("reviewValue"));
                                            reviewbean.setServiceSuccess(true);

                                            searchbean_single.setReviewbean(reviewbean);

                                        } else {
                                            ReviewBean reviewbean = new ReviewBean();
                                            reviewbean.setServiceSuccess(false);

                                            searchbean_single.setReviewbean(reviewbean);
                                        }

                                        // End Code by rahul


                                        if (jsonObject_single_search.has("productDetails")) {
                                            JSONArray jsonArray_product_details = jsonObject_single_search.getJSONArray("productDetails");
                                            if (jsonArray_product_details == null) {
                                                searchbean_single.setExistExtraFeildsA(false);
                                                searchbean_single.setExistExtraFeildsB(false);

                                            } else {
                                                if (jsonArray_product_details.length() == 0) {
                                                    searchbean_single.setExistExtraFeildsA(false);
                                                    searchbean_single.setExistExtraFeildsB(false);

                                                } else {
                                                    searchbean_single.setExistExtraFeildsA(false);
                                                    searchbean_single.setExistExtraFeildsB(false);
                                                    ArrayList<Searchbean> searchbeanArrayList_extra_fields_type_a = new ArrayList<>();
                                                    ArrayList<Searchbean> searchbeanArrayList_extra_fields_type_b = new ArrayList<>();

                                                    for (int j = 0; j < jsonArray_product_details.length(); j++) {
                                                        //type = 0 = text
                                                        //type = 1 = certification and on click image redirect to URL
                                                        JSONObject jsonObject_single_product_details = jsonArray_product_details.getJSONObject(j);
                                                        Searchbean searchbean_product_field = new Searchbean();


                                                        if (jsonObject_single_product_details.has("type")) {
                                                            searchbean_product_field.setType(jsonObject_single_product_details.getString("type"));

                                                        }
                                                        if (jsonObject_single_product_details.has("placeholder")) {
                                                            searchbean_product_field.setPlaceholder(jsonObject_single_product_details.getString("placeholder"));

                                                        }
                                                        if (jsonObject_single_product_details.has("value")) {
                                                            searchbean_product_field.setValue(jsonObject_single_product_details.getString("value"));

                                                        }
                                                        if (jsonObject_single_product_details.has("redirectUrl")) {
                                                            searchbean_product_field.setRedirectUrl(jsonObject_single_product_details.getString("redirectUrl"));

                                                        }


                                                        if (searchbean_product_field.getType() == null) {

                                                        } else {
                                                            if (searchbean_product_field.getType().equals("0")) {
                                                                searchbeanArrayList_extra_fields_type_a.add(searchbean_product_field);

                                                            } else if (searchbean_product_field.getType().equals("1")) {
                                                                searchbeanArrayList_extra_fields_type_b.add(searchbean_product_field);

                                                            }
                                                        }


                                                    }


                                                    if (searchbeanArrayList_extra_fields_type_a.size() != 0) {
                                                        searchbean_single.setExistExtraFeildsA(true);

                                                    } else {
                                                        searchbean_single.setExistExtraFeildsA(false);

                                                    }

                                                    if (searchbeanArrayList_extra_fields_type_b.size() != 0) {
                                                        searchbean_single.setExistExtraFeildsB(true);

                                                    } else {
                                                        searchbean_single.setExistExtraFeildsB(false);

                                                    }

                                                    searchbean_single.setSearchbeanArrayList_extra_feilds_typa_a(searchbeanArrayList_extra_fields_type_a);
                                                    searchbean_single.setSearchbeanArrayList_extra_feilds_typa_b(searchbeanArrayList_extra_fields_type_b);

                                                }
                                            }
                                        }


                                        searchbeanArrayList.add(searchbean_single);

                                    }
                                }
                            }

                        } else {
                            searchbean_globle.setServiceSuccess(false);
                            searchbeanArrayList.add(searchbean_globle);

                        }


                    } else {


                        searchbean_globle.setServiceSuccess(false);
                        if (jsonObject_parent.has("message")) {
                            searchbean_globle.setErrorMessage(jsonObject_parent.getString("message"));
                        }
                        searchbeanArrayList.add(searchbean_globle);


                    }
                }

            }

        } catch (Exception e) {
            searchbean_globle.setServiceSuccess(false);

            searchbean_globle.setErrorMessage(e.getMessage());
            CommonMethods.printFirebaseLogcat(TAG, e, null);

            searchbeanArrayList.add(searchbean_globle);
        }
        return searchbeanArrayList;

    }

    public Responcebean checkThisProductExistByThisKeyword(String searchKeyword) {

        JSONObject jsonObject_parent = null;
        String success_value = null;
        Responcebean responcebean = new Responcebean();

        try {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("searchword", searchKeyword));
            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    "https://t.certified.bz/search_product_status.php?searchword=" + searchKeyword, params);

            if (jsonObject_parent == null) {

                responcebean.setIsServiceSuccess(false);

            } else {

                if (jsonObject_parent.has("status")) {
                    responcebean.setIsServiceSuccess(true);

                    boolean isProductExist = jsonObject_parent.getBoolean("status");
                    if (isProductExist) {
                        responcebean.setIsProductExist(1);
                    } else {
                        responcebean.setIsProductExist(2);

                    }
                } else {
                    responcebean.setIsServiceSuccess(false);

                }


            }

        } catch (Exception e) {
            responcebean.setIsServiceSuccess(false);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return responcebean;

    }

    public Loginbean getLoginAndUpdateLogin(Parameterbean parameterbean) {

        JSONObject jsonObject_parent = null;
        String success_value = null;
        Loginbean loginbean_globle = new Loginbean();

        try {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            if (parameterbean.isGetloginDetails()) {
                //get login data
                params.add(new BasicNameValuePair("name", parameterbean.getName()));
                params.add(new BasicNameValuePair("password", parameterbean.getPassword()));
                params.add(new BasicNameValuePair("email", parameterbean.getEmail()));
                params.add(new BasicNameValuePair("birthDay", parameterbean.getBirthDay()));
                params.add(new BasicNameValuePair("mobileNumber", parameterbean.getMobileNumber()));
                params.add(new BasicNameValuePair("socialType", parameterbean.getSocialType()));
                params.add(new BasicNameValuePair("isSocial", parameterbean.getIsSocial()));
                params.add(new BasicNameValuePair("firebaseId", parameterbean.getFirebaseId()));
                params.add(new BasicNameValuePair("profileImage", parameterbean.getProfileImage()));
                params.add(new BasicNameValuePair("referalCode", parameterbean.getReceiverReferralCode()));

                jsonObject_parent = mJsonParser.getJSONFromUrl(
                        JsonURL.signin, params);

            } else {
                //update data
                //get login data
                params.add(new BasicNameValuePair("userId", parameterbean.getUserID()));
                params.add(new BasicNameValuePair("name", parameterbean.getName()));
                params.add(new BasicNameValuePair("birthDay", parameterbean.getBirthDay()));
                params.add(new BasicNameValuePair("mobileNumber", parameterbean.getMobileNumber()));

                jsonObject_parent = mJsonParser.getJSONFromUrl(
                        JsonURL.updateProfile, params);
            }


            if (jsonObject_parent == null) {


            } else {

                JSONObject jsonObject_profile = null;
                int successvalue = jsonObject_parent.getInt("success");
                if (successvalue == 1) {


                    if (parameterbean.isGetloginDetails()) {
                        jsonObject_profile = jsonObject_parent.getJSONObject("profile");


                    } else {
                        jsonObject_profile = jsonObject_parent.getJSONObject("profile");

                    }


                    loginbean_globle.setServiceSuccess(true);
                    if (jsonObject_profile.has("userId")) {
                        loginbean_globle.setUserID(jsonObject_profile.getString("userId"));

                    }
                    if (jsonObject_profile.has("name")) {
                        loginbean_globle.setName(jsonObject_profile.getString("name"));

                    }
                    if (jsonObject_profile.has("email")) {
                        loginbean_globle.setEmail(jsonObject_profile.getString("email"));

                    }
                    if (jsonObject_profile.has("birthDay")) {
                        loginbean_globle.setBirthday(jsonObject_profile.getString("birthDay"));

                    }
                    if (jsonObject_profile.has("mobileNumber")) {
                        loginbean_globle.setMobileNumber(jsonObject_profile.getString("mobileNumber"));

                    }
                    if (jsonObject_profile.has("firebaseId")) {
                        loginbean_globle.setFirebaseId(jsonObject_profile.getString("firebaseId"));

                    }
                    if (jsonObject_profile.has("socialType")) {
                        loginbean_globle.setSocialType(jsonObject_profile.getString("socialType"));

                    }
                    if (jsonObject_profile.has("profileImage")) {
                        loginbean_globle.setProfileImage(jsonObject_profile.getString("profileImage"));


                    }
                    if (jsonObject_profile.has("referalCode")) {
                        loginbean_globle.setReferalCode(jsonObject_profile.getString("referalCode"));


                    }

                } else if (successvalue == 2) {

                    if (jsonObject_parent.has("message")) {
                        loginbean_globle.setErrorMessage(jsonObject_parent.getString("message"));


                    }
                    loginbean_globle.setServiceSuccess(false);

                } else {


                    if (jsonObject_parent.has("message")) {
                        loginbean_globle.setErrorMessage(jsonObject_parent.getString("message"));
                    }
                    loginbean_globle.setServiceSuccess(false);

                }

            }

        } catch (Exception e) {
            loginbean_globle.setServiceSuccess(false);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return loginbean_globle;

    }

    public Loginbean normalLogin(Parameterbean parameterbean) {

        JSONObject jsonObject_parent = null;
        String success_value = null;
        Loginbean loginbean_globle = new Loginbean();

        try {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            //get login data
            params.add(new BasicNameValuePair("email", parameterbean.getEmail()));
            params.add(new BasicNameValuePair("password", parameterbean.getPassword()));


            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.normalLogin, params);


            if (jsonObject_parent == null) {


            } else {

                JSONObject jsonObject_profile = null;
                int successvalue = jsonObject_parent.getInt("success");
                if (successvalue == 1) {


                    jsonObject_profile = jsonObject_parent.getJSONObject("profile");


                    loginbean_globle.setServiceSuccess(true);
                    if (jsonObject_profile.has("userId")) {
                        loginbean_globle.setUserID(jsonObject_profile.getString("userId"));

                    }
                    if (jsonObject_profile.has("name")) {
                        loginbean_globle.setName(jsonObject_profile.getString("name"));

                    }
                    if (jsonObject_profile.has("email")) {
                        loginbean_globle.setEmail(jsonObject_profile.getString("email"));

                    }
                    if (jsonObject_profile.has("birthDay")) {
                        loginbean_globle.setBirthday(jsonObject_profile.getString("birthDay"));

                    }
                    if (jsonObject_profile.has("mobileNumber")) {
                        loginbean_globle.setMobileNumber(jsonObject_profile.getString("mobileNumber"));

                    }
                    if (jsonObject_profile.has("firebaseId")) {
                        loginbean_globle.setFirebaseId(jsonObject_profile.getString("firebaseId"));

                    }
                    if (jsonObject_profile.has("socialType")) {
                        loginbean_globle.setSocialType(jsonObject_profile.getString("socialType"));

                    }
                    if (jsonObject_profile.has("referalCode")) {
                        loginbean_globle.setReferalCode(jsonObject_profile.getString("referalCode"));


                    }
                    if (jsonObject_profile.has("profileImage")) {
                        loginbean_globle.setProfileImage(jsonObject_profile.getString("profileImage"));


                    } else {
                        loginbean_globle.setServiceSuccess(false);

                    }


                } else if (successvalue == 2) {

                    if (jsonObject_parent != null ? jsonObject_parent.has("message") : false) {
                        loginbean_globle.setErrorMessage(jsonObject_parent.getString("message"));
                    }
                    loginbean_globle.setServiceSuccess(false);

                } else if (successvalue == 0) {


                    if (jsonObject_parent.has("message")) {
                        loginbean_globle.setErrorMessage(jsonObject_parent.getString("message"));
                    }
                    loginbean_globle.setServiceSuccess(false);

                }
            }

        } catch (Exception e) {
            loginbean_globle.setServiceSuccess(false);
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return loginbean_globle;

    }

    public Responcebean forgetPassword(String email) throws Exception {
        ArrayList arraylist;

        JSONObject jsonObject;
        String success_value = null;
        Responcebean responcebean = null;
        JSONObject jsonObject_parent = null;

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("email", email));


            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.forgotPassword, params);
            if (jsonObject_parent == null) {
                responcebean.setIsServiceSuccess(false);
            } else {


                success_value = jsonObject_parent.getString("success");
                if (success_value.equals("1")) {


                    if (jsonObject_parent.has("message")) {
                        responcebean.setErrorMessage(jsonObject_parent.getString("message"));
                    }
                    ///add records here in array list

                    responcebean.setIsServiceSuccess(true);
                } else {
                    responcebean.setIsServiceSuccess(false);

                    if (jsonObject_parent.has("message")) {
                        responcebean.setErrorMessage(jsonObject_parent.getString("message"));
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
            responcebean.setErrorMessage(e.getMessage());
            responcebean.setIsServiceSuccess(false);

        }


        return responcebean;
    }


    public static HashMap<String, String> getQueryMap(String uri) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();

        try {
            if (uri == null || uri.equals("")) {

            } else {
                String queryParms[] = uri.split("\\?");


                if (queryParms == null || queryParms.length == 0) ;

                String[] params = queryParms[1].split("&");
                for (String param : params) {
                    String name = param.split("=")[0];
                    String value = param.split("=")[1];
                    stringStringHashMap.put(name, value);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringStringHashMap;
    }

    // Services By rahul

    public Searchbean postReview(String productId, String title, String desc, String ratingVale) {
        // TODO Auto-generated method stub
        String success_value = null;

        Searchbean searchbean_single = new Searchbean();
        try {

            JSONObject jsonObject_parent = null;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userId", mCommonSession.getLoggedUserID()));
                params.add(new BasicNameValuePair("productId", productId));
                params.add(new BasicNameValuePair("reviewTitle", title));
                params.add(new BasicNameValuePair("reviewText", desc));
                params.add(new BasicNameValuePair("reviewValue", ratingVale));
                jsonObject_parent = mJsonParser.getJSONFromUrl(
                        JsonURL.GIVEREVIEW, params);


                if (jsonObject_parent == null) {
                    searchbean_single.setServiceSuccess(false);
                } else {
                    success_value = jsonObject_parent
                            .getString("success");

                    if (success_value.equals("1")) {
                        searchbean_single.setServiceSuccess(true);

                        JSONObject jsonObject_single_search = jsonObject_parent
                                .getJSONObject("result");


                        searchbean_single.setServiceSuccess(true);


                        if (jsonObject_single_search.has("productId")) {
                            searchbean_single.setProductId(jsonObject_single_search.getString("productId"));

                        }

                        if (jsonObject_single_search.has("productName")) {
                            searchbean_single.setProductName(jsonObject_single_search.getString("productName"));

                        }
                        if (jsonObject_single_search.has("productDesc")) {
                            searchbean_single.setProductDesc(jsonObject_single_search.getString("productDesc"));

                        }
                        if (jsonObject_single_search.has("category")) {
                            searchbean_single.setCategory(jsonObject_single_search.getString("category"));

                        }
                        if (jsonObject_single_search.has("lat")) {
                            searchbean_single.setLat(jsonObject_single_search.getString("lat"));

                        }
                        if (jsonObject_single_search.has("lng")) {
                            searchbean_single.setLng(jsonObject_single_search.getString("lng"));

                        }
                        if (jsonObject_single_search.has("companyUniqueId")) {
                            searchbean_single.setCompanyUniqueId(jsonObject_single_search.getString("companyUniqueId"));

                        }
                        if (jsonObject_single_search.has("sealPercentage")) {
                            searchbean_single.setSealPercentage(jsonObject_single_search.getString("sealPercentage"));

                        }


                        if (jsonObject_single_search.has("upcCode")) {
                            searchbean_single.setUpcCode(jsonObject_single_search.getString("upcCode"));

                        }
                        if (jsonObject_single_search.has("website")) {
                            searchbean_single.setWebsite(jsonObject_single_search.getString("website"));

                        }
                        if (jsonObject_single_search.has("thumbImage")) {
                            searchbean_single.setThumbImage(jsonObject_single_search.getString("thumbImage"));

                        }

                        if (jsonObject_single_search.has("productImage")) {
                            searchbean_single.setProductImage(jsonObject_single_search.getString("productImage"));

                        }

                        // code by Rahul
                        if (jsonObject_single_search.has("isRate")) {
                            searchbean_single.setIsRate(jsonObject_single_search.getString("isRate"));

                        }

                        if (jsonObject_single_search.has("isReview")) {
                            searchbean_single.setIsReview(jsonObject_single_search.getString("isReview"));

                        }

                        if (jsonObject_single_search.has("avgRating")) {
                            searchbean_single.setAvgRating(jsonObject_single_search.getString("avgRating"));

                        }

                        if (jsonObject_single_search.has("rateUserCount")) {
                            searchbean_single.setRateuserCount(jsonObject_single_search.getString("rateUserCount"));

                        }

                        if (jsonObject_single_search.has("review")) {

                            JSONObject jsonObjReview = jsonObject_single_search.getJSONObject("review");
                            ReviewBean reviewbean = new ReviewBean();

                            reviewbean.set_id(jsonObjReview.getString("id"));
                            reviewbean.set_productId(jsonObjReview.getString("productId"));
                            reviewbean.set_reviewDate(jsonObjReview.getString("reviewDate"));
                            reviewbean.set_reviewText(jsonObjReview.getString("reviewText"));
                            reviewbean.set_reviewTitle(jsonObjReview.getString("reviewTitle"));
                            reviewbean.set_userName(jsonObjReview.getString("userName"));
                            reviewbean.set_rating(jsonObjReview.getString("reviewValue"));
                            reviewbean.setServiceSuccess(true);

                            searchbean_single.setReviewbean(reviewbean);

                        } else {
                            ReviewBean reviewbean = new ReviewBean();
                            reviewbean.setServiceSuccess(false);

                            searchbean_single.setReviewbean(reviewbean);
                        }

                        // End Code by rahul


                        if (jsonObject_single_search.has("productDetails")) {
                            JSONArray jsonArray_product_details = jsonObject_single_search.getJSONArray("productDetails");
                            if (jsonArray_product_details == null) {
                                searchbean_single.setExistExtraFeildsA(false);
                                searchbean_single.setExistExtraFeildsB(false);

                            } else {
                                if (jsonArray_product_details.length() == 0) {
                                    searchbean_single.setExistExtraFeildsA(false);
                                    searchbean_single.setExistExtraFeildsB(false);

                                } else {
                                    searchbean_single.setExistExtraFeildsA(false);
                                    searchbean_single.setExistExtraFeildsB(false);
                                    ArrayList<Searchbean> searchbeanArrayList_extra_fields_type_a = new ArrayList<>();
                                    ArrayList<Searchbean> searchbeanArrayList_extra_fields_type_b = new ArrayList<>();

                                    for (int j = 0; j < jsonArray_product_details.length(); j++) {
                                        //type = 0 = text
                                        //type = 1 = certification and on click image redirect to URL
                                        JSONObject jsonObject_single_product_details = jsonArray_product_details.getJSONObject(j);
                                        Searchbean searchbean_product_field = new Searchbean();


                                        if (jsonObject_single_product_details.has("type")) {
                                            searchbean_product_field.setType(jsonObject_single_product_details.getString("type"));

                                        }
                                        if (jsonObject_single_product_details.has("placeholder")) {
                                            searchbean_product_field.setPlaceholder(jsonObject_single_product_details.getString("placeholder"));

                                        }
                                        if (jsonObject_single_product_details.has("value")) {
                                            searchbean_product_field.setValue(jsonObject_single_product_details.getString("value"));

                                        }
                                        if (jsonObject_single_product_details.has("redirectUrl")) {
                                            searchbean_product_field.setRedirectUrl(jsonObject_single_product_details.getString("redirectUrl"));

                                        }


                                        if (searchbean_product_field.getType() == null) {

                                        } else {
                                            if (searchbean_product_field.getType().equals("0")) {
                                                searchbeanArrayList_extra_fields_type_a.add(searchbean_product_field);

                                            } else if (searchbean_product_field.getType().equals("1")) {
                                                searchbeanArrayList_extra_fields_type_b.add(searchbean_product_field);

                                            }
                                        }


                                    }


                                    if (searchbeanArrayList_extra_fields_type_a.size() != 0) {
                                        searchbean_single.setExistExtraFeildsA(true);

                                    } else {
                                        searchbean_single.setExistExtraFeildsA(false);

                                    }

                                    if (searchbeanArrayList_extra_fields_type_b.size() != 0) {
                                        searchbean_single.setExistExtraFeildsB(true);

                                    } else {
                                        searchbean_single.setExistExtraFeildsB(false);

                                    }

                                    searchbean_single.setSearchbeanArrayList_extra_feilds_typa_a(searchbeanArrayList_extra_fields_type_a);
                                    searchbean_single.setSearchbeanArrayList_extra_feilds_typa_b(searchbeanArrayList_extra_fields_type_b);

                                }
                            }
                        }


                    } else {
                        searchbean_single.setServiceSuccess(false);
                    }
                }

            } catch (Exception exception) {
                searchbean_single.setServiceSuccess(false);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            searchbean_single.setServiceSuccess(false);
            e.printStackTrace();
        }
        return searchbean_single;
    }


    public ArrayList<ReviewBean> getReviewList(String postId) {
        // TODO Auto-generated method stub
        String success_value = null;

        ArrayList<ReviewBean> arrayListService = new ArrayList<ReviewBean>();
        JSONArray jsonArray = null;
        JSONObject jsonObject_parent = null;
        ReviewBean my_Bean_global = new ReviewBean();

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("productId", postId));
            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.REVIEW_LISTING, params);

            if (jsonObject_parent == null) {

                my_Bean_global.setServiceSuccess(false);
                arrayListService.add(my_Bean_global);

            } else {
                success_value = jsonObject_parent
                        .getString("success");

                if (success_value.equals("1")) {
                    jsonArray = jsonObject_parent.getJSONArray("result");

                    if (jsonArray == null) {
                        my_Bean_global.setServiceSuccess(false);
                        arrayListService.add(my_Bean_global);

                    } else {

                        my_Bean_global.setServiceSuccess(true);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject mobj = jsonArray
                                    .getJSONObject(i);

                            ReviewBean mBean = new ReviewBean();

                            mBean.setServiceSuccess(true);
                            mBean.set_id(mobj.getString("id"));
                            mBean.set_productId(mobj.getString("productId"));
                            mBean.set_reviewDate(mobj.getString("reviewDate"));
                            mBean.set_reviewText(mobj.getString("reviewText"));
                            mBean.set_reviewTitle(mobj.getString("reviewTitle"));
                            mBean.set_userName(mobj.getString("userName"));
                            mBean.set_rating(mobj.getString("reviewValue"));

                            arrayListService.add(mBean);
                        }
                    }

                } else {
                    my_Bean_global.setServiceSuccess(false);
                    arrayListService.add(my_Bean_global);
                }
            }
        } catch (Exception exception) {
            my_Bean_global.setServiceSuccess(false);
            arrayListService.add(my_Bean_global);
        }
        return arrayListService;
    }


    public String postRate(String productId, String ratingvale) {
        // TODO Auto-generated method stub
        String success_value = null;


        try {

            JSONObject jsonObject_parent = null;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userId", mCommonSession.getLoggedUserID()));
                params.add(new BasicNameValuePair("productId", productId));
                params.add(new BasicNameValuePair("reviewValue", ratingvale));
                jsonObject_parent = mJsonParser.getJSONFromUrl(
                        JsonURL.GIVE_RATING, params);

                ProductDetailsActivity.avgRating = null;
                ProductDetailsActivity.rateuserCount = null;


                if (jsonObject_parent == null) {
                    success_value = CommonKeyword.FALSE;
                } else {
                    success_value = jsonObject_parent
                            .getString("success");

                    if (success_value.equals("1")) {
                        success_value = CommonKeyword.TRUE;

                        JSONObject mobj = jsonObject_parent
                                .getJSONObject("result");

//                        {"success":"1","result":{"id":"373","rateUserCount":"5","ratingValue":"0.250000"}}

                        ProductDetailsActivity.avgRating = mobj.getString("ratingValue");
                        ProductDetailsActivity.rateuserCount = mobj.getString("rateUserCount");

                    } else {
                        success_value = CommonKeyword.FALSE;
                    }
                }

            } catch (Exception exception) {
                success_value = CommonKeyword.FALSE;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            success_value = CommonKeyword.FALSE;
            e.printStackTrace();
        }
        return success_value;
    }


    public String addProduct(String UpcCode, String title, Bitmap bitmap, String selectedImagePath) {
        // TODO Auto-generated method stub
        String success_value = null;

        try {
            JSONObject jsonObject_parent = null;
            try {
                JSONObject json = new JSONObject();
                json.put("UPCcode", UpcCode);
                json.put("userId", mCommonSession.getLoggedUserID());
                json.put("productName", title);

                jsonObject_parent = mJsonParser.sentMIMEContent(JsonURL.ADD_PRODUCT,
                        json, bitmap, selectedImagePath);

                if (jsonObject_parent == null) {
                    success_value = CommonKeyword.FALSE;
                } else {
                    success_value = jsonObject_parent
                            .getString("success");

                    if (success_value.equals("1")) {
                        success_value = CommonKeyword.TRUE;

                    } else {
                        success_value = jsonObject_parent
                                .getString("message");
                    }
                }

            } catch (Exception exception) {
                success_value = CommonKeyword.FALSE;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            success_value = CommonKeyword.FALSE;
            e.printStackTrace();
        }
        return success_value;
    }



    /*
    Created To Get List Of Rssfeed In Recycler View
    */

    public Example getRssFeedsList(int Type_RecallRss) {
        // TODO Auto-generated method stub
              JSONObject jsonObject_parent = null;


            List<NameValuePair> rssFeedArrayList = new ArrayList<NameValuePair>();

        if (Type_RecallRss==0)
        {

            rssFeedArrayList.add(new BasicNameValuePair("feedtype", "usda"));
        }
        else if (Type_RecallRss==1)
        {
            rssFeedArrayList.add(new BasicNameValuePair("feedtype", "allrecall"));

        }
        else if (Type_RecallRss==2)
        {
            rssFeedArrayList.add(new BasicNameValuePair("feedtype", "children"));

        }
            jsonObject_parent = mJsonParser.getJSONFromUrl(
                    JsonURL.GET_RSS_FEEDS, rssFeedArrayList);


            Gson gson = new GsonBuilder().create();
            Example example = gson.fromJson(jsonObject_parent.toString(), Example.class);


            return example;



        }


}
