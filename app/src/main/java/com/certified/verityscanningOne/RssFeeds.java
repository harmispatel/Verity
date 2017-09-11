package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.certified.verityscanningOne.Adapter.RssFeedListAdapter;
import com.certified.verityscanningOne.Model.Example;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;


public class RssFeeds extends AppCompatActivity {

    public static Activity activity = null;
    CommonSession hJoomAppCommonSession = null;
    String TAG = "RssFeedsActivity";
    ImageView imageView_back = null, imageView_normal_settings = null;
    TextView textView_header_title = null;
    TextView txt_no_comment;
    CommonMethods mCommonMethods;
    LinearLayoutManager mLayoutManager;
    private ProgressDialog pDialog;
     private RecyclerView mRecyclerView;
    int Type_RecallRss;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_feeds_activity);


        Intent intent = getIntent();
         Type_RecallRss = intent.getIntExtra("Type_RecallRss", 0);
        Log.d("Type_RecallRss",String.valueOf(Type_RecallRss));

        try {

            activity = this;
            hJoomAppCommonSession = new CommonSession(this);
            mCommonMethods = new CommonMethods(this);

            findViewById();
        }
        catch (Exception e) {
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            mLayoutManager = new LinearLayoutManager(this);

            imageView_back = (ImageView)
                    findViewById(R.id.imageview_back);
            imageView_normal_settings = (ImageView)
                    findViewById(R.id.imageview_setting);

            textView_header_title = (TextView) findViewById(R.id.textview_header_title);
            textView_header_title.setVisibility(View.GONE);
            mRecyclerView = (RecyclerView) findViewById(R.id.rss_feeds_recycler_view);
             txt_no_comment = (TextView) findViewById(R.id.rss_feeds_no_data);


            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            textView_header_title.setVisibility(View.VISIBLE);

            if (Type_RecallRss==0)
            {

                textView_header_title.setText(getResources().getString(R.string.usda_recalls));
            }
            else if (Type_RecallRss==1)
            {
                textView_header_title.setText(getResources().getString(R.string.allRecallRss));
            }
            else if (Type_RecallRss==2)
            {
                textView_header_title.setText(getResources().getString(R.string.recallsforChildren));
            }



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

            if (CommonMethods.isNetworkConnected(RssFeeds.this)) {
                // Internet Connection is Present

                GetRssFeedData placesTask_notification = new GetRssFeedData();
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

    private void StartAsyncTaskInParallel(GetRssFeedData task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    public class GetRssFeedData extends AsyncTask<Void, Void, Example> {
        @Override
        protected Example doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Example mList = null;

            try {
                     mList = mCommonMethods.getRssFeedsList(Type_RecallRss);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return mList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RssFeeds.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Example result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result.getSuccess() == 1) {
                mRecyclerView.setVisibility(View.VISIBLE);
                txt_no_comment.setVisibility(View.GONE);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(new RssFeedListAdapter(result.getRssFeeds(), RssFeeds.this));

            } else {
                mRecyclerView.setVisibility(View.GONE);
                txt_no_comment.setVisibility(View.VISIBLE);

            }
        }
    }

}
