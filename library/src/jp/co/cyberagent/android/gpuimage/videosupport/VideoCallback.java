package jp.co.cyberagent.android.gpuimage.videosupport;

/**
 * Created by Ronan Burns on 02/06/2017.
 */

public interface VideoCallback
{
    void onVideoFrame(byte[] data, int width, int height);
}
