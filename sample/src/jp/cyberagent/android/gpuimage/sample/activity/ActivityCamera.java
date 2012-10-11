
package jp.cyberagent.android.gpuimage.sample.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.cyberagent.android.gpuimage.GPUImage;
import jp.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.cyberagent.android.gpuimage.GPUImageFilter;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.cyberagent.android.gpuimage.sample.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
        findViewById(R.id.button_capture).setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.button_choose_filter:
                GPUImageFilterTools.showDialog(this, new OnGpuImageFilterChosenListener() {

                    @Override
                    public void onGpuImageFilterChosenListener(final GPUImageFilter filter) {
                        switchFilterTo(filter);
                    }
                });
                break;

            case R.id.button_capture:
                Camera.Size size = mCamera.mCameraInstance.getParameters().getPictureSize();
                Log.i("ASDF", size.width + "x" + size.height);
                // TODO get a size that is about the size of the screen
                Camera.Parameters params = mCamera.mCameraInstance.getParameters();
                params.setPictureSize(1280, 960);
                params.setRotation(90);
                mCamera.mCameraInstance.setParameters(params);
                for (Camera.Size size2 : mCamera.mCameraInstance.getParameters()
                        .getSupportedPictureSizes()) {
                    Log.i("ASDF", "Supported: " + size2.width + "x" + size2.height);
                }
                mCamera.mCameraInstance.autoFocus(new Camera.AutoFocusCallback() {

                    @Override
                    public void onAutoFocus(final boolean success, final Camera camera) {
                        mCamera.mCameraInstance.takePicture(null, null,
                                new Camera.PictureCallback() {

                                    @Override
                                    public void onPictureTaken(byte[] data, final Camera camera) {

                                        final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                        if (pictureFile == null) {
                                            Log.d("ASDF",
                                                    "Error creating media file, check storage permissions");
                                            return;
                                        }

                                        try {
                                            FileOutputStream fos = new FileOutputStream(pictureFile);
                                            fos.write(data);
                                            fos.close();
                                        } catch (FileNotFoundException e) {
                                            Log.d("ASDF", "File not found: " + e.getMessage());
                                        } catch (IOException e) {
                                            Log.d("ASDF", "Error accessing file: " + e.getMessage());
                                        }

                                        data = null;
                                        Bitmap bitmap = BitmapFactory.decodeFile(pictureFile
                                                .getAbsolutePath());
                                        // mGPUImage.setImage(bitmap);
                                        final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                                        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                                        mGPUImage.saveToPictures(bitmap, "GPUImage",
                                                System.currentTimeMillis() + ".jpg",
                                                new OnPictureSavedListener() {

                                                    @Override
                                                    public void onPictureSaved(final Uri
                                                            uri) {
                                                        pictureFile.delete();
                                                        camera.startPreview();
                                                        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                                                    }
                                                });
                                    }
                                });
                    }
                });
                break;
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static File getOutputMediaFile(final int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
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
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
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
