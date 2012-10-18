
package jp.cyberagent.android.gpuimage.sample.utils;

import jp.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraHelperImpl;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class CameraHelperBase implements CameraHelperImpl {

    private final Context mContext;

    public CameraHelperBase(final Context context) {
        mContext = context;
    }

    @Override
    public Camera openDefaultCamera() {
        return Camera.open();
    }

    @Override
    public boolean hasCamera(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }
        return false;
    }

    @Override
    public Camera openCamera(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return Camera.open();
        }
        return null;
    }
}
