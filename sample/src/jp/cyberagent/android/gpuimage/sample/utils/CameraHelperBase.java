
package jp.cyberagent.android.gpuimage.sample.utils;

import jp.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraHelperImpl;
import jp.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraInfo2;
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
    public int getNumberOfCameras() {
        return hasCameraSupport() ? 1 : 0;
    }

    @Override
    public Camera openCamera(final int id) {
        return Camera.open();
    }

    @Override
    public Camera openDefaultCamera() {
        return Camera.open();
    }

    @Override
    public boolean hasCamera(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return hasCameraSupport();
        }
        return false;
    }

    @Override
    public Camera openCameraFacing(final int facing) {
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return Camera.open();
        }
        return null;
    }

    @Override
    public void getCameraInfo(final int cameraId, final CameraInfo2 cameraInfo) {
        cameraInfo.facing = Camera.CameraInfo.CAMERA_FACING_BACK;
        cameraInfo.orientation = 90;
    }

    private boolean hasCameraSupport() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
