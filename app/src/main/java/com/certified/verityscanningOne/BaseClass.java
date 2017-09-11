package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.Chat.utils.qb.QbChatDialogMessageListenerImp;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.video.db.QbUsersDbManager;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.PermissionsChecker;
import com.certified.verityscanningOne.video.utils.WebRtcSessionManager;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;

import org.json.JSONObject;


/**
 * Created by harmis on 19/3/17.
 */

public class BaseClass extends CoreBaseActivity {

    //application
    public LinearLayout linearBack;
    public TextView txtTitle;
    public ImageView chat_message_img;

    public Toolbar toolbar;
    LinearLayout linearMenu;
    TextView txtTitleMenu;

    public static CommonSession commonSession = null;
    public CommonMethods commomMethod = null;

    LinearLayout linearClientHome, linearClientProfile, linearClientMyFeed, linearClientNotification, linearClientSearchLawyer, linearClientCreateCase;
    LinearLayout linearLawyerHome, linearlawyerFeed, linearLawyerOngoing, linearLawyerPast, linearLawyerNotification, linearLawyerMyProfile;

    LinearLayout linear_home_ll, linear_feed_ll, linear_ongoing_ll, linear_notifiaction_ll, linear_me_ll;
    View linear_home_active_view, linear_feed_active_view, linear_ongoing_active_view, linear_notification_active_view, linear_me_active_view;
    //video
    SharedPrefsHelper sharedPrefsHelper;
    protected QBResRequestExecutor requestExecutor;
    GooglePlayServicesHelper googlePlayServicesHelper;
    QBUser userForSave;

    public static Activity pushActivity;
    PermissionsChecker checker;

    WebRtcSessionManager webRtcSessionManager;
    QbUsersDbManager dbManager;
    protected ActionBar actionBar;

    // ========== chat push ===========
    public static QBIncomingMessagesManager incomingMessagesManager;
    public static QBChatDialogMessageListener allDialogsMessagesListener;






    public void init(Activity mActivity) {
        try {


            actionBar = getSupportActionBar();
            commonSession = new CommonSession(mActivity);
            commomMethod = new CommonMethods(mActivity);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }

            pushActivity = mActivity;

        //    initVideo(pushActivity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ===============  audio & video ====================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {

            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                try {
                    saveUserData(userForSave);
                    signInCreatedUser(userForSave);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toaster.longToast(getString(R.string.login_chat_login_error) + errorMessage);

            }
        }

    }

    private void login(String name) {

        try {
            QBUsers.getUserByLogin(name).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    // Log.e("Name====>>", qbUser.getFullName());
                    loginToChat(qbUser);


                }

                @Override
                public void onError(QBResponseException errors) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loginToChat(final QBUser qbUser) {
        try {
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

            userForSave = qbUser;
            startLoginService(qbUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoginService(QBUser qbUser) {
        try {
            Intent tempIntent = new Intent(this, CallService.class);
            PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
            CallService.start(this, qbUser, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInCreatedUser(final QBUser user) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {

            }

            @Override
            public void onError(QBResponseException responseException) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void saveUserData(QBUser qbUser) {
        try {
            SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
            sharedPrefsHelper.saveQbUser(qbUser);

            allDialogsMessagesListener = new AllDialogsMessageListener();
            registerQbChatListeners();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void registerQbChatListeners() {
        try {
            incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();

            if (incomingMessagesManager != null) {
                incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                        ? allDialogsMessagesListener : new AllDialogsMessageListener());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void unregisterQbChatListeners() {
        try {
            if (incomingMessagesManager != null) {
                incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActiveDetailFooter(boolean homeVisible, boolean feedVisible, boolean ongoingVisible, boolean notification, boolean profile) {
        try {
            if (homeVisible) linear_home_active_view.setVisibility(View.VISIBLE);
            else linear_home_active_view.setVisibility(View.GONE);

            if (feedVisible) linear_feed_active_view.setVisibility(View.VISIBLE);
            else linear_feed_active_view.setVisibility(View.GONE);

            if (ongoingVisible) linear_ongoing_active_view.setVisibility(View.VISIBLE);
            else linear_ongoing_active_view.setVisibility(View.GONE);

            if (notification) linear_notification_active_view.setVisibility(View.VISIBLE);
            else linear_notification_active_view.setVisibility(View.GONE);

            if (profile) linear_me_active_view.setVisibility(View.VISIBLE);
            else linear_me_active_view.setVisibility(View.GONE);
        } catch (Exception e) {
        }
    }

    static class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {

            QBUsers.getUser(senderId).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    try {

                        String message = qbUser.getFullName() + ":" + qbChatMessage.getBody();
                        NotificationUtils.showNotification(pushActivity, DialogsActivity.class,
                                ResourceUtils.getString(R.string.notification_title), message,
                                R.mipmap.ic_launcher_transperent, 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(QBResponseException errors) {

                }
            });


        }

    }


    // video

    public void initVideo(Activity mActivity) {
        try {




            dbManager = QbUsersDbManager.getInstance(getApplicationContext());
            webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
            checker = new PermissionsChecker(getApplicationContext());

            login(commonSession.getLoggedEmail());

            QBRTCClient.getInstance(this).prepareToProcessCalls();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============  audio & video ====================


}
