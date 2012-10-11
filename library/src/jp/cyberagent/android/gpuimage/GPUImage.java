
package jp.cyberagent.android.gpuimage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

public class GPUImage {
    private final GLSurfaceView mGlSurfaceView;
    private final GPUImageRenderer mRenderer;
    private GPUImageFilter mFilter;
    private Bitmap mCurrentBitmap;

    public GPUImage(final Activity activity, final GLSurfaceView glSurfaceView) {
        mGlSurfaceView = glSurfaceView;

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager)
                activity.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >=
                0x20000;
        if (!supportsEs2) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }

        mFilter = new GPUImageFilter();
        mRenderer = new GPUImageRenderer(mFilter);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(mRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGlSurfaceView.requestRender();
    }

    public void requestRender() {
        mGlSurfaceView.requestRender();
    }

    public void setFilter(final GPUImageFilter filter) {
        mFilter = filter;
        mRenderer.setFilter(mFilter);
    }

    public void setImage(final Bitmap bitmap) {
        setImage(bitmap, false);
        mCurrentBitmap = bitmap;
    }

    private void setImage(final Bitmap bitmap, final boolean recycle) {
        mRenderer.setImageBitmap(bitmap, recycle);
        mGlSurfaceView.requestRender();
    }

    public Bitmap getBitmapWithFilterApplied() {
        mRenderer.deleteImage();
        mRenderer.runOnDraw(new Runnable() {

            @Override
            public void run() {
                mFilter.onDestroy();
            }
        });
        requestRender();

        GPUImageRenderer renderer = new GPUImageRenderer(mFilter);
        PixelBuffer buffer = new PixelBuffer(mCurrentBitmap.getWidth(),
                mCurrentBitmap.getHeight());
        buffer.setRenderer(renderer);
        renderer.setImageBitmap(mCurrentBitmap, false);
        Bitmap result = buffer.getBitmap();
        mFilter.onDestroy();
        renderer.deleteImage();
        buffer.destroy();

        mRenderer.setFilter(mFilter);
        mRenderer.setImageBitmap(mCurrentBitmap, false);
        mGlSurfaceView.requestRender();

        return result;
    }
}
