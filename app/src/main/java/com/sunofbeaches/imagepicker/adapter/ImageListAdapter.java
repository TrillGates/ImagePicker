package com.sunofbeaches.imagepicker.adapter;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunofbeaches.imagepicker.R;
import com.sunofbeaches.imagepicker.domain.ImageItem;
import com.sunofbeaches.imagepicker.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.InnerHolder> {

    private List<ImageItem> mImageItems = new ArrayList<>();
    private List<ImageItem> mSelectedItems = new ArrayList<>();
    private OnItemSelectedChangeListener mItemSelectedChangeListener = null;
    public static final int MAX_SELECTED_COUNT = 9;
    private int maxSelectedCount = MAX_SELECTED_COUNT;

    public List<ImageItem> getSelectedItems() {
        return mSelectedItems;
    }

    public void setSelectedItems(List<ImageItem> selectedItems) {
        mSelectedItems = selectedItems;
    }

    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        //加载ItemView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        Point point = SizeUtils.getScreenSize(itemView.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(point.x / 3,point.x / 3);
        itemView.setLayoutParams(layoutParams);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder,int position) {
        //绑定数据
        final View itemView = holder.itemView;
        ImageView imageView = itemView.findViewById(R.id.image_iv);
        final ImageItem imageItem = mImageItems.get(position);
        final CheckBox checkBox = itemView.findViewById(R.id.image_check_box);
        final View cover = itemView.findViewById(R.id.image_cover);
        Glide.with(imageView.getContext()).load(imageItem.getPath()).into(imageView);
        //根据数据状态显示内容

        if(mSelectedItems.contains(imageItem)) {
            //没有选择上，应该上
            mSelectedItems.add(imageItem);
            //修改UI
            checkBox.setChecked(false);
            cover.setVisibility(View.VISIBLE);
            checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.mipmap.pick_select_checked));
        } else {
            //已经选择上了，应该取消选择
            mSelectedItems.remove(imageItem);
            //修改UI
            checkBox.setChecked(true);
            checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.mipmap.pick_select_unchecked));
            cover.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否选择上
                //如果选择上就变成取消，
                //如果没选择上，就选择上
                if(mSelectedItems.contains(imageItem)) {
                    //已经选择上了，应该取消选择
                    mSelectedItems.remove(imageItem);
                    //修改UI
                    checkBox.setChecked(true);
                    checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.mipmap.pick_select_unchecked));
                    cover.setVisibility(View.GONE);
                } else {
                    if(mSelectedItems.size() >= maxSelectedCount) {
                        //给个提示
                        Toast toast = Toast.makeText(checkBox.getContext(),null,Toast.LENGTH_SHORT);
                        toast.setText("最多可以选择" + maxSelectedCount + "张图片");
                        toast.show();
                        return;
                    }

                    //没有选择上，应该上
                    mSelectedItems.add(imageItem);
                    //修改UI
                    checkBox.setChecked(false);
                    cover.setVisibility(View.VISIBLE);
                    checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.mipmap.pick_select_checked));
                }
                if(mItemSelectedChangeListener != null) {
                    mItemSelectedChangeListener.onItemSelectedChange(mSelectedItems);
                }
            }
        });
    }

    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener listener) {
        this.mItemSelectedChangeListener = listener;
    }

    public void release() {
        mSelectedItems.clear();
    }

    public interface OnItemSelectedChangeListener {
        void onItemSelectedChange(List<ImageItem> selectedItems);
    }

    @Override
    public int getItemCount() {
        return mImageItems.size();
    }

    public void setData(List<ImageItem> imageItems) {
        mImageItems.clear();
        mImageItems.addAll(imageItems);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
