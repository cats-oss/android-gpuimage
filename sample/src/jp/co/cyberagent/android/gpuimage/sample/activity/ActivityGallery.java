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

package jp.co.cyberagent.android.gpuimage.sample.activity;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import jp.co.cyberagent.android.gpuimage.sample.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class ActivityGallery extends Activity implements OnSeekBarChangeListener,
        OnClickListener, OnPictureSavedListener {

    private static final int REQUEST_PICK_IMAGE = 1;
    private GPUImageFilter mFilter;
    private FilterAdjuster mFilterAdjuster;
    private GPUImageView mGPUImageView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
        findViewById(R.id.button_choose_filter).setOnClickListener(this);
        findViewById(R.id.button_save).setOnClickListener(this);

        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
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
                        mGPUImageView.requestRender();
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

    @Override
    public void onPictureSaved(final Uri uri) {
        Toast.makeText(this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT).show();
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
        mGPUImageView.saveToPictures("GPUImage", fileName, this);
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mGPUImageView.setFilter(mFilter);
            mFilterAdjuster = new FilterAdjuster(mFilter);
        }
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
        mGPUImageView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    private void handleImage(final Uri selectedImage) {
        mGPUImageView.setImage(selectedImage);
    }
}
