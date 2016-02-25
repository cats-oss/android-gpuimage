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

public class GPUImageMultiplyColorBlendFilter extends GPUImageFilter {
    public static final String MULTIPLY_COLOR_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform vec4 mColor;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" +
            "          \n" +
            "     gl_FragColor = mColor * base + mColor * (1.0 - base.a) + base * (1.0 - mColor.a);\n" +
            " }";
    private float[] mColor;
    private int mColorLocation;

    public GPUImageMultiplyColorBlendFilter(final float[] mColor) {
        super(NO_FILTER_VERTEX_SHADER, MULTIPLY_COLOR_BLEND_FRAGMENT_SHADER);
        this.mColor = mColor;
    }

    public GPUImageMultiplyColorBlendFilter() {
        this(new float[]{0.0f, 0.0f, 0.0f, 0.0f});
    }

    @Override
    public void onInit() {
        super.onInit();
        mColorLocation = GLES20.glGetUniformLocation(getProgram(), "mColor");
        setBlendColor(mColor);
    }

    public void setBlendColor(final float[] color) {
        mColor = color;
        setFloatVec4(mColorLocation, mColor);
    }
}
