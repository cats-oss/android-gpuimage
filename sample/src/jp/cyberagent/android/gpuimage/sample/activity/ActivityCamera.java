
package jp.cyberagent.android.gpuimage.sample.activity;

import jp.cyberagent.android.gpuimage.GPUImage;
import jp.cyberagent.android.gpuimage.GPUImageFilter;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.cyberagent.android.gpuimage.sample.R;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActivityCamera extends Activity implements OnSeekBarChangeListener,
        OnClickListener {

    private GPUImage mGPUImage;
    private CameraHelper mCamera;
    private GPUImageFilter mFilter;
    private FilterAdjuster mFilterAdjuster;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
        findViewById(R.id.button_choose_filter).setOnClickListener(this);

        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));

        mCamera = new CameraHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.onResume();
    }

    @Override
    protected void onPause() {
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
            mGPUImage.setFilter(mFilter);
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
            mCameraInstance.setParameters(parameters);

            mGPUImage.setUpCamera(mCameraInstance);
        }

        public void onPause() {
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
                e.printStackTrace();
            }
            return c;
        }
    }
}
