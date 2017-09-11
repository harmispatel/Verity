package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.certified.verityscanningOne.Utils.CommonMethods;

/**
 * Created by Nakul Sheth on 01-09-2016.
 */
public class PrivacyPolicyActivity extends BaseActivity {
    String TAG="PrivacyPolicyActivity";

    WebView webview;
    ProgressDialog pd;
    View view_privacy_policy=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            findViewById();
            setUI();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG,e,null);

        }


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


    private void setUI() {
        // TODO Auto-generated method stub

        try {
            pd = new ProgressDialog(PrivacyPolicyActivity.this);
            pd.setMessage("Page is loading...");
            pd.show();



            // ===================

            /** Load the url entered in the edittext box */
            webview.loadUrl("https://www.iubenda.com/privacy-policy/7766075");

            /** Showing Indeterminate progress bar in the title bar */
            this.setProgressBarIndeterminateVisibility(true);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (!pd.isShowing()) {
                        pd.show();
                    }

                    /** This prevents the loading of pages in system browser */
                    return false;
                }

                /** Callback method, executed when the page is completely loaded */
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    /** Hiding Indeterminate Progress Bar in the title bar */
                    PrivacyPolicyActivity.this
                            .setProgressBarIndeterminateVisibility(false);
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }

                }

            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG,e,null);

        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void findViewById() {
        // TODO Auto-generated method stub
        try {
            view_privacy_policy = getLayoutInflater().inflate(R.layout.webview_open_link, null);

            webview = (WebView) view_privacy_policy.findViewById(R.id.about_maya_webview);

            if (android.os.Build.VERSION.SDK_INT < 16) {
                webview.setBackgroundColor(0x00000000);
            } else {
                webview.setBackgroundColor(Color.argb(1, 0, 0, 0));
            }

            /** Enabling Javascript for the WebView */
            webview.getSettings().setJavaScriptEnabled(true);

            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            imageView_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

			/*mCommomMethod.setHeaderBackGroundColor(WebviewOpenLink.this,
                    relative_header_bg, imageButton, btn_settings);*/

            frameLayout_container.addView(view_privacy_policy);



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG,e,null);

        }

    }

}