package com.sunofbeaches.imagepicker;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sunofbeaches.imagepicker.adapter.ImageListAdapter;
import com.sunofbeaches.imagepicker.domain.ImageItem;
import com.sunofbeaches.imagepicker.utils.PickerConfig;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//D/PickerActivity: _data ==== /storage/emulated/0/DCIM/Screenshots/Screenshot_2019-11-19-09-13-53-473_com.sunofbeaches.imagepickerdemo.jpg
//D/PickerActivity: _size ==== 1449050
//D/PickerActivity: _display_name ==== Screenshot_2019-11-19-09-13-53-473_com.sunofbeaches.imagepickerdemo.jpg
//D/PickerActivity: mime_type ==== image/jpeg
//D/PickerActivity: title ==== Screenshot_2019-11-19-09-13-53-473_com.sunofbeaches.imagepickerdemo
//D/PickerActivity: date_added ==== 1574126033
//D/PickerActivity: date_modified ==== 1574126033
//D/PickerActivity: description ==== null
//D/PickerActivity: picasa_id ==== null
//D/PickerActivity: isprivate ==== null
//D/PickerActivity: latitude ==== null
//D/PickerActivity: longitude ==== null
//D/PickerActivity: datetaken ==== 1574126033473
//D/PickerActivity: orientation ==== null
//D/PickerActivity: mini_thumb_magic ==== null
//D/PickerActivity: bucket_id ==== -1313584517
//D/PickerActivity: bucket_display_name ==== Screenshots
//D/PickerActivity: width ==== 1080
//D/PickerActivity: height ==== 2340
//D/PickerActivity: ============================

public class PickerActivity extends AppCompatActivity implements ImageListAdapter.OnItemSelectedChangeListener {

    private static final String TAG = "PickerActivity";

    public static final int LOADER_ID = 1;

    private List<ImageItem> mImageItems = new ArrayList<>();
    private ImageListAdapter mImageListAdapter;
    private TextView mFinishView;
    private PickerConfig mPickerConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        initLoaderManager();
        //初始化view
        initView();
        initEvent();
        initConfig();
    }

    private void initConfig() {
        mPickerConfig = PickerConfig.getInstance();
        int maxSelectedCount = mPickerConfig.getMaxSelectedCount();
        mImageListAdapter.setMaxSelectedCount(maxSelectedCount);
    }

    private void initEvent() {
        mImageListAdapter.setOnItemSelectedChangeListener(this);
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取所选择的数据
                List<ImageItem> result = new ArrayList<>();
                result.addAll(mImageListAdapter.getSelectedItems());
                mImageListAdapter.release();
                //通知其他地方
                PickerConfig.OnImagesSelectedFinishedListener imageSelectedFinishedListener = mPickerConfig.getImageSelecteFinishedListener();
                Log.d(TAG,"OnImagesSelectedFinishedListener -- > " + imageSelectedFinishedListener);
                Log.d(TAG,"result size -- > " + result.size());
                if(imageSelectedFinishedListener != null) {
                    imageSelectedFinishedListener.onSelectedFinished(result);
                }
                //结束界面
                finish();
            }
        });
        findViewById(R.id.back_press_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mFinishView = this.findViewById(R.id.finish_tv);
        RecyclerView listView = this.findViewById(R.id.image_list_view);
        listView.setLayoutManager(new GridLayoutManager(this,3));
        //设置适配器
        mImageListAdapter = new ImageListAdapter();
        listView.setAdapter(mImageListAdapter);
    }

    private void initLoaderManager() {
        mImageItems.clear();
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_ID,null,new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id,@Nullable Bundle args) {
                if(id == LOADER_ID) {
                    return new CursorLoader(PickerActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{"_data","_display_name","date_added"},
                            null,null,null);
                }
                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader,Cursor cursor) {
                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        String path = cursor.getString(0);
                        String title = cursor.getString(1);
                        long date = cursor.getLong(2);
                        ImageItem imageItem = new ImageItem(path,title,date);
                        mImageItems.add(imageItem);
                    }
                    cursor.close();
//                    for(ImageItem imageItem : mImageItems) {
//                        Log.d(TAG,"imageItem == > " + imageItem);
//                    }
                    mImageListAdapter.setData(mImageItems);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    @Override
    public void onItemSelectedChange(List<ImageItem> selectedItems) {
        //所选择的数据发生变化
        mFinishView.setText("(" + selectedItems.size() + "/" + mImageListAdapter.getMaxSelectedCount() + ")完成");
    }
}
