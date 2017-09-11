package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nakul Sheth on 01-09-2016.
 */
public class AboutusActivity extends BaseActivity {

    public static Activity activity = null;
    RelativeLayout relativeLayout_privacy_policy = null, relativeLayout_terms_and_condition = null, relativeLayout_location_summary = null;

    CommonSession hJoomAppCommonSession = null;
    String TAG = "AboutusActivity";
    View view_abouts_us = null;

    // Remote config code

    TextView txtTitle, txtCertifiedInc, txtAddress1, txtAddress2, txtMap, txtPhone, txtCertifiedBZ, txtDesc;
    TextView txtPolicy, txtTermsOfService;
    LinearLayout linearMainBG;
    //FirebaseRemoteConfig mFirebaseRemoteConfig;


    private static final String ABOUT_US_KEY = "aboutUsText";
    private static final String PRIVACY_POLICY_KEY = "privacyPolicyText";
    private static final String TERMS_OF_SERVICE_KEY = "termsofServiceText";

    private static final String ABOUT_US_CERTIFIED_IN_KEY = "aboutUsTxtViewCertified";
    private static final String ABOUT_US_CERTIFIED_ADDRESS1_KEY = "aboutUsTxtViewAddress1";
    private static final String ABOUT_US_CERTIFIED_ADDRESS2_KEY = "aboutUsTxtViewAddress2";
    private static final String ABOUT_US_CERTIFIED_MAP_KEY = "aboutUsTxtViewMap";
    private static final String ABOUT_US_CERTIFIED_PHONE_KEY = "aboutUsTxtViewPhone";
    private static final String ABOUT_US_CERTIFIED_BZ_KEY = "aboutUsTxtViewBz";
    private static final String ABOUT_US_CERTIFIED_DESC_KEY = "aboutUsTxtViewDesc";

    private static final String ABOUT_US_BACKGROUND_COLOR = "aboutUsBackgroundColor";
    private static final String ABOUT_US_TEXT_COLOR = "aboutUsTextColor";
    private static final String ABOUT_US_HEARDER_BG_COLOR = "aboutUsHeaderColor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
//            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                    .build();
//            mFirebaseRemoteConfig.setConfigSettings(configSettings);
//            mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_about);

            activity = this;
            hJoomAppCommonSession = new CommonSession(this);
            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            imageView_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

            }

            findViewById();
            setUI();

        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    private void setUI() {
        // TODO Auto-generated method stub
        try {

            relativeLayout_privacy_policy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AboutusActivity.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                }
            });

            relativeLayout_terms_and_condition.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AboutusActivity.this, TermsAndConditionActivity.class);
                    startActivity(intent);
                }
            });
            relativeLayout_location_summary.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AboutusActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_abouts_us = getLayoutInflater().inflate(R.layout.about_us, null);
            relativeLayout_privacy_policy = (RelativeLayout) view_abouts_us.findViewById(R.id.layout_privacy_policy);
            relativeLayout_terms_and_condition = (RelativeLayout) view_abouts_us.findViewById(R.id.layout_terms_of_service);
            relativeLayout_location_summary = (RelativeLayout) view_abouts_us.findViewById(R.id.layout_locations_summary);

            txtTitle = (TextView) view_abouts_us.findViewById(R.id.aboutUs_title);
            txtCertifiedInc = (TextView) view_abouts_us.findViewById(R.id.txtCertifiedInc);
            txtAddress1 = (TextView) view_abouts_us.findViewById(R.id.txtAddress1);
            txtAddress2 = (TextView) view_abouts_us.findViewById(R.id.txtAddress2);
            txtMap = (TextView) view_abouts_us.findViewById(R.id.txtAddressMap);
            txtPhone = (TextView) view_abouts_us.findViewById(R.id.txtPhone);
            txtCertifiedBZ = (TextView) view_abouts_us.findViewById(R.id.txtCertifiedBZ);
            txtDesc = (TextView) view_abouts_us.findViewById(R.id.txtDesc);
            linearMainBG = (LinearLayout) view_abouts_us.findViewById(R.id.aboutusManinBg);

            txtPolicy = (TextView) view_abouts_us.findViewById(R.id.txtPrivacyPolicy);
            txtTermsOfService = (TextView) view_abouts_us.findViewById(R.id.txtTermsOfService);

            frameLayout_container.addView(view_abouts_us);
            fetchWelcome();

            updateImage();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    /**
     * Fetch welcome message from server.
     */
    private void fetchWelcome() {
        // txtTitle.setText(mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));

        updateViews();

//        long cacheExpiration = 3600; // 1 hour in seconds.
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//
//                            // Once the config is successfully fetched it must be activated before newly fetched
//                            // values are returned.
//                            mFirebaseRemoteConfig.activateFetched();
//                            updateViews();
//
//                        } else {
//                        }
//                    }
//                });
        // [END fetch_config_with_callback]
    }

    private void updateViews() {

        try {
            String aboutUs = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_KEY);
            String privacyPolicy = HomeActivity.mFirebaseRemoteConfig.getString(PRIVACY_POLICY_KEY);
            String TermsOfService = HomeActivity.mFirebaseRemoteConfig.getString(TERMS_OF_SERVICE_KEY);
            txtTitle.setText(aboutUs);
            txtPolicy.setText(privacyPolicy);
            txtTermsOfService.setText(TermsOfService);

            String about_certifiedIn = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_IN_KEY);
            String about_address1 = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_ADDRESS1_KEY);
            String about_address2 = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_ADDRESS2_KEY);
            String about_map = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_MAP_KEY);
            String about_phone = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_PHONE_KEY);
            String about_certifiedBZ = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_BZ_KEY);
            String about_desc = HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_CERTIFIED_DESC_KEY);

            txtCertifiedInc.setText(about_certifiedIn);
            txtAddress1.setText(about_address1);
            txtAddress2.setText(about_address2);
            txtMap.setText(about_map);
            txtPhone.setText(about_phone);
            txtCertifiedBZ.setText(about_certifiedBZ);
            txtDesc.setText(about_desc);


            int about_bg_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_BACKGROUND_COLOR));
            int about_text_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_TEXT_COLOR));
            int header_bg_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_HEARDER_BG_COLOR));


            linearMainBG.setBackgroundColor(about_bg_color);

            txtTitle.setTextColor(about_text_color);
            txtPolicy.setTextColor(about_text_color);
            txtTermsOfService.setTextColor(about_text_color);
            txtCertifiedInc.setTextColor(about_text_color);
            txtAddress1.setTextColor(about_text_color);
            txtAddress2.setTextColor(about_text_color);
            txtMap.setTextColor(about_text_color);
            txtPhone.setTextColor(about_text_color);
            txtCertifiedBZ.setTextColor(about_text_color);
            txtDesc.setTextColor(about_text_color);

            headerBG.setBackgroundColor(header_bg_color);
            toolbar.setBackgroundColor(header_bg_color);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Fetch Images from server.
     */
    private void updateImage() {


        //gs://verity-ebd35.appspot.com/Verity/Common/back.png
        //gs://verity-ebd35.appspot.com/Verity/Common/settings.png
        StorageReference storageRef = HomeActivity.storage.getReferenceFromUrl("gs://verity-ebd35.appspot.com").child("Verity/Common/back@2x.png");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView_back.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }

    }


}
