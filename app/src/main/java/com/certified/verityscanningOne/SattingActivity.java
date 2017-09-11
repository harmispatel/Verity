package com.certified.verityscanningOne;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.certified.verityscanningOne.barcodescanner.core.ApplicationPrefs;
import com.certified.verityscanningOne.Utils.CommonSession;


public class SattingActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	CheckBox checkBox1;
	ApplicationPrefs appPraference;
	Spinner   spinner2;
	ImageView actionBarBack;
	TextView txt_country;

	CommonSession mCommonSession;

	public static Activity myActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_satting);
		myActivity = this;

		mCommonSession = new CommonSession(this);

		appPraference = ApplicationPrefs.getInstance(getApplicationContext());
 		spinner2 = (Spinner) findViewById(R.id.spinner2);

		actionBarBack = (ImageView) findViewById(R.id.actionBarBack);
		txt_country = (TextView) findViewById(R.id.textView_country_display);
		LinearLayout linear_country = (LinearLayout) findViewById(R.id.country_ll);

		if (mCommonSession.getSelectedCountry() != null) {
			linear_country.setVisibility(View.VISIBLE);
			txt_country.setText(mCommonSession.getSelectedCountry());
		} else {
			linear_country.setVisibility(View.GONE);
		}


		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.sound, R.layout.spinner_item_setting);
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter1);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				appPraference.setBeep(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		spinner2.setSelection(appPraference.getBeep());

		actionBarBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		initUi();
	}

	private void initUi() {
		// TODO Auto-generated method stub
		try {
			checkBox1 = (CheckBox) findViewById(R.id.checkBox1);

			if (appPraference.getAutoScan()) {
                checkBox1.setChecked(true);
            } else {
                checkBox1.setChecked(false);
            }


			checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    // do stuff
                    if (isChecked) {
                        appPraference.setAutoScan(isChecked);

                    } else {
                        appPraference.setAutoScan(isChecked);
                    }
                }
            });
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(getApplicationContext(),
//				ScannerActivity.class);
//		startActivity(intent);
		finish();

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}
}
