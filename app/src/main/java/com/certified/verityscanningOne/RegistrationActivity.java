package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.CommonUtils;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.beans.Parameterbean;
import com.certified.verityscanningOne.beans.User;
import com.certified.verityscanningOne.helper.DataHolder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;


public class RegistrationActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    Button btn_proceed;

    EditText edt_registration_name,
            edt_registration_email, edt_mobilenumber, edt_pass,
            edt_com_pass, editText_receiver_referral_code = null;
    CommonMethods commonMethods = null;
    TextView textView_birthdate = null;
    ;
    Parameterbean parameterbean = new Parameterbean();

    public static Activity myactivity = null;
    View view_register = null;
    CommonSession commonSession = null;
    Bundle bundle_for_selected_social = new Bundle();
    ImageView imageView_google = null, imageView_facebook = null, imageView_twitter = null;
    private static final int RC_SIGN_IN = 9001;
    View view_social_login = null;
    // [END declare_auth]
    private CallbackManager mCallbackManager;
    // [END declare_auth_listener]
    // [START declare_auth]
    public static FirebaseAuth mAuth;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static GoogleApiClient mGoogleApiClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TwitterAuthClient twitterAuthClient;
    String selected_social = null;
    boolean flagNeedToCall = false;
    private DatabaseReference mDatabase;

    QBUser user = new QBUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.register);

        try {

            myactivity = RegistrationActivity.this;
            FacebookSdk.sdkInitialize(getApplicationContext());
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret));
            Fabric.with(this, new Twitter(authConfig));
            twitterAuthClient = new TwitterAuthClient();
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            commonMethods = new CommonMethods(RegistrationActivity.this);
            commonSession = new CommonSession(RegistrationActivity.this);
            view_register = getLayoutInflater().inflate(R.layout.register, null);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mDatabase = FirebaseDatabase.getInstance().getReference();

            if (commonSession.getInviterReceviedRefereCode() != null) {
                parameterbean.setReceiverReferralCode(commonSession.getInviterReceviedRefereCode());
            }
            mFirebaseAnalytics.setUserProperty("Login", "Test");

            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            findviewbyID();
            frameLayout_container.addView(view_register);
            imageView_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setUI();
            setupSocial();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setupSocial() {
        try {
            imageView_google.setOnClickListener(this);


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

            // [START auth_state_listener]
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    try {
                        FirebaseUser user = firebaseAuth.getCurrentUser();


                        if (user != null) {
                            // User is signed in

                            if (flagNeedToCall) {
                                // Even a user's provider-specific profile information
                                // only reveals basic information
                                flagNeedToCall = false;


                                // Even a user's provider-specific profile information
                                // only reveals basic information

                                parameterbean.setName(user.getDisplayName());
                                parameterbean.setPassword("");

                                if (selected_social.equals(CommonKeyword.TWITTER)) {
                                    parameterbean.setEmail(user.getDisplayName() + "@gmail.com");

                                } else {
                                    parameterbean.setEmail(user.getEmail());

                                }


                                parameterbean.setBirthDay("");
                                parameterbean.setMobileNumber("");
                                parameterbean.setSocialType(selected_social);
                                parameterbean.setIsSocial("YES");
                                parameterbean.setFirebaseId(user.getUid());
                                parameterbean.setProfileImage(user.getPhotoUrl().toString());


                                if (CommonMethods.isNetworkConnected(RegistrationActivity.this)) {
                                    new SaveUpdateProfile().execute("");

                                } else {
                                    mCommonMethods.mToast("No Internet Connection");
                                }


                            } else {

                            }


                        } else {
                            // User is signed out
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonMethods.printFirebaseLogcat(TAG, e, null);


                    }
                    // [START_EXCLUDE]
                    // updateUI(user);
                    // [END_EXCLUDE]
                }
            };
            // [END auth_state_listener]


            // [START initialize_fblogin]
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    try {
                        selected_social = CommonKeyword.FACEBOOK;
                        bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_ID, CommonKeyword.TAG_FACEBOOK_LOGIN_ANALYTICES);
                        showProgressDialog();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonMethods.printFirebaseLogcat(TAG, e, null);

                    }


                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // [START_EXCLUDE]
                    //  updateUI(null);
                    // [END_EXCLUDE]
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // [START_EXCLUDE]
                    //  updateUI(null);
                    // [END_EXCLUDE]
                }
            });
            // [END initialize_fblogin]

            imageView_facebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));
                }
            });


            imageView_twitter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    twitterAuthClient.authorize(RegistrationActivity.this, new Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> twitterSessionResult) {
                            bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_ID, CommonKeyword.TAG_TWITTER_LOGIN_ANALYTICES);
                            selected_social = CommonKeyword.TWITTER;
                            showProgressDialog();

                            handleTwitterSession(twitterSessionResult.data);
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Log.w(TAG, "twitterLogin:failure", e);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUI() {
        try {

            textView_birthdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    try {


                                        int month = monthOfYear + 1;


                                        String _Date = year + "-" + month + "-" + dayOfMonth;


                                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat fmt2 = new SimpleDateFormat("MM-dd-yyyy");

                                        Date date = null;
                                        try {
                                            date = fmt.parse(_Date);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }


                                        textView_birthdate.setText(fmt2.format(date));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            });

            imageView_back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    finish();
                }
            });


            btn_proceed.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {
                        String str_registration_fname = edt_registration_name.getText()
                                .toString().trim();
                        String str_registration_birthday = textView_birthdate
                                .getText().toString().trim();
                        String str_registration_mobilenumber = edt_mobilenumber
                                .getText().toString().trim();
                        String str_registration_email = edt_registration_email.getText()
                                .toString().trim();

                        String str_pass = edt_pass.getText().toString().trim();
                        String str_com_pass = edt_com_pass.getText().toString().trim();

                        if (str_registration_fname == null
                                || str_registration_fname.equalsIgnoreCase("")) {

                            commonMethods.mToast(getResources().getString(
                                    R.string.enter_register_name));

                        } else if (str_registration_birthday == null
                                || str_registration_birthday.equalsIgnoreCase(getResources().getString(R.string.set_birthday))) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.set_birthday));

                        } else if (str_registration_mobilenumber == null
                                || str_registration_mobilenumber
                                .equalsIgnoreCase("")) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.enter_mobile_number));

                        } else if (str_registration_email == null
                                || str_registration_email.equalsIgnoreCase("")) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.enter_register_email));

                        } else if (!emailValidator(str_registration_email)) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.invalid_id));
                        } else if (str_pass == null
                                || str_pass.equalsIgnoreCase("")) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.enter_register_password));

                        } else if (str_pass.length() < 6) {
                            mCommonMethods.mToast(getResources().getString(
                                    R.string.password_min_lenght));

                        } else if (str_com_pass == null
                                || str_com_pass.equalsIgnoreCase("")) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.enter_register_confirm_password));

                        } else if (!str_pass.equalsIgnoreCase(str_com_pass)) {
                            commonMethods.mToast(getResources().getString(
                                    R.string.do_not_match_pass_confirmpass));

                        } else {

                            // get Internet status
                            isInternetPresent = cd.isConnectingToInternet();

                            // check for Internet status
                            if (isInternetPresent) {
                                // Internet Connection is Present

                                parameterbean.setName(edt_registration_name.getText().toString());
                                parameterbean.setBirthDay(textView_birthdate.getText().toString());
                                parameterbean.setMobileNumber(edt_mobilenumber.getText().toString());
                                parameterbean.setEmail(edt_registration_email.getText().toString());
                                parameterbean.setPassword(edt_pass.getText().toString());
                                parameterbean.setConfirmPassword(edt_com_pass.getText().toString());

                                parameterbean.setIsSocial("0");
                                showProgressDialog();
                                mAuth.createUserWithEmailAndPassword(parameterbean.getEmail(), parameterbean.getPassword())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                hideProgressDialog();

                                                if (task.isSuccessful()) {
                                                    // Write new user
                                                    onAuthSuccess(task.getResult().getUser());

                                                    Log.e("Tga","email="+parameterbean.getEmail());
                                                    Log.e("Tga","getFirstName="+parameterbean.getName());
                                                    Log.e("Tga","getLastName="+parameterbean.getName());
                                                    Log.e("Tga","getUserId="+parameterbean.getUserID());
                                                    quickBloxRegister(parameterbean.getEmail(),
                                                            parameterbean.getName() + " " +parameterbean.getName(),
                                                            parameterbean.getUserID(), true);




                                                } else {
                                                    String message = task.getException().getMessage();
                                                    if (message != null) {

                                                        showMessage(message);
                                                    } else {

                                                        showMessage(getResources().getString(R.string.signup_failed));

                                                    }


                                                    hideProgressDialog();
                                                }
                                            }
                                        });


                            } else {
                                // Internet connection is not present

                                commonMethods.mToast(getResources().getString(
                                        R.string.no_internet_connection));

                            }
                        }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        if (CommonMethods.isNetworkConnected(RegistrationActivity.this)) {
            new RegisterUser().execute("");
        } else {
            mCommonMethods.mToast("No Internet Connection");
        }


    }
    private void quickBloxRegister(final String email, final String name, final String externalID,
                                   final boolean isDialogDisplay) {


        try {
            StringifyArrayList<String> tagsArray = new StringifyArrayList<>();
            tagsArray.add("Verity");
            QBSettings.getInstance().init(getApplicationContext(), "60556", "wHFNW3rTDVDAHbv", "zDw33GJcs2fGKm");
            QBSettings.getInstance().setAccountKey("iaKpK7oKiNgtwtCs2zsc");
            QBUser qbUser = new QBUser();
            qbUser.setLogin(email);
            qbUser.setFullName(name);
            qbUser.setExternalId(externalID);
            qbUser.setPassword("123456789");
            qbUser.setTags(tagsArray);

            QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {

                    DataHolder.getInstance().addQbUser(qbUser);
                    DataHolder.getInstance().setSignInQbUser(qbUser);



                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onError(QBResponseException error) {

                    Log.e("QB register error==>", error.getMessage().toString());

                    if (!isDialogDisplay) {


                    } else {
                        quickBloxRegister(email, name, externalID,  false);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        try {
            User user = new User(name, email);

            mDatabase.child("users").child(userId).setValue(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void signIn() {
        try {
            bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_ID, CommonKeyword.TAG_GOOGLE_LOGIN_ANALYTICES);
            selected_social = CommonKeyword.GOOGLE;

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public boolean emailValidator(String email) {
        Matcher matcher = null;
        try {
            Pattern pattern;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matcher.matches();
    }

    private void findviewbyID() {
        // TODO Auto-generated method stub

        try {


            // linear_main_ll.getBackground().setAlpha(120);
            imageView_google = (ImageView) view_register.findViewById(R.id.imageview_google);
            imageView_facebook = (ImageView) view_register.findViewById(R.id.imageview_facebook);
            imageView_twitter = (ImageView) view_register.findViewById(R.id.imageview_twitter);
            editText_receiver_referral_code = (EditText) view_register.findViewById(R.id.edittext_referral_code);
            if (commonSession.getInviterReceviedRefereCode() != null) {
                editText_receiver_referral_code.setText(commonSession.getInviterReceviedRefereCode());
            }
            btn_proceed = (Button) view_register.findViewById(R.id.button_register);

            edt_registration_name = (EditText) view_register.findViewById(R.id.register_name_edittext);
            textView_birthdate = (TextView) view_register.findViewById(R.id.register_birthday_textview);
            edt_mobilenumber = (EditText) view_register.findViewById(R.id.register_mobilenumber_edittext);
            edt_registration_email = (EditText) view_register.findViewById(R.id.register_email_edittext);

            edt_pass = (EditText) view_register.findViewById(R.id.register_passsword_edittext);
            edt_com_pass = (EditText) view_register.findViewById(R.id.register_re_passsword_edittext);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public class RegisterUser extends AsyncTask<String, String, Loginbean> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
        }

        @Override
        protected Loginbean doInBackground(String... params) {
            Loginbean loginbean = null;
            // TODO Auto-generated method stub
            try {

                parameterbean.setGetloginDetails(true);
                if (commonSession.getInviterReceviedRefereCode() != null) {
                    parameterbean.setReceiverReferralCode(commonSession.getInviterReceviedRefereCode());

                }
                loginbean = mCommonMethods.getLoginAndUpdateLogin(parameterbean);


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

            return loginbean;
        }

        @Override
        protected void onPostExecute(Loginbean loginbean) {
            // TODO Auto-generated method stub
            super.onPostExecute(loginbean);
            try {
                hideProgressDialog();

                super.onPostExecute(loginbean);
                if (loginbean.isServiceSuccess()) {
                    final Gson gson = new Gson();
                    commonSession.storeLoggedUserID(loginbean.getUserID());
                    commonSession.setLoggedUserReferrerCode(loginbean.getReferalCode());


                    String json = gson.toJson(loginbean);
                    commonSession.storeLoginContent(json);
                    if (SocialLoginActivity.socialLoginActivity != null) {
                        SocialLoginActivity.socialLoginActivity.finish();
                    }
                    finish();
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(intent);

                    bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_NAME, loginbean.getName());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle_for_selected_social);
                } else {

                    if (loginbean.getErrorMessage() != null) {
                        showMessage(loginbean.getErrorMessage());
                    } else {
                        showMessage(getResources().getString(R.string.authentication_failed));

                    }

                }


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }

    public void showMessage(final String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }
                    })
            ;
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.imageview_google) {
                signIn();
            }
        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        try {
            mAuth.addAuthStateListener(mAuthListener);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed, update UI appropriately
                    // [START_EXCLUDE]
                    // updateUI(null);
                    // [END_EXCLUDE]
                }
            } else {
                // Pass the activity result to the Twitter login button.
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                twitterAuthClient.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        try {
            flagNeedToCall = true;
            CommonKeyword.FROM_SETTINGS = false;

            showProgressDialog();

            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
            // [START_EXCLUDE silent]
            //showProgressDialog();
            // [END_EXCLUDE]

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            CommonKeyword.FROM_SETTINGS = false;

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                hideProgressDialog();
                                showMessage(task.getException().getMessage());
                            }
                            // [START_EXCLUDE]
                            // hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END auth_with_google]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        try {
            flagNeedToCall = true;
            CommonKeyword.FROM_SETTINGS = false;

            Log.d(TAG, "handleFacebookAccessToken:" + token);
            // [START_EXCLUDE silent]

            // [END_EXCLUDE]

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                CommonKeyword.FROM_SETTINGS = false;

                                Log.w(TAG, "signInWithCredential", task.getException());

                                hideProgressDialog();
                                showMessage(task.getException().getMessage());
                            }

                            // [START_EXCLUDE]
                            //hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END auth_with_facebook]

    // [START auth_with_twitter]
    private void handleTwitterSession(TwitterSession session) {
        try {
            flagNeedToCall = true;
            CommonKeyword.FROM_SETTINGS = false;

            // [START_EXCLUDE silent]
            //  showProgressDialog();
            // [END_EXCLUDE]

            AuthCredential credential = TwitterAuthProvider.getCredential(
                    session.getAuthToken().token,
                    session.getAuthToken().secret);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            CommonKeyword.FROM_SETTINGS = false;

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                hideProgressDialog();
                                showMessage(task.getException().getMessage());
                            }

                            // [START_EXCLUDE]
                            // hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
    // [END auth_with_twitter]

    private void signOut() {
        try {
            mAuth.signOut();
            Twitter.logOut();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

        //updateUI(null);
    }

    public class SaveUpdateProfile extends AsyncTask<String, String, Loginbean> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                showProgressDialog();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
        }

        @Override
        protected Loginbean doInBackground(String... params) {
            Loginbean loginbean = null;
            // TODO Auto-generated method stub
            try {

                parameterbean.setGetloginDetails(true);
                loginbean = mCommonMethods.getLoginAndUpdateLogin(parameterbean);


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

            return loginbean;
        }

        @Override
        protected void onPostExecute(Loginbean loginbean) {
            // TODO Auto-generated method stub
            super.onPostExecute(loginbean);
            try {
                hideProgressDialog();

                if (loginbean.isServiceSuccess()) {
                    final Gson gson = new Gson();
                    commonSession.storeLoggedUserID(loginbean.getUserID());
                    commonSession.setLoggedUserReferrerCode(loginbean.getReferalCode());


                    String json = gson.toJson(loginbean);
                    commonSession.storeLoginContent(json);

                    String deviceTocken = commonSession.getDeviceTocken();
                    if (deviceTocken == null) {
                        deviceTocken = "";
                    }
                    bundle_for_selected_social.putString(FirebaseAnalytics.Param.ITEM_NAME, loginbean.getName());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle_for_selected_social);


                    Log.e("Tga","email="+loginbean.getEmail());
                    Log.e("Tga","getFirstName="+loginbean.getName());
                    Log.e("Tga","getLastName="+loginbean.getName());
                    Log.e("Tga","getUserId="+loginbean.getUserID());
                    quickBloxRegister(loginbean.getEmail(),
                            loginbean.getName() + " " +loginbean.getName(),
                            loginbean.getUserID(), true);




                } else {

                    if (loginbean.getErrorMessage() != null) {
                        showMessage(loginbean.getErrorMessage());
                    } else {
                        showMessage(getResources().getString(R.string.authentication_failed));

                    }

                }


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }
}
