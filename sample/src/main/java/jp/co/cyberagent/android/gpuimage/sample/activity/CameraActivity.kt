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

package jp.co.cyberagent.android.gpuimage.sample.activity

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.co.cyberagent.android.gpuimage.GPUImageMovieWriter
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.sample.utils.Camera1Loader
import jp.co.cyberagent.android.gpuimage.sample.utils.Camera2Loader
import jp.co.cyberagent.android.gpuimage.sample.utils.CameraLoader
import jp.co.cyberagent.android.gpuimage.sample.utils.doOnLayout
import jp.co.cyberagent.android.gpuimage.util.Rotation

class CameraActivity : AppCompatActivity() {

    private val gpuImageView: GPUImageView by lazy { findViewById<GPUImageView>(R.id.surfaceView) }
    private val seekBar: SeekBar by lazy { findViewById<SeekBar>(R.id.seekBar) }
    private val recordBtn: Button by lazy { findViewById<Button>(R.id.btn_record) }
    private val cameraLoader: CameraLoader by lazy {
        if (Build.VERSION.SDK_INT < 21) {
            Camera1Loader(this)
        } else {
            Camera2Loader(this)
        }
    }
    private var filterAdjuster: FilterAdjuster? = null
    private val mMovieWriter: GPUImageMovieWriter by lazy {
        GPUImageMovieWriter()
    }

    private val mFilterGroup = GPUImageFilterGroup()

    private var mIsRecording = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                filterAdjuster?.adjust(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        findViewById<View>(R.id.button_choose_filter).setOnClickListener {
            GPUImageFilterTools.showDialog(this) { filter -> switchFilterTo(filter) }
        }
        findViewById<View>(R.id.button_capture).setOnClickListener {
            saveSnapshot()
        }
        recordBtn.setOnClickListener {
            mIsRecording = !mIsRecording
            if (mIsRecording) {
                val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path +
                        "/new" + System.nanoTime().toString() + ".mp4"
                mMovieWriter.startRecording(path, 540, 960)
                recordBtn.text = "stop record"
            } else {
                mMovieWriter.stopRecording()
                recordBtn.text = "start record"
            }
        }
        findViewById<View>(R.id.img_switch_camera).run {
            if (!cameraLoader.hasMultipleCamera()) {
                visibility = View.GONE
            }
            setOnClickListener {
                cameraLoader.switchCamera()
                gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
            }
        }
        cameraLoader.setOnPreviewFrameListener { data, width, height ->
            gpuImageView.updatePreviewFrame(data, width, height)
        }
        gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)
        mFilterGroup.addFilter(mMovieWriter)
        gpuImageView.filter = mFilterGroup
    }

    override fun onResume() {
        super.onResume()
        gpuImageView.doOnLayout {
            cameraLoader.onResume(it.width, it.height)
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
        if (mIsRecording) {
            mMovieWriter.stopRecording()
            mIsRecording = false
            recordBtn.text = "start record"
        }
    }

    private fun saveSnapshot() {
        val folderName = "GPUImage"
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        gpuImageView.saveToPictures(folderName, fileName) {
            Toast.makeText(this, "$folderName/$fileName saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRotation(orientation: Int): Rotation {
        return when (orientation) {
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    private fun switchFilterTo(filter: GPUImageFilter) {
        if (gpuImageView.filter == null || gpuImageView.filter!!.javaClass != filter.javaClass) {
            mFilterGroup.addFilter(0, filter)
            gpuImageView.filter = mFilterGroup
            filterAdjuster = FilterAdjuster(mFilterGroup)
            filterAdjuster?.adjust(seekBar.progress)
        }
    }
}
