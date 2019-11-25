package com.sunofbeaches.imagepicker.adapter;

import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunofbeaches.imagepicker.R;
import com.sunofbeaches.imagepicker.domain.ImageItem;
import com.sunofbeaches.imagepicker.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultImageAdapter extends RecyclerView.Adapter<ResultImageAdapter.InnerHolder> {

    private static final String TAG = "ResultImageAdapter";
    private List<ImageItem> mImageItems = new ArrayList<>();
    private int mHorizontalCount = 1;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        itemView.findViewById(R.id.image_check_box).setVisibility(View.GONE);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder,int position) {
        View itemView = holder.itemView;
        Point point = SizeUtils.getScreenSize(itemView.getContext());
        Log.d(TAG,"mHorizontalCount -- > " + mHorizontalCount);
        Log.d(TAG,"point width -- > " + point.x);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(point.x / mHorizontalCount,point.x / mHorizontalCount);
        itemView.setLayoutParams(layoutParams);
        ImageView imageView = itemView.findViewById(R.id.image_iv);
        ImageItem imageItem = mImageItems.get(position);
        Glide.with(imageView.getContext()).load(imageItem.getPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mImageItems.size();
    }

    public void setData(List<ImageItem> result,int horizontalCount) {
        this.mHorizontalCount = horizontalCount;
        mImageItems.clear();
        mImageItems.addAll(result);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
