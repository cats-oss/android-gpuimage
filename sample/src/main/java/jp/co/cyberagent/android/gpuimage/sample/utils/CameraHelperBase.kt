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

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.Camera.CameraInfo

import jp.co.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraHelperImpl
import jp.co.cyberagent.android.gpuimage.sample.utils.CameraHelper.CameraInfo2

class CameraHelperBase(private val mContext: Context) : CameraHelperImpl {

    override val numberOfCameras: Int
        get() = if (hasCameraSupport()) 1 else 0

    override fun openCamera(id: Int): Camera {
        return Camera.open()
    }

    override fun openDefaultCamera(): Camera {
        return Camera.open()
    }

    override fun hasCamera(cameraFacingFront: Int): Boolean {
        return if (cameraFacingFront == CameraInfo.CAMERA_FACING_BACK) {
            hasCameraSupport()
        } else false
    }


    override fun getCameraInfo(cameraId: Int, cameraInfo: CameraInfo2) {
        cameraInfo.facing = Camera.CameraInfo.CAMERA_FACING_BACK
        cameraInfo.orientation = 90
    }

    private fun hasCameraSupport(): Boolean {
        return mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
}
