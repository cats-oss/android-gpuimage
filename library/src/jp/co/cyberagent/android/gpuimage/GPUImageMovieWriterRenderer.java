/*
 * Copyright (C) 2016 Peter Lu
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

import android.opengl.EGL14;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GPUImageMovieWriterRenderer extends GPUImageRenderer {

    public static final int RECORDING_OFF = 0;
    public static final int RECORDING_ON = 1;
    public static final int RECORDING_RESUMED = 2;

    private TextureMovieEncoder mVideoEncoder;
    private boolean mIsRecordingEnabled = false;
    private int mRecordingStatus = RECORDING_OFF;
    private File mOutputFile = null;
    private int mOutputWidth = 480;
    private int mOutputHeight = 640;
    private int mOutputBitrate = 1000000;

    public void changeRecordingState(boolean isRecording) {
        mIsRecordingEnabled = isRecording;
    }

    public GPUImageMovieWriterRenderer(GPUImageFilter filter, File outputFile) {
        super(filter);
        mOutputFile = outputFile;
        if (mVideoEncoder == null) {
            mVideoEncoder = new TextureMovieEncoder(mFilter);
        }
    }

    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        mRecordingStatus = mIsRecordingEnabled ? RECORDING_RESUMED : RECORDING_OFF;
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        super.onDrawFrame(gl);
        if (mIsRecordingEnabled) {
            switch (mRecordingStatus) {
                case RECORDING_OFF:
                    mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
                            mOutputFile, mOutputWidth, mOutputHeight, mOutputBitrate, EGL14.eglGetCurrentContext()
                    ));
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_RESUMED:
                    mVideoEncoder.updateSharedContext(EGL14.eglGetCurrentContext());
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_ON:
                    break;
                default:
                    throw new RuntimeException("unknown status: " + mRecordingStatus);
            }
        } else {
            switch (mRecordingStatus) {
                case RECORDING_ON:
                case RECORDING_RESUMED:
                    mVideoEncoder.stopRecording();
                    mRecordingStatus = RECORDING_OFF;
                    break;
                case RECORDING_OFF:
                    break;
                default:
                    throw new RuntimeException("unknown status: " + mRecordingStatus);
            }
        }
        mVideoEncoder.setTextureId(mGLTextureId);
        mVideoEncoder.frameAvailable(mSurfaceTexture);
    }

    @Override
    protected void adjustImageScaling() {
        super.adjustImageScaling();
        if (mVideoEncoder == null) {
            // this may be called before the child constructor
            mVideoEncoder = new TextureMovieEncoder(mFilter);
        }
        mVideoEncoder.setGLCubeBuffer(mGLCubeBuffer);
        mVideoEncoder.setGLTextureBuffer(mGLTextureBuffer);
    }

    @Override
    public void setFilter(GPUImageFilter filter) {
        super.setFilter(filter);
        mVideoEncoder.setFilter(filter);
    }

    public void setOutputResolution(int width, int height) {
        mOutputWidth = width;
        mOutputHeight = height;
    }

    public void setOutputBitrate(int bitrate) {
        mOutputBitrate = bitrate;
    }
}
