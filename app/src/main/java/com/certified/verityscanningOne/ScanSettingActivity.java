//package com.certified.verityscanning;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//
//import com.certified.verityscanning.barcodescanner.core.ApplicationPrefs;
//import com.certified.verityscanning.Utils.CommonMethods;
//import com.certified.verityscanning.Utils.CommonSession;
//
//
//public class ScanSettingActivity extends BaseActivity implements OnClickListener,
//        CompoundButton.OnCheckedChangeListener {
//    String TAG = "ScanSettingActivity";
//
//    public static Activity myActivity = null;
//    CheckBox checkBox1;
//    ApplicationPrefs appPraference;
//    CommonSession mCommonSession;
//    View view_scan_setting = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        try {
//            super.onCreate(savedInstanceState);
//            appPraference = ApplicationPrefs.getInstance(getApplicationContext());
//
//            myActivity = this;
//            mCommonSession = new CommonSession(this);
//
//            initUi();
//            imageView_normal_settings.setVisibility(View.GONE);
//            imageView_back.setVisibility(View.VISIBLE);
//            imageView_back.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        if (ScannerActivity.myActivity != null) {
//                            ScannerActivity.myActivity.finish();
//                        }
//                        Intent intent = new Intent(getApplicationContext(),
//                                ScannerActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//
//        } catch (Exception e) {
//            CommonMethods.printFirebaseLogcat(TAG, e, null);
//
//            e.printStackTrace();
//        }
//    }
//
//    private void initUi() {
//        // TODO Auto-generated method stub
//        try {
//            view_scan_setting = getLayoutInflater().inflate(R.layout.setting_for_scan, null);
//            checkBox1 = (CheckBox) view_scan_setting.findViewById(R.id.checkBox1);
//            checkBox1.setChecked(true);
//
//            /*if (appPraference.getAutoScan()) {
//                checkBox1.setChecked(true);
//            } else {
//                checkBox1.setChecked(false);
//            }*/
//
//
//          /*  checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView,
//                                             boolean isChecked) {
//                    // do stuff
//                    if (isChecked) {
//                        appPraference.setAutoScan(isChecked);
//
//                    } else {
//                        appPraference.setAutoScan(isChecked);
//                    }
//                }
//            });*/
//
//            frameLayout_container.addView(view_scan_setting);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            CommonMethods.printFirebaseLogcat(TAG, e, null);
//
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        try {
//           /* if (ScannerActivityNew.scannerActivity != null) {
//                ScannerActivityNew.scannerActivity.finish();
//            }
//            Intent intent = new Intent(getApplicationContext(),
//                    ScannerActivityNew.class);
//            startActivity(intent);*/
//            finish();
//        } catch (Exception e) {
//            e.printStackTrace();
//            CommonMethods.printFirebaseLogcat(TAG, e, null);
//
//        }
//
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//}