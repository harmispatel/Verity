package com.certified.verityscanningOne.Chat.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;


import com.certified.verityscanningOne.App;
import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.certified.verityscanningOne.video.utils.Consts;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;


public abstract class BaseActivity extends CoreBaseActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    public SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progressDialog;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    protected QBResRequestExecutor requestExecutor;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        requestExecutor = App.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("dummy_value", 0);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    protected abstract View getSnackbarAnchorView();

    protected Snackbar showErrorSnackbar(@StringRes int resId, Exception e,
                                         View.OnClickListener clickListener) {
        return ErrorUtils.showSnackbar(getSnackbarAnchorView(), resId, e,
                com.quickblox.sample.core.R.string.dlg_retry, clickListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

   public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }




    public void initDefaultActionBar() {
        String currentUserFullName = "";
        String currentRoomName = sharedPrefsHelper.get(Consts.PREF_CURREN_ROOM_NAME, "");

        if (sharedPrefsHelper.getQbUser() != null) {
            currentUserFullName = sharedPrefsHelper.getQbUser().getFullName();
        }

        setActionBarTitle(currentRoomName);
        setActionbarSubTitle(String.format(getString(R.string.subtitle_text_logged_in_as), currentUserFullName));
    }

    public void setActionbarSubTitle(String subTitle) {
        if (actionBar != null)
            actionBar.setSubtitle(subTitle);
    }

    public void removeActionbarSubTitle() {
        if (actionBar != null)
            actionBar.setSubtitle(null);
    }

  public   void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }

        progressDialog.setMessage(getString(messageId));

        progressDialog.show();

    }

}