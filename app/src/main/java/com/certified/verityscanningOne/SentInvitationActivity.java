package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by harmis on 18/10/16.
 */

public class SentInvitationActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_INVITE = 101;
    public static Activity activity = null;
    CommonSession commonSession = null;
    View view_invitation = null;
    TextView textView_referral_code = null, textView_share = null;
    ImageView imageView_share = null;
    CommonMethods commonMethods = null;
    private GoogleApiClient mGoogleApiClient;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    //12345CODE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            activity = this;
            commonSession = new CommonSession(this);
            commonMethods = new CommonMethods(this);
            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            textView_header_title.setVisibility(View.VISIBLE);
            textView_header_title.setText(getResources().getString(R.string.invite));
            imageView_back.setOnClickListener(new View.OnClickListener() {
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
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this, this)
                    .build();
            mGoogleApiClient.connect();
            findViewById();
            setUI();
        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void setUI() {
        // TODO Auto-generated method stub

        try {


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
            view_invitation = getLayoutInflater().inflate(R.layout.send_invitation_screen, null);
            textView_referral_code = (TextView) view_invitation.findViewById(R.id.textview_referral_code);
            textView_share = (TextView) view_invitation.findViewById(R.id.textview_share_content);
            imageView_share = (ImageView) view_invitation.findViewById(R.id.imageview_share);


            String loogedUserreferral_code = commonSession.getLoggedUserReferrerCode();
            if (loogedUserreferral_code != null) {
                textView_referral_code.setText(getResources().getString(R.string.ref) + loogedUserreferral_code);
            }
            textView_share.setOnClickListener(this);
            imageView_share.setOnClickListener(this);
            textView_referral_code.setOnClickListener(this);


            frameLayout_container.addView(view_invitation);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }


    private void onInviteClicked() {
        try {
            final Uri deepLink = buildDeepLink(0, false);

            Intent intent = new AppInviteInvitation.IntentBuilder(getResources().getString(R.string.invitation_title))
                    .setMessage(getResources().getString(R.string.invitation_message))
                    .setDeepLink(deepLink)
                    .setCustomImage(Uri.parse(getResources().getString(R.string.invitation_custom_image_t)))
                    .setCallToActionText(getResources().getString(R.string.invitation_cta)).setOtherPlatformsTargetApplication(AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS,
                            getResources().getString(R.string.ios_clientID))
                    .build();


            startActivityForResult(intent, REQUEST_INVITE);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

            if (requestCode == REQUEST_INVITE) {
                if (resultCode == RESULT_OK) {
                    // Get the invitation IDs of all sent messages
                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + id);
                    }
                } else {


                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + id);
                    }
                    // Sending failed or it was canceled, show failure message to the user
                    // [START_EXCLUDE]
                    //   showMessage(getString(R.string.send_failed));
                    // [END_EXCLUDE]
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @VisibleForTesting
    public Uri buildDeepLink(int minVersion, boolean isAd) {
      //  Uri.Builder builder = null;
        Uri myUri=null;
        try {
            // Get the unique appcode for this app.
            String appCode = getResources().getString(R.string.app_code);

            // Get this app's package name.
           // String packageName = getApplicationContext().getPackageName();

            String uristring="https://"+appCode+".app.goo.gl/?link=https://t.certified.bz/index.php&apn=com.certified.verityscanningOne&ibi=com.certified.verityscanningOne&isi=1124462403&ius=dynamicScheme&refCode="+commonSession.getLoggedUserReferrerCode();

             myUri = Uri.parse(uristring);



           /* // Build the link with all required parameters
            builder = new Uri.Builder()
                    .scheme("https")
                    .authority(appCode + ".app.goo.gl")
                    .path("/")
                    .appendQueryParameter("link", commonSession.getLoggedUserReferrerCode())
                    .appendQueryParameter("apn", packageName);

            // If the deep link is used in an advertisement, this value must be set to 1.
            if (isAd) {
                builder.appendQueryParameter("ad", "1");
            }

            // Minimum version is optional.
            if (minVersion > 0) {
                builder.appendQueryParameter("amv", Integer.toString(minVersion));
            }*/
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        // Return the completed deep link.
        return myUri;
    }


    @Override
    public void onClick(View v) {
        try {
            onInviteClicked();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {

        try {
            Toast.makeText(SentInvitationActivity.this,"onConnectionFailed Called",Toast.LENGTH_LONG).show();
            if (mResolvingError) {
                // Already attempting to resolve an error.
                return;

            } else if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    // There was an error with the resolution intent. Try again.
                    mGoogleApiClient.connect();
                }
            } else {
                // Show dialog using GoogleApiAvailability.getErrorDialog()
                showErrorDialog(result.getErrorCode());
                mResolvingError = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        try {
            // Create a fragment for the error dialog
            ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
            // Pass the error that should be displayed
            Bundle args = new Bundle();
            args.putInt(DIALOG_ERROR, errorCode);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "errordialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SentInvitationActivity) getActivity()).onDialogDismissed();
        }
    }
}