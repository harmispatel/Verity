package com.certified.verityscanningOne;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by harmis on 9/9/16.
 */
public class NotificationMessageDialogActivity extends Activity implements View.OnClickListener {

    Button ok_btn;

    TextView textView_message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.95);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.notification_dailoge);

            getWindow().setLayout(screenWidth, WindowManager.LayoutParams.WRAP_CONTENT); //set below the setContentview
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ok_btn = (Button) findViewById(R.id.ok_btn_id);
            textView_message = (TextView) findViewById(R.id.textview_message_content);


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String message = null;
                message = bundle.getString("message");
                if (message != null) {
                    textView_message.setText(message);
                }
            }
            ok_btn.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ok_btn_id:

                this.finish();

                break;


        }

    }


}