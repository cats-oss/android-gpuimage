
package jp.cyberagent.android.gpuimage.sample.utils;

import jp.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraHelperImpl;
import jp.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraInfo2;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

@TargetApi(9)
public class CameraHelperGB implements CameraHelperImpl {

    @Override
    public int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    @Override
    public Camera openCamera(final int id) {
        return Camera.open(id);
    }

    @Override
    public Camera openDefaultCamera() {
        return Camera.open(0);
    }

    @Override
    public boolean hasCamera(final int facing) {
        return getCameraId(facing) != -1;
    }

    @Override
    public Camera openCameraFacing(final int facing) {
        return Camera.open(getCameraId(facing));
    }

    @Override
    public void getCameraInfo(final int cameraId, final CameraInfo2 cameraInfo) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        cameraInfo.facing = info.facing;
        cameraInfo.orientation = info.orientation;
    }

    private int getCameraId(final int facing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo info = new CameraInfo();
        for (int id = 0; id < numberOfCameras; id++) {
            Camera.getCameraInfo(id, info);
            if (info.facing == facing) {
                return id;
            }
        }
        return -1;
    }
}
