package com.sunofbeaches.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sunofbeaches.imagepicker.adapter.ResultImageAdapter;
import com.sunofbeaches.imagepicker.domain.ImageItem;
import com.sunofbeaches.imagepicker.utils.PickerConfig;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by TrillGates in 2019/11/23
 *
 * @Description : main activity for user.
 * @Usage : called by system.
 **/
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements PickerConfig.OnImagesSelectedFinishedListener {
    public static final int MAX_SELECTED_COUNT = 9;

    public static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private RecyclerView mResultListView;
    private ResultImageAdapter mResultImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        checkPermission();
        initPickerConfig();
    }

    private void initView() {
        mResultListView = this.findViewById(R.id.result_list);
        mResultImageAdapter = new ResultImageAdapter();
        mResultListView.setAdapter(mResultImageAdapter);
    }

    private void initPickerConfig() {
        PickerConfig pickerConfig = PickerConfig.getInstance();
        pickerConfig.setMaxSelectedCount(MAX_SELECTED_COUNT);
        pickerConfig.setOnImagesSelectedFinishedListener(this);
    }

    private void checkPermission() {
        int readExStoragePermissionRest = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d(TAG,"readExStoragePermissionRest -- > " + readExStoragePermissionRest);
        if(readExStoragePermissionRest != PackageManager.PERMISSION_GRANTED) {
            //没有权限
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //有权限
            } else {
                //没有权限
                // 根据交互去处理了
            }
        }
    }

    public void pickImages(View view) {
        //打开另外一个界面
        startActivity(new Intent(this,PickerActivity.class));
    }

    @Override
    public void onSelectedFinished(List<ImageItem> result) {
        Log.d(TAG,"onSelectedFinished -- > " + result.size());
        //所选择的图片列表回来了。
        for(ImageItem imageItem : result) {
            Log.d(TAG,"item -- > " + imageItem);
        }
        int horizontalCount;
        if(result.size() < 3) {
            horizontalCount = result.size();
        } else {
            horizontalCount = 3;
        }
        mResultListView.setLayoutManager(null);
        mResultListView.setLayoutManager(new GridLayoutManager(this,horizontalCount));
        mResultImageAdapter.setData(result,horizontalCount);
    }
}
