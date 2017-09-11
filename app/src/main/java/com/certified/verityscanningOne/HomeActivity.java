package com.certified.verityscanningOne;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.certified.verityscanningOne.Chat.ui.activity.ChatActivity;
import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.Chat.ui.activity.SelectUsersActivity;
import com.certified.verityscanningOne.Chat.utils.chat.ChatHelper;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogHolder;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.CommonUtils;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.video.activities.CallActivity;
import com.certified.verityscanningOne.video.activities.PermissionsActivity;
import com.certified.verityscanningOne.video.db.QbUsersDbManager;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.utils.CollectionsUtils;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.PermissionsChecker;
import com.certified.verityscanningOne.video.utils.PushNotificationSender;
import com.certified.verityscanningOne.video.utils.QBEntityCallbackImpl;
import com.certified.verityscanningOne.video.utils.WebRtcSessionManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static android.R.string.yes;
import static com.certified.verityscanningOne.BaseClass.commonSession;
import static com.certified.verityscanningOne.MyApplication.context;
import static org.jivesoftware.smackx.rsm.packet.RSMSet.PageDirection.after;


public class HomeActivity extends BaseActivity {

    public static HomeActivity activity = null;
    int REQUST_CAMERA = 2;
    private static final String TAG = "HomeActivity";

    CommonSession hJoomAppCommonSession = null;
    ImageView imageView_scan = null, imageView_search = null, imageView_about_us = null, imageView_social = null,imageview_usdarecalls = null, imageView_logo = null,imageView_chat = null;

    View view_home = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    Bundle bundle_for_selected_social = new Bundle();
    // Remote Config keys
    private static final String PRICE_CONFIG_KEY = "price";
    private static final String DISCOUNT_CONFIG_KEY = "discount";
    public final static int REQUEST_CODE = 20;

    Loginbean loginbean;
    // remote config
    RelativeLayout relativeBG;
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String HOME_BACKGROUND_COLOR = "homebackgroundColor";
    private static final String HOME_HEARDER_BG_COLOR = "homeHeaderColor";

    public static FirebaseStorage storage;
    SharedPrefsHelper sharedPrefsHelper;
    public static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;
    ArrayList<QBUser> userList;
    private QBUser currentUser;
    private ArrayList<QBUser> currentOpponentsList;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    private QbUsersDbManager dbManager;
    private PermissionsChecker checker;

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            activity = this;
            //video
            sharedPrefsHelper = SharedPrefsHelper.getInstance();
            initFields();
            hJoomAppCommonSession = new CommonSession(this);
            initVideo(activity);

            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(HomeActivity.this, true);
                finish();
            }





            checker = new PermissionsChecker(getApplicationContext());
            imageView_normal_settings.setVisibility(View.VISIBLE);
            imageView_back.setVisibility(View.GONE);
            imageView_normal_settings.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(HomeActivity.this, NormalSettingActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            if (checkStoragePermission()) {

            } else {
                requestStoragePermission();
            }

            // Get Remote Config instance.
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            mFirebaseRemoteConfig.setConfigSettings(configSettings);
            //           mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_about);
            // End remote config

            // get Firebase storage
            storage = FirebaseStorage.getInstance();

            // turnGPSOff();
            findViewById();
            SetUI();

            if (canDrawOverlayViews(HomeActivity.this)) {

            } else {
                requestOverlayDrawPermission(HomeActivity.this, REQUEST_CODE);
            }

        } catch (Exception e) {
            e.printStackTrace();


            CommonMethods.printFirebaseLogcat(TAG, e, null);
        }





    }

    @SuppressLint("NewApi")
    public static boolean canDrawOverlayViews(Context con) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        try {
            return Settings.canDrawOverlays(con);
        } catch (NoSuchMethodError e) {
            return canDrawOverlaysUsingReflection(con);
        }
    }


    public static boolean canDrawOverlaysUsingReflection(Context context) {

        try {

            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method dispatchMethod = clazz.getMethod("checkOp", new Class[]{int.class, int.class, String.class});
            int mode = (Integer) dispatchMethod.invoke(manager, new Object[]{24, Binder.getCallingUid(), context.getApplicationContext().getPackageName()});

            return AppOpsManager.MODE_ALLOWED == mode;

        } catch (Exception e) {
            return false;
        }

    }

    @SuppressLint("InlinedApi")
    public static void requestOverlayDrawPermission(Activity act, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + act.getPackageName()));
        act.startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            // ** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                }
            }
        }if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {

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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {


            view_home = getLayoutInflater().inflate(R.layout.home_screen, null);
            imageView_logo = (ImageView) view_home.findViewById(R.id.imageview_top_logo);
            imageView_scan = (ImageView) view_home.findViewById(R.id.imageview_scan);
            imageView_search = (ImageView) view_home.findViewById(R.id.imageview_search);
            imageView_about_us = (ImageView) view_home.findViewById(R.id.imageview_about_us);
            imageView_chat = (ImageView) view_home.findViewById(R.id.imageview_chat);




            imageView_social = (ImageView) view_home.findViewById(R.id.imageview_social);
            imageview_usdarecalls=(ImageView)view_home.findViewById(R.id.imageview_usdarecalls);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MobileAds.initialize(getApplicationContext(), "ca-app-pub-6103044640428504/4894956476");
                    AdView mAdView = (AdView) view_home.findViewById(R.id.adView);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                }
            });




            relativeBG = (RelativeLayout) view_home.findViewById(R.id.homeBG);
            fetchBG();
            updateImage();

            frameLayout_container.addView(view_home);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    private void requestStoragePermission() {

        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUST_CAMERA);

            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    private boolean checkStoragePermission() {
        try {
            int result = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

        return false;
    }



    private void selectImageTypeDialog() {

        try {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);

            builderSingle.setTitle(R.string.selectItem);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
            arrayAdapter.add(getResources().getString(R.string. USDA_recall));
            arrayAdapter.add(getResources().getString(R.string.allRecallRss));
            arrayAdapter.add(getResources().getString(R.string.recallsforChildren));
            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    Intent intent=new Intent(HomeActivity.this,RssFeeds.class);
                    intent.putExtra("Type_RecallRss", which);
                    startActivity(intent);


                }
            });
            builderSingle.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void SetUI() {
        // TODO Auto-generated method stub

        try {
            imageView_social.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, SentInvitationActivity.class);
                    startActivity(intent);
                }
            });
            imageview_usdarecalls.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectImageTypeDialog();


                }
            });
            imageView_scan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_ID, CommonKeyword.TAG_SCANNING_BUTTON_ANALYTICES);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle_for_selected_social);*/
                    System.gc();
                    Intent intent = new Intent(HomeActivity.this,ScannerActivity.class);
                    startActivity(intent);
                }
            });
            imageView_search.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(intent);
                   /* try {
                        startOpponentsActivity(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            });
            imageView_about_us.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_ID, CommonKeyword.TAG_ABOUT_US_ANALYTICES);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle_for_selected_social);

                    Intent intent = new Intent(HomeActivity.this, AboutusActivity.class);
                    startActivity(intent);
                }
            });


            imageView_chat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (hJoomAppCommonSession.getLoggedEmail().equalsIgnoreCase("adam@certified.bz")) {

                        DialogsActivity.start(activity);

                    }
                    else
                    {
                        createChatDialog();
                    }

                }
            });




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    // automatic turn off the gps
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }

//    /**
//     * Fetch discount from server.
//     */
//    private void fetchDiscount() {
//
//        long cacheExpiration = 3600; // 1 hour in seconds.
//        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
//        // the server.
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//
//        // [START fetch_config_with_callback]
//        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
//        // fetched and cached config would be considered expired because it would have been fetched
//        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
//        // throttling is in progress. The default expiration duration is 43200 (12 hours).
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(HomeActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();
//
//                            // Once the config is successfully fetched it must be activated before newly fetched
//                            // values are returned.
//                            mFirebaseRemoteConfig.activateFetched();
//                        } else {
//                            Toast.makeText(HomeActivity.this, "Fetch Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        displayPrice();
//                    }
//                });
//        // [END fetch_config_with_callback]
//    }
//
//    /**
//     * Display price with discount applied if promotion is on. Otherwise display original price.
//     */
//    // [START display_price]
//    private void displayPrice() {
//        long initialPrice = mFirebaseRemoteConfig.getLong(PRICE_CONFIG_KEY);
//        long finalPrice = initialPrice;
//        // [START get_config_values]
//        finalPrice = initialPrice - mFirebaseRemoteConfig.getLong(DISCOUNT_CONFIG_KEY);
//        // [END get_config_values]
//
//    }
//    // [END display_price]


    // Remote Config

    private void fetchBG() {

        long cacheExpiration = 3600; // 1 hour in seconds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            updateBG();

                        } else {
                        }
                    }
                });

    }

    private void updateBG() {

        try {
            int about_bg_color = Color.parseColor(mFirebaseRemoteConfig.getString(HOME_BACKGROUND_COLOR));
            int header_bg_color = Color.parseColor(mFirebaseRemoteConfig.getString(HOME_HEARDER_BG_COLOR));


            relativeBG.setBackgroundColor(about_bg_color);

            headerBG.setBackgroundColor(header_bg_color);
            toolbar.setBackgroundColor(header_bg_color);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateImage() {


        StorageReference storageRefHomeLogo = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/HomeScreen/homeLogo@3x.png");
        StorageReference storageRefHomeScan = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/HomeScreen/scan@3x.png");
        StorageReference storageRefHomeSearch = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/HomeScreen/search@3x.png");
        StorageReference storageRefHomeAbout = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/HomeScreen/about-us@3x.png");

        try {

            final File localFileHome = File.createTempFile("images", "jpg");
            final File localFileScan = File.createTempFile("images", "jpg");
            final File localFileSearch = File.createTempFile("images", "jpg");
            final File localFileAbout = File.createTempFile("images", "jpg");

            // logo
            storageRefHomeLogo.getFile(localFileHome).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFileHome.getAbsolutePath());
                    imageView_logo.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });


            // scan
            storageRefHomeScan.getFile(localFileScan).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFileScan.getAbsolutePath());
                    imageView_scan.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

            // search
            storageRefHomeSearch.getFile(localFileSearch).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFileSearch.getAbsolutePath());
                    imageView_search.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

            // About
            storageRefHomeAbout.getFile(localFileAbout).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFileAbout.getAbsolutePath());
                    imageView_about_us.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });


            //gs://verity-ebd35.appspot.com/Verity/Common/settings.png
            StorageReference storageRefSetting = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/Common/settings@2x.png");
            try {
                final File localFile = File.createTempFile("images", "jpg");
                storageRefSetting.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView_normal_settings.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e) {
            }

        } catch (IOException e) {
        }

    }

    private void createChatDialog() {

        try {
            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.please_wait);

            QBUsers.getUserByLogin("adam@certified.bz").performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    try {
                        userList = new ArrayList<>();
                        userList.add(SharedPrefsHelper.getInstance().getQbUser());
                        userList.add(qbUser);
                        createDialog(userList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(QBResponseException errors) {
                    ProgressDialogFragment.hide(getSupportFragmentManager());
                    CommonUtils.commonToast(HomeActivity.this, "User is not available");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // =============== chat  audio & video ====================

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        try {
            ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                    new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog dialog, Bundle args) {
                            ProgressDialogFragment.hide(getSupportFragmentManager());
                            ChatActivity.startForResult(HomeActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, dialog,"38713707");
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            ProgressDialogFragment.hide(getSupportFragmentManager());

                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startOpponentsActivity(final boolean isVideo) {

        try {

            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.please_wait);
            currentUser = sharedPrefsHelper.getQbUser();

            QBUsers.getUserByLogin(hJoomAppCommonSession.getLoggedEmail()).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {

                    currentOpponentsList = new ArrayList<>();
                    currentOpponentsList.add(qbUser);
                    dbManager.saveAllUsers(currentOpponentsList, true);

                    if (isLoggedInChat()) {
                        startCall(isVideo);
                    }
                    if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                        startPermissionsActivity(false);
                    }
                }

                @Override
                public void onError(QBResponseException errors) {

                    ProgressDialogFragment.hide(getSupportFragmentManager());
                    CommonUtils.commonToast(HomeActivity.this, "User is not available");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //  Opponents
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (intent.getExtras() != null) {
                isRunForCall = intent.getExtras().getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
                if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                    CallActivity.start(HomeActivity.this, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }

    private void initFields() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            }

            dbManager = QbUsersDbManager.getInstance(getApplicationContext());
            webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        try {
            if (sharedPrefsHelper.hasQbUser()) {
                QBUser qbUser = sharedPrefsHelper.getQbUser();
                CallService.start(this, qbUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCall(boolean isVideoCall) {

        try {

            ProgressDialogFragment.hide(getSupportFragmentManager());
            Log.d("TAG", "startCall()");
            ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(currentOpponentsList);
            QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                    ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                    : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

            QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

            QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

            WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

            PushNotificationSender.sendPushMessage(opponentsList,currentUser.getFullName() );
            CallActivity.start(this, false);
            Log.d("TAG", "conferenceType = " + conferenceType);
        } catch (IllegalStateException e) {
            e.printStackTrace();
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

            login(hJoomAppCommonSession.getLoggedEmail());

            QBRTCClient.getInstance(this).prepareToProcessCalls();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============  audio & video ====================



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


}