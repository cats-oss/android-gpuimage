package jp.co.cyberagent.android.gpuimage.sample.activity

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.*
import jp.co.cyberagent.android.gpuimage.sample.R

/*
 * Copyright (C) 2018 CyberAgent, Inc.
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
class ImageActivity : AppCompatActivity() {

    private val gpuImage: GPUImageView by lazy { findViewById<GPUImageView>(R.id.gpu_image) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        gpuImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        gpuImage.filter = GPUImageFilterGroup(listOf(GPUImageBrightnessFilter(),
            GPUImageSaturationFilter(),
            GPUImageWhiteBalanceFilter(),
            GPUImageSharpenFilter())).apply {
            setBackgroundColor(0.27f, 0.27f, 0.27f)
        }
        gpuImage.setImage(Uri.parse("file:///android_asset/image.png"))
    }
}