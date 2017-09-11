package com.certified.verityscanningOne;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.GPSTracker;
import com.certified.verityscanningOne.Utils.PlaceJSONParser;
import com.certified.verityscanningOne.Utils.Utils;
import com.certified.verityscanningOne.beans.Locationbean;

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
import com.google.android.gms.maps.model.LatLng;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.certified.verityscanningOne.R.drawable.camera;


public class GifPlayActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    GifPlayActivity gifPlayActivity = null;
    int index_counter = 0;
    CommonSession mCommonSession;
    private GoogleApiClient mGoogleApiClient_for_inviteapi;

    int radius = 100;
    public double newLatitude = 0;
    public double newLongitude = 0;
    String TAG = "GifPlayActivity";
    GPSTracker gps;

    ArrayList<LatLng> m_listPosition = new ArrayList<LatLng>();
    ArrayList<String> m_listName = new ArrayList<String>();

    public static String str_certified = "certified";
    public static String str_publix = "publix";
    public static String str_wawa = "wawa";
    public static String str_trader_joes = "trader Joe's";
    public static String str_walmart = "walmart";
    public static String str_whole_foods = "whole foods";
    public static String str_the_fresh_market = "the fresh market";
    public static String str_kroger = "kroger";
    public static String str_costco = "costco";
    //public static String str_shnuck = "Schnucks";
    public static String str_shnuck = "Schnucks";
    public static String starbucks = "Starbucks";
    public static String chipotle = "Chipotle";
    public static String AlfalfaMarket = "AlfalfaMarket";
    public static String CosmoProf = "CosmoProf";
    public static String CVS_Pharmacy = "CVS Pharmacy";
    public static String Dunkin_Donut = "Dunkin' Donuts";
    public static String GNC = "GNC";
    public static String Panera_Bread = "Panera Bread";
    public static String Pet_Supermarket = "PetSuperMarket";
    public static String Salon_Centric = "Salon Centric";
    public static String Wallgreen = "Walgreens";
    public static Location location_from_gif = null;
    public static ArrayList<Locationbean> locationbeanArrayList_below_75 = new ArrayList<>();

    ArrayList<Double> m_distance = new ArrayList<Double>();

    LinearLayout linearMainLL;

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
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.gif_view);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Utils.getDatabase();


        QBSettings.getInstance().init(getApplicationContext(), "60556", "wHFNW3rTDVDAHbv", "zDw33GJcs2fGKm-");
        QBSettings.getInstance().setAccountKey("iaKpK7oKiNgtwtCs2zsc");
        initChat();
        try {
            gifPlayActivity = this;
            mCommonSession = new CommonSession(this);
            m_listPosition.clear();
            m_listName.clear();
            index_counter = 0;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            checkInterNetConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void initChat() {
        try {
            //chat
            googlePlayServicesHelper = new GooglePlayServicesHelper();
            pushBroadcastReceiver = new PushBroadcastReceiver();


            //video
            requestExecutor = App.getInstance().getQbResRequestExecutor();
            sharedPrefsHelper = SharedPrefsHelper.getInstance();
            if (sharedPrefsHelper.hasQbUser()) {
                startLoginService(sharedPrefsHelper.getQbUser());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkInterNetConnection() {

        try {
            if (CommonMethods.isNetworkConnected(this)) {
                performNextTask();

            } else {
                linearMainLL = (LinearLayout) findViewById(R.id.mainLL);
                Snackbar snackbar = Snackbar
                        .make(linearMainLL, "No InterNet Connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                checkInterNetConnection();

                            }
                        });

                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performNextTask() {

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                if (checkAndRequestPermissions()) {
                    setupLocationMain();
                }
            } else {
                // Pre-Marshmallow
                setupLocationMain();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkAndRequestPermissions() {

        try {
            int access_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int read_phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int sendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            int read_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            int writeexterna = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int Readexternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);



            List<String> listPermissionsNeeded = new ArrayList<>();

            if (access_fine_location != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (read_phone_state != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (sendSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
            }

            if (read_contacts != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }

            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }

            if (writeexterna != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (Readexternal != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }


            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        // process the normal flow

                        setupLocationMain();


                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    setupLocationMain();
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void setupLocationMain() {
        try {

            if (mGoogleApiClient_for_inviteapi == null) {

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
                                                    if (stringStringHashMap.containsKey("refCode")) {
                                                        receiver_refere_code = stringStringHashMap.get("refCode");

                                                    }
                                                    if (stringStringHashMap.containsKey("apn")) {
                                                        String packageName = stringStringHashMap.get("apn");
                                                        if (packageName.equals(getApplicationContext().getPackageName())) {
                                                            mCommonSession.setInviterReceviedRefereCode(receiver_refere_code);
                                                        }

                                                    }

                                                }


                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

            }


            // create class object
            gps = new GPSTracker(GifPlayActivity.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                //DO SOME ACTIONS HERE , THIS ACTIONS WILL WILL EXECUTE AFTER 5 SECONDS...
                                gps = new GPSTracker(GifPlayActivity.this);

                                newLatitude = Double.parseDouble(String.format("%.6f", gps.getLatitude()));
                                newLongitude = Double.parseDouble(String.format("%.6f", gps.getLongitude()));

                                if (mCommonSession.getoldLat() == 0) {
                                    mCommonSession.setoldLat(Float.parseFloat(String.valueOf(newLatitude)));
                                    mCommonSession.setoldlong(Float.parseFloat(String.valueOf(newLongitude)));

                                } else {
                                    double latitude_local = Double.parseDouble(String.valueOf(mCommonSession.getoldLat()));

                                    boolean isSame = checkNewLatlongsameAsOld(newLatitude, latitude_local);
                                    if (isSame) {
                                        if (location_from_gif != null) {
                                            newLatitude = location_from_gif.getLatitude();
                                            newLongitude = location_from_gif.getLongitude();

                                            boolean isSameagain = checkNewLatlongsameAsOld(newLatitude, latitude_local);

                                            mCommonSession.setoldLat(Float.parseFloat(String.valueOf(newLatitude)));
                                            mCommonSession.setoldlong(Float.parseFloat(String.valueOf(newLongitude)));
                                            if (isSameagain) {
                                                if (location_from_gif != null) {
                                                    newLatitude = location_from_gif.getLatitude();
                                                    newLongitude = location_from_gif.getLongitude();

                                                    mCommonSession.setoldLat(Float.parseFloat(String.valueOf(newLatitude)));
                                                    mCommonSession.setoldlong(Float.parseFloat(String.valueOf(newLongitude)));

                                                }
                                            }

                                        }
                                    }

                                }

                                setupLOcationData();


                            }
                        });
                    }
                }, 3000);


            } else {
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean checkNewLatlongsameAsOld(double latitude, double longitude) {
        int retval = Double.compare(latitude, longitude);
        boolean isitSame = false;

        if (retval > 0) {
            isitSame = false;

        } else if (retval < 0) {
            isitSame = false;

        } else {
            isitSame = true;
        }
        return isitSame;
    }


    public void setupLOcationData() {
        try {

            String Key = "AIzaSyBHMVfcKqZC0gQky3zgP5r6fEUWo9bEBcc";

            // \n is for new line
            //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + newLatitude + "\nLong: " + newLongitude, Toast.LENGTH_LONG).show();

            String type = "walmart";
            String type1 = "the%20fresh%20market";
            String type2 = "kroger";
            String type3 = "whole%20foods";
            String type4 = "costco";
            String type5 = "publix";
            String type6 = "trader%20Joe%27s";
            String type7 = "wawa";
            String type8 = "schnucks";
            String type9 = "Starbucks";
            String type10 = "Chipotle";
            String type11 = "Alfalfa%27s%20Market";
            String type12 = "CosmoProf";
            String type13 = "CVS%20Pharmacy";
            String type14 = "Dunkin%20Donut%27s";
            String type15 = "GNC%27s";
            String type16 = "Panera%20Bread%27s";
            String type17 = "Pet%20Supermarket%27s";
            String type18 = "Salon%20Centric%27s";
            String type19 = "Walgreen%27s";


            StringBuilder sb01 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb01.append("location=" + newLatitude + "," + newLongitude);
            sb01.append("&radius=" + radius);
            sb01.append("&types=store&name=" + type);
            sb01.append("&sensor=true");
            sb01.append("&key=" + Key + "");

            PlacesTask placesTask = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask, sb01.toString(), "walmart");


            // ========
            StringBuilder sb1 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb1.append("location=" + newLatitude + "," + newLongitude);
            sb1.append("&radius=" + radius);
            sb1.append("&types=store&name=" + type1);
            sb1.append("&sensor=true");
            sb1.append("&key=" + Key + "");


            PlacesTask placesTask1 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask1, sb1.toString(), "the fresh market");

            // // ========
            StringBuilder sb2 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb2.append("location=" + newLatitude + "," + newLongitude);
            sb2.append("&radius=" + radius);
            sb2.append("&types=store&name=" + type2);
            sb2.append("&sensor=true");
            sb2.append("&key=" + Key + "");


            PlacesTask placesTask2 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask2, sb2.toString(), type2);

            // ========
            StringBuilder sb3 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb3.append("location=" + newLatitude + "," + newLongitude);
            sb3.append("&radius=" + radius);
            sb3.append("&types=store&name=" + type3);
            sb3.append("&sensor=true");
            sb3.append("&key=" + Key + "");


            PlacesTask placesTask3 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask3, sb3.toString(), "whole foods");

            // ========
            StringBuilder sb4 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb4.append("location=" + newLatitude + "," + newLongitude);
            sb4.append("&radius=" + radius);
            sb4.append("&types=store&name=" + type4);
            sb4.append("&sensor=true");
            sb4.append("&key=" + Key + "");

            PlacesTask placesTask4 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask4, sb4.toString(), type4);

            // ========
            StringBuilder sb5 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb5.append("location=" + newLatitude + "," + newLongitude);
            sb5.append("&radius=" + radius);
            sb5.append("&types=store&name=" + type5);
            sb5.append("&sensor=true");
            sb5.append("&key=" + Key + "");
            PlacesTask placesTask5 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask5, sb5.toString(), type5);


            StringBuilder sb6 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb6.append("location=" + newLatitude + "," + newLongitude);
            sb6.append("&radius=" + radius);
            sb6.append("&types=store&name=" + type6);
            sb6.append("&sensor=true");
            sb6.append("&key=" + Key + "");
            PlacesTask placesTask6 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask6, sb6.toString(), "trader Joe's");


            // ========
            StringBuilder sb7 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb7.append("location=" + newLatitude + "," + newLongitude);
            sb7.append("&radius=" + radius);
            sb7.append("&types=store&name=" + type7);
            sb7.append("&sensor=true");
            sb7.append("&key=" + Key + "");
            PlacesTask placesTask7 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask7, sb7.toString(), type7);

            // ========
            StringBuilder sb8 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb8.append("location=" + newLatitude + "," + newLongitude);
            sb8.append("&radius=" + radius);
            sb8.append("&types=store&name=" + type8);
            sb8.append("&sensor=true");
            sb8.append("&key=" + Key + "");


            PlacesTask placesTask8 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask8, sb8.toString(), type8);

            // ========
            StringBuilder sb9 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb9.append("location=" + newLatitude + "," + newLongitude);
            sb9.append("&radius=" + radius);
            sb9.append("&types=store&name=" + type9);
            sb9.append("&sensor=true");
            sb9.append("&key=" + Key + "");


            PlacesTask placesTask9 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask9, sb9.toString(), type9);


            StringBuilder sb10 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb10.append("location=" + newLatitude + "," + newLongitude);
            sb10.append("&radius=" + radius);
            sb10.append("&types=restaurant&name=" + type10);
            sb10.append("&sensor=true");
            sb10.append("&key=" + Key + "");


            PlacesTask placesTask10 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask10, sb10.toString(), type10);


///These are new 9


            StringBuilder sb11 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb11.append("location=" + newLatitude + "," + newLongitude);
            sb11.append("&radius=" + radius);
            sb11.append("&types=store&name=" + type11);
            sb11.append("&sensor=true");
            sb11.append("&key=" + Key + "");


            PlacesTask placesTask11 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask11, sb11.toString(), "Alfalfa's");


            StringBuilder sb12 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb12.append("location=" + newLatitude + "," + newLongitude);
            sb12.append("&radius=" + radius);
            sb12.append("&types=store&name=" + type12);
            sb12.append("&sensor=true");
            sb12.append("&key=" + Key + "");


            PlacesTask placesTask12 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask12, sb12.toString(), "CosmoProf");


            StringBuilder sb13 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb13.append("location=" + newLatitude + "," + newLongitude);
            sb13.append("&radius=" + radius);
            sb13.append("&types=pharmacy&name=" + type13);
            sb13.append("&sensor=true");
            sb13.append("&key=" + Key + "");


            PlacesTask placesTask13 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask13, sb13.toString(), "CVS Pharmacy");


            StringBuilder sb14 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb14.append("location=" + newLatitude + "," + newLongitude);
            sb14.append("&radius=" + radius);
            sb14.append("&types=food&name=" + type14);
            sb14.append("&sensor=true");
            sb14.append("&key=" + Key + "");


            PlacesTask placesTask14 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask14, sb14.toString(), "Dunkin' Donuts");


            StringBuilder sb15 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb15.append("location=" + newLatitude + "," + newLongitude);
            sb15.append("&radius=" + radius);
            sb15.append("&types=store&name=" + type15);
            sb15.append("&sensor=true");
            sb15.append("&key=" + Key + "");


            PlacesTask placesTask15 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask15, sb15.toString(), "GNC");


            StringBuilder sb16 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb16.append("location=" + newLatitude + "," + newLongitude);
            sb16.append("&radius=" + radius);
            sb16.append("&types=food&name=" + type16);
            sb16.append("&sensor=true");
            sb16.append("&key=" + Key + "");


            PlacesTask placesTask16 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask16, sb16.toString(), "Panera Bread");


            StringBuilder sb17 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb17.append("location=" + newLatitude + "," + newLongitude);
            sb17.append("&radius=" + radius);
            sb17.append("&types=store&name=" + type17);
            sb17.append("&sensor=true");
            sb17.append("&key=" + Key + "");


            PlacesTask placesTask17 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask17, sb17.toString(), "Pet Supermarket");


            StringBuilder sb18 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb18.append("location=" + newLatitude + "," + newLongitude);
            sb18.append("&radius=" + radius);
            sb18.append("&types=store&name=" + type18);
            sb18.append("&sensor=true");
            sb18.append("&key=" + Key + "");


            PlacesTask placesTask18 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask18, sb18.toString(), "Salon Centric");


            StringBuilder sb19 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb19.append("location=" + newLatitude + "," + newLongitude);
            sb19.append("&radius=" + radius);
            sb19.append("&types=store&name=" + type19);
            sb19.append("&sensor=true");
            sb19.append("&key=" + Key + "");


            PlacesTask placesTask19 = new PlacesTask(true);
            StartAsyncTaskInParallel(placesTask19, sb19.toString(), "Walgreens");


        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(PlacesTask task, String string, String type) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, string, type);
            else
                task.execute(string, type);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }


            data = sb.toString();
            Log.e("URLS", strUrl);

            // Log.e("Responce api", data);
            br.close();

        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        } finally {
            assert iStream != null;
            iStream.close();
            assert urlConnection != null;
            urlConnection.disconnect();
        }

        return data;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gps != null) {
            gps.stopUsingGPS();
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;
        String type = null;
        boolean shouldNowCalculate = false;

        public PlacesTask(boolean mshouldNowCalculate) {
            shouldNowCalculate = mshouldNowCalculate;
        }


        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {

                type = url[1];
                data = downloadUrl(url[0]);


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            try {
                ParserTask parserTask = new ParserTask(shouldNowCalculate);


                parserTask.execute(result, type);
            } catch (Exception e) {
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
        }

    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends
            AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;
        String type = null;
        boolean shouldNowCalculate = false;

        public ParserTask(boolean mshouldNowCalculate) {
            shouldNowCalculate = mshouldNowCalculate;
        }


        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(
                String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                type = jsonData[1];
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject, type);

            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            try {
                if (list != null) {

                    for (int i = 0; i < list.size(); i++) {

                        // Getting a place from the places list
                        HashMap<String, String> hmPlace = list.get(i);
                        if (hmPlace.size() != 0) {
                            // Getting latitude of the place
                            double lat = Double.parseDouble(hmPlace.get("lat"));

                            // Getting longitude of the place
                            double lng = Double.parseDouble(hmPlace.get("lng"));

                            // Getting name
                            String name = hmPlace.get("place_name");

                            // Getting vicinity
                            String vicinity = hmPlace.get("vicinity");

                            LatLng latLng = new LatLng(lat, lng);

                            m_listPosition.add(latLng);
                            m_listName.add(name + " : " + vicinity);
                        }


                    }
                } else {


                }


                if (shouldNowCalculate) {
                    String splashName = null;
                    splashName = str_certified;

                    if (m_listPosition.size() > 0) {

                        m_distance.clear();

                        for (int i = 0; i < m_listPosition.size(); i++) {

                            Location selected_location = new Location("locationA");
                            selected_location.setLatitude(newLatitude);
                            selected_location.setLongitude(newLongitude);

                            Location near_locations = new Location("locationA");
                            near_locations
                                    .setLatitude(m_listPosition.get(i).latitude);
                            near_locations
                                    .setLongitude(m_listPosition.get(i).longitude);

                            DecimalFormat df = new DecimalFormat("#.00");

                            double distance = selected_location
                                    .distanceTo(near_locations);
                            Log.d("distance ===>>>", "" + distance);
                            double newdistance = Double.parseDouble(String.format("%.2f", distance));

                            if (distance <= 100) {
                                m_distance.add(distance);
                                Locationbean locationbean = new Locationbean();
                                locationbean.setLatitude(String.valueOf(m_listPosition.get(i).latitude));
                                locationbean.setLongitude(String.valueOf(m_listPosition.get(i).longitude));
                                locationbean.setDistance(newdistance);
                                locationbean.setLocationName(String.valueOf(m_listName.get(i)));
                                locationbeanArrayList_below_75.add(locationbean);
                            }


                        }

                        if (m_distance.size() == 0) {

                        } else {

                            Collections.sort(locationbeanArrayList_below_75, new Comparator<Locationbean>() {
                                @Override
                                public int compare(Locationbean c1, Locationbean c2) {
                                    return Double.compare(c1.getDistance(), c2.getDistance());
                                }
                            });

                            String nearest_store_name = locationbeanArrayList_below_75.get(0).getLocationName();
                            Toast.makeText(GifPlayActivity.this, "Lowest distance ::" + locationbeanArrayList_below_75.get(0).getDistance() + "\n" + "Place name" + locationbeanArrayList_below_75.get(0).getLocationName(), Toast.LENGTH_LONG).show();


                            if (nearest_store_name.toLowerCase().indexOf(
                                    str_publix.toLowerCase()) != -1) {

                                System.out.println("I found the publix");

                                splashName = str_publix;

                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_wawa.toLowerCase()) != -1) {

                                System.out.println("I found the wawa");
                                splashName = str_wawa;

                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_trader_joes.toLowerCase()) != -1) {

                                System.out.println("I found the trader joes");
                                splashName = str_trader_joes;

                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_walmart.toLowerCase()) != -1) {

                                System.out.println("I found the walmart");
                                splashName = str_walmart;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_whole_foods.toLowerCase()) != -1) {

                                System.out.println("I found the whole foods");
                                splashName = str_whole_foods;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_the_fresh_market.toLowerCase()) != -1) {

                                System.out.println("I found the the fresh market");
                                splashName = str_the_fresh_market;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_kroger.toLowerCase()) != -1) {

                                System.out.println("I found the kroger");
                                splashName = str_kroger;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_shnuck.toLowerCase()) != -1) {

                                System.out.println("I found the schuncks");
                                splashName = str_shnuck;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    str_costco.toLowerCase()) != -1) {

                                System.out.println("I found the costco");
                                splashName = str_costco;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    starbucks.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = starbucks;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    chipotle.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = chipotle;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    AlfalfaMarket.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = AlfalfaMarket;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    CosmoProf.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = CosmoProf;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    CVS_Pharmacy.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = CVS_Pharmacy;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    Dunkin_Donut.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = Dunkin_Donut;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    GNC.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = GNC;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    Panera_Bread.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = Panera_Bread;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    Pet_Supermarket.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = Pet_Supermarket;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    Salon_Centric.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = Salon_Centric;
                            } else if (nearest_store_name.toLowerCase().indexOf(
                                    Wallgreen.toLowerCase()) != -1) {

                                System.out.println("I found the starbucks");
                                splashName = Wallgreen;
                            } else {
                                System.out.println("not found");
                                splashName = str_certified;
                            }
                        }


                    } else {
                        splashName = str_certified;
                    }

                    Intent in = new Intent(GifPlayActivity.this,
                            SplashScreenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("splashname", splashName);
                    bundle.putInt("value", 2);
                    in.putExtras(bundle);
                    startActivity(in);
                    finish();


                }
            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }



    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
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



    @Override
    protected void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(pushBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}