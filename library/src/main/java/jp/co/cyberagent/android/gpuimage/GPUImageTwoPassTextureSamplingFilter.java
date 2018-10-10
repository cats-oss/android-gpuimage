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

package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
    public GPUImageTwoPassTextureSamplingFilter(String firstVertexShader, String firstFragmentShader,
                                                String secondVertexShader, String secondFragmentShader) {
        super(firstVertexShader, firstFragmentShader,
                secondVertexShader, secondFragmentShader);
    }

    @Override
    public void onInit() {
        super.onInit();
        initTexelOffsets();
    }

    protected void initTexelOffsets() {
        float ratio = getHorizontalTexelOffsetRatio();
        GPUImageFilter filter = mFilters.get(0);
        int texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        int texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, ratio / mOutputWidth);
        filter.setFloat(texelHeightOffsetLocation, 0);

        ratio = getVerticalTexelOffsetRatio();
        filter = mFilters.get(1);
        texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, 0);
        filter.setFloat(texelHeightOffsetLocation, ratio / mOutputHeight);
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        initTexelOffsets();
    }

    public float getVerticalTexelOffsetRatio() {
        return 1f;
    }

    public float getHorizontalTexelOffsetRatio() {
        return 1f;
    }
}
