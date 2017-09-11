package com.certified.verityscanningOne.video.gcm;//package lexlink.app.com.lexlink.video.gcm;
//
//import android.os.Bundle;
// import android.util.Log;
//
//import com.google.android.gms.gcm.GcmListenerService;
//import com.quickblox.sample.core.utils.SharedPrefsHelper;
//import com.quickblox.sample.core.utils.constant.GcmConsts;
//import com.quickblox.users.model.QBUser;
//
//import lexlink.app.com.lexlink.video.services.CallService;
//
///**
// * Created by tereha on 13.05.16.
// */
//public class GcmPushListenerServiceVideo extends GcmListenerService {
//    private static final String TAG = GcmPushListenerServiceVideo.class.getSimpleName();
//
//    @Override
//    public void onMessageReceived(String from, Bundle data) {
//        String message = data.getString(GcmConsts.EXTRA_GCM_MESSAGE);
//        Log.e(TAG, "From: " + from);
//        Log.e(TAG, "Message: " + message);
//
//        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
//        if (sharedPrefsHelper.hasQbUser()) {
//            Log.e(TAG, "App have logined user");
//            QBUser qbUser = sharedPrefsHelper.getQbUser();
//            startLoginService(qbUser);
//        }
//    }
//
//    private void startLoginService(QBUser qbUser){
//        CallService.start(this, qbUser);
//    }
//}