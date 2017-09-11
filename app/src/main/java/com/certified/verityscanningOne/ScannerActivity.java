package com.certified.verityscanningOne;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.barcodescanner.core.ApplicationPrefs;
import com.certified.verityscanningOne.barcodescanner.core.IViewFinder;
import com.certified.verityscanningOne.barcodescanner.core.ViewFinderView;
import com.certified.verityscanningOne.barcodescanner.scan.AppConstants;
import com.certified.verityscanningOne.barcodescanner.scan.BarcodeFormat;
import com.certified.verityscanningOne.barcodescanner.scan.CameraSelectorDialogFragment;
import com.certified.verityscanningOne.barcodescanner.scan.FormatSelectorDialogFragment;
import com.certified.verityscanningOne.barcodescanner.scan.MessageDialogFragment;
import com.certified.verityscanningOne.barcodescanner.scan.Result;
import com.certified.verityscanningOne.barcodescanner.scan.ZBarScannerView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


public class ScannerActivity extends Activity implements
        MessageDialogFragment.MessageDialogListener,
        ZBarScannerView.ResultHandler,
        FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    FrameLayout frameContainer;
    Button btn_scan, button_flash, sattingButton, button_ref;
    ApplicationPrefs appPraference;
    ImageView actionBarBack;
    SwitchCompat toogleButton;
    TextView switchStatus;
    MediaPlayer mp;
    public Boolean scanFlag = false;
    static ScannerActivity This = null;
    private IViewFinder mViewFinderView;

    public static Activity myActivity = null;

    RelativeLayout rootLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    @SuppressWarnings("deprecation")
    public ScannerActivity() {
        This = this;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        myActivity = this;

        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }


        setupFormats();
        setContentView(R.layout.activity_main123);
        frameContainer = (FrameLayout) findViewById(R.id.frameContainer);
        mScannerView = new ZBarScannerView(this);
        frameContainer.addView(mScannerView);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btn_scan = (Button) findViewById(R.id.btn_scan);
        appPraference = ApplicationPrefs.getInstance(getApplicationContext());
        actionBarBack = (ImageView) findViewById(R.id.actionBarBack);
        button_ref = (Button) findViewById(R.id.button_ref);
        button_flash = (Button) findViewById(R.id.button_flash);
        toogleButton = (SwitchCompat) findViewById(R.id.toogleButton);
        switchStatus = (TextView) findViewById(R.id.switchStatus);


        toogleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus.setText(R.string.ocr_scan);

                } else {
                    switchStatus.setText((R.string.ucr_scan));

                    toogleButton.setChecked(true);
                    switchStatus.setText(R.string.ocr_scan);

                    Intent intent = new Intent(getApplicationContext(), OCRScanActivity.class);
                    startActivity(intent);

                }
            }
        });


        button_ref.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onResume();
            }
        });

        button_flash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mFlash = !mFlash;
                if (mFlash) {
                    // item.setTitle(R.string.flash_on);
                } else {
                    // item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
            }
        });

        sattingButton = (Button) findViewById(R.id.sattingButton);
        sattingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        SattingActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        actionBarBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });

        btn_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scanConfig();
            }
        });
    }

    public void setupLayout() {

        mViewFinderView = createViewFinderView(getApplicationContext());
        if (mViewFinderView instanceof View) {
            mScannerView.addView((View) mViewFinderView);
        } else {
            throw new IllegalArgumentException(
                    "IViewFinder object returned by "
                            + "'createViewFinderView()' should be instance of android.view.View");
        }
    }

    protected IViewFinder createViewFinderView(Context context) {
        return new ViewFinderView(context);
    }

    private void addLoadingView() {
        // TODO Auto-generated method stub
        rootLayout = new RelativeLayout(this);
        rootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 500);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.setMargins(30, 0, 30, 0);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 5);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ivOne = new ImageView(this);
        ivOne.setImageResource(R.drawable.custom_progress_dialog_animation);
        ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
        ivOne.setLayoutParams(params1);
        // ivOne.setLayoutParams(new
        // LayoutParams(LayoutParams.MATCH_PARENT,50));
        rootLayout.addView(ivOne);

        ImageView ivOne1 = new ImageView(this);
        ivOne1.setImageResource(R.drawable.border);
        ivOne1.setScaleType(ImageView.ScaleType.FIT_XY);
        ivOne1.setLayoutParams(params);
        rootLayout.addView(ivOne1);
        mScannerView.addView(rootLayout);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (appPraference.getAutoScan()) {
            btn_scan.setVisibility(View.INVISIBLE);

            scanConfig();
        } else {
            mScannerView.stopCamera();

            scanFlag = false;
            mScannerView.startCamera(mCameraId);
            mScannerView.setFlash(mFlash);
            mScannerView.setAutoFocus(false);
            btn_scan.setVisibility(View.VISIBLE);
        }
    }

    private void scanConfig() {
        mScannerView.stopCamera();
        setupLayout();
        scanFlag = true;
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0,
                    R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0,
                    R.string.flash_off);
        }
        MenuItemCompat
                .setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0,
                    R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0,
                    R.string.auto_focus_off);
        }
        MenuItemCompat
                .setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat
                .setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0,
                R.string.select_camera);
        MenuItemCompat
                .setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(
                        this, mSelectedIndices);
                // fragment.show(getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment
                        .newInstance(this, mCameraId);
                // cFragment.show(getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(final Result rawResult) {

        if (!scanFlag) {
            mScannerView.stopCamera();
            mScannerView.startCamera();

            return;
        }

        try {

            mp = MediaPlayer.create(getApplicationContext(),
                    AppConstants.BEEP[appPraference.getBeep()]); // Do the

            mp.start();

        } catch (Exception e) {
        }


        Bundle params = new Bundle();
        params.putString("full_text", rawResult.getContents());

        mFirebaseAnalytics.logEvent(CommonKeyword.TAG_SCANNING_BUTTON_ANALYTICES, params);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ScannerActivity.this,
                        SearchActivity.class);
                intent.putExtra("value", rawResult.getContents());
                startActivity(intent);


            }
        }, 500);

    }


    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // DialogFragment fragment = (DialogFragment)
        // fragmentManager.findFragmentByTag(dialogName);
        // if(fragment != null) {
        // fragment.dismiss();
        // }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }
}
