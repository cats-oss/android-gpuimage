
package jp.cyberagent.android.gpuimage.sample.utils;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

public class CameraHelper {
    private final CameraHelperImpl mImpl;

    public CameraHelper(final Context context) {
        if (SDK_INT >= GINGERBREAD) {
            mImpl = new CameraHelperGB();
        } else {
            mImpl = new CameraHelperBase(context);
        }
    }

    public interface CameraHelperImpl {
        Camera openDefaultCamera();

        Camera openCamera(int facing);

        boolean hasCamera(int cameraFacingFront);
    }

    public Camera openDefaultCamera() {
        return mImpl.openDefaultCamera();
    }

    public Camera openFrontCamera() {
        return mImpl.openCamera(CameraInfo.CAMERA_FACING_FRONT);
    }

    public Camera openBackCamera() {
        return mImpl.openCamera(CameraInfo.CAMERA_FACING_BACK);
    }

    public boolean hasFrontCamera() {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_FRONT);
    }

    public boolean hasBackCamera() {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_BACK);
    }
}
