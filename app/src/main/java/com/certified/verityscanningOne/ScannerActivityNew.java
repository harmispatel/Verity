//
//package com.certified.verityscanning;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.drawable.AnimationDrawable;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.certified.verityscanning.Utils.CommonKeyword;
//import com.certified.verityscanning.Utils.CommonSession;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.zxing.Result;
//
//import java.util.ArrayList;
//
//import me.dm7.barcodescanner.core.IViewFinder;
//import me.dm7.barcodescanner.zxing.ZXingScannerView;
//
//
//public class ScannerActivityNew extends Activity implements ZXingScannerView.ResultHandler {
//
//    String TAG = "ScannerActivityNew";
//
//    private static final String FLASH_STATE = "FLASH_STATE";
//    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
//    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
//    private static final String CAMERA_ID = "CAMERA_ID";
//    public static Activity myActivity = null;
//    public static ScannerActivityNew scannerActivity = null;
//    static ScannerActivityNew This = null;
//    public Boolean scanFlag = false;
//    FrameLayout frameContainer;
//    ImageView imageView_back, imageView_flash, imageView_refresh;
//    MediaPlayer mp;
//    RelativeLayout rootLayout;
//    CommonSession mCommonSession;
//    ImageView imageView_scan_setting = null, imageView_history = null;
//    private boolean mFlash;
//    private boolean mAutoFocus;
//    private ArrayList<Integer> mSelectedIndices;
//    private int mCameraId = -1;
//    private IViewFinder mViewFinderView;
//    private AnimationDrawable animation;
//    private ZXingScannerView mScannerView;
//    boolean isFlashOn = false;
//
//    LinearLayout frameLayout_scanner_content = null;
//
//    @SuppressWarnings("deprecation")
//    public ScannerActivityNew() {
//        This = this;
//    }
//
//    private FirebaseAnalytics mFirebaseAnalytics;
//
//    @Override
//    public void onCreate(Bundle state) {
//        super.onCreate(state);
//        try {
//            myActivity = this;
//            scannerActivity = this;
//            mCommonSession = new CommonSession(this);
//            if (state != null) {
//                mFlash = state.getBoolean(FLASH_STATE, false);
//                mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
//                mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
//                mCameraId = state.getInt(CAMERA_ID, -1);
//            } else {
//                mFlash = false;
//                mAutoFocus = true;
//                mSelectedIndices = null;
//                mCameraId = -1;
//            }
//
//            // ;
//
//            //setupFormats();
//            // setContentView(mScannerView);
//            setContentView(R.layout.scanner_main);
//            frameContainer = (FrameLayout) findViewById(R.id.frameContainer);
//            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//
//            mScannerView = new ZXingScannerView(this) {
//                @Override
//                protected IViewFinder createViewFinderView(Context context) {
//                    return new CustomViewFinderView(context);
//                }
//            };
//
//            frameContainer.addView(mScannerView);
//
//
//            imageView_back = (ImageView) findViewById(R.id.imageview_white_back);
//            imageView_refresh = (ImageView) findViewById(R.id.imageview_refresh);
//            imageView_flash = (ImageView) findViewById(R.id.imageview_flash);
//
//            imageView_flash.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//
//                    try {
//                        if (isFlashOn) {
//
//                            mScannerView.setFlash(false);
//                            isFlashOn = false;
//
//                        } else {
//                            mScannerView.setFlash(true);
//                            isFlashOn = true;
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            imageView_refresh.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//
//                    try {
//                        finish();
//                        if (ScannerActivityNew.scannerActivity != null) {
//                            ScannerActivityNew.scannerActivity.finish();
//                        }
//                        Intent intent = new Intent(ScannerActivityNew.this, ScannerActivityNew.class);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            imageView_scan_setting = (ImageView) findViewById(R.id.imageview_setting_scan);
//            imageView_scan_setting.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    try {
//                        Intent intent = new Intent(getApplicationContext(),
//                                ScanSettingActivity.class);
//
//                        startActivity(intent);
//                        finish();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            imageView_back.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    if (ScannerActivityNew.scannerActivity != null) {
//                        ScannerActivityNew.scannerActivity.finish();
//                    }
//                    onBackPressed();
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    protected IViewFinder createViewFinderView(Context context) {
//        return new ViewFinderView(context);
//    }
//
//    private void addLoadingView() {
//        // TODO Auto-generated method stub
//        rootLayout = new RelativeLayout(this);
//        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, 500);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        params.setMargins(30, 0, 30, 0);
//
//        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, 5);
//        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        ImageView ivOne = new ImageView(this);
//        ivOne.setImageResource(R.drawable.custom_progress_dialog_animation);
//        ivOne.setScaleType(ImageView.ScaleType.FIT_XY);
//        ivOne.setLayoutParams(params1);
//        // ivOne.setLayoutParams(new
//        // LayoutParams(LayoutParams.MATCH_PARENT,50));
//        rootLayout.addView(ivOne);
//
//        ImageView ivOne1 = new ImageView(this);
//        ivOne1.setImageResource(R.drawable.border);
//        ivOne1.setScaleType(ImageView.ScaleType.FIT_XY);
//        ivOne1.setLayoutParams(params);
//        rootLayout.addView(ivOne1);
//
//        //mScannerView.addView(rootLayout);
//    }
//
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(FLASH_STATE, mFlash);
//        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
//        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
//        outState.putInt(CAMERA_ID, mCameraId);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
//    }
//
//    @Override
//    public void handleResult(final Result rawResult) {
//
//
//        try {
//
//
//            Bundle params = new Bundle();
//            params.putString("full_text", rawResult.getText());
//            mFirebaseAnalytics.logEvent(CommonKeyword.TAG_SCANNING_BUTTON_ANALYTICES, params);
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScannerView.resumeCameraPreview(ScannerActivityNew.this);
//                    Intent intent = new Intent(ScannerActivityNew.this,
//                            SearchActivity.class);
//                    intent.putExtra("value", rawResult.getText());
//                    startActivity(intent);
//
//
//                }
//            }, 2000);
//
//
//            // Note:
//            // * Wait 2 seconds to resume the preview.
//            // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//            // * I don't know why this is the case but I don't have the time to figure out.
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (ScannerActivityNew.scannerActivity != null) {
//            mScannerView.resumeCameraPreview(ScannerActivityNew.this);
//            ScannerActivityNew.scannerActivity.finish();
//        }
//
//    }
//
//    private static class CustomViewFinderView extends com.certified.verityscanning.ViewFinderView {
//        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
//        public final Paint PAINT = new Paint();
//
//        public CustomViewFinderView(Context context) {
//            super(context);
//            init();
//        }
//
//        public CustomViewFinderView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            init();
//        }
//
//        private void init() {
//            PAINT.setColor(Color.WHITE);
//            PAINT.setAntiAlias(true);
//            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
//            PAINT.setTextSize(textPixelSize);
//
//        }
//
//        @Override
//        public void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//        }
//
//
//    }
//}
//
