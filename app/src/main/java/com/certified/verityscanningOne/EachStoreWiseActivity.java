package com.certified.verityscanningOne;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.certified.verityscanningOne.Utils.BaseActivityLocation;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.beans.Locationbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by harmis on 26/9/16.
 */
public class EachStoreWiseActivity extends BaseActivityLocation {

    public static HomeActivity activity = null;

    CommonSession hJoomAppCommonSession = null;

    String TAG = "GifPlayActivity";


    public static String str_certified = "certified";
    public static String str_publix = "publix";
    public static String str_wawa = "wawa";
    public static String str_trader_joes = "trader Joe's";
    public static String str_walmart = "walmart";
    public static String str_whole_foods = "whole foods";
    public static String str_the_fresh_market = "the fresh market";
    public static String str_kroger = "kroger";
    public static String str_costco = "costco";
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


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    CommonSession mCommonSession;
    int radius = 75;
    public static boolean a = false;
    TextView textView_near_place = null;
    EditText editText_meters = null, editText_latitude = null, editText_longitude = null;
    LinearLayout linearLayout_taken_container = null, linearLayout_ignore_container = null, linearLayout_below_75;
    Button button_check = null, button_each_store_wise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.each_store_main);

            hJoomAppCommonSession = new CommonSession(this);


            findViewById();
            SetUI();


        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }

    @Override
    public void locationFetched(Location mLocal, Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {


            editText_meters = (EditText) findViewById(R.id.editText_meters);
            editText_latitude = (EditText) findViewById(R.id.editText_latitude);
            editText_longitude = (EditText) findViewById(R.id.editText_current_longitude);


            if (ReportActivity.editText_meters != null) {
                editText_meters.setText(ReportActivity.editText_meters.getText().toString());
            }

            if (ReportActivity.editText_latitude != null) {
                editText_latitude.setText(ReportActivity.editText_latitude.getText().toString());
            }
            if (ReportActivity.editText_latitude != null) {
                editText_longitude.setText(ReportActivity.editText_longitude.getText().toString());
            }


            linearLayout_taken_container = (LinearLayout) findViewById(R.id.layout_dynamical_container_taken);


        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }


    private void SetUI() {
        // TODO Auto-generated method stub

        try {

            if (ReportActivity.stringArrayListHashMap_LocationWise != null) {
                if (ReportActivity.stringArrayListHashMap_LocationWise.size() != 0) {
                    for (Map.Entry<String, ArrayList<Locationbean>> entry : ReportActivity.stringArrayListHashMap_LocationWise.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());

                        View view = getLayoutInflater().inflate(R.layout.each_store_dynamical, null);
                        TextView textView_placename = (TextView) view.findViewById(R.id.textview_place_name);

                        textView_placename.setText(entry.getKey());


                        LinearLayout linearLayout_dynamical = (LinearLayout) view.findViewById(R.id.layout_dynamical);
                        ArrayList<Locationbean> locationbeanArrayList = entry.getValue();

                        Collections.sort(locationbeanArrayList, new Comparator<Locationbean>() {
                            @Override
                            public int compare(Locationbean c1, Locationbean c2) {
                                return Double.compare(c1.getDistance(), c2.getDistance());
                            }
                        });
                        setupLocationView(locationbeanArrayList, linearLayout_dynamical);
                        linearLayout_taken_container.addView(view);
                    }
                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

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
