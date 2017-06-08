package jp.co.cyberagent.android.gpuimage.videosupport;

/**
 * Created by Ronan Burns on 02/06/2017.
 */

public interface VideoFrameCallback
{
    void onVideoFrame(byte[] data, int width, int height);
}
