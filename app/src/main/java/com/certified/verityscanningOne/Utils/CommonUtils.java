package com.certified.verityscanningOne.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {


    public static void forceHide(FragmentActivity activity, EditText editText) {
        try {
            if (activity.getCurrentFocus() == null
                    || !(activity.getCurrentFocus() instanceof EditText)) {
                editText.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void OpenHide(AppCompatActivity activity, EditText editText) {
        try {
            if (activity.getCurrentFocus() == null
                    || !(activity.getCurrentFocus() instanceof EditText)) {
                editText.requestFocus();
            }

            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isEmailValid(String email) {
        Matcher matcher = null;
        try {
            Pattern pattern;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matcher.matches();
    }


    //=======================


//    public static boolean isSuccessFromServer(JSONObject jsonObject) {
//        if (jsonObject.has(JSONCommonKeywords.success)) {
//            try {
//                int success = jsonObject.getInt(JSONCommonKeywords.success);
//                return success == 1;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return false;
//
//            }
//
//        }
//        return false;
//    }
//
//    public static boolean isMessageAvailableInResponse(JSONObject jsonObject) {
//        if (jsonObject.has(JSONCommonKeywords.Message)) {
//            try {
//                String message = jsonObject.getString(JSONCommonKeywords.Message);
//
//                return isTextAvailable(message);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false;
//    }


    public static boolean isTextAvailable(String text) {
        return !(text == null || text.equals("") || text.equals("null"));


    }

    public static void commonToast(Activity activity, String text) {
        if (CommonUtils.isTextAvailable(text)) {
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
        }


    }

//    public static void commonToast(Context context, String text) {
//        if (CommonUtils.isTextAvailable(text)) {
//            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
//        }
//
//
//    }

//    public static void displayMessageInToLog(String prefix, String text) {
//        Log.d("Lex_" + prefix, text);
//
//
//    }
//
//    public static void displayMessageInToLog(String text) {
//        Log.d("Lex", text);
//
//
//    }


}
