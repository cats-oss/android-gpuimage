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

import android.content.Context;

import java.io.File;

public class GPUImageMovieWriter extends GPUImage {

    public final static int RECORDING_OFF = 0;
    public final static int RECORDING_ON = 1;

    private int mRecordingState = RECORDING_OFF;

    public GPUImageMovieWriter(final Context context, final File outputFile) {
        super(context);
        mRenderer = new GPUImageMovieWriterRenderer(new GPUImageFilter(), outputFile);
        mRecordingState = RECORDING_OFF;
    }

    public int toggleRecording() {
        if (mRecordingState == RECORDING_OFF) {
            ((GPUImageMovieWriterRenderer)mRenderer).changeRecordingState(true);
            mRecordingState = RECORDING_ON;
        } else {
            ((GPUImageMovieWriterRenderer)mRenderer).changeRecordingState(false);
            mRecordingState = RECORDING_OFF;
        }
        return mRecordingState;
    }

    public void setOutputResolution(int width, int height) {
        ((GPUImageMovieWriterRenderer)mRenderer).setOutputResolution(width, height);
    }

    public void setOutputBitrate(int bitrate) {
        ((GPUImageMovieWriterRenderer)mRenderer).setOutputBitrate(bitrate);
    }

    public int getRecordingState() {
        return mRecordingState;
    }
}
