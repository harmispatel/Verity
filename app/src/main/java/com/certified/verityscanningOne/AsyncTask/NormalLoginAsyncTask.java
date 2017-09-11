//package com.certified.verityscanning.AsyncTask;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.os.AsyncTask;
//
//import com.certified.verityscanning.Utils.CommonKeyword;
//import com.certified.verityscanning.Utils.CommonMethods;
//import com.certified.verityscanning.Utils.CommonSession;
//import com.certified.verityscanning.Utils.JSONParser;
//import com.certified.verityscanning.Utils.LoadingDialog;
//
//
//public class NormalLoginAsyncTask extends AsyncTask<String, String, String> {
//
//    LoadingDialog myLoadingDialog;
//    Dialog dialog;
//    CommonMethods myCommonMethods;
//    CommonSession myCommonSession = null;
//    JSONParser myJsonParser = null;
//    Dialog mDialog_custome = null;
//    Boolean isInternetPresent = false;
//    String user_name = null, password = null;
//    String call_From_where = null;
//    String isMenuOrHome;
//    private Activity myActivity = null;
//
//    public NormalLoginAsyncTask(String uname, String mpassword,
//                                Activity mActivity, String call_From, String misMenuOrHome) {
//        try {
//            myActivity = mActivity;
//            myCommonSession = new CommonSession(mActivity);
//            myCommonMethods = new CommonMethods(mActivity);
//            myJsonParser = new JSONParser();
//
//            // TODO Auto-generated constructor stub
//            myLoadingDialog = new LoadingDialog(mActivity,
//                    myCommonSession.getDeviceWidth(),
//                    myCommonSession.getDeviceHeight());
//            dialog = myLoadingDialog.getDialog();
//
//            user_name = uname;
//            password = mpassword;
//            call_From_where = call_From;
//            isMenuOrHome = misMenuOrHome;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPreExecute() {
//        // TODO Auto-generated method stub
//        super.onPreExecute();
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        String responce = null;
//        // TODO Auto-generated method stub
//        try {
//            responce = myCommonMethods.login(user_name, password);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//        return responce;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // TODO Auto-generated method stub
//        super.onPostExecute(result);
//        try {
//            super.onPostExecute(result);
//
//            if (dialog != null && dialog.isShowing()) {
//                dialog.dismiss();
//            }
//
//            if (result.equalsIgnoreCase(CommonKeyword.TRUE)) {
//
//
//            }
//
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//    }
//
//}
