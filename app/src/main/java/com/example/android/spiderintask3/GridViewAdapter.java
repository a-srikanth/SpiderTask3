package com.example.android.spiderintask3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Srikanth on 7/7/2016.
 */
public class GridViewAdapter extends ArrayAdapter<GridItem> {

    private Context mContext;
    private Activity mActivity;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

    public GridViewAdapter(Activity activity , Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        mActivity = activity;
    }


    /**
     * Updates grid data and refresh grid items.
     * //@param mGridData
     */
    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (mActivity).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_imageview);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
        //holder.imageView.setImageResource(item.getImagePath());
        if(item.getImagePath().equalsIgnoreCase("unavailable")) {
            holder.imageView.setImageResource(R.drawable.poster_unavailable);
        }else{
            if(item.getImagePath().equals("N/A"))
                Picasso.with(mContext).load(R.drawable.poster_unavailable).into(holder.imageView);
            else
                Picasso.with(mContext).load(item.getImagePath()).into(holder.imageView);
        }
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}