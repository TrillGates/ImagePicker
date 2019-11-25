package com.sunofbeaches.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class ScreenSizeUtil {
    public static void getScreenSize(Context context,Point point) {
        WindowManager vm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        vm.getDefaultDisplay().getRealSize(point);
    }
}
