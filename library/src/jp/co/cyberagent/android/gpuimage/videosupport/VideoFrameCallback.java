package jp.co.cyberagent.android.gpuimage.videosupport;

/**
 * Created by Ronan Burns on 02/06/2017.
 */
/*
    This interface accepts video frames in YUV420sp (NV21) format, similar to most camera previews
 */
public interface VideoFrameCallback
{
    void onVideoFrame(byte[] data, int width, int height);
}
