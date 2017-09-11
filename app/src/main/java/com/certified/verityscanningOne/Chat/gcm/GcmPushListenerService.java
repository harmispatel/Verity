package com.certified.verityscanningOne.Chat.gcm;


import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.video.services.CallService;
import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.users.model.QBUser;


public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {


        NotificationUtils.showNotification(this, DialogsActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.drawable.app_logo_new_scanning, NOTIFICATION_ID);

//        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
//        if (sharedPrefsHelper.hasQbUser()) {
//            Log.e("TAG", "App have logined user");
//            QBUser qbUser = sharedPrefsHelper.getQbUser();
//            startLoginService(qbUser);
//        }
    }

    private void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);
    }

}