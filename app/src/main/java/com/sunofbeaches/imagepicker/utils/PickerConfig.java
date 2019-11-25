package com.sunofbeaches.imagepicker.utils;

import com.sunofbeaches.imagepicker.domain.ImageItem;

import java.util.List;

public class PickerConfig {

    private PickerConfig() {
    }

    private static PickerConfig sPickerConfig;

    public static PickerConfig getInstance() {
        if(sPickerConfig == null) {
            sPickerConfig = new PickerConfig();
        }
        return sPickerConfig;
    }


    private int maxSelectedCount = 1;
    private OnImagesSelectedFinishedListener mImageSelecteFinishedListener = null;


    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    public OnImagesSelectedFinishedListener getImageSelecteFinishedListener() {
        return mImageSelecteFinishedListener;
    }

    public void setOnImagesSelectedFinishedListener(OnImagesSelectedFinishedListener listener) {
        this.mImageSelecteFinishedListener = listener;
    }

    public interface OnImagesSelectedFinishedListener {
        void onSelectedFinished(List<ImageItem> result);
    }
}
