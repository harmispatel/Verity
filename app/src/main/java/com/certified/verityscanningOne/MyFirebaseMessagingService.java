package com.certified.verityscanningOne;

import android.util.Log;

import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.video.services.CallService;
;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;




/**
 * Created by harmis on 27/12/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            // {user_id=26718500, message=lawyer: k, dialog_id=58fddb3ba0eb47e6ad000022}
            //{"M":{"nType":"1","id":"34","m":"Newnormaluser comment on your kipanga John update post","subtype":"4"}}

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e(TAG, "json ==: " + json.toString());
                JSONObject jsonM = json.getJSONObject("M");

                String notification_type = jsonM.getString("nType");//2 type case id

                String _id = "";
                if (jsonM.has("id")) {
                    _id = jsonM.getString("id");//bid type //award-case id
                }

                String notification_subType = "";
                if (jsonM.has("subType")) {
                    notification_subType = jsonM.getString("subType");//bid type //award-case id
                }
                String message = jsonM.getString("m");

                //{"M":{"nType":"6","MeetingDate":"2017-04-14","MeetingTime":"05:35 PM","m":"james wants to fix meeting on 2017-04-14 05:35 PM"}}

                String notification_time = "";
                if (jsonM.has("MeetingTime")) {
                    notification_time = jsonM.getString("MeetingTime");
                }

                String notification_date = "";
                if (jsonM.has("MeetingDate")) {
                    notification_date = jsonM.getString("MeetingDate");
                }


                PushNotification.notify(this, message, notification_type, _id, (int) System.currentTimeMillis(),
                        notification_subType, notification_time, notification_date);


            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());

                //video
                //      Data Payload: {message=2131231160}
                //      json ==: {"message":2131231160}
                //      Exception: No value for M


                //     Data Payload: {user_id=26840576, message=rahul patel: hggggcd, dialog_id=590099c3a28f9aba94000001}
                //     {user_id=26840576, message=rahul patel: hggggcd, dialog_id=590099c3a28f9aba94000001}


                String message = remoteMessage.getData().get(GcmConsts.EXTRA_GCM_MESSAGE);
                String dialogID = remoteMessage.getData().get(GcmConsts.EXTRA_GCM_DIALOG_ID);
                String userID = remoteMessage.getData().get(GcmConsts.EXTRA_GCM_USER_ID);

                if (dialogID == null) {

                    SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
                    if (sharedPrefsHelper.hasQbUser()) {
                        Log.e("TAG", "App have logined user");
                        QBUser qbUser = sharedPrefsHelper.getQbUser();
                        startLoginService(qbUser);

//                        NotificationUtils.showNotification(this, DialogsActivity.class,
//                                ResourceUtils.getString(R.string.notification_title), "Incoming call",
//                                R.mipmap.ic_launcher_transperent, 1);


                     }

                } else {

                    NotificationUtils.showNotification(this, DialogsActivity.class,
                            ResourceUtils.getString(R.string.notification_title), message,
                            R.drawable.app_logo_new_scanning, 1);
                }

            }
        }

    }


    private void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);


//        Intent intent = new Intent(this, CallActivity.class);
//        intent.putExtra(Consts.EXTRA_IS_INCOMING_CALL, true);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        this.startActivity(intent);

    }
}