@file:Suppress("DEPRECATION")

package jp.co.cyberagent.android.gpuimage.sample.utils

import android.app.Activity
import android.hardware.Camera
import android.util.Log
import android.view.Surface

class CameraLoader1(val activity: Activity) : CameraLoader() {

    private var cameraInstance: Camera? = null
    private var cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_BACK

    override fun onResume() {
        setUpCamera()
    }

    override fun onPause() {
        releaseCamera()
    }

    override fun getCameraWidth(): Int? {
        return cameraInstance?.parameters?.previewSize?.width
    }

    override fun getCameraHeight(): Int? {
        return cameraInstance?.parameters?.previewSize?.height
    }

    override fun switchCamera() {
        cameraFacing = when (cameraFacing) {
            Camera.CameraInfo.CAMERA_FACING_FRONT -> Camera.CameraInfo.CAMERA_FACING_BACK
            Camera.CameraInfo.CAMERA_FACING_BACK -> Camera.CameraInfo.CAMERA_FACING_FRONT
            else -> return
        }
        releaseCamera()
        setUpCamera()
    }

    override fun getCameraOrientation(): Int? {
        val degrees = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
        return if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (90 + degrees) % 360
        } else { // back-facing
            (90 - degrees) % 360
        }
    }

    override fun takePicture(onPictureTaken: (data: ByteArray) -> Unit) {
        val params = cameraInstance!!.parameters
        params.setRotation(90)
        cameraInstance!!.parameters = params

        when (cameraInstance!!.parameters.focusMode) {
            Camera.Parameters.FOCUS_MODE_FIXED ->
                cameraInstance!!.takePicture(null, null) { data, _ -> onPictureTaken(data) }
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE ->
                cameraInstance!!.takePicture(null, null) { data, _ -> onPictureTaken(data) }
            else -> cameraInstance!!.autoFocus { _, _ ->
                cameraInstance!!.takePicture(null, null) { data, _ -> onPictureTaken(data) }
            }
        }
    }

    override fun hasMultipleCamera(): Boolean {
        return Camera.getNumberOfCameras() > 1
    }

    private fun setUpCamera() {
        val id = getCurrentCameraId()
        cameraInstance = getCameraInstance(id)
        if (cameraInstance == null) {
            Log.e("CameraLoader1", "Camera not found")
        }
        val parameters = cameraInstance!!.parameters

        if (parameters.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        }
        cameraInstance!!.parameters = parameters

        cameraInstance!!.setPreviewCallback { data, camera ->
            if (data == null || camera == null) {
                return@setPreviewCallback
            }
            val size = camera.parameters.previewSize
            onPreviewFrame?.invoke(data, size.width, size.height)
        }
        cameraInstance!!.startPreview()
    }

    private fun getCurrentCameraId(): Int {
        val cameraInfo = Camera.CameraInfo()
        for (id in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(id, cameraInfo)
            if (cameraInfo.facing == cameraFacing) {
                return id
            }
        }
        return 0
    }

    private fun getCameraInstance(id: Int): Camera? {
        return try {
            Camera.open(id)
        } catch (e: Exception) {
            Log.e("Camera not found", e.toString())
            null
        }
    }

    private fun releaseCamera() {
        cameraInstance!!.setPreviewCallback(null)
        cameraInstance!!.release()
        cameraInstance = null
    }
}