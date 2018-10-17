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

import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.Parameters
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools.FilterAdjuster
import jp.co.cyberagent.android.gpuimage.sample.R
import jp.co.cyberagent.android.gpuimage.sample.utils.CameraHelper
import jp.co.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraInfo2
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private val gpuImageView: GPUImageView by lazy { findViewById<GPUImageView>(R.id.surfaceView) }
    private val seekBar: SeekBar by lazy { findViewById<SeekBar>(R.id.seekBar) }
    private val cameraHelper: CameraHelper by lazy { CameraHelper(this) }
    private val cameraLoader: CameraLoader by lazy { CameraLoader() }
    private var filterAdjuster: FilterAdjuster? = null

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
            when (cameraLoader.cameraInstance!!.parameters.focusMode) {
                Parameters.FOCUS_MODE_FIXED -> takePicture()
                Parameters.FOCUS_MODE_CONTINUOUS_PICTURE -> takePicture()
                else -> cameraLoader.cameraInstance!!.autoFocus { _, _ -> takePicture() }
            }
        }
        findViewById<View>(R.id.img_switch_camera).run {
            setOnClickListener { cameraLoader.switchCamera() }
            if (!cameraHelper.hasFrontCamera() || !cameraHelper.hasBackCamera()) {
                visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cameraLoader.onResume()
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
    }

    private fun takePicture() {
        // TODO get a size that is about the size of the screen
        val params = cameraLoader.cameraInstance!!.parameters
        params.setRotation(90)
        cameraLoader.cameraInstance!!.parameters = params
        for (size in params.supportedPictureSizes) {
            Log.i("ASDF", "Supported: " + size.width + "x" + size.height)
        }
        cameraLoader.cameraInstance!!.takePicture(null, null,
            Camera.PictureCallback { data, camera ->
                val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE)
                if (pictureFile == null) {
                    Log.d("ASDF", "Error creating media file, check storage permissions")
                    return@PictureCallback
                }

                try {
                    val fos = FileOutputStream(pictureFile)
                    fos.write(data!!)
                    fos.close()
                } catch (e: FileNotFoundException) {
                    Log.d("ASDF", "File not found: " + e.message)
                } catch (e: IOException) {
                    Log.d("ASDF", "Error accessing file: " + e.message)
                }

                val view = findViewById<GLSurfaceView>(R.id.surfaceView)
                view.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
                gpuImageView.saveToPictures(
                    "GPUImage",
                    System.currentTimeMillis().toString() + ".jpg"
                ) {
                    pictureFile.delete()
                    camera.startPreview()
                    view.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                }
            })
    }

    private fun switchFilterTo(filter: GPUImageFilter) {
        if (gpuImageView.filter == null || gpuImageView.filter!!.javaClass != filter.javaClass) {
            gpuImageView.filter = filter
            filterAdjuster = FilterAdjuster(filter)
            filterAdjuster?.adjust(seekBar.progress)
        }
    }

    @Suppress("DEPRECATION")
    private inner class CameraLoader {

        var currentCameraId = 0
        var cameraInstance: Camera? = null

        fun onResume() {
            setUpCamera(currentCameraId)
        }

        fun onPause() {
            releaseCamera()
        }

        fun switchCamera() {
            releaseCamera()
            currentCameraId = (currentCameraId + 1) % cameraHelper.numberOfCameras
            setUpCamera(currentCameraId)
        }

        private fun setUpCamera(id: Int) {
            cameraInstance = getCameraInstance(id)
            val parameters = cameraInstance!!.parameters
            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            if (parameters.supportedFocusModes.contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                )
            ) {
                parameters.focusMode = Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            }
            cameraInstance!!.parameters = parameters

            val orientation = cameraHelper.getCameraDisplayOrientation(
                this@CameraActivity, currentCameraId
            )
            val cameraInfo = CameraInfo2()
            cameraHelper.getCameraInfo(currentCameraId, cameraInfo)
            val flipHorizontal = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT
            gpuImageView.setUpCamera(cameraInstance, orientation, flipHorizontal, false)
        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private fun getCameraInstance(id: Int): Camera? {
            return try {
                cameraHelper.openCamera(id)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun releaseCamera() {
            cameraInstance!!.setPreviewCallback(null)
            cameraInstance!!.release()
            cameraInstance = null
        }
    }

    companion object {

        const val MEDIA_TYPE_IMAGE = 1
        private const val MEDIA_TYPE_VIDEO = 2

        private fun getOutputMediaFile(type: Int): File? {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), "MyCameraApp"
            )
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            return when (type) {
                MEDIA_TYPE_IMAGE -> File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
                MEDIA_TYPE_VIDEO -> File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")
                else -> return null
            }
        }
    }
}
