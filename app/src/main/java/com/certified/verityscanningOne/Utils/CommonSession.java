package com.certified.verityscanningOne.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CommonSession {

    public Activity mActivity = null;


    private static final String DEVICE_WIDTH = "device_width";
    private static final String DEVICE_HIGHT = "device_hieght";
    private static final String DEVICE_INCH = "device_inch";

    private static final String LOGIN_USER_ID = "mynewloginuser_id";
    private static final String LOGIN_USER_FNAME = "user_FirstName";

    private static final String SCAN_POSITION = "scan_pos";
    private static final String SCAN_COUNT = "scan_count";
    private static final String INVITE_COUNT = "invite_count";

    private static final String LANGUAGE = "language";
    private static final String COUNTRY = "country";
    private static final String SOCIAL_ADDED = "social";

    private static final String LOCAION_DISPLAY = "location_display";

    private static final String CATEGORY_ID_POPUP = "category_id_popup";
    private static final String CATEGORY_NAME_POPUP = "category_name_popup";

    private static final String FACEBOOK_APP_INSATLLED = "facebook_app_installed";
    private static final String INSTALLED_FACEBOOK_LOGIN_BUTTON_CLIKED = "installed_facebook_clicked";
    private static final String FACEBOOK_SELECTED = "facebook_selected";

    private static final String RATE_DIALOG_DISPLAY = "rate_dialog";
    private static final String SET_RATE_DIALOG_TRUE = "set_rate_dialog";
    private static final String MENU_CONTENT = "mmenu_content";

    private static final String COMPARESESSION_PREFENCE_NAME = "futbolmx_parent";

    private static final String SELECTED_CATEGORY_ID = "category_id";
    private static final String LOADER_CALLED_FLAG = "loader_called_flag";
    private static final String ACTIVITY_LOADER_CALL_FLAG = "activity_call_loader_flag";
    private static final String PROFILE_ACTIVITY_LOADER_CALL_FLAG = "profile_activity_call_loader_flag";
    private static final String GROUP_LOADER_CALL_FLAG = "group_call_loader_flag";
    private static final String CURRENT_CITY = "current_city";

    private static final String GROUP_LOADER_CALL_FLAG_ALL = "group_call_loader_flag_all";
    private static final String GROUP_LOADER_CALL_FLAG_MY = "group_call_loader_flag_my";
    private static final String GROUP_LOADER_CALL_FLAG_PENDING = "group_call_loader_flag_pending";
    // constructor
    private static final String LOGIN_CONTENT = "login_content";
    private static final String CURRENT_LAT = "current_lat";
    private static final String CURRENT_LONG = "current_long";

    private static final String INVITER_REFERER_CODE = "inviter_referer_code";
    private static final String IS_LOGGED_GMAIL = "is_logged_from_gmail";
    private static final String LOGGED_USER_REFERRER_CODE = "logged_user_referral_code";

    private static final String DEVICE_TOCKEN = "devide_tocken";
    private static final String LOGGED_USER_EMAIL = "email";


    private SharedPreferences sharedPref;
    private Editor editor;

    // constructor

    @SuppressLint("CommitPrefEdits")
    public CommonSession(Context activity) {
        sharedPref = activity.getSharedPreferences(
                COMPARESESSION_PREFENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }

    @SuppressLint("CommitPrefEdits")
    public CommonSession(Activity mfragmentactivity) {
        sharedPref = mfragmentactivity.getSharedPreferences(
                COMPARESESSION_PREFENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }


    // device width or height

    public int getDeviceWidth() {
        return sharedPref.getInt(DEVICE_WIDTH, 0);
    }

    public void storeDeviceWidth(int device_width) {
        try {
            editor.putInt(DEVICE_WIDTH, device_width);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDeviceWidth() {
        try {
            editor.putInt(DEVICE_WIDTH, 0);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Device tocken Id
    public String getDeviceTocken() {
        return sharedPref.getString(DEVICE_TOCKEN, null);
    }

    public void storeDeviceTocken(String device_tocken) {
        try {
            editor.putString(DEVICE_TOCKEN, device_tocken);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDeviceTocken() {
        try {
            editor.putString(DEVICE_TOCKEN, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getDeviceHeight() {
        return sharedPref.getInt(DEVICE_HIGHT, 0);
    }

    public void storeDeviceHeight(int device_height) {
        try {
            editor.putInt(DEVICE_HIGHT, device_height);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDeviceHeight() {
        try {
            editor.putInt(DEVICE_HIGHT, 0);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // login user email
    public String getLoggedEmail() {
        return sharedPref.getString(LOGGED_USER_EMAIL, null);
    }

    public void storeLoggedEmail(String user_email) {
        try {
            editor.putString(LOGGED_USER_EMAIL, user_email);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoggedEmail() {
        try {
            editor.putString(LOGGED_USER_EMAIL, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceInch() {
        return sharedPref.getString(DEVICE_INCH, null);
    }

    public void storeDeviceInch(String device_inch) {
        try {
            editor.putString(DEVICE_INCH, device_inch);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetDeviceInch() {
        try {
            editor.putString(DEVICE_INCH, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getMenuContent() {
        return sharedPref.getString(MENU_CONTENT, null);
    }

    public void storeMenuContent(String menu_content) {
        try {
            editor.putString(MENU_CONTENT, menu_content);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetMenuContent() {
        try {
            editor.putString(MENU_CONTENT, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String getLoginContent() {
        return sharedPref.getString(LOGIN_CONTENT, null);
    }

    public void storeLoginContent(String menu_content) {
        try {
            editor.putString(LOGIN_CONTENT, menu_content);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoginCOntent() {
        try {
            editor.putString(LOGIN_CONTENT, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // login ID
    public  String getLoggedUserID() {
        return sharedPref.getString(LOGIN_USER_ID, null);
    }

    public void storeLoggedUserID(String login_id) {
        try {
            editor.putString(LOGIN_USER_ID, login_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoggedUserID() {
        try {
            editor.putString(LOGIN_USER_ID, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // login user First name
    public  String getLoggedUserFName() {
        return sharedPref.getString(LOGIN_USER_FNAME, null);
    }

    public void storeLoggedUserFName(String user_fname) {
        try {
            editor.putString(LOGIN_USER_FNAME, user_fname);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoggedUserFName() {
        try {
            editor.putString(LOGIN_USER_FNAME, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String getScanPosition() {
        return sharedPref.getString(SCAN_POSITION, null);
    }

    public void storeScanPosition(String pos) {
        try {
            editor.putString(SCAN_POSITION, pos);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetScanPosition() {
        try {
            editor.putString(SCAN_POSITION, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // invite count
    public  String getInviteCount() {
        return sharedPref.getString(INVITE_COUNT, null);
    }

    public void storeInviteCount(String pos) {
        try {
            editor.putString(INVITE_COUNT, pos);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetInviteCount() {
        try {
            editor.putString(INVITE_COUNT, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // scan count
    public  String getScanCount() {
        return sharedPref.getString(SCAN_COUNT, null);
    }

    public void storeScanCount(String pos) {
        try {
            editor.putString(SCAN_COUNT, pos);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetScanCount() {
        try {
            editor.putString(SCAN_COUNT, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // categoryId Popup
    public String getCategoryIDPopUp() {
        return sharedPref.getString(CATEGORY_ID_POPUP, null);
    }

    public void storeCategoryIDPopUp(String mcat_id) {
        try {
            editor.putString(CATEGORY_ID_POPUP, mcat_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCategoryIDPopUp() {
        try {
            editor.putString(CATEGORY_ID_POPUP, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // categoryName Popup
    public String getCategoryNamePopUp() {
        return sharedPref.getString(CATEGORY_NAME_POPUP, null);
    }

    public void storeCategoryNamePopUp(String mcat_name) {
        try {
            editor.putString(CATEGORY_NAME_POPUP, mcat_name);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCategoryNamePopUp() {
        try {
            editor.putString(CATEGORY_NAME_POPUP, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // check fcebook installed

    public boolean isFacebookAppInstalled() {
        return sharedPref.getBoolean(FACEBOOK_APP_INSATLLED, false);
    }

    public void storeFacebookAppInstalled(boolean check_facebook_app_installed) {

        try {
            editor.putBoolean(FACEBOOK_APP_INSATLLED,
                    check_facebook_app_installed);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reseFacebookAppInstalled() {
        try {
            editor.putBoolean(FACEBOOK_APP_INSATLLED, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // facebook clicked once

    public void storeIntalledFacebookButtonClicked(
            boolean check_twitter_activituy) {

        try {
            editor.putBoolean(INSTALLED_FACEBOOK_LOGIN_BUTTON_CLIKED,
                    check_twitter_activituy);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetIntalledFacebookButtonClicked() {
        try {
            editor.putBoolean(INSTALLED_FACEBOOK_LOGIN_BUTTON_CLIKED, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getIntalledFacebookButtonClicked() {
        return sharedPref.getBoolean(INSTALLED_FACEBOOK_LOGIN_BUTTON_CLIKED,
                false);
    }

    public boolean isFacebookSelected() {
        return sharedPref.getBoolean(FACEBOOK_SELECTED, false);
    }

    public void storeFacebookSelected(boolean facebook_selected) {

        try {
            editor.putBoolean(FACEBOOK_SELECTED, facebook_selected);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reseFacebookSelected() {
        try {
            editor.putBoolean(FACEBOOK_SELECTED, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // rate dialog

    public boolean isRatedialogDisplay() {
        return sharedPref.getBoolean(RATE_DIALOG_DISPLAY, false);
    }

    public void storeRatedialogDisplay(boolean is_display) {

        try {
            editor.putBoolean(RATE_DIALOG_DISPLAY, is_display);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetRatedialogDisplay() {
        try {
            editor.putBoolean(RATE_DIALOG_DISPLAY, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //

    public boolean isSetRatedialogDisplay() {
        return sharedPref.getBoolean(SET_RATE_DIALOG_TRUE, false);
    }

    public void storeSetRatedialogDisplay(boolean is_display) {

        try {
            editor.putBoolean(SET_RATE_DIALOG_TRUE, is_display);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSetRatedialogDisplay() {
        try {
            editor.putBoolean(SET_RATE_DIALOG_TRUE, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // language
    public String getSelectedLanguage() {
        return sharedPref.getString(LANGUAGE, null);
    }

    public void storeSelectedLanguage(String value) {
        try {
            editor.putString(LANGUAGE, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSelectedLanguage() {
        try {
            editor.putString(LANGUAGE, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // country
    public String getSelectedCountry() {
        return sharedPref.getString(COUNTRY, null);
    }

    public void storeSelectedCountry(String value) {
        try {
            editor.putString(COUNTRY, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSelectedCountry() {
        try {
            editor.putString(COUNTRY, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // social
    public String getSocialValue() {
        return sharedPref.getString(SOCIAL_ADDED, null);
    }

    public void storeSocialValue(String value) {
        try {
            editor.putString(SOCIAL_ADDED, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSocialvalue() {
        try {
            editor.putString(SOCIAL_ADDED, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // social
    public String getLocationDisplay() {
        return sharedPref.getString(LOCAION_DISPLAY, null);
    }

    public void storeLocationDisplay(String value) {
        try {
            editor.putString(LOCAION_DISPLAY, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLocationDisplay() {
        try {
            editor.putString(LOCAION_DISPLAY, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // check logged id

    public String getSelectedcategory() {
        return sharedPref.getString(SELECTED_CATEGORY_ID, null);
    }

    public void setSelectedcategory(String selected_cate_id) {

        try {
            editor.putString(SELECTED_CATEGORY_ID, selected_cate_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSelectedcategory() {
        try {
            editor.putString(SELECTED_CATEGORY_ID, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getLoaderCalledFlag() {
        return sharedPref.getBoolean(LOADER_CALLED_FLAG, false);
    }

    public void storeLoaderCalledFlag(boolean loader_flag) {
        try {
            editor.putBoolean(LOADER_CALLED_FLAG, loader_flag);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetLoaderCalledFlag() {
        try {
            editor.putBoolean(LOADER_CALLED_FLAG, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActivityLoaderFlag() {
        return sharedPref.getBoolean(ACTIVITY_LOADER_CALL_FLAG, false);
    }

    public void storeActivityLoaderFlag(boolean activity_loader_flag) {

        try {
            editor.putBoolean(ACTIVITY_LOADER_CALL_FLAG, activity_loader_flag);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reseActivityLoaderFlag() {
        try {
            editor.putBoolean(ACTIVITY_LOADER_CALL_FLAG, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isProfileActivityLoaderFlag() {
        return sharedPref.getBoolean(PROFILE_ACTIVITY_LOADER_CALL_FLAG, false);
    }

    public void storeProfileActivityLoaderFlag(
            boolean profile_activity_loader_flag) {

        try {
            editor.putBoolean(PROFILE_ACTIVITY_LOADER_CALL_FLAG,
                    profile_activity_loader_flag);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetProfileActivityLoaderFlag() {
        try {
            editor.putBoolean(PROFILE_ACTIVITY_LOADER_CALL_FLAG, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGroupCallLoaderFlag() {
        return sharedPref.getBoolean(GROUP_LOADER_CALL_FLAG, false);
    }

    public void storeGroupCallLoaderFlag(boolean group_loader_flag) {

        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG, group_loader_flag);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetGroupCallLoaderFlag() {
        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentCity() {
        return sharedPref.getString(CURRENT_CITY, null);
    }

    public void storeCurrentCity(String current_city) {
        try {
            editor.putString(CURRENT_CITY, current_city);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCurrentCity() {
        try {
            editor.putString(CURRENT_CITY, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============

    public boolean isGroupCallLoaderFlagAll() {
        return sharedPref.getBoolean(GROUP_LOADER_CALL_FLAG_ALL, false);
    }

    public void storeGroupCallLoaderFlagAll(boolean group_loader_flag_all) {

        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_ALL, group_loader_flag_all);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetGroupCallLoaderFlagAll() {
        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_ALL, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGroupCallLoaderFlagMy() {
        return sharedPref.getBoolean(GROUP_LOADER_CALL_FLAG_MY, false);
    }

    public void storeGroupCallLoaderFlagMy(boolean group_loader_flag_my) {

        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_MY, group_loader_flag_my);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetGroupCallLoaderFlagMy() {
        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_MY, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGroupCallLoaderFlagPending() {
        return sharedPref.getBoolean(GROUP_LOADER_CALL_FLAG_PENDING, false);
    }

    public void storeGroupCallLoaderFlagPending(boolean group_loader_flag) {

        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_PENDING, group_loader_flag);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetGroupCallLoaderFlagPending() {
        try {
            editor.putBoolean(GROUP_LOADER_CALL_FLAG_PENDING, false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }













    public float getoldLat() {
        return sharedPref.getFloat(CURRENT_LAT, 0);
    }

    public void setoldLat(float currentlat) {

        try {
            editor.putFloat(CURRENT_LAT, currentlat);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetoldLat() {
        try {
            editor.putFloat(CURRENT_LAT, 0);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public float getoldlong() {
        return sharedPref.getFloat(CURRENT_LONG, 0);
    }

    public void setoldlong(float currentlongt) {

        try {
            editor.putFloat(CURRENT_LONG, currentlongt);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetoldlong() {
        try {
            editor.putFloat(CURRENT_LONG, 0);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getInviterReceviedRefereCode() {
        return sharedPref.getString(INVITER_REFERER_CODE, null);
    }

    public void setInviterReceviedRefereCode(String selected_cate_id) {

        try {
            editor.putString(INVITER_REFERER_CODE, selected_cate_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetInviterReceviedRefereCode() {
        try {
            editor.putString(INVITER_REFERER_CODE, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public String getLoggedUserReferrerCode() {
        return sharedPref.getString(LOGGED_USER_REFERRER_CODE, null);
    }

    public void setLoggedUserReferrerCode(String selected_cate_id) {

        try {
            editor.putString(LOGGED_USER_REFERRER_CODE, selected_cate_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetILoggedUserReferrerCode() {
        try {
            editor.putString(LOGGED_USER_REFERRER_CODE, null);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
