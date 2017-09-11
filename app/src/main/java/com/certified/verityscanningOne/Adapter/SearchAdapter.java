package com.certified.verityscanningOne.Adapter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.SearchActivity;
import com.certified.verityscanningOne.Utils.CommonMethods;
import com.certified.verityscanningOne.beans.Searchbean;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;

    FragmentActivity activity = null;
    String TAG="SearchAdapter";


    public SearchAdapter(FragmentActivity mactivity) {
        activity = mactivity;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        try {
            view = LayoutInflater.from(activity).inflate(R.layout.search_screen_dynamical, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return new CustomViewHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        try {
            //Setting text view title


            Searchbean searchbean = SearchActivity.searchbeanArrayList.get(i);
            final CustomViewHolder customViewHolder = (CustomViewHolder) holder;

            if (searchbean != null)

            {
                customViewHolder.view.setTag(searchbean);


                setDefualtTextview(customViewHolder.textView_productName, searchbean.getProductName());
                setDefualtTextview(customViewHolder.textView_Upccode, searchbean.getUpcCode());
                setDefualtTextview(customViewHolder.textView_categoryName, searchbean.getCategory());


                if (CommonMethods.isImageUrlValid(searchbean.getProductImage())) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                            .displayImage(searchbean.getProductImage(), customViewHolder.imageview_product, SearchActivity.displayImageOptions, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    customViewHolder.progressBar.setProgress(0);
                                    customViewHolder.progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    customViewHolder.progressBar.setVisibility(View.GONE);
                                    customViewHolder.imageview_product.setVisibility(View.VISIBLE);
                                    customViewHolder.imageview_product.setImageResource(R.drawable.noimagefound);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    customViewHolder.progressBar.setVisibility(View.GONE);
                                    customViewHolder.imageview_product.setVisibility(View.VISIBLE);
                                    customViewHolder.imageview_product.setImageBitmap(loadedImage);
                                }
                            }, new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                    customViewHolder.progressBar.setProgress(Math.round(100.0f * current / total));
                                }
                            });

                } else {
                    customViewHolder.progressBar.setVisibility(View.GONE);
                    customViewHolder.imageview_product.setVisibility(View.VISIBLE);
                    customViewHolder.imageview_product.setImageResource(R.drawable.noimagefound);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }


    }

    @Override
    public int getItemCount() {
        try {
            return (null != SearchActivity.searchbeanArrayList ? SearchActivity.searchbeanArrayList.size() : 0);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview_product = null;
        TextView textView_productName, textView_Upccode, textView_categoryName = null;
        View view_line = null;
        ProgressBar progressBar = null;
        View view = null;


        public CustomViewHolder(View convertView) {
            super(convertView);
            // viewpager = (ViewPager) view.findViewById(R.id.viewpager);


            try {
                imageview_product = (ImageView) convertView.findViewById(R.id.imageview_product);

                textView_productName = (TextView) convertView.findViewById(R.id.textview_productName);
                textView_Upccode = (TextView) convertView.findViewById(R.id.textview_upccode);
                textView_categoryName = (TextView) convertView.findViewById(R.id.textview_categoryName);

                progressBar = (ProgressBar) convertView.findViewById(R.id.progressbar_news_thumb);
                view = convertView;
            } catch (Exception e) {
                e.printStackTrace();
                CommonMethods.printFirebaseLogcat(TAG, e, null);

            }

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
/*
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }*/

}
