package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.certified.verityscanningOne.Model.RssFeed;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;

public class RssFeedsDetails extends AppCompatActivity {

    public static Activity activity = null;
    CommonSession hJoomAppCommonSession = null;
    String TAG = "RssFeedDetailsActivity";
    CommonMethods mCommonMethods;

    ImageView imageView_back = null, imageView_normal_settings = null;
    TextView textView_header_title = null;
    TextView text_title;
    WebView webView;

    RssFeed rssFeed;

    private ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_feeds_details_activity);

        try {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                rssFeed = (RssFeed) bundle.getSerializable("bean");
            }
            setUI();

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setUI() {
        // TODO Auto-generated method stub

        try {

            try {

                activity = this;
                hJoomAppCommonSession = new CommonSession(this);
                mCommonMethods = new CommonMethods(this);

                webView = (WebView) findViewById(R.id.rss_feeds_webview);

                text_title = (TextView) findViewById(R.id.txtRssfeedsTitled);
                imageView_back = (ImageView)
                        findViewById(R.id.imageview_back);
                imageView_normal_settings = (ImageView)
                        findViewById(R.id.imageview_setting);

                textView_header_title = (TextView) findViewById(R.id.textview_header_title);

                imageView_normal_settings.setVisibility(View.GONE);
                imageView_back.setVisibility(View.VISIBLE);
                textView_header_title.setVisibility(View.VISIBLE);
                textView_header_title.setText(getResources().getString(R.string.usda_recalls_details));
                imageView_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (webView.canGoBack()) {
                                webView.goBack();
                            } else {
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                pDialog = new ProgressDialog(RssFeedsDetails.this);
                pDialog.setMessage("Page is loading...");
                pDialog.show();

                /** Load the url entered in the edittext box */

                text_title.setText(rssFeed.getTitle());
                webView.loadUrl(rssFeed.getLink());


                /** Showing Indeterminate progress bar in the title bar */
                this.setProgressBarIndeterminateVisibility(true);

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (!pDialog.isShowing()) {
                            pDialog.show();
                        }

                        /** This prevents the loading of pages in system browser */
                        return false;
                    }

                    /**
                     * Callback method, executed when the page is completely loaded
                     */
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        /** Hiding Indeterminate Progress Bar in the title bar */
                        RssFeedsDetails.this
                                .setProgressBarIndeterminateVisibility(false);
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }

                    }

                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }
}