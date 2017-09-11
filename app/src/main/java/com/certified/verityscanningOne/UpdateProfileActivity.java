package com.certified.verityscanningOne;

import android.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.ConnectionDetector;
import com.certified.verityscanningOne.beans.Loginbean;
import com.certified.verityscanningOne.beans.Parameterbean;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harmis on 9/9/16.
 */
public class UpdateProfileActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    String TAG = "NormalSettingActivity";

    public static UpdateProfileActivity updateProfileActivity;
    CommonMethods mCommonMethods = null;
    ConnectionDetector cd;
    CommonSession mCommonSession;
    View view_settings = null;
    EditText editText_update_item = null;
    TextView textView_update_profile_content = null;
    TextView textView_save_button = null;
    Parameterbean parameterbean = new Parameterbean();
    Loginbean loginbean = null;
    String key;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            updateProfileActivity = this;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            mCommonSession = new CommonSession(this);
            mCommonMethods = new CommonMethods(this);
            cd = new ConnectionDetector(getApplicationContext());

            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
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
            view_settings = getLayoutInflater().inflate(R.layout.update_profile, null);


            textView_save_button = (TextView) view_settings.findViewById(R.id.textview_search_button);
            textView_update_profile_content = (TextView) view_settings.findViewById(R.id.textview_update_profile);
            editText_update_item = (EditText) view_settings.findViewById(R.id.edittext_update_item);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                key = bundle.getString("key");
                String value = null;
                value = bundle.getString("value");
                if (key.equals("1")) {
                    //name
                    editText_update_item.setVisibility(View.VISIBLE);
                    editText_update_item.setInputType(EditorInfo.TYPE_CLASS_TEXT);

                    textView_update_profile_content.setVisibility(View.GONE);
                    if (value == null || value.isEmpty() || value.equals("") || TextUtils.isEmpty(value)) {
                        editText_update_item.setHint(getResources().getString(R.string.name));
                    } else {
                        editText_update_item.setText(value);

                    }
                } else if (key.equals("2")) {//birthdate
                    editText_update_item.setVisibility(View.GONE);
                    textView_update_profile_content.setVisibility(View.VISIBLE);
                    if (value == null || value.isEmpty() || value.equals("") || TextUtils.isEmpty(value)) {
                        textView_update_profile_content.setText(getResources().getString(R.string.set_birthday));
                    } else {
                        textView_update_profile_content.setText(value);

                    }
                } else if (key.equals("3")) {
                    //phone number
                    editText_update_item.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    editText_update_item.setVisibility(View.VISIBLE);
                    textView_update_profile_content.setVisibility(View.GONE);
                    if (value == null || value.isEmpty() || value.equals("") || TextUtils.isEmpty(value)) {
                        editText_update_item.setHint(getResources().getString(R.string.mobile_number));
                    } else {
                        editText_update_item.setText(value);

                    }
                }
            }


            final Gson gson = new Gson();


            Type collectionType = new TypeToken<Loginbean>() {
            }.getType();
            String json = mCommonSession.getLoginContent();
            loginbean = gson.fromJson(json, collectionType);
            if (loginbean != null) {


            }
            frameLayout_container.addView(view_settings);
            textView_update_profile_content.setOnClickListener(new View.OnClickListener() {
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


                                        textView_update_profile_content.setText(fmt2.format(date));


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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    private void setUI() {
        // TODO Auto-generated method stub

        try {
            textView_save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        parameterbean.setUserID(loginbean.getUserID());

                        if (key.equals("1")) {


                            parameterbean.setName(editText_update_item.getText().toString());
                            parameterbean.setBirthDay(loginbean.getBirthday());
                            parameterbean.setMobileNumber(loginbean.getMobileNumber());

                            //name

                        } else if (key.equals("2")) {//birthdate

                            parameterbean.setName(loginbean.getName());
                            parameterbean.setBirthDay(textView_update_profile_content.getText().toString());
                            parameterbean.setMobileNumber(loginbean.getMobileNumber());

                        } else if (key.equals("3")) {
                            //phone number
                            parameterbean.setName(loginbean.getName());
                            parameterbean.setBirthDay(loginbean.getBirthday());
                            parameterbean.setMobileNumber(editText_update_item.getText().toString());
                        }


                        if (CommonMethods.isNetworkConnected(UpdateProfileActivity.this)) {
                            new SaveUpdateProfile().execute("");
                        } else {
                            mCommonMethods.mToast("No Internet Connection");
                        }

                    } catch (Exception e) {
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
            if (value == null || value.isEmpty() || value.equals("")) {

            } else {
                textView.setText(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                    Gson gson = new Gson();
                    String json = gson.toJson(loginbean);
                    mCommonSession.storeLoginContent(json);
                    showMessage(getResources().getString(R.string.profile_update_done));
                } else {

                }


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }

    public void showMessage(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                dialog.cancel();

                                if (NormalSettingActivity.normalSettingActivity != null) {
                                    NormalSettingActivity.normalSettingActivity.finish();
                                }
                                if (UpdateProfileActivity.updateProfileActivity != null) {
                                    UpdateProfileActivity.updateProfileActivity.finish();
                                }
                                Intent intent = new Intent(UpdateProfileActivity.this, NormalSettingActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    })
            ;
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}

