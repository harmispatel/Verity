package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.beans.Searchbean;

import java.util.ArrayList;

/**
 * Created by harmis on 24/2/17.
 */

public class ReviewProductActivity extends BaseActivity {

    public static Activity activity = null;
    EditText edtTitle = null, edtDesc = null;
    TextView txtSubmit = null;
    Spinner spnrRating;

    String strTitle, strDesc, strRating;

    ArrayList<String> rating_list;
    ArrayAdapter<String> adapter_Rating;

    CommonSession hJoomAppCommonSession = null;
    String TAG = "ReviewProductActivity";
    View view_review_product = null;

    Searchbean searchbean_globle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            activity = this;
            hJoomAppCommonSession = new CommonSession(this);
            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);


                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                searchbean_globle = (Searchbean) bundle.getSerializable("bean");

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

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);

        super.onBackPressed();
    }

    private void setUI() {
        // TODO Auto-generated method stub
        try {

            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        strTitle = edtTitle.getText().toString();
                        strDesc = edtDesc.getText().toString();

                        int ratingvalue = spnrRating.getSelectedItemPosition() + 1;
                        strRating = String.valueOf(ratingvalue);

                        if (strTitle == null || strTitle.equalsIgnoreCase("")) {
                            mCommonMethods.mToast("Enter title");
                        } else if (strDesc == null || strDesc.equalsIgnoreCase("")) {
                            mCommonMethods.mToast("Enter description");
                        } else {

                            if (CommonMethods.isNetworkConnected(ReviewProductActivity.this)) {
                                new GiveReviewAsynctask().execute();
                            } else {
                                mCommonMethods.mToast("No Internet Connection");
                            }
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

    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_review_product = getLayoutInflater().inflate(R.layout.review_product_layout, null);
            edtTitle = (EditText) view_review_product.findViewById(R.id.edittext_review_title);
            edtDesc = (EditText) view_review_product.findViewById(R.id.edittext_review_desc);
            txtSubmit = (TextView) view_review_product.findViewById(R.id.review_submit_button);
            spnrRating = (Spinner) view_review_product.findViewById(R.id.spinnerReview);


            frameLayout_container.addView(view_review_product);

            rating_list = new ArrayList<String>();

            rating_list.add("Very Poor");
            rating_list.add("Fair");
            rating_list.add("Average");
            rating_list.add("Good");
            rating_list.add("Excellent!");

            adapter_Rating = new ArrayAdapter<String>(
                    ReviewProductActivity.this, R.layout.spinner_privacy,
                    rating_list);
            adapter_Rating.setDropDownViewResource(R.layout.spinner_privacy_post);
            spnrRating.setAdapter(adapter_Rating);
            spnrRating.setPrompt("Select your rating");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        }

    }


    @SuppressLint("NewApi")
    public class GiveReviewAsynctask extends AsyncTask<String, String, Searchbean> {


        ProgressDialog progress;

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {


                progress = new ProgressDialog(ReviewProductActivity.this);
                progress.setMessage("please wait...");
                progress.setIndeterminate(true);
                progress.show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Searchbean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Searchbean mbean = null;
            try {
                mbean = mCommonMethods.postReview(searchbean_globle.getProductId(), strTitle, strDesc, strRating);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mbean;
        }

        @Override
        protected void onPostExecute(Searchbean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                progress.dismiss();
                if (result.isServiceSuccess()) {
                    mCommonMethods.mToast("Your review successfully posted.");

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Bean", result);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                } else {
                    mCommonMethods.mToast("Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}

