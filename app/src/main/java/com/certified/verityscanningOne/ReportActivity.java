package com.certified.verityscanningOne;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.GPSTracker;
import com.certified.verityscanningOne.Utils.PlaceJSONParser;
import com.certified.verityscanningOne.beans.Locationbean;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

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

/**
 * Created by harmis on 26/9/16.
 */
public class ReportActivity extends FragmentActivity {

    public static HomeActivity activity = null;
    int REQUST_CAMERA = 2;

    CommonSession hJoomAppCommonSession = null;
    ImageView imageView_scan = null, imageView_search = null, imageView_about_us = null;

    private FirebaseAnalytics mFirebaseAnalytics;
    Bundle bundle_for_selected_social = new Bundle();
    // Remote Config keys
    private static final String PRICE_CONFIG_KEY = "price";
    private static final String DISCOUNT_CONFIG_KEY = "discount";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    Button button_location_report = null;
    String TAG = "GifPlayActivity";


    public static double lat = 0.0, longi = 0.0;

    private static final int PERMISSION_REQUEST_CODE = 1;

    ArrayList<LatLng> m_listPosition = new ArrayList<LatLng>();
    ArrayList<String> m_listName = new ArrayList<String>();
    ArrayList<Double> m_distance = new ArrayList<Double>();

    int index_counter = 0;

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
    public static HashMap<String, ArrayList<Locationbean>> stringArrayListHashMap_LocationWise = new HashMap<>();

    public static ArrayList<Locationbean> locationbeanArrayList_taken = new ArrayList<>();
    public static ArrayList<Locationbean> locationbeanArrayList_below_75 = new ArrayList<>();
    public static ArrayList<Locationbean> locationbeanArrayList_ignored = new ArrayList<>();


    private static final int REQUEST_LOCATION = 1;
    // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    CommonSession mCommonSession;
    int radius = 100;
    public static boolean a = false;
    TextView textView_near_place = null;
    public static EditText editText_meters = null, editText_latitude = null, editText_longitude = null;
    LinearLayout linearLayout_taken_container = null, linearLayout_ignore_container = null, linearLayout_below_75;
    Button button_check = null, button_each_store_wise, button_check_splash;
    ProgressBar progressBar = null;
    TextView textView_below_title = null;
    String splashName = null;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.report_activity_main);
            splashName = str_certified;

            hJoomAppCommonSession = new CommonSession(this);


            findViewById();
            SetUI();
            // create class object
            gps = new GPSTracker(ReportActivity.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {
                lat = Double.parseDouble(String.format("%.6f", gps.getLatitude()));
                longi = Double.parseDouble(String.format("%.6f", gps.getLongitude()));


                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + longi, Toast.LENGTH_LONG).show();

                if (editText_latitude != null) {
                    editText_latitude.setText(String.valueOf(lat));
                }
                if (editText_longitude != null) {
                    editText_longitude.setText(String.valueOf(longi));
                }

            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }
/*

    @Override
    public void locationFetched(Location mLocal, Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);
        try {
            lat = mLocal.getLatitude();
            longi = mLocal.getLongitude();

            lat = Double.parseDouble(String.format("%.6f", mLocal.getLatitude()));
            longi = Double.parseDouble(String.format("%.6f", mLocal.getLongitude()));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {


            editText_meters = (EditText) findViewById(R.id.editText_meters);
            editText_latitude = (EditText) findViewById(R.id.editText_latitude);
            editText_longitude = (EditText) findViewById(R.id.editText_current_longitude);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            linearLayout_taken_container = (LinearLayout) findViewById(R.id.layout_dynamical_container_taken);
            linearLayout_ignore_container = (LinearLayout) findViewById(R.id.layout_dynamical_container_ignored);
            linearLayout_below_75 = (LinearLayout) findViewById(R.id.layout_dynamical_below_75);
            textView_near_place = (TextView) findViewById(R.id.textview_got_result_place_name);
            button_check = (Button) findViewById(R.id.button_check);
            button_each_store_wise = (Button) findViewById(R.id.button_each_store_wise);
            button_check_splash = (Button) findViewById(R.id.button_check_splash);
            editText_meters.setText(String.valueOf(radius));
            textView_below_title = (TextView) findViewById(R.id.textview_below_title);
            textView_below_title.setText("Below <" + radius);

            editText_meters.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // TODO Auto-generated method stub
                    if (s.length() != 0) {
                        textView_below_title.setText("Below <" + s);

                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }


    private void SetUI() {
        // TODO Auto-generated method stub

        try {

            button_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        linearLayout_below_75.removeAllViews();
                        linearLayout_ignore_container.removeAllViews();
                        ;
                        linearLayout_taken_container.removeAllViews();
                        stringArrayListHashMap_LocationWise.clear();

                        locationbeanArrayList_below_75.clear();
                        ;
                        locationbeanArrayList_taken.clear();
                        locationbeanArrayList_ignored.clear();


                        m_listPosition.clear();
                        m_listName.clear();
                        m_distance.clear();

                        progressBar.setVisibility(View.VISIBLE);
                        radius = Integer.parseInt(editText_meters.getText().toString());
                        setupLOcationData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            button_each_store_wise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent in = new Intent(ReportActivity.this,
                                EachStoreWiseActivity.class);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            button_check_splash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent in = new Intent(ReportActivity.this,
                                SplashScreenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("splashname", splashName);
                        bundle.putInt("value", 1);
                        in.putExtras(bundle);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    public void setupLOcationData() {
        try {


            String Key = "AIzaSyBHMVfcKqZC0gQky3zgP5r6fEUWo9bEBcc";

            lat = Double.parseDouble(editText_latitude.getText().toString());
            longi = Double.parseDouble(editText_longitude.getText().toString());


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
            sb01.append("location=" + lat + "," + longi);
            sb01.append("&radius=" + radius);
            sb01.append("&types=store&name=" + type);
            sb01.append("&sensor=true");
            sb01.append("&key=" + Key + "");


            PlacesTask placesTask = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask, sb01.toString(), "walmart");


            // ========
            StringBuilder sb1 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb1.append("location=" + lat + "," + longi);
            sb1.append("&radius=" + radius);
            sb1.append("&types=store&name=" + type1);
            sb1.append("&sensor=true");
            sb1.append("&key=" + Key + "");


            PlacesTask placesTask1 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask1, sb1.toString(), "the fresh market");

            // // ========
            StringBuilder sb2 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb2.append("location=" + lat + "," + longi);
            sb2.append("&radius=" + radius);
            sb2.append("&types=store&name=" + type2);
            sb2.append("&sensor=true");
            sb2.append("&key=" + Key + "");


            PlacesTask placesTask2 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask2, sb2.toString(), type2);

            // ========
            StringBuilder sb3 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb3.append("location=" + lat + "," + longi);
            sb3.append("&radius=" + radius);
            sb3.append("&types=store&name=" + type3);
            sb3.append("&sensor=true");
            sb3.append("&key=" + Key + "");


            PlacesTask placesTask3 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask3, sb3.toString(), "whole foods");

            // ========
            StringBuilder sb4 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb4.append("location=" + lat + "," + longi);
            sb4.append("&radius=" + radius);
            sb4.append("&types=store&name=" + type4);
            sb4.append("&sensor=true");
            sb4.append("&key=" + Key + "");

            PlacesTask placesTask4 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask4, sb4.toString(), type4);

            // ========
            StringBuilder sb5 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb5.append("location=" + lat + "," + longi);
            sb5.append("&radius=" + radius);
            sb5.append("&types=store&name=" + type5);
            sb5.append("&sensor=true");
            sb5.append("&key=" + Key + "");
            PlacesTask placesTask5 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask5, sb5.toString(), type5);


            StringBuilder sb6 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb6.append("location=" + lat + "," + longi);
            sb6.append("&radius=" + radius);
            sb6.append("&types=store&name=" + type6);
            sb6.append("&sensor=true");
            sb6.append("&key=" + Key + "");
            PlacesTask placesTask6 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask6, sb6.toString(), "trader Joe's");


            // ========
            StringBuilder sb7 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb7.append("location=" + lat + "," + longi);
            sb7.append("&radius=" + radius);
            sb7.append("&types=store&name=" + type7);
            sb7.append("&sensor=true");
            sb7.append("&key=" + Key + "");
            PlacesTask placesTask7 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask7, sb7.toString(), type7);

            // ========
            StringBuilder sb8 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb8.append("location=" + lat + "," + longi);
            sb8.append("&radius=" + radius);
            sb8.append("&types=store&name=" + type8);
            sb8.append("&sensor=true");
            sb8.append("&key=" + Key + "");


            PlacesTask placesTask8 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask8, sb8.toString(), type8);

// ========


            // ========
            StringBuilder sb9 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb9.append("location=" + lat + "," + longi);
            sb9.append("&radius=" + radius);
            sb9.append("&types=store&name=" + type9);
            sb9.append("&sensor=true");
            sb9.append("&key=" + Key + "");


            PlacesTask placesTask9 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask9, sb9.toString(), type9);


            StringBuilder sb10 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb10.append("location=" + lat + "," + longi);
            sb10.append("&radius=" + radius);
            sb10.append("&types=restaurant&name=" + type10);
            sb10.append("&sensor=true");
            sb10.append("&key=" + Key + "");


            PlacesTask placesTask10 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask10, sb10.toString(), type10);


///These are new 9


            StringBuilder sb11 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb11.append("location=" + lat + "," + longi);
            sb11.append("&radius=" + radius);
            sb11.append("&types=store&name=" + type11);
            sb11.append("&sensor=true");
            sb11.append("&key=" + Key + "");


            PlacesTask placesTask11 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask11, sb11.toString(), "Alfalfa's");


            StringBuilder sb12 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb12.append("location=" + lat + "," + longi);
            sb12.append("&radius=" + radius);
            sb12.append("&types=store&name=" + type12);
            sb12.append("&sensor=true");
            sb12.append("&key=" + Key + "");


            PlacesTask placesTask12 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask12, sb12.toString(), "CosmoProf");


            StringBuilder sb13 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb13.append("location=" + lat + "," + longi);
            sb13.append("&radius=" + radius);
            sb13.append("&types=pharmacy&name=" + type13);
            sb13.append("&sensor=true");
            sb13.append("&key=" + Key + "");


            PlacesTask placesTask13 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask13, sb13.toString(), "CVS Pharmacy");


            StringBuilder sb14 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb14.append("location=" + lat + "," + longi);
            sb14.append("&radius=" + radius);
            sb14.append("&types=food&name=" + type14);
            sb14.append("&sensor=true");
            sb14.append("&key=" + Key + "");


            PlacesTask placesTask14 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask14, sb14.toString(), "Dunkin' Donuts");


            StringBuilder sb15 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb15.append("location=" + lat + "," + longi);
            sb15.append("&radius=" + radius);
            sb15.append("&types=store&name=" + type15);
            sb15.append("&sensor=true");
            sb15.append("&key=" + Key + "");


            PlacesTask placesTask15 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask15, sb15.toString(), "GNC");


            StringBuilder sb16 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb16.append("location=" + lat + "," + longi);
            sb16.append("&radius=" + radius);
            sb16.append("&types=food&name=" + type16);
            sb16.append("&sensor=true");
            sb16.append("&key=" + Key + "");


            PlacesTask placesTask16 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask16, sb16.toString(), "Panera Bread");


            StringBuilder sb17 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb17.append("location=" + lat + "," + longi);
            sb17.append("&radius=" + radius);
            sb17.append("&types=store&name=" + type17);
            sb17.append("&sensor=true");
            sb17.append("&key=" + Key + "");


            PlacesTask placesTask17 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask17, sb17.toString(), "Pet Supermarket");


            StringBuilder sb18 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb18.append("location=" + lat + "," + longi);
            sb18.append("&radius=" + radius);
            sb18.append("&types=store&name=" + type18);
            sb18.append("&sensor=true");
            sb18.append("&key=" + Key + "");


            PlacesTask placesTask18 = new PlacesTask(false);
            StartAsyncTaskInParallel(placesTask18, sb18.toString(), "Salon Centric");


            StringBuilder sb19 = new StringBuilder(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb19.append("location=" + lat + "," + longi);
            sb19.append("&radius=" + radius);
            sb19.append("&types=store&name=" + type19);
            sb19.append("&sensor=true");
            sb19.append("&key=" + Key + "");


            PlacesTask placesTask19 = new PlacesTask(true);
            StartAsyncTaskInParallel(placesTask19, sb19.toString(), "Walgreens");


        } catch (Exception e) {
            e.printStackTrace();

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

            Log.e("URLS", "===================================");
            Log.e("Responce api", data);
            br.close();

        } catch (Exception e) {

        } finally {
            assert iStream != null;
            iStream.close();
            assert urlConnection != null;
            urlConnection.disconnect();
        }

        return data;
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


                double limit = Double.parseDouble(editText_meters.getText().toString());
                if (shouldNowCalculate) {

                    if (m_listPosition.size() > 0) {

                        m_distance.clear();

                        for (int i = 0; i < m_listPosition.size(); i++) {

                            Location selected_location = new Location("locationA");
                            selected_location.setLatitude(lat);
                            selected_location.setLongitude(longi);
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

                            if (newdistance <= limit) {
                                m_distance.add(newdistance);
                                Locationbean locationbean = new Locationbean();
                                locationbean.setLatitude(String.valueOf(m_listPosition.get(i).latitude));
                                locationbean.setLongitude(String.valueOf(m_listPosition.get(i).longitude));
                                locationbean.setDistance(newdistance);
                                locationbean.setLocationName(String.valueOf(m_listName.get(i)));
                                locationbeanArrayList_below_75.add(locationbean);
                            }


                        }

                        if (m_distance.size() == 0) {
                            splashName = str_certified;

                        } else {

                            Collections.sort(locationbeanArrayList_below_75, new Comparator<Locationbean>() {
                                @Override
                                public int compare(Locationbean c1, Locationbean c2) {
                                    return Double.compare(c1.getDistance(), c2.getDistance());
                                }
                            });

                            String nearest_store_name = locationbeanArrayList_below_75.get(0).getLocationName();
                            // String nearest_store_name =
                            // "Publix Pharmacy at Village of Oriole Plaza : 7375 West Atlantic Avenue, Delray Beach";
                            // String nearest_store_name =
                            // "Walmart Neighborhood Market : 3155 South Federal Highway, Delray Beach";

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


                    Collections.sort(locationbeanArrayList_taken, new Comparator<Locationbean>() {
                        @Override
                        public int compare(Locationbean c1, Locationbean c2) {
                            return Double.compare(c1.getDistance(), c2.getDistance());
                        }
                    });

                    Collections.sort(locationbeanArrayList_ignored, new Comparator<Locationbean>() {
                        @Override
                        public int compare(Locationbean c1, Locationbean c2) {
                            return Double.compare(c1.getDistance(), c2.getDistance());
                        }
                    });
                    setupLocationView(locationbeanArrayList_below_75, linearLayout_below_75);
                    setupLocationView(locationbeanArrayList_taken, linearLayout_taken_container);
                    setupLocationView(locationbeanArrayList_ignored, linearLayout_ignore_container);

                    textView_near_place.setText("Nearest place :" + splashName);

                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }


        }

    }


    public void setupLocationView(ArrayList<Locationbean> locationbeanArrayList, LinearLayout linearLayout_container)

    {
        if (locationbeanArrayList == null) {
            linearLayout_container.removeAllViews();

        } else {
            if (locationbeanArrayList.size() == 0) {
                linearLayout_container.removeAllViews();

            } else {
                for (int i = 0; i < locationbeanArrayList.size(); i++) {

                    Locationbean locationbean = locationbeanArrayList.get(i);
                    View view = getLayoutInflater().inflate(R.layout.report_activity_dynamical, null);

                    TextView locationName = (TextView) view.findViewById(R.id.textview_locationName);
                    TextView locationDistance = (TextView) view.findViewById(R.id.textview_distance);
                    TextView locationlatitude = (TextView) view.findViewById(R.id.textview_latitude);
                    TextView locationlongitude = (TextView) view.findViewById(R.id.textview_longitude);
                    TextView locationlongitude_type = (TextView) view.findViewById(R.id.textview_type);
                    TextView locationlongitude_placeID = (TextView) view.findViewById(R.id.textview_placeID);
                    setTextviewValue(locationbean.getLocationName(), locationName);
                    setTextviewValue(String.valueOf(locationbean.getDistance()), locationDistance);
                    setTextviewValue(locationbean.getType(), locationlongitude_type);
                    setTextviewValue(locationbean.getPlaceIDFromGoolge(), locationlongitude_placeID);

                    if (locationbean.getLatitude() == null) {

                    } else {
                        locationlatitude.setText("Latitude :" + locationbean.getLatitude());
                    }
                    if (locationbean.getLongitude() == null) {

                    } else {
                        locationlongitude.setText("Longitude :" + locationbean.getLongitude());
                    }

                    linearLayout_container.addView(view);
                }
            }
        }

    }


    public void setTextviewValue(String s, TextView textView) {
        if (s == null || s.equals("")) {

        } else {
            textView.setText(String.valueOf(s));
        }
    }


}
