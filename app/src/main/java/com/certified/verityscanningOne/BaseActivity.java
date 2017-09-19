package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.Chat.utils.qb.QbChatDialogMessageListenerImp;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.ConnectionDetector;
import com.certified.verityscanningOne.video.db.QbUsersDbManager;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.PermissionsChecker;
import com.certified.verityscanningOne.video.utils.WebRtcSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;

import static com.certified.verityscanningOne.BaseClass.commonSession;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class BaseActivity extends FragmentActivity {
    String TAG = "BaseActivity";

    CommonSession mCommonSessions = null;
    Boolean isInternetPresent = false;
    CommonMethods mCommonMethods = null;
    ConnectionDetector cd;
    // Interstitial Ad interstitial;
    public ProgressDialog mProgressDialog;

    public static Activity myActivity = null;
    RelativeLayout relativeLayout_for_login;
    CommonSession futBolMaxCommonSessions = null;
    ImageView imageView_back = null, imageView_normal_settings = null;
    TextView textView_header_title = null;
    FrameLayout frameLayout_container = null;

    RelativeLayout headerBG;
    Toolbar toolbar;


    //video
    SharedPrefsHelper sharedPrefsHelper;
    protected QBResRequestExecutor requestExecutor;
    GooglePlayServicesHelper googlePlayServicesHelper;
    QBUser userForSave;

    public static Activity pushActivity;
    PermissionsChecker checker;

    WebRtcSessionManager webRtcSessionManager;
    QbUsersDbManager dbManager;


    // ========== chat push ===========
    public static QBIncomingMessagesManager incomingMessagesManager;
    public static QBChatDialogMessageListener allDialogsMessagesListener;

    protected ActionBar actionBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            setContentView(R.layout.activity_main);


            myActivity = BaseActivity.this;

            frameLayout_container = (FrameLayout) findViewById(R.id.main_container);


            imageView_back = (ImageView)
                    findViewById(R.id.imageview_back);
            imageView_normal_settings = (ImageView)
                    findViewById(R.id.imageview_setting);

            textView_header_title = (TextView) findViewById(R.id.textview_header_title);
            textView_header_title.setVisibility(View.GONE);
            futBolMaxCommonSessions = new CommonSession(this);

            mCommonSessions = new CommonSession(this);
            mCommonMethods = new CommonMethods(this);
            cd = new ConnectionDetector(getApplicationContext());


            headerBG = (RelativeLayout) findViewById(R.id.headerBG);
            toolbar = (Toolbar) findViewById(R.id.toolBarHeader);



            requestExecutor = App.getInstance().getQbResRequestExecutor();
            sharedPrefsHelper = SharedPrefsHelper.getInstance();
            googlePlayServicesHelper = new GooglePlayServicesHelper();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @VisibleForTesting
    public void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Loading");
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }



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




    // video

    public void initVideo(Activity mActivity) {
        try {

            googlePlayServicesHelper = new GooglePlayServicesHelper();
            requestExecutor = App.getInstance().getQbResRequestExecutor();
            sharedPrefsHelper = SharedPrefsHelper.getInstance();


            dbManager = QbUsersDbManager.getInstance(getApplicationContext());
            webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
            checker = new PermissionsChecker(getApplicationContext());

            login(commonSession.getLoggedEmail());

            QBRTCClient.getInstance(this).prepareToProcessCalls();


        } catch (Exception e) {
            e.printStackTrace();
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

            allDialogsMessagesListener = new BaseClass.AllDialogsMessageListener();
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
                        ? allDialogsMessagesListener : new BaseClass.AllDialogsMessageListener());
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
                                R.drawable.app_logo_new_scanning, 1);

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

}
