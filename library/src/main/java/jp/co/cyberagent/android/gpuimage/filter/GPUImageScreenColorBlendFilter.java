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

public class GPUImageScreenColorBlendFilter extends GPUImageFilter {
    public static final String SCREEN_COLOR_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2;\n" +
            "\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform vec4 mVec4Color;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     mediump vec4 whiteColor = vec4(1.0);\n" +
            "     gl_FragColor = whiteColor - ((whiteColor - mVec4Color) * (whiteColor - textureColor));\n" +
            " }";


    private float[] mVec4Color;
    private int mColorLocation;

    public GPUImageScreenColorBlendFilter(final float[] mColor) {
        super(NO_FILTER_VERTEX_SHADER, SCREEN_COLOR_BLEND_FRAGMENT_SHADER);
        this.mVec4Color = mColor;
    }

    public GPUImageScreenColorBlendFilter() {
        this(new float[]{0.0f, 0.0f, 0.0f, 0.0f});
    }

    @Override
    public void onInit() {
        super.onInit();
        mColorLocation = GLES20.glGetUniformLocation(getProgram(), "mVec4Color");
        setBlendColor(mVec4Color);
    }

    public void setBlendColor(final float[] color) {
        mVec4Color = color;
        setFloatVec4(mColorLocation, mVec4Color);
    }
}
