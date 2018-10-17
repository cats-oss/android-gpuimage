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

package jp.co.cyberagent.android.gpuimage.sample.utils

import android.app.Activity
import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.view.Surface

class CameraHelper(context: Context) {
    private val mImpl: CameraHelperImpl = CameraHelperBase(context)

    val numberOfCameras: Int
        get() = mImpl.numberOfCameras

    interface CameraHelperImpl {
        val numberOfCameras: Int

        fun openCamera(id: Int): Camera

        fun openDefaultCamera(): Camera

        fun hasCamera(cameraFacingFront: Int): Boolean

        fun getCameraInfo(cameraId: Int, cameraInfo: CameraInfo2)
    }

    fun openCamera(id: Int): Camera {
        return mImpl.openCamera(id)
    }

    fun hasFrontCamera(): Boolean {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_FRONT)
    }

    fun hasBackCamera(): Boolean {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_BACK)
    }

    fun getCameraInfo(cameraId: Int, cameraInfo: CameraInfo2) {
        mImpl.getCameraInfo(cameraId, cameraInfo)
    }

    fun getCameraDisplayOrientation(activity: Activity, cameraId: Int): Int {
        val degrees = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        val info = CameraInfo2()
        getCameraInfo(cameraId, info)
        return if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (info.orientation + degrees) % 360
        } else { // back-facing
            (info.orientation - degrees + 360) % 360
        }
    }

    class CameraInfo2 {
        var facing: Int = 0
        var orientation: Int = 0
    }
}
