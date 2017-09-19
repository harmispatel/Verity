package com.certified.verityscanningOne;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Chat.ui.activity.DialogsActivity;
import com.certified.verityscanningOne.Chat.utils.chat.ChatHelper;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogHolder;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.CommonUtils;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.beans.Parameterbean;
import com.certified.verityscanningOne.beans.Responcebean;
import com.certified.verityscanningOne.helper.DataHolder;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.UsersUtils;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;

public class SocialLoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //Gooogle sign in
    private static final int RC_SIGN_IN = 9001;
    // [END declare_auth_listener]
    // [START declare_auth]
    public static FirebaseAuth mAuth;
    public static GoogleApiClient mGoogleApiClient;
    public static SocialLoginActivity socialLoginActivity = null;
    String TAG = "SocialLoginActivity";
    CommonSession commonSession = null;
    View view_social_login = null;
    Bundle bundle_for_selected_social = new Bundle();
    boolean flagNeedToCall = false;
    Parameterbean parameterbean = new Parameterbean();
    String selected_social = null;
    EditText editText_email = null, editText_password = null;
    Button button_normal_login = null;
    ImageView imageView_google = null, imageView_facebook = null, imageView_twitter = null;
    TextView textView_forget_password = null, textView_register = null;
    // [END declare_auth]
    private CallbackManager mCallbackManager;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TwitterAuthClient twitterAuthClient;
    // [START define_variables]
    // [END define_variables]
    QBChatService chatService;

    Loginbean loginbean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            socialLoginActivity = this;
            //chat
            loginbean = new Loginbean();
            SubscribeService.unSubscribeFromPushes(SocialLoginActivity.this);
            ChatHelper.getInstance().destroy();
            QbDialogHolder.getInstance().clear();
            QBUsers.signOut();
            SharedPrefsHelper.getInstance().removeQbUser();
            //video
            CallService.logout(SocialLoginActivity.this);

            FacebookSdk.sdkInitialize(getApplicationContext());

            commonSession = new CommonSession(this);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret));
            Fabric.with(this, new Twitter(authConfig));
            twitterAuthClient = new TwitterAuthClient();

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseAnalytics.setUserProperty("Login", "Test");
            view_social_login = getLayoutInflater().inflate(R.layout.social_login_activity, null);

            editText_email = (EditText) view_social_login.findViewById(R.id.edittext_email);
            editText_password = (EditText) view_social_login.findViewById(R.id.edittext_password);
            textView_forget_password = (TextView) view_social_login.findViewById(R.id.textView_forget_password);
            textView_register = (TextView) view_social_login.findViewById(R.id.textView_not_registered_register_here);

            imageView_google = (ImageView) view_social_login.findViewById(R.id.imageview_google);
            imageView_facebook = (ImageView) view_social_login.findViewById(R.id.imageview_facebook);
            imageView_twitter = (ImageView) view_social_login.findViewById(R.id.imageview_twitter);

            button_normal_login = (Button) view_social_login.findViewById(R.id.button_login);


            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

            imageView_google.setOnClickListener(this);


//google
            // [START config_signin]
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail().requestIdToken("874376514949-h6ba1n3v8pa3pcu4j4bip6t5aecnr49b.apps.googleusercontent.com")

                    .build();
            // [END config_signin]

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

          /*  boolean autoLaunchDeepLink = true;
            AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
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
                                                        commonSession.setInviterReceviedRefereCode(receiver_refere_code);
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
                            });*/
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
                                if (CommonKeyword.FROM_SETTINGS) {
                                    CommonKeyword.FROM_SETTINGS = false;
                                } else {


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


                                    if (CommonMethods.isNetworkConnected(SocialLoginActivity.this)) {
                                        new SaveUpdateProfile().execute("");
                                    } else {
                                        mCommonMethods.mToast("No Internet Connection");
                                    }


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
                        bundle_for_selected_social.putString(Param.ITEM_ID, CommonKeyword.TAG_FACEBOOK_LOGIN_ANALYTICES);
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

            imageView_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LoginManager.getInstance().logInWithReadPermissions(SocialLoginActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));
                }
            });


            imageView_twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twitterAuthClient.authorize(SocialLoginActivity.this, new Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> twitterSessionResult) {
                            bundle_for_selected_social.putString(Param.ITEM_ID, CommonKeyword.TAG_TWITTER_LOGIN_ANALYTICES);
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


            frameLayout_container.addView(view_social_login);

            textView_register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(SocialLoginActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            textView_forget_password.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        showForgetPasswordDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



            button_normal_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {


                        String str_registration_email = editText_email.getText()
                                .toString().trim();

                        String str_pass = editText_password.getText().toString().trim();
                        if (str_registration_email == null
                                || str_registration_email.equalsIgnoreCase("")) {
                            mCommonMethods.mToast(getResources().getString(
                                    R.string.enter_register_email));

                        } else if (!emailValidator(str_registration_email)) {
                            mCommonMethods.mToast(getResources().getString(
                                    R.string.invalid_id));
                        } else if (str_pass == null
                                || str_pass.equalsIgnoreCase("")) {
                            mCommonMethods.mToast(getResources().getString(
                                    R.string.enter_register_password));

                        } else if (str_pass.length() < 6) {
                            mCommonMethods.mToast(getResources().getString(
                                    R.string.password_min_lenght));

                        } else {

                            parameterbean.setEmail(str_registration_email);
                            parameterbean.setPassword(str_pass);

                            showProgressDialog();

                            //authenticate user
                            mAuth.signInWithEmailAndPassword(parameterbean.getEmail(), parameterbean.getPassword())
                                    .addOnCompleteListener(SocialLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (task.isSuccessful()) {
                                                // there was an error

                                                String deviceTocken = commonSession.getDeviceTocken();
                                                if (deviceTocken == null) {
                                                    deviceTocken = "";
                                                }
                                                if (CommonMethods.isNetworkConnected(SocialLoginActivity.this)) {
                                                    new NormalLogin().execute();
                                                } else {
                                                    mCommonMethods.mToast("No Internet Connection");
                                                }

                                            } else {
                                                if (task.getException().getMessage() == null) {
                                                    showMessage(getResources().getString(R.string.authentication_failed));

                                                } else {
                                                    showMessage(task.getException().getMessage());
                                                }
                                                hideProgressDialog();

                                            }
                                        }
                                    });



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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

    private void signIn() {
        try {
            bundle_for_selected_social.putString(Param.ITEM_ID, CommonKeyword.TAG_GOOGLE_LOGIN_ANALYTICES);
            selected_social = CommonKeyword.GOOGLE;

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
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

                    Status status = result.getStatus();
                    String m = status.getStatusMessage();
                    String m1 = status.getStatusMessage();

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

    @VisibleForTesting

    // forgot password

    private void showForgetPasswordDialog() {
        try {
            final Dialog dialog = new Dialog(SocialLoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);

            dialog.setContentView(R.layout.forgot_password_screen);

            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);

            final EditText customEdittext_email = (EditText) dialog.findViewById(R.id.edittext_email);

            TextView customTextview_submit = (TextView) dialog.findViewById(R.id.textview_ok_button);
            TextView customTextview_cancel = (TextView) dialog.findViewById(R.id.textview_cancel_button);


            customTextview_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(customEdittext_email.getText().toString())) {

                        mCommonMethods.mToast(getResources().getString(
                                R.string.enter_register_email));
                    } else {
                        showProgressDialog();

                        mAuth.sendPasswordResetEmail(customEdittext_email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        try {
                                            if (task.isSuccessful()) {

                                                if (CommonMethods.isNetworkConnected(SocialLoginActivity.this)) {
                                                    new ForgetPassowrd(dialog).execute(customEdittext_email.getText().toString());
                                                } else {
                                                    mCommonMethods.mToast("No Internet Connection");
                                                }

                                            } else {
                                                Toast.makeText(SocialLoginActivity.this, getResources().getString(R.string.failed_to_send_reset_email), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Resources.NotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                    }

                }
            });
            customTextview_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            });

//The below code is EXTRA - to dim the parent view by 70%
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.7f;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dialog.getWindow().setAttributes(lp);
//Show the dialog
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                super.onPostExecute(loginbean);
                if (loginbean.isServiceSuccess()) {
                    final Gson gson = new Gson();
                    commonSession.storeLoggedUserID(loginbean.getUserID());
                    commonSession.setLoggedUserReferrerCode(loginbean.getReferalCode());
                    String UserEmail =  loginbean.getEmail();
                    if (CommonUtils.isTextAvailable(UserEmail)) {
                        commonSession.storeLoggedEmail(UserEmail);
                    }

                    String json = gson.toJson(loginbean);
                    commonSession.storeLoginContent(json);

                    QBUser qbUser = null;
                    StringifyArrayList<String> userTags = new StringifyArrayList<>();
                    userTags.add("Verity");
                    qbUser = new QBUser();
                    qbUser.setFullName(parameterbean.getEmail());
                    qbUser.setLogin(parameterbean.getEmail());
                    qbUser.setPassword("123456789");
                    qbUser.setTags(userTags);
                    loginToChat(qbUser);



                    bundle_for_selected_social.putString(Param.ITEM_NAME, loginbean.getName());
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

    public class NormalLogin extends AsyncTask<String, String, Loginbean> {

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
                loginbean = mCommonMethods.normalLogin(parameterbean);


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


                super.onPostExecute(loginbean);
                if (loginbean.isServiceSuccess()) {
                    final Gson gson = new Gson();
                    commonSession.storeLoggedUserID(loginbean.getUserID());
                    commonSession.setLoggedUserReferrerCode(loginbean.getReferalCode());


                    String UserEmail =  loginbean.getEmail();
                    if (CommonUtils.isTextAvailable(UserEmail)) {
                        commonSession.storeLoggedEmail(UserEmail);
                    }

                    String json = gson.toJson(loginbean);
                    commonSession.storeLoginContent(json);
                    bundle_for_selected_social.putString(Param.ITEM_NAME, loginbean.getName());


                    QBUser qbUser = null;
                    StringifyArrayList<String> userTags = new StringifyArrayList<>();
                    userTags.add("Verity");
                    qbUser = new QBUser();
                    qbUser.setFullName(parameterbean.getEmail());
                    qbUser.setLogin(parameterbean.getEmail());
                    qbUser.setPassword("123456789");
                    qbUser.setTags(userTags);
                    loginToChat(qbUser);


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





    public class ForgetPassowrd extends AsyncTask<String, String, Responcebean> {

        Dialog dialog;

        ForgetPassowrd(Dialog mdialog)

        {
            dialog = mdialog;
        }


        @Override
        protected Responcebean doInBackground(String... params) {
            // TODO Auto-generated method stub

            Responcebean responcebean = null;
            try {
                responcebean = mCommonMethods.forgetPassword(params[0]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return responcebean;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Responcebean responcebean) {
            // TODO Auto-generated method stub
            super.onPostExecute(responcebean);

            try {

                hideProgressDialog();

                if (responcebean.isServiceSuccess()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (responcebean.getErrorMessage() == null) {

                        mCommonMethods.mToast(getResources().getString(
                                R.string.password_sent_on_your_mail));
                    } else {
                        mCommonMethods.mToast(responcebean.getErrorMessage());
                    }

                    //
                } else {

                    if (responcebean.getErrorMessage() == null) {
                        mCommonMethods.mToast(getResources().getString(
                                R.string.forget_password_failed));
                    } else {
                        mCommonMethods.mToast(responcebean.getErrorMessage());
                    }

                }


               /**/

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mCommonMethods.mToast(e.getMessage());

            }

        }

    }

    private void startSignUpNewUser(final QBUser newUser) {
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {

                        loginToChat(result);
                    }
                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser,true);
                        } else {
                            Toaster.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData();
    }
    private QBUser createQBUserWithCurrentData() {
        QBUser qbUser = null;

        StringifyArrayList<String> userTags = new StringifyArrayList<>();
        userTags.add("Verity");
        qbUser = new QBUser();
        qbUser.setFullName(parameterbean.getName());
        qbUser.setLogin(parameterbean.getEmail());
        qbUser.setPassword("123456789");
        qbUser.setTags(userTags);

        return qbUser;
    }
    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                if (deleteCurrentUser) {
                    removeAllUserData(result);
                } else {
                    loginToChat(result);
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
                hideProgressDialog();
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {

                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }

    private void loginToChat(final QBUser user) {
        try {
            ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void result, Bundle bundle) {

                    try {

                        hideProgressDialog();
                        SharedPrefsHelper.getInstance().saveQbUser(user);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle_for_selected_social);
                        Intent intent = new Intent(SocialLoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(QBResponseException e) {

                    Log.e("tag","e="+e.getMessage());

                   // startSignUpNewUser(createUserWithEnteredData());

                    quickBloxRegister(parameterbean.getEmail(),
                            parameterbean.getName()+ " " +parameterbean.getName(),
                            parameterbean.getUserID(), true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void quickBloxRegister(final String email, final String name, final String externalID,
                                   final boolean isDialogDisplay) {

        try {
            StringifyArrayList<String> tagsArray = new StringifyArrayList<>();
            tagsArray.add("Verity");
            QBUser qbUser = new QBUser();
            qbUser.setLogin(email);
            qbUser.setFullName(name);
            qbUser.setExternalId(externalID);
            qbUser.setPassword("123456789");
            qbUser.setTags(tagsArray);

            QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    hideProgressDialog();

                    DataHolder.getInstance().addQbUser(qbUser);
                    DataHolder.getInstance().setSignInQbUser(qbUser);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle_for_selected_social);
                    Intent intent = new Intent(SocialLoginActivity.this, HomeActivity.class);
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

}
