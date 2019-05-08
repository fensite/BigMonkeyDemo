package com.fensite.screening.utils;

import android.hardware.Camera;

/**
 * create by zhaikn on 2019/1/24
 * 通用工具类
 */
public class CommonUtil {

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            if (mCamera != null)
                mCamera.release();
            mCamera = null;
        }
        return canUse;
    }
}
