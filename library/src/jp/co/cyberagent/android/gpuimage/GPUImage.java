/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import jp.co.cyberagent.android.gpuimage.GPUImageRenderer.Rotation;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

/**
 * The main accessor for GPUImage functionality. This class helps to do common
 * tasks through a simple interface.
 */
public class GPUImage {
    private final Context mContext;
    private final GPUImageRenderer mRenderer;
    private GLSurfaceView mGlSurfaceView;
    private GPUImageFilter mFilter;
    private Bitmap mCurrentBitmap;

    /**
     * Instantiates a new GPUImage object.
     * 
     * @param context the context
     */
    public GPUImage(final Context context) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }

        mContext = context;
        mFilter = new GPUImageFilter();
        mRenderer = new GPUImageRenderer(mFilter);
    }

    /**
     * Checks if OpenGL ES 2.0 is supported on the current device.
     * 
     * @param context the context
     * @return true, if successful
     */
    private boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >=
                0x20000;
        return supportsEs2;
    }

    /**
     * Sets the GLSurfaceView which will display the preview.
     * 
     * @param view the GLSurfaceView
     */
    public void setGLSurfaceView(final GLSurfaceView view) {
        mGlSurfaceView = view;
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(mRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGlSurfaceView.requestRender();
    }

    /**
     * Request the preview to be rendered again.
     */
    public void requestRender() {
        if (mGlSurfaceView != null) {
            mGlSurfaceView.requestRender();
        }
    }

    /**
     * Sets the up camera to be connected to GPUImage to get a filtered preview.
     * 
     * @param camera the camera
     */
    public void setUpCamera(final Camera camera) {
        setUpCamera(camera, 0, false, false);
    }

    /**
     * Sets the up camera to be connected to GPUImage to get a filtered preview.
     * 
     * @param camera the camera
     * @param degrees by how many degrees the image should be rotated
     * @param flipHorizontal if the image should be flipped horizontally
     * @param flipVertical if the image should be flipped vertically
     */
    public void setUpCamera(final Camera camera, final int degrees, final boolean flipHorizontal,
            final boolean flipVertical) {
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            setUpCameraGingerbread(camera);
        } else {
            camera.setPreviewCallback(mRenderer);
            camera.startPreview();
        }
        Rotation rotation = Rotation.NORMAL;
        switch (degrees) {
            case 90:
                rotation = Rotation.ROTATION_90;
                break;
            case 180:
                rotation = Rotation.ROTATION_180;
                break;
            case 270:
                rotation = Rotation.ROTATION_270;
                break;
        }
        mRenderer.setRotation(rotation, flipHorizontal, flipVertical);
    }

    @TargetApi(11)
    private void setUpCameraGingerbread(final Camera camera) {
        mRenderer.setUpSurfaceTexture(camera);
    }

    /**
     * Sets the filter which should be applied to the image which was (or will
     * be) set by setImage(...).
     * 
     * @param filter the new filter
     */
    public void setFilter(final GPUImageFilter filter) {
        mFilter = filter;
        mRenderer.setFilter(mFilter);
        requestRender();
    }

    /**
     * Sets the image on which the filter should be applied.
     * 
     * @param bitmap the new image
     */
    public void setImage(final Bitmap bitmap) {
        setImage(bitmap, false);
        mCurrentBitmap = bitmap;
    }

    private void setImage(final Bitmap bitmap, final boolean recycle) {
        mRenderer.setImageBitmap(bitmap, recycle);
        requestRender();
    }

    /**
     * Sets the image on which the filter should be applied from a Uri.
     * 
     * @param uri the uri of the new image
     */
    public void setImage(final Uri uri) {
        setImage(new File(getPath(uri)));
    }

    /**
     * Sets the image on which the filter should be applied from a File.
     * 
     * @param file the file of the new image
     */
    public void setImage(final File file) {
        new LoadImageTask(this, file).run();
    }

    private String getPath(final Uri uri) {
        String[] projection = {
                MediaStore.Images.Media.DATA,
        };
        Cursor cursor = mContext.getContentResolver()
                .query(uri, projection, null, null, null);
        int pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String path = null;
        if (cursor.moveToFirst()) {
            path = cursor.getString(pathIndex);
        }
        cursor.close();
        return path;
    }

    /**
     * Gets the current displayed image with applied filter as a Bitmap.
     * 
     * @return the current image with filter applied
     */
    public Bitmap getBitmapWithFilterApplied() {
        return getBitmapWithFilterApplied(mCurrentBitmap);
    }

    /**
     * Gets the given bitmap with current filter applied as a Bitmap.
     * 
     * @param bitmap the bitmap on which the current filter should be applied
     * @return the bitmap with filter applied
     */
    public Bitmap getBitmapWithFilterApplied(final Bitmap bitmap) {
        if (mGlSurfaceView != null) {
            mRenderer.deleteImage();
            final Semaphore lock = new Semaphore(0);
            mRenderer.runOnDraw(new Runnable() {

                @Override
                public void run() {
                    mFilter.onDestroy();
                    lock.release();
                }
            });
            requestRender();

            try {
                lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GPUImageRenderer renderer = new GPUImageRenderer(mFilter);
        renderer.setRotation(Rotation.NORMAL,
                mRenderer.isFlippedHorizontally(), mRenderer.isFlippedVertically());
        PixelBuffer buffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        buffer.setRenderer(renderer);
        renderer.setImageBitmap(bitmap, false);
        Bitmap result = buffer.getBitmap();
        mFilter.onDestroy();
        renderer.deleteImage();
        buffer.destroy();

        mRenderer.setFilter(mFilter);
        if (mCurrentBitmap != null) {
            mRenderer.setImageBitmap(mCurrentBitmap, false);
        }
        requestRender();

        return result;
    }

    /**
     * Gets the images for multiple filters on a image. This can be used to
     * quickly get thumbnail images for filters. <br />
     * Whenever a new Bitmap is ready, the listener will be called with the
     * bitmap. The order of the calls to the listener will be the same as the
     * filter order.
     * 
     * @param bitmap the bitmap on which the filters will be applied
     * @param filters the filters which will be applied on the bitmap
     * @param listener the listener on which the results will be notified
     */
    public static void getBitmapForMultipleFilters(final Bitmap bitmap,
            final List<GPUImageFilter> filters, final ResponseListener<Bitmap> listener) {
        if (filters.isEmpty()) {
            return;
        }
        GPUImageRenderer renderer = new GPUImageRenderer(filters.get(0));
        renderer.setImageBitmap(bitmap, false);
        PixelBuffer buffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        buffer.setRenderer(renderer);

        for (GPUImageFilter filter : filters) {
            renderer.setFilter(filter);
            listener.response(buffer.getBitmap());
            filter.onDestroy();
        }
        renderer.deleteImage();
        buffer.destroy();
    }

    /**
     * Save current image with applied filter to Pictures. It will be stored on
     * the default Picture folder on the phone below the given folerName and
     * fileName. <br />
     * This method is async and will notify when the image was saved through the
     * listener.
     * 
     * @param folderName the folder name
     * @param fileName the file name
     * @param listener the listener
     */
    public void saveToPictures(final String folderName, final String fileName,
            final OnPictureSavedListener listener) {
        saveToPictures(mCurrentBitmap, folderName, fileName, listener);
    }

    /**
     * Apply and save the given bitmap with applied filter to Pictures. It will
     * be stored on the default Picture folder on the phone below the given
     * folerName and fileName. <br />
     * This method is async and will notify when the image was saved through the
     * listener.
     * 
     * @param bitmap the bitmap
     * @param folderName the folder name
     * @param fileName the file name
     * @param listener the listener
     */
    public void saveToPictures(final Bitmap bitmap, final String folderName, final String fileName,
            final OnPictureSavedListener listener) {
        new SaveTask(bitmap, folderName, fileName, listener).execute();
    }

    private class SaveTask extends AsyncTask<Void, Void, Void> {

        private final Bitmap mBitmap;
        private final String mFolderName;
        private final String mFileName;
        private final OnPictureSavedListener mListener;
        private final Handler mHandler;

        public SaveTask(final Bitmap bitmap, final String folderName, final String fileName,
                final OnPictureSavedListener listener) {
            mBitmap = bitmap;
            mFolderName = folderName;
            mFileName = fileName;
            mListener = listener;
            mHandler = new Handler();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Bitmap result = getBitmapWithFilterApplied(mBitmap);
            saveImage(mFolderName, mFileName, result);
            return null;
        }

        private void saveImage(final String folderName, final String fileName, final Bitmap image) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, folderName + "/" + fileName);
            try {
                file.getParentFile().mkdirs();
                image.compress(CompressFormat.JPEG, 80, new FileOutputStream(file));
                MediaScannerConnection.scanFile(mContext,
                        new String[] {
                            file.toString()
                        }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path, final Uri uri) {
                                if (mListener != null) {
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            mListener.onPictureSaved(uri);
                                        }
                                    });
                                }
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPictureSavedListener {
        void onPictureSaved(Uri uri);
    }

    private class LoadImageTask implements Runnable {

        private final GPUImage mGPUImage;
        private final File mImageFile;
        private final int mMaxWidth;
        private final int mMaxHeight;

        @SuppressWarnings("deprecation")
        public LoadImageTask(final GPUImage gpuImage, final File file) {
            mImageFile = file;
            mGPUImage = gpuImage;

            WindowManager windowManager =
                    (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            mMaxWidth = display.getWidth();
            mMaxHeight = display.getHeight();
        }

        @Override
        public void run() {
            Bitmap bitmap = loadResizedImage(mImageFile);
            mGPUImage.setImage(bitmap);
        }

        private Bitmap loadResizedImage(final File imageFile) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            int scale = 1;
            while (options.outWidth / scale > mMaxWidth || options.outHeight / scale > mMaxHeight) {
                scale++;
            }
            Bitmap bitmap = null;
            Bitmap scaledBitmap = null;
            if (scale > 1) {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                if (bitmap == null) {
                    return null;
                }

                // resize to desired dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double newWidth;
                double newHeight;
                if ((double) width / mMaxWidth < (double) height / mMaxHeight) {
                    newHeight = mMaxHeight;
                    newWidth = (newHeight / height) * width;
                } else {
                    newWidth = mMaxWidth;
                    newHeight = (newWidth / width) * height;
                }

                scaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round((float) newWidth),
                        Math.round((float) newHeight), true);
                bitmap.recycle();
                bitmap = scaledBitmap;

                System.gc();
            } else {
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }

            return rotateImage(bitmap, imageFile);
        }

        private Bitmap rotateImage(final Bitmap bitmap, final File fileWithExifInfo) {
            if (bitmap == null) {
                return null;
            }
            Bitmap rotatedBitmap = bitmap;
            int orientation = 0;
            try {
                orientation = getImageOrientation(fileWithExifInfo.getAbsolutePath());
                if (orientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rotatedBitmap;
        }

        private int getImageOrientation(final String file) throws IOException {
            ExifInterface exif = new ExifInterface(file);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return 0;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        }
    }

    public interface ResponseListener<T> {
        void response(T item);
    }
}
