package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.certified.verityscanningOne.Chat.utils.chat.ChatHelper;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogHolder;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.ConnectionDetector;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.video.services.CallService;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quickblox.chat.QBChatService;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nakul Sheth on 02-09-2016.
 */
public class NormalSettingActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    String TAG = "NormalSettingActivity";

    public static NormalSettingActivity normalSettingActivity;
    CommonMethods mCommonMethods = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    CommonSession mCommonSession;
    TextView textView_name = null, textView_username = null, textView_birthday = null, textView_mobile_number = null, textView_email = null;
    RelativeLayout relativeLayout_privacy_policy = null, relativeLayout_terms_and_condition = null;
    // used to store app title
    private CharSequence mTitle;
    View view_settings = null;
    public GoogleApiClient mGoogleApiClient;
    RelativeLayout relativeLayout_name_new = null, relativeLayout_birthdate_new = null, relativeLayout_mobile_number_new = null;
    public FirebaseAuth mAuth;

    Button btn_signOut;
    TextView txtTitleLabel, txtNameLabel, txtUserNameLabel, txtBirthdayLabel, txtMobileNUmberLabel, txtEmailLabel, txtPrivacyPolicyLabel, txtTermsOfServiceLabel;
    ScrollView scroll;

    private static final String SETTING_TITLE = "settingTxt";
    private static final String SETTING_SIGNOUT = "settingSignOut";

    private static final String SETTING_NAME = "settingTxtName";
    private static final String SETTING_BIRTHDAY = "settingTxtBirthday";
    private static final String SETTING_MOBILENUMBER = "settingTxtMobileNumber";
    private static final String SETTING_EMAIL = "settingTxtEmail";
    private static final String SETTING_PRIVACY_POLICY = "privacyPolicyText";
    private static final String SETTING_TERMSOFSERVICE = "termsofServiceText";

    private static final String ABOUT_US_BACKGROUND_COLOR = "aboutUsBackgroundColor";
    private static final String SETTING_TEXT_COLOR = "settingTxtColor";
    private static final String SETTING_SIGNOUT_TEXT_COLOR = "settingTxtSignColor";
    private static final String SETTING_SIGNOUT_BACKROUND_COLOR = "settingSignOutBackgColor";
    private static final String ABOUT_US_HEARDER_BG_COLOR = "aboutUsHeaderColor";

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            normalSettingActivity = this;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            FacebookSdk.sdkInitialize(getApplicationContext());
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret));
            Fabric.with(this, new Twitter(authConfig));
            //google
            // [START config_signin]
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("874376514949-h6ba1n3v8pa3pcu4j4bip6t5aecnr49b.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            // [END config_signin]
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]
            mCommonSession = new CommonSession(this);

            mCommonMethods = new CommonMethods(this);
            cd = new ConnectionDetector(getApplicationContext());

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
            findViewByIDs();
            setUI();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }


    private void findViewByIDs() {
        // TODO Auto-generated method stub

        try {
            view_settings = getLayoutInflater().inflate(R.layout.normal_setting_screen, null);

            relativeLayout_privacy_policy = (RelativeLayout) view_settings.findViewById(R.id.layout_privacy_policy);
            relativeLayout_terms_and_condition = (RelativeLayout) view_settings.findViewById(R.id.layout_terms_of_service);

            btn_signOut = (Button) view_settings.
                    findViewById(R.id.button_signout);

            textView_name = (TextView) view_settings.findViewById(R.id.textview_name);
            textView_username = (TextView) view_settings.findViewById(R.id.textview_username);
            textView_birthday = (TextView) view_settings.findViewById(R.id.textview_birthday);
            textView_mobile_number = (TextView) view_settings.findViewById(R.id.textview_mobile_number);
            textView_email = (TextView) view_settings.findViewById(R.id.textview_email);

            txtTitleLabel = (TextView) view_settings.findViewById(R.id.setting_title);
            txtNameLabel = (TextView) view_settings.findViewById(R.id.setting_nameLabel);
            txtUserNameLabel = (TextView) view_settings.findViewById(R.id.setting_userNameLabel);
            txtBirthdayLabel = (TextView) view_settings.findViewById(R.id.setting_BirthDayLabel);
            txtMobileNUmberLabel = (TextView) view_settings.findViewById(R.id.setting_MobileNumberLabel);
            txtEmailLabel = (TextView) view_settings.findViewById(R.id.setting_emailLabel);
            txtPrivacyPolicyLabel = (TextView) view_settings.findViewById(R.id.setting_PrivacyPolicyLabel);
            txtTermsOfServiceLabel = (TextView) view_settings.findViewById(R.id.setting_TermsOfServiceLabel);

            scroll = (ScrollView) view_settings.findViewById(R.id.settingMainBg);

            relativeLayout_name_new = (RelativeLayout) view_settings.findViewById(R.id.relativelayout_name_new);
            relativeLayout_birthdate_new = (RelativeLayout) view_settings.findViewById(R.id.layout_birthdate_new);
            relativeLayout_mobile_number_new = (RelativeLayout) view_settings.findViewById(R.id.layout_mobilenumber_new);

            final Gson gson = new Gson();

            Type collectionType = new TypeToken<Loginbean>() {
            }.getType();
            String json = mCommonSession.getLoginContent();
            Loginbean loginbean = gson.fromJson(json, collectionType);
            if (loginbean != null) {
                checkExist(textView_name, loginbean.getName());
                //checkExist(textView_username, loginbean.getUsername());
                checkExist(textView_birthday, loginbean.getBirthday());
                checkExist(textView_mobile_number, loginbean.getMobileNumber());
                checkExist(textView_email, loginbean.getEmail());

            }
            frameLayout_container.addView(view_settings);

            updateViews();
            updateImage();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    private void setUI() {
        // TODO Auto-generated method stub

        try {
            relativeLayout_name_new.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(NormalSettingActivity.this, UpdateProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "1");
                        bundle.putString("value", textView_name.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            relativeLayout_birthdate_new.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(NormalSettingActivity.this, UpdateProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "2");
                        bundle.putString("value", textView_birthday.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            relativeLayout_mobile_number_new.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(NormalSettingActivity.this, UpdateProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "3");
                        bundle.putString("value", textView_mobile_number.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            relativeLayout_terms_and_condition.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NormalSettingActivity.this, TermsAndConditionActivity.class);
                    startActivity(intent);
                }
            });
            relativeLayout_privacy_policy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NormalSettingActivity.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                }
            });
            btn_signOut.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {

                        showdialogforLogout();

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    public void checkExist(TextView textView, String value) {
        try {
            if (value == null || value.isEmpty() || value.equals("") || value.equals("null")) {

            } else {
                textView.setText(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public void showdialogforLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.logout_message))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                mCommonSession.resetLoggedUserID();
                                mCommonSession.resetLoggedUserFName();
                                mCommonSession.resetScanPosition();
                                if (HomeActivity.activity != null) {
                                    HomeActivity.activity.finish();
                                }
                                if (AboutusActivity.activity != null) {
                                    AboutusActivity.activity.finish();
                                }


                                LoginManager.getInstance().logOut();
                                Twitter.logOut();
                                QBChatService.getInstance().destroy();
                                FirebaseAuth.getInstance().signOut();
                                CommonKeyword.FROM_SETTINGS = true;

                                //chat
                                SharedPrefsHelper.getInstance().removeQbUser();
                                ChatHelper.getInstance().destroy();
                                SubscribeService.unSubscribeFromPushes(NormalSettingActivity.this);
                                QbDialogHolder.getInstance().clear();
                                QBUsers.signOut();

                                //video
                                CallService.logout(NormalSettingActivity.this);
                                Intent intent = new Intent(NormalSettingActivity.this, SocialLoginActivity.class);
                                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                                CommonMethods.printFirebaseLogcat(TAG, e, null);

                            }


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //  Remote config


    private void updateViews() {

        try {

            String title = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_TITLE);
            String signOut = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_SIGNOUT);

            String strName = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_NAME);
            String strBirthDay = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_BIRTHDAY);
            String strMobileNumber = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_MOBILENUMBER);
            String strEmail = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_EMAIL);
            String strPrivacyPolicy = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_PRIVACY_POLICY);
            String strTermsOfService = HomeActivity.mFirebaseRemoteConfig.getString(SETTING_TERMSOFSERVICE);

            txtTitleLabel.setText(title);
            txtNameLabel.setText(strName);
            txtBirthdayLabel.setText(strBirthDay);
            txtMobileNUmberLabel.setText(strMobileNumber);
            txtEmailLabel.setText(strEmail);
            txtPrivacyPolicyLabel.setText(strPrivacyPolicy);
            txtTermsOfServiceLabel.setText(strTermsOfService);
            btn_signOut.setText(signOut);

            int setting_bg_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_BACKGROUND_COLOR));
            int setting_text_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(SETTING_TEXT_COLOR));
            int signOutBgColor = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(SETTING_SIGNOUT_BACKROUND_COLOR));
            int signOut_text_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(SETTING_SIGNOUT_TEXT_COLOR));
            int header_bg_color = Color.parseColor(HomeActivity.mFirebaseRemoteConfig.getString(ABOUT_US_HEARDER_BG_COLOR));

            txtTitleLabel.setTextColor(setting_text_color);
            txtNameLabel.setTextColor(setting_text_color);
            txtBirthdayLabel.setTextColor(setting_text_color);
            txtMobileNUmberLabel.setTextColor(setting_text_color);
            txtEmailLabel.setTextColor(setting_text_color);
            txtPrivacyPolicyLabel.setTextColor(setting_text_color);
            txtTermsOfServiceLabel.setTextColor(setting_text_color);

            btn_signOut.setTextColor(signOut_text_color);
           // btn_signOut.setBackgroundColor(signOutBgColor);
            scroll.setBackgroundColor(setting_bg_color);

            headerBG.setBackgroundColor(header_bg_color);
            toolbar.setBackgroundColor(header_bg_color);

            btn_signOut.setBackgroundResource(R.drawable.tags_rounded_corners);

            GradientDrawable drawable = (GradientDrawable) btn_signOut.getBackground();
            drawable.setColor(signOutBgColor);


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
