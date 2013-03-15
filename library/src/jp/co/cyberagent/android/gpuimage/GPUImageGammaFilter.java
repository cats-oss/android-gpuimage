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

/**
 * gamma value ranges from 0.0 to 3.0, with 1.0 as the normal level
 */
public class GPUImageGammaFilter extends GPUImageFilter {
    public static final String GAMMA_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform lowp float gamma;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n" +
            " }";

    private int mGammaLocation;
    private float mGamma;

    public GPUImageGammaFilter() {
        this(1.2f);
    }

    public GPUImageGammaFilter(final float gamma) {
        super(NO_FILTER_VERTEX_SHADER, GAMMA_FRAGMENT_SHADER);
        mGamma = gamma;
    }

    @Override
    public void onInit() {
        super.onInit();
        mGammaLocation = GLES20.glGetUniformLocation(getProgram(), "gamma");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setGamma(mGamma);
    }

    public void setGamma(final float gamma) {
        mGamma = gamma;
        setFloat(mGammaLocation, mGamma);
    }
}
