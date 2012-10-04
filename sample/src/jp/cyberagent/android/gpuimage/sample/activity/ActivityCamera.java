
package jp.cyberagent.android.gpuimage.sample.activity;

import java.io.IOException;

import jp.cyberagent.android.gpuimage.GPUImageFilter;
import jp.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.cyberagent.android.gpuimage.GPUImageRenderer.Rotation;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.cyberagent.android.gpuimage.sample.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActivityCamera extends Activity implements OnSeekBarChangeListener,
        OnClickListener {

    private GLSurfaceView mGLSurfaceView;
    private CameraHelper mCamera;
    private GPUImageFilter mFilter;
    private FilterAdjuster mFilterAdjuster;
    private GPUImageRenderer mRenderer;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >=
                0x20000;
        if (supportsEs2) {
            // Handle if not supported?
        }

        mFilter = new GPUImageFilter();
        mRenderer = new GPUImageRenderer(mFilter);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(mRenderer);
        mGLSurfaceView.requestRender();

        // Camera camera = Camera.open();
        // // get the pixel format of the camera
        // // the default is YCbCr_420_SP (NV21), see:
        // int pixelFormat = camera.getParameters().getPreviewFormat();
        // try {
        // renderer = new OpenGLCamRenderer(pixelFormat, res);
        // } catch (Exception e) {
        // e.printStackTrace();
        // finish();
        // }

        // Render the view only when there is a change in the drawing data
        // TODO uncomment this if not camera
        // mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mCamera = new CameraHelper();

        findViewById(R.id.button_choose_filter).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        mGLSurfaceView.onPause();
        mCamera.onPause();
        super.onPause();
    }

    @Override
    public void onClick(final View v) {
        GPUImageFilterTools.showDialog(this, new OnGpuImageFilterChosenListener() {

            @Override
            public void onGpuImageFilterChosenListener(final GPUImageFilter filter) {
                switchFilterTo(filter);
            }
        });
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mRenderer.setFilter(mFilter);
            mFilterAdjuster = new FilterAdjuster(mFilter);
        }
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    private class CameraHelper {
        private Camera mCameraInstance;

        public void onResume() {
            mCameraInstance = getCameraInstance();
            Parameters parameters = mCameraInstance.getParameters();
            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            parameters.setPreviewSize(720, 480);
            for (Size size : parameters.getSupportedPreviewSizes()) {
                Log.i("ASDF", "Size: " + size.width + "x" + size.height);
            }

            Log.i("ASDF", "Preview Format: " + parameters.getPreviewFormat());
            mCameraInstance.setParameters(parameters);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                setUpGingerbreadTexture();
            } else {
                mCameraInstance.setPreviewCallback(mRenderer);
                mCameraInstance.startPreview();
            }
            mRenderer.setRotation(Rotation.RIGHT);
        }

        @TargetApi(11)
        private void setUpGingerbreadTexture() {
            try {
                mRenderer.setUpSurfaceTexture(mCameraInstance);
                mCameraInstance.setPreviewTexture(new SurfaceTexture(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void onPause() {
            // mCameraInstance.stopPreview();
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }

        /** A safe way to get an instance of the Camera object. */
        private Camera getCameraInstance() {
            Camera c = null;
            try {
                // TODO allow opening front view camera with open(int) if exists
                // Camera.getNumberOfCameras()
                c = Camera.open();
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
            }
            return c; // returns null if camera is unavailable
        }
    }
}
