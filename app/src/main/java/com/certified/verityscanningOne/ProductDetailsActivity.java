package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.beans.ReviewBean;
import com.certified.verityscanningOne.beans.Searchbean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class ProductDetailsActivity extends BaseActivity implements OnMapReadyCallback {

    public static Activity activity = null;
    RelativeLayout relativeLayout_privacy_policy = null, relativeLayout_terms_and_condition = null;
    ImageView imageview_product = null;
    ProgressBar progressBar = null;
    CommonSession hJoomAppCommonSession = null;
    String TAG = "ProductDetailsActivity";
    View view_product_details = null;
    Searchbean searchbean_globle = null;
    private GoogleMap mMap;
    LinearLayout linearLayout_dynamical_container = null, linearLayout_certificate_container = null;
    TextView txtCertificationLabel;

    SupportMapFragment supportMapFragment = null;
    FragmentManager fragmentManager = null;


    TextView txtGiveReview, txtAllReview, txtRateUserCount, txtrateNow;
    //RatingBar ratingBarAvg;
    RatingBar ratingBarAvgNew;
    View viewReviewSingle;
    TextView txtTitle, txtdesc, txtDate, txtUserName;
    RatingBar ratingreviewSingle;

    public static String avgRating, rateuserCount = null;


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
                        backEvent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                searchbean_globle = (Searchbean) bundle.getSerializable("bean");

            }
            fragmentManager = getSupportFragmentManager();

            findViewById();
            setUI();
        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        backEvent();

    }

    private void backEvent() {

        try {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Bean", searchbean_globle);
            setResult(RESULT_OK, returnIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUI() {
        // TODO Auto-generated method stub

        try {
            if (searchbean_globle != null) {

                setUpTypeA();
                setUpTypeB();

                if (CommonMethods.isImageUrlValid(searchbean_globle.getProductImage())) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                            .displayImage(searchbean_globle.getProductImage(), imageview_product, SearchActivity.displayImageOptions, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    progressBar.setVisibility(View.GONE);
                                    imageview_product.setVisibility(View.VISIBLE);
                                    imageview_product.setImageResource(R.drawable.no_image_large);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    progressBar.setVisibility(View.GONE);
                                    imageview_product.setVisibility(View.VISIBLE);
                                    imageview_product.setImageBitmap(loadedImage);
                                }
                            }, new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });

                } else {
                    progressBar.setVisibility(View.GONE);
                    imageview_product.setVisibility(View.VISIBLE);
                    imageview_product.setImageResource(R.drawable.no_image_large);


                }

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    private void setUpTypeA() {

        try {
            //type 0 data found it means wihtout images data found
            if (searchbean_globle.isExistExtraFeildsA()) {
                ArrayList<Searchbean> searchbeanArrayList = searchbean_globle.getSearchbeanArrayList_extra_feilds_typa_a();
                for (int i = 0; i < searchbeanArrayList.size(); i++) {

                    Searchbean searchbean_local = searchbeanArrayList.get(i);

                    View view = getLayoutInflater().inflate(R.layout.extra_feild_dynamical_layout, null);
                    TextView textView_place_holder_name = (TextView) view.findViewById(R.id.textview_placeholder_name);
                    TextView textView_place_holder_value = (TextView) view.findViewById(R.id.textview_placeholder_value);
                    ImageView imageview_certificate_icon = (ImageView) view.findViewById(R.id.imageview_certificate_icon);
                    imageview_certificate_icon.setVisibility(View.GONE);
                    textView_place_holder_value.setVisibility(View.VISIBLE);
                    setDefualtTextview(textView_place_holder_name, searchbean_local.getPlaceholder());


                    if (searchbean_local.getPlaceholder() != null || !searchbean_local.getPlaceholder().equals("")) {
                        if (searchbean_local.getPlaceholder().equalsIgnoreCase("Website")) {
                            SpannableString content = new SpannableString(searchbean_local.getValue());
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            textView_place_holder_value.setTextColor(getResources().getColor(R.color.app_bg_blue));

                            textView_place_holder_value.setText(content);
                            textView_place_holder_value.setTag(searchbean_local);
                            textView_place_holder_value.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Searchbean searchbean_inner = (Searchbean) v.getTag();
                                        if (searchbean_inner.getValue() != null || !searchbean_inner.getValue().equals("")) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(searchbean_inner.getValue()));
                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } else {
                            setDefualtTextview(textView_place_holder_value, searchbean_local.getValue());

                        }
                    }


//                    if (i == 1) {
//                        View view_category = getLayoutInflater().inflate(R.layout.extra_feild_dynamical_layout, null);
//
//                        TextView textView_place_holder_namea = (TextView) view_category.findViewById(R.id.textview_placeholder_name);
//                        TextView textView_place_holder_valuea = (TextView) view_category.findViewById(R.id.textview_placeholder_value);
//                        ImageView imageview_certificate_icona = (ImageView) view_category.findViewById(R.id.imageview_certificate_icon);
//
//                        imageview_certificate_icona.setVisibility(View.GONE);
//                        textView_place_holder_valuea.setVisibility(View.VISIBLE);
//
//                        textView_place_holder_namea.setText(getResources().getString(R.string.category));
//                        setDefualtTextview(textView_place_holder_valuea, searchbean_globle.getCategory());
//
//
//                        linearLayout_dynamical_container.addView(view_category);
//
//                    } else {
//
//
//                    }
                    linearLayout_dynamical_container.addView(view);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    private void setUpTypeB() {
        try {
            //type 0 data found it means wihtout images data found
            if (searchbean_globle.isExistExtraFeildsB()) {


                txtCertificationLabel.setVisibility(View.VISIBLE);
                linearLayout_certificate_container.setVisibility(View.VISIBLE);

                ArrayList<Searchbean> searchbeanArrayList = searchbean_globle.getSearchbeanArrayList_extra_feilds_typa_b();
                for (int i = 0; i < searchbeanArrayList.size(); i++) {

                    View view = getLayoutInflater().inflate(R.layout.extra_feild_dynamical_layout, null);
                    Searchbean searchbean_local = searchbeanArrayList.get(i);
                    TextView textView_place_holder_name = (TextView) view.findViewById(R.id.textview_placeholder_name);
                    TextView textView_place_holder_value = (TextView) view.findViewById(R.id.textview_placeholder_value);
                    final ImageView imageview_certificate_icon = (ImageView) view.findViewById(R.id.imageview_certificate_icon);
                    imageview_certificate_icon.setVisibility(View.VISIBLE);
                    textView_place_holder_value.setVisibility(View.GONE);
                    setDefualtTextview(textView_place_holder_name, searchbean_local.getPlaceholder());
                    setDefualtTextview(textView_place_holder_value, searchbean_local.getValue());

                    if (CommonMethods.isImageUrlValid(searchbean_local.getValue())) {
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                                .displayImage(searchbean_local.getValue(), imageview_certificate_icon, SearchActivity.displayImageOptions, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        imageview_certificate_icon.setVisibility(View.VISIBLE);
                                        imageview_certificate_icon.setImageResource(R.drawable.no_image);
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        imageview_certificate_icon.setVisibility(View.VISIBLE);
                                        imageview_certificate_icon.setImageBitmap(loadedImage);
                                    }
                                }, new ImageLoadingProgressListener() {
                                    @Override
                                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    }
                                });

                    } else {
                        imageview_certificate_icon.setVisibility(View.VISIBLE);
                        imageview_certificate_icon.setImageResource(R.drawable.no_image);


                    }


                    imageview_certificate_icon.setTag(searchbean_local);
                    imageview_certificate_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Searchbean searchbean_inner = (Searchbean) v.getTag();
                                String value = searchbean_inner.getRedirectUrl();
                                if (value == null || value.isEmpty() || value.equals("") || value.equals("null")) {
                                } else {
//                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
//                                    startActivity(browserIntent);
                                    Intent in = new Intent(ProductDetailsActivity.this, WebviewActivity.class);
                                    in.putExtra("url", value);
                                    startActivity(in);


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    linearLayout_certificate_container.addView(view);
                }
            } else {
                txtCertificationLabel.setVisibility(View.GONE);
                linearLayout_certificate_container.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public void setDefualtTextview(TextView textview, String value) {
        try {
            if (value == null || value.isEmpty() || value.equals("")) {
                textview.setText("NA");
            } else {
                textview.setText(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_product_details = getLayoutInflater().inflate(R.layout.product_details, null);

            imageview_product = (ImageView) view_product_details.findViewById(R.id.imageview_product);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            supportMapFragment.getMapAsync(this);


            linearLayout_dynamical_container = (LinearLayout) view_product_details.findViewById(R.id.linerlayout_listing_details_container);
            linearLayout_certificate_container = (LinearLayout) view_product_details.findViewById(R.id.linerlayout_certificates_container);


            txtCertificationLabel = (TextView) view_product_details.findViewById(R.id.txtCertificationLabel);

            progressBar = (ProgressBar) view_product_details.findViewById(R.id.progressbar_news_thumb);
            frameLayout_container.addView(view_product_details);


            // code by rahul
            txtGiveReview = (TextView) view_product_details.findViewById(R.id.giveReviewTxt);
            txtAllReview = (TextView) view_product_details.findViewById(R.id.allReviewTxt);
            txtRateUserCount = (TextView) view_product_details.findViewById(R.id.rateusertxt);
            txtrateNow = (TextView) view_product_details.findViewById(R.id.rateNowrtxt);
            //ratingBarAvg = (RatingBar) view_product_details.findViewById(R.id.ratingBarAvg);
            ratingBarAvgNew = (RatingBar) view_product_details.findViewById(R.id.ratingBarAvgNew);
            // ratingBarAvg.setEnabled(false);
            ratingBarAvgNew.setEnabled(false);


            viewReviewSingle = (View) view_product_details.findViewById(R.id.review_list_layout);
            txtTitle = (TextView) view_product_details.findViewById(R.id.txtReviewTitle);
            txtdesc = (TextView) view_product_details.findViewById(R.id.txtReviewDesc);
            txtDate = (TextView) view_product_details.findViewById(R.id.reviewDate);
            txtUserName = (TextView) view_product_details.findViewById(R.id.txtReviewuserName);
            ratingreviewSingle = (RatingBar) view_product_details.findViewById(R.id.reviewRating);
            ratingreviewSingle.setEnabled(false);


            String strAvgRating = searchbean_globle.getAvgRating();
            Log.e("AvgRating ==>>", strAvgRating);
            float avgRating;
            if (strAvgRating == null || strAvgRating.equalsIgnoreCase("")) {
                avgRating = 0;
            } else {
                avgRating = Float.parseFloat(strAvgRating);
            }
            Log.e("AvgRating float ==>>", "" + avgRating);
            // ratingBarAvg.setRating(avgRating);
            ratingBarAvgNew.setRating(avgRating);

            String rateuserCount = searchbean_globle.getRateuserCount();
            if (rateuserCount == null || rateuserCount.equalsIgnoreCase("")) {
                txtRateUserCount.setText("(0 Vote)");
            } else {
                txtRateUserCount.setText("(" + rateuserCount + " Vote)");
            }

            String isRate = searchbean_globle.getIsRate();
            if (isRate == null || isRate.equalsIgnoreCase("")
                    || isRate.equalsIgnoreCase("0")) {
                txtrateNow.setVisibility(View.VISIBLE);
            } else {
                txtrateNow.setVisibility(View.GONE);
            }

            String isReview = searchbean_globle.getIsReview();
            if (isReview == null || isReview.equalsIgnoreCase("")
                    || isReview.equalsIgnoreCase("0")) {
                txtGiveReview.setVisibility(View.VISIBLE);
            } else {
                txtGiveReview.setVisibility(View.GONE);
            }

            ReviewBean reviewbean = searchbean_globle.getReviewbean();
            if (reviewbean.isServiceSuccess()) {
                viewReviewSingle.setVisibility(View.VISIBLE);

                String title = reviewbean.get_reviewTitle();
                if (title == null || title.equalsIgnoreCase("")) {
                    txtTitle.setText("");
                } else {
                    txtTitle.setText(title);
                }

                String desc = reviewbean.get_reviewText();
                if (desc == null || desc.equalsIgnoreCase("")) {
                    txtdesc.setText("");
                } else {
                    txtdesc.setText(desc);
                }

                String date = reviewbean.get_reviewDate();
                if (date == null || date.equalsIgnoreCase("")) {
                    txtDate.setText("");
                } else {
                    txtDate.setText(date);
                }


                String userName = reviewbean.get_userName();
                if (userName == null || userName.equalsIgnoreCase("")) {
                    txtUserName.setText("");
                } else {
                    txtUserName.setText(userName);
                }

                String strreviewRating = reviewbean.get_rating();
                float reviewRating;
                if (strreviewRating == null || strreviewRating.equalsIgnoreCase("")) {
                    reviewRating = 1;
                } else {
                    reviewRating = Float.parseFloat(strreviewRating);
                }
                ratingreviewSingle.setRating(reviewRating);


            } else {
                viewReviewSingle.setVisibility(View.GONE);
            }


            txtrateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        String isRate = searchbean_globle.getIsRate();
                        if (isRate == null || isRate.equalsIgnoreCase("")
                                || isRate.equalsIgnoreCase("0")) {

                            ShowDialog();

                        } else {
                            mCommonMethods.mToast("You can review only one time");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            txtGiveReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent in = new Intent(ProductDetailsActivity.this, ReviewProductActivity.class);
                        in.putExtra("bean", searchbean_globle);
                        startActivityForResult(in, CommonKeyword.RESPONCE_CODE_GIVE_REVIEW);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            txtAllReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent in = new Intent(ProductDetailsActivity.this, ReviewListingActivity.class);
                        in.putExtra("productId", searchbean_globle.getProductId());
                        startActivity(in);
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


    public void ShowDialog() {
        try {
            AlertDialog.Builder popDialog = new AlertDialog.Builder(ProductDetailsActivity.this);

            View dialogRating = getLayoutInflater().inflate(R.layout.rate_now_dialog, null);
            final RatingBar rating = (RatingBar) dialogRating.findViewById(R.id.ratingBarNow);

            popDialog.setTitle("Rate Now");
            popDialog.setView(dialogRating);

            // Button OK
            popDialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            float ratingValue = rating.getRating();

                            if (ratingValue == 0) {

                            } else {

                                if (CommonMethods.isNetworkConnected(ProductDetailsActivity.this)) {
                                    new GiveRatingAsynctask("" + rating.getRating()).execute();
                                } else {
                                    mCommonMethods.mToast("No Internet Connection");
                                }


                                dialog.dismiss();

                            }

                        }

                    })

                    // Button Cancel
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            popDialog.create();
            popDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;


            double lat = 0.00, longi = 0.00;
            if (searchbean_globle.getLat() == null || searchbean_globle.getLat().equals("") || searchbean_globle.getLat().equals("null") || searchbean_globle.getLat().equals("0.00000") || searchbean_globle.getLat().equals("0.000000") || searchbean_globle.getLat().equals("0.000000") || searchbean_globle.getLat().equals("0.000000")) {
                fragmentManager.beginTransaction().hide(supportMapFragment).commit();
            } else {
                lat = Double.parseDouble(searchbean_globle.getLat());
                fragmentManager.beginTransaction().show(supportMapFragment).commit();
                if (searchbean_globle.getLng() == null || searchbean_globle.getLng().equals("") || searchbean_globle.getLng().equals("null")) {

                } else {
                    longi = Double.parseDouble(searchbean_globle.getLng());
                }
                LatLng latlngnew = new LatLng(lat, longi);

                googleMap.addMarker(new MarkerOptions().position(latlngnew).title(searchbean_globle.getProductName()).snippet("UPC :" + searchbean_globle.getUpcCode()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlngnew));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("NewApi")
    public class GiveRatingAsynctask extends AsyncTask<String, String, String> {


        String ratingValue = "";

        public GiveRatingAsynctask(String ratingValue) {
            this.ratingValue = ratingValue;
        }

        ProgressDialog progress;

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {

                progress = new ProgressDialog(ProductDetailsActivity.this);
                progress.setMessage("please wait...");
                progress.setIndeterminate(true);
                progress.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String mbean = null;
            try {
                mbean = mCommonMethods.postRate(searchbean_globle.getProductId(), ratingValue);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mbean;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                progress.dismiss();

                if (result.equalsIgnoreCase(CommonKeyword.FALSE)) {

                } else if (result.equalsIgnoreCase(CommonKeyword.TRUE)) {

                    searchbean_globle.setAvgRating(avgRating);
                    searchbean_globle.setRateuserCount(rateuserCount);
                    searchbean_globle.setIsRate("1");

                    String strAvgRating = searchbean_globle.getAvgRating();
                    Log.e("AvgRating ==>>", strAvgRating);
                    float avgRating;
                    if (strAvgRating == null || strAvgRating.equalsIgnoreCase("")) {
                        avgRating = 0;
                    } else {
                        avgRating = Float.parseFloat(strAvgRating);
                    }
                    Log.e("AvgRating float ==>>", "" + avgRating);
                    ratingBarAvgNew.setRating(avgRating);

                    String rateuserCount = searchbean_globle.getRateuserCount();
                    if (rateuserCount == null || rateuserCount.equalsIgnoreCase("")) {
                        txtRateUserCount.setText("(0 Vote)");
                    } else {
                        txtRateUserCount.setText("(" + rateuserCount + " Vote)");
                    }

                    String isRate = searchbean_globle.getIsRate();
                    if (isRate == null || isRate.equalsIgnoreCase("")
                            || isRate.equalsIgnoreCase("0")) {
                        txtrateNow.setVisibility(View.VISIBLE);
                    } else {
                        txtrateNow.setVisibility(View.GONE);
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CommonKeyword.RESPONCE_CODE_GIVE_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Searchbean mBean = (Searchbean) data.getSerializableExtra("Bean");
                    searchbean_globle = mBean;


                    String isReview = searchbean_globle.getIsReview();
                    if (isReview == null || isReview.equalsIgnoreCase("")
                            || isReview.equalsIgnoreCase("0")) {
                        txtGiveReview.setVisibility(View.VISIBLE);
                    } else {
                        txtGiveReview.setVisibility(View.GONE);
                    }

                    ReviewBean reviewbean = searchbean_globle.getReviewbean();
                    if (reviewbean.isServiceSuccess()) {
                        viewReviewSingle.setVisibility(View.VISIBLE);

                        String title = reviewbean.get_reviewTitle();
                        if (title == null || title.equalsIgnoreCase("")) {
                            txtTitle.setText("");
                        } else {
                            txtTitle.setText(title);
                        }

                        String desc = reviewbean.get_reviewText();
                        if (desc == null || desc.equalsIgnoreCase("")) {
                            txtdesc.setText("");
                        } else {
                            txtdesc.setText(desc);
                        }

                        String date = reviewbean.get_reviewDate();
                        if (date == null || date.equalsIgnoreCase("")) {
                            txtDate.setText("");
                        } else {
                            txtDate.setText(date);
                        }


                        String userName = reviewbean.get_userName();
                        if (userName == null || userName.equalsIgnoreCase("")) {
                            txtUserName.setText("");
                        } else {
                            txtUserName.setText(userName);
                        }

                        String strreviewRating = reviewbean.get_rating();
                        float reviewRating;
                        if (strreviewRating == null || strreviewRating.equalsIgnoreCase("")) {
                            reviewRating = 1;
                        } else {
                            reviewRating = Float.parseFloat(strreviewRating);
                        }
                        ratingreviewSingle.setRating(reviewRating);


                    } else {
                        viewReviewSingle.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

    }
}
