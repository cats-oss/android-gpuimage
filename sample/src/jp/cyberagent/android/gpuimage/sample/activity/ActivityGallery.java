
package jp.cyberagent.android.gpuimage.sample.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.cyberagent.android.gpuimage.GPUImageFilter;
import jp.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.cyberagent.android.gpuimage.PixelBuffer;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.cyberagent.android.gpuimage.sample.ImageUtils;
import jp.cyberagent.android.gpuimage.sample.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class ActivityGallery extends Activity implements OnSeekBarChangeListener,
        OnClickListener {

    private static final int REQUEST_PICK_IMAGE = 1;
    private GLSurfaceView mGLSurfaceView;
    private GPUImageFilter mFilter;
    private FilterAdjuster mFilterAdjuster;
    private GPUImageRenderer mRenderer;
    private Bitmap mCurrentBitmap;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
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
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGLSurfaceView.requestRender();

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
        findViewById(R.id.button_choose_filter).setOnClickListener(this);
        findViewById(R.id.button_save).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    handleImage(data.getData());
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_choose_filter:
                GPUImageFilterTools.showDialog(this, new OnGpuImageFilterChosenListener() {

                    @Override
                    public void onGpuImageFilterChosenListener(final GPUImageFilter filter) {
                        switchFilterTo(filter);
                        mGLSurfaceView.requestRender();
                    }

                });
                break;
            case R.id.button_save:
                saveImage();
                break;

            default:
                break;
        }

    }

    private void saveImage() {
        new SaveTask().execute();
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
        mGLSurfaceView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    private void handleImage(final Uri selectedImage) {
        new ShowImage(new File(getPath(selectedImage))).execute();
    }

    private String getPath(final Uri uri) {
        String[] projection = {
                MediaStore.Images.Media.DATA,
        };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String path = null;
        if (cursor.moveToFirst()) {
            path = cursor.getString(pathIndex);
        }
        cursor.close();
        return path;
    }

    private class ShowImage extends AsyncTask<Void, Void, Bitmap> {

        private final File mImageFile;

        public ShowImage(final File file) {
            mImageFile = file;
        }

        @Override
        protected Bitmap doInBackground(final Void... params) {
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
            return ImageUtils.loadResizedImageMax(mImageFile, screenWidth, screenHeight);
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            super.onPostExecute(result);
            mRenderer.setImageBitmap(result, false);
            mGLSurfaceView.requestRender();
            mCurrentBitmap = result;
        }
    }

    private class SaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            GPUImageRenderer renderer = new GPUImageRenderer(mFilter);
            PixelBuffer buffer = new PixelBuffer(mCurrentBitmap.getWidth(),
                    mCurrentBitmap.getHeight());
            buffer.setRenderer(renderer);
            renderer.setImageBitmap(mCurrentBitmap, false);
            Bitmap result = buffer.getBitmap();
            String fileName = System.currentTimeMillis() + ".jpg";
            saveImage("GPUImage", fileName, result);
            return null;
        }

        private void saveImage(final String folderName, final String fileName, final Bitmap image) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, folderName + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                image.compress(CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(ActivityGallery.this,
                        new String[] {
                            file.toString()
                        }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path, final Uri uri) {
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            mRenderer.deleteImage();
            mRenderer.setFilter(mFilter);
            mRenderer.setImageBitmap(mCurrentBitmap, false);
            mGLSurfaceView.requestRender();
            Toast.makeText(ActivityGallery.this, "Image saved", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
