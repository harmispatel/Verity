package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.certified.verityscanningOne.Adapter.ReviewListAdapter;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.PaddingItemDecoration;
import com.certified.verityscanningOne.beans.ReviewBean;

import java.util.List;

/**
 * Created by harmis on 25/2/17.
 */

public class ReviewListingActivity extends BaseActivity {

    public static Activity activity = null;

    CommonSession hJoomAppCommonSession = null;
    String TAG = "ReviewListingActivity";
    View view_review_product = null;

    String productId;

    RecyclerView mRecyclerView;
    TextView txt_no_comment;

    private ReviewListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    List<ReviewBean> mList;

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
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                productId = bundle.getString("productId");
            }

            findViewById();

        } catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_review_product = getLayoutInflater().inflate(R.layout.review_list, null);

            mRecyclerView = (RecyclerView) view_review_product.findViewById(R.id.social_comment_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mRecyclerView.addItemDecoration(new PaddingItemDecoration());
            txt_no_comment = (TextView) view_review_product.findViewById(R.id.social_comment_no_data);

            frameLayout_container.addView(view_review_product);


            // check for Internet status
            if (CommonMethods.isNetworkConnected(ReviewListingActivity.this)) {
                // Internet Connection is Present

                GetSocialLikeData placesTask_notification = new GetSocialLikeData();
                StartAsyncTaskInParallel(placesTask_notification);


            } else {


                // Internet connection is not present
                mCommonMethods
                        .mToast("You do not have internet connection");

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        }

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTaskInParallel(GetSocialLikeData task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }


    @SuppressLint("NewApi")
    public class GetSocialLikeData extends AsyncTask<String, String, String> {
        ProgressDialog progress;

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {

                progress = new ProgressDialog(ReviewListingActivity.this);
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

            try {

                mList = mCommonMethods.getReviewList(productId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            try {

                progress.dismiss();

                ReviewBean mBean = mList.get(0);
                if (mBean.isServiceSuccess()) {

                    mAdapter = new ReviewListAdapter(ReviewListingActivity.this, mList, mRecyclerView);
                    mRecyclerView.setAdapter(mAdapter);

                } else {

                    mList.clear();

                    mAdapter = new ReviewListAdapter(ReviewListingActivity.this, mList, mRecyclerView);
                    mRecyclerView.setAdapter(mAdapter);

                    txt_no_comment.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}

