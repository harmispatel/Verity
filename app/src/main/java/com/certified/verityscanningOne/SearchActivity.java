package com.certified.verityscanningOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.certified.verityscanningOne.Adapter.SearchAdapter;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.PaddingItemDecoration;
import com.certified.verityscanningOne.Utils.RecyclerItemClickListener;
import com.certified.verityscanningOne.beans.Responcebean;
import com.certified.verityscanningOne.beans.Searchbean;
import com.certified.verityscanningOne.customviews.OnMoreListener;
import com.certified.verityscanningOne.customviews.SuperRecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SearchActivity extends BaseActivity implements OnMoreListener {

    public static DisplayImageOptions displayImageOptions = null;

    String TAG = "SearchActivity";
    private FirebaseAnalytics mFirebaseAnalytics;

    CommonMethods mCommomMethod;
    CommonSession commonSession = null;
    EditText editText_search_item = null;
    TextView textView_button_search = null;
    View view_search = null;
    public static ArrayList<Searchbean> searchbeanArrayList = new ArrayList<>();
    int start_limit = 0;
    String searchKeyword = "";
    SuperRecyclerView superRecyclerView_news_items = null;
    SearchAdapter searchAdapter = null;
    TextView customTextview_empty_message = null;
    Bundle bundle_for_selected_social = new Bundle();
    boolean onMoreWorking = false;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);

            displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(false)
                    .cacheOnDisc(true).resetViewBeforeLoading(true).build();
            mCommomMethod = new CommonMethods(this);
            commonSession = new CommonSession(this);

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            findViewById();

            String searchFromScan = null;
            searchFromScan = getIntent().getStringExtra("value");
            if (searchFromScan != null) {

                onMoreWorking = false;
                searchKeyword = searchFromScan;
                start_limit = 0;
                editText_search_item.setText(searchKeyword);



                if (CommonMethods.isNetworkConnected(SearchActivity.this)) {
                    new ProductExistenceCheck().execute("");
                } else {
                    mCommonMethods.mToast("No Internet Connection");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public void showDialogForProductExistOrNot() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.this_product_does_not_exist))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
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

            imageView_normal_settings.setVisibility(View.GONE);
            imageView_back.setVisibility(View.VISIBLE);
            imageView_normal_settings.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(SearchActivity.this, NormalSettingActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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
            view_search = getLayoutInflater().inflate(R.layout.search_screen, null);

            editText_search_item = (EditText) view_search.findViewById(R.id.edittext_search_item);
            textView_button_search = (TextView) view_search.findViewById(R.id.textview_search_button);
            superRecyclerView_news_items = (SuperRecyclerView) view_search.findViewById(R.id.recycler_view);
            superRecyclerView_news_items.getProgressView().setVisibility(View.GONE);
            superRecyclerView_news_items.setLayoutManager(new LinearLayoutManager(this));
            superRecyclerView_news_items.setOnMoreListener(this);
            superRecyclerView_news_items.addItemDecoration(new PaddingItemDecoration());
            superRecyclerView_news_items.addOnItemTouchListener(
                    new RecyclerItemClickListener(SearchActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            try {

                                pos = position;

//                              Searchbean searchbean = (Searchbean) view.getTag();
                                Searchbean searchbean = searchbeanArrayList.get(position);

                                Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("bean", searchbean);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, CommonKeyword.RESPONCE_CODE_PRODUCT_DETAIL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
            );

            customTextview_empty_message = (TextView) view_search.findViewById(R.id.textView);


            textView_button_search.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            textView_button_search.setBackgroundColor(getResources().getColor(R.color.gray_line_color));
                            textView_button_search.setTextColor(getResources().getColor(R.color.white));
                            start_limit = 0;
                            onMoreWorking = false;
                            hide();

                            Bundle params = new Bundle();
                            params.putString("full_text", editText_search_item.getText().toString());
                            mFirebaseAnalytics.logEvent(CommonKeyword.TAG_SEARCH_BUTTON_ANALYTICES, params);

                            searchKeyword = editText_search_item.getText().toString();

                            searchbeanArrayList.clear();
                            searchAdapter = new SearchAdapter(SearchActivity.this);
                            superRecyclerView_news_items.setAdapter(searchAdapter);
                            customTextview_empty_message.setText("");


                            if (CommonMethods.isNetworkConnected(SearchActivity.this)) {
                                new GetSearchResults(1).execute();
                            } else {
                                mCommonMethods.mToast("No Internet Connection");
                            }


                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            hide();

                            //textView_button_search.setBackground(getDrawable(R.drawable.edittext_bg));
                            textView_button_search.setBackground(getResources().getDrawable(R.drawable.edittext_bg));

                            textView_button_search.setTextColor(getResources().getColor(R.color.black));
                            return true; // if you want to handle the touch event
                    }
                    return false;


                }
            });
            frameLayout_container.addView(view_search);
        } catch (
                Exception e
                )

        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        try {
            Log.e("onMOreAsked", "Onmoreasked called" + start_limit);
            superRecyclerView_news_items.getMoreProgressView().setVisibility(View.GONE);
            if (onMoreWorking) {

            } else {
                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                //DO SOME ACTIONS HERE , THIS ACTIONS WILL WILL EXECUTE AFTER 5 SECONDS...
//                                onMoreWorking = true;
                                start_limit += 10;
                                new GetSearchResults(2).execute();
                            }
                        });
                    }
                }, 5000);

            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }

    }


    public class GetSearchResults extends AsyncTask<String, String, ArrayList<Searchbean>> {
        int index = 1;

        GetSearchResults(int mindex) {
            index = mindex;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                if (index == 1) {
                    superRecyclerView_news_items.getProgressView().setVisibility(View.VISIBLE);
                } else if (index == 2) {
                    superRecyclerView_news_items.getMoreProgressView().setVisibility(View.VISIBLE);

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
        }

        @Override
        protected ArrayList<Searchbean> doInBackground(String... params) {
            ArrayList<Searchbean> searchbeanArrayList = null;
            // TODO Auto-generated method stub
            try {
                searchbeanArrayList = mCommomMethod.getSeachListing(searchKeyword, start_limit);
            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

            return searchbeanArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Searchbean> msearchbeanArrayList) {
            // TODO Auto-generated method stub
            super.onPostExecute(msearchbeanArrayList);
            try {
                super.onPostExecute(msearchbeanArrayList);

                Searchbean bean = msearchbeanArrayList.get(0);
                if (bean.isServiceSuccess()) {

                    onMoreWorking = false;
                } else {
                    onMoreWorking = true;
                }

                superRecyclerView_news_items.getProgressView().setVisibility(View.GONE);
                superRecyclerView_news_items.getMoreProgressView().setVisibility(View.GONE);

                setUpArraylist(msearchbeanArrayList, index);

            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }

    public class ProductExistenceCheck extends AsyncTask<String, String, Responcebean> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {

                superRecyclerView_news_items.getProgressView().setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }
        }

        @Override
        protected Responcebean doInBackground(String... params) {
            Responcebean responcebean = null;
            // TODO Auto-generated method stub
            try {
                responcebean = mCommomMethod.checkThisProductExistByThisKeyword(searchKeyword);
            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

            return responcebean;
        }

        @Override
        protected void onPostExecute(Responcebean responcebean) {
            // TODO Auto-generated method stub
            super.onPostExecute(responcebean);
            try {
                super.onPostExecute(responcebean);

                superRecyclerView_news_items.getProgressView().setVisibility(View.GONE);

                if (responcebean.isServiceSuccess()) {
                    if (responcebean.getIsProductExist() == 1) {
                        //call get product listing service
                        new GetSearchResults(1).execute();
                    } else if (responcebean.getIsProductExist() == 2) {
                        //display dialog no product found
                        onMoreWorking = true;

                        //showDialogForProductExistOrNot();
                        Intent intent = new Intent(SearchActivity.this,
                                AddProductActivity.class);
                        intent.putExtra("upcCode", searchKeyword);
                        startActivityForResult(intent, CommonKeyword.RESPONCE_CODE_ADD_PRODUCT);
                    }

                } else {

                }


            } catch (Exception e) {
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }


        }

    }

    public void setUpArraylist(ArrayList<Searchbean> msearchbeanArrayList, int index) {
        try {
            if (index == 1) {
                if (msearchbeanArrayList == null) {

                } else {
                    if (msearchbeanArrayList.size() == 0) {

                    } else {
                        Searchbean searchbean = msearchbeanArrayList.get(0);
                        if (searchbean.isServiceSuccess()) {
                            searchbeanArrayList = msearchbeanArrayList;
                            searchAdapter = new SearchAdapter(SearchActivity.this);
                            superRecyclerView_news_items.setAdapter(searchAdapter);

                        } else {
                            searchbeanArrayList.clear();
                            searchAdapter = new SearchAdapter(SearchActivity.this);
                            superRecyclerView_news_items.setAdapter(searchAdapter);

                            if (searchbean.getErrorMessage() != null) {
                                customTextview_empty_message.setText(searchbean.getErrorMessage());

                            } else {
                                customTextview_empty_message.setText(getString(R.string.no_result_found));

                            }
                        }
                    }
                }


            } else if (index == 2) {

                if (msearchbeanArrayList == null) {

                } else {
                    if (msearchbeanArrayList.size() == 0) {

                    } else {
                        Searchbean searchbean = msearchbeanArrayList.get(0);
                        if (searchbean.isServiceSuccess()) {

                            int item_count = msearchbeanArrayList.size() - 1;
                            for (int i = 0; i < msearchbeanArrayList.size(); i++) {

                                Searchbean searchbean_i = msearchbeanArrayList.get(i);
                                searchbeanArrayList.add(searchbean_i);

                                item_count++;
                                searchAdapter.notifyItemInserted(item_count);
                            }
                        } else {
                            if (searchbean.getErrorMessage() != null) {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
    }

    public void show() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
    }

    public void hide() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CommonKeyword.RESPONCE_CODE_PRODUCT_DETAIL) {
            if (resultCode == Activity.RESULT_OK) {

                Searchbean mBean = (Searchbean) data.getSerializableExtra("Bean");
                searchbeanArrayList.set(pos, mBean);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        } else if (requestCode == CommonKeyword.RESPONCE_CODE_ADD_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                String upcCode = data.getStringExtra("result");
                new GetSearchResults(1).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

    }
}