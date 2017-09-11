package com.certified.verityscanningOne.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.beans.ReviewBean;

import java.util.List;


/**
 * Created by harmis on 1/7/16.
 */
public class ReviewListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;




    private List<ReviewBean> mList;
    static FragmentActivity mFragmentActivity;


    public ReviewListAdapter(FragmentActivity fragmentActivity, List<ReviewBean> data, RecyclerView mRecyclerView) {
        this.mList = data;
        this.mFragmentActivity = fragmentActivity;


    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public void delete(int position) { //removes the row

        Log.e("Position : ", "" + position);
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {

            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.review_list_layout, null);

            return new ViewHolder(itemLayoutView);
        } else if (viewType == VIEW_TYPE_LOADING) {


            return null;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            try {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.singleBean = mList.get(position);

                ReviewBean reviewbean = mList.get(position);


                String title = reviewbean.get_reviewTitle();
                if (title == null || title.equalsIgnoreCase("")) {
                    viewHolder.txtTitle.setText("");
                } else {
                    viewHolder.txtTitle.setText(title);
                }

                String desc = reviewbean.get_reviewText();
                if (desc == null || desc.equalsIgnoreCase("")) {
                    viewHolder.txtdesc.setText("");
                } else {
                    viewHolder.txtdesc.setText(desc);
                }

                String date = reviewbean.get_reviewDate();
                if (date == null || date.equalsIgnoreCase("")) {
                    viewHolder.txtDate.setText("");
                } else {
                    viewHolder.txtDate.setText(date);
                }


                String userName = reviewbean.get_userName();
                if (userName == null || userName.equalsIgnoreCase("")) {
                    viewHolder.txtUserName.setText("");
                } else {
                    viewHolder.txtUserName.setText(userName);
                }

                String strreviewRating = reviewbean.get_rating();
                float reviewRating;
                if (strreviewRating == null || strreviewRating.equalsIgnoreCase("")) {
                    reviewRating = 1;
                } else {
                    reviewRating = Float.parseFloat(strreviewRating);
                }
                viewHolder.ratingreviewSingle.setRating(reviewRating);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (holder instanceof LoadingViewHolder) {
            try {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtdesc, txtDate, txtUserName;
        RatingBar ratingreviewSingle;

        public ReviewBean singleBean;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            try {

                txtTitle = (TextView) itemLayoutView.findViewById(R.id.txtReviewTitle);
                txtdesc = (TextView) itemLayoutView.findViewById(R.id.txtReviewDesc);
                txtDate = (TextView) itemLayoutView.findViewById(R.id.reviewDate);
                txtUserName = (TextView) itemLayoutView.findViewById(R.id.txtReviewuserName);
                ratingreviewSingle = (RatingBar) itemLayoutView.findViewById(R.id.reviewRating);
                ratingreviewSingle.setEnabled(false);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


}