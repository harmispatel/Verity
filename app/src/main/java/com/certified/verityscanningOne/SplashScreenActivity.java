package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.widget.RelativeLayout;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.ConnectionDetector;
import com.certified.verityscanningOne.Utils.Utils;
import com.certified.verityscanningOne.video.db.QbUsersDbManager;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.PermissionsChecker;
import com.certified.verityscanningOne.video.utils.WebRtcSessionManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;

import java.security.MessageDigest;
import java.util.HashMap;

import static com.certified.verityscanningOne.BaseClass.commonSession;
import static org.webrtc.NetworkMonitor.init;

public class SplashScreenActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private final int SPLASH_DISPLAY_LENGHT = 2000;
    String TAG = "SplashScreenActivity";

    ConnectionDetector cd;
    CommonSession mCommonSession = null;


    CommonMethods mCommonMethods = null;

    RelativeLayout relative_bgLayout;
    String splashName = "";
    private GoogleApiClient mGoogleApiClient_for_inviteapi;

    QBUser userForSave;

    public static Activity pushActivity;
    PermissionsChecker checker;

    WebRtcSessionManager webRtcSessionManager;
    QbUsersDbManager dbManager;


    // ========== chat push ===========
    public static QBIncomingMessagesManager incomingMessagesManager;
    public static QBChatDialogMessageListener allDialogsMessagesListener;


    //chat
    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;

    //video
    SharedPrefsHelper sharedPrefsHelper;
    protected QBResRequestExecutor requestExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.splash_screen);

            init(this);

            Bundle bundle = getIntent().getExtras();
            PackageInfo info;
            try {
                info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md;
                    md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String something = new String(Base64.encode(md.digest(), 0));
                    Log.e("hash key", something);
                }
            } catch (Exception e) {
                Log.e("exception", e.toString());


            }
            int index = 0;
            if (bundle != null) {
                splashName = bundle.getString("splashname");
                index = bundle.getInt("value");
            } else {

            }

            mGoogleApiClient_for_inviteapi = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this, this)
                    .build();

            boolean autoLaunchDeepLink = true;
            AppInvite.AppInviteApi.getInvitation(mGoogleApiClient_for_inviteapi, this, autoLaunchDeepLink)
                    .setResultCallback(
                            new ResultCallback<AppInviteInvitationResult>() {
                                @Override
                                public void onResult(AppInviteInvitationResult result) {
                                    try {
                                        if (result.getStatus().isSuccess()) {
                                            // Extract information from the intent
                                            Intent intent = result.getInvitationIntent();
                                            String deepLink = AppInviteReferral.getDeepLink(intent);

                                            String invitationId = AppInviteReferral.getInvitationId(intent);
                                            String receiver_refere_code = null;
                                            HashMap<String, String> stringStringHashMap = CommonMethods.getQueryMap(deepLink);
                                            if (stringStringHashMap.size() != 0) {
                                                if (stringStringHashMap.containsKey("link")) {
                                                    receiver_refere_code = stringStringHashMap.get("link");

                                                }
                                                if (stringStringHashMap.containsKey("apn")) {
                                                    String packageName = stringStringHashMap.get("apn");

                                                    if (packageName.equals(getApplicationContext().getPackageName())) {
                                                        mCommonSession.setInviterReceviedRefereCode(receiver_refere_code);
                                                    }

                                                }

                                            }

                                            // Because autoLaunchDeepLink = true we don't have to do anything
                                            // here, but we could set that to false and manually choose
                                            // an Activity to launch to handle the deep link here.
                                            // ...
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

            //Toast.makeText(SplashScreenActivity.this, "Latitude :=" + GifPlayActivity.mLatitude + "\nLongitude :=" + GifPlayActivity.mLongitude, Toast.LENGTH_LONG).show();

            relative_bgLayout = (RelativeLayout) findViewById(R.id.splash_bg);

            if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_publix)) {

                relative_bgLayout.setBackgroundResource(R.drawable.splash_publix);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_wawa)) {

                relative_bgLayout.setBackgroundResource(R.drawable.splash_wawa);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_trader_joes)) {

                relative_bgLayout.setBackgroundResource(R.drawable.splash_trader);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_walmart)) {

                relative_bgLayout.setBackgroundResource(R.drawable.splash_walmart);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_whole_foods)) {

                relative_bgLayout
                        .setBackgroundResource(R.drawable.splash_wholefoods);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_the_fresh_market)) {

                relative_bgLayout
                        .setBackgroundResource(R.drawable.splash_fresh_market);

            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_kroger)) {

                relative_bgLayout.setBackgroundResource(R.drawable.splash_kroger);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.str_costco)) {
                relative_bgLayout.setBackgroundResource(R.drawable.splash_costco);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.starbucks)) {
                relative_bgLayout.setBackgroundResource(R.drawable.starbucks);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.chipotle)) {
                relative_bgLayout.setBackgroundResource(R.drawable.chipotle);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.AlfalfaMarket)) {
                relative_bgLayout.setBackgroundResource(R.drawable.alfamarket);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.CosmoProf)) {
                relative_bgLayout.setBackgroundResource(R.drawable.cosmoprof);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.CVS_Pharmacy)) {
                relative_bgLayout.setBackgroundResource(R.drawable.cvcpharmacy);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.Dunkin_Donut)) {
                relative_bgLayout.setBackgroundResource(R.drawable.duncandonuts);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.GNC)) {
                relative_bgLayout.setBackgroundResource(R.drawable.gnc);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.Panera_Bread)) {
                relative_bgLayout.setBackgroundResource(R.drawable.perera_bread);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.Pet_Supermarket)) {
                relative_bgLayout.setBackgroundResource(R.drawable.pet_supermarket);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.Salon_Centric)) {
                relative_bgLayout.setBackgroundResource(R.drawable.salon_centric);
            } else if (splashName
                    .equalsIgnoreCase(GifPlayActivity.Wallgreen)) {
                relative_bgLayout.setBackgroundResource(R.drawable.walgreens);
            } else {
                relative_bgLayout
                        .setBackgroundResource(R.drawable.normal_splas);
            }


            mCommonMethods = new CommonMethods(SplashScreenActivity.this);
            mCommonSession = new CommonSession(SplashScreenActivity.this);
            cd = new ConnectionDetector(getApplicationContext());


            if (index == 1) {

            } else {
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method

                    @Override
                    public void run() {
                        try {

                            
                            // mCommonSession.resetLoggedUserID();

                            if (mCommonSession.getLoggedUserID() == null) {


                                Intent i = new Intent(SplashScreenActivity.this,
                                        SocialLoginActivity.class);
                                startActivity(i);
                                // close this activity
                                finish();

                            } else {
                                Intent i = new Intent(SplashScreenActivity.this,
                                        HomeActivity.class);

                                startActivity(i);
                                // close this activity
                                finish();

                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            CommonMethods.printFirebaseLogcat(TAG, e, null);

                        }
                    }
                }, SPLASH_DISPLAY_LENGHT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





}