package com.certified.verityscanningOne.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.certified.verityscanningOne.Model.RssFeed;
import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.RssFeedsDetails;

import java.util.List;

/**
 * Created by harmis on 19/5/17.
 */

public class RssFeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RssFeed> mList;
    private Activity context;

    public RssFeedListAdapter(List<RssFeed> mList, Activity context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_feeds_list_items, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RssFeed result = mList.get(position);
        final MyViewHolder customViewHolder = (MyViewHolder) holder;
        customViewHolder.result = result;
        customViewHolder.txtTitle.setText(result.getTitle());
        customViewHolder.txtdesc.setText(result.getDescription());
        customViewHolder.txtDate.setText(result.getPubDate());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtTitle, txtdesc, txtDate;
        RssFeed result;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);


            txtTitle = (TextView) itemLayoutView.findViewById(R.id.txtRssfeedsTitle);
            txtdesc = (TextView) itemLayoutView.findViewById(R.id.txtRssfeedsdesc);
            txtDate = (TextView) itemLayoutView.findViewById(R.id.txtRssfeedsdate);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent( context, RssFeedsDetails.class);
                        intent.putExtra("bean", result);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }


    }


}

