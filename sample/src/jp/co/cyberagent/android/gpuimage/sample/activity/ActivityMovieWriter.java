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

package jp.co.cyberagent.android.gpuimage.sample.activity;

import android.widget.ImageButton;

import jp.co.cyberagent.android.gpuimage.GPUImageMovieWriter;
import jp.co.cyberagent.android.gpuimage.sample.R;

public class ActivityMovieWriter extends ActivityCamera {

    @Override
    protected void createGPUImage() {
        mGPUImage = new GPUImageMovieWriter(this, getOutputMediaFile(MEDIA_TYPE_VIDEO));
    }

    @Override
    protected void onCaptureButtonClicked() {
        ((GPUImageMovieWriter)mGPUImage).toggleRecording();
        String uri;
        switch (((GPUImageMovieWriter)mGPUImage).getRecordingState()) {
            case GPUImageMovieWriter.RECORDING_ON:
                uri = "@android:drawable/ic_media_pause";
                break;
            case GPUImageMovieWriter.RECORDING_OFF:
                uri = "@android:drawable/ic_menu_camera";
                break;
            default:
                uri = "@android:drawable/ic_menu_camera";
                break;
        }

        ((ImageButton)findViewById(R.id.button_capture)).setImageDrawable(
                getResources().getDrawable(
                        getResources().getIdentifier(uri, null, getPackageName())
                )
        );
    }


}
