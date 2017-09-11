package com.certified.verityscanningOne;

import android.util.Log;

import com.certified.verityscanningOne.Utils.CommonSession;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by harmis on 27/12/16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    CommonSession myCommonSession;

    @Override
    public void onTokenRefresh() {


        myCommonSession = new CommonSession(this);

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        myCommonSession.storeDeviceTocken(refreshedToken);

        //Displaying token on logcat
        Log.v(TAG, "Refreshed token: " + refreshedToken);


        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

}