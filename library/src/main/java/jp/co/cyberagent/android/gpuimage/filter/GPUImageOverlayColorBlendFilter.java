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

public class GPUImageOverlayColorBlendFilter extends GPUImageFilter {
    public static final String OVERLAY_COLOR_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform vec4 mVec4Color;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     mediump float ra;\n" +
            "     if (2.0 * base.r < base.a) {\n" +
            "         ra = 2.0 * mVec4Color.r * base.r + mVec4Color.r * (1.0 - base.a) + base.r * (1.0 - mVec4Color.a);\n" +
            "     } else {\n" +
            "         ra = mVec4Color.a * base.a - 2.0 * (base.a - base.r) * (mVec4Color.a - mVec4Color.r) + mVec4Color.r * (1.0 - base.a) + base.r * (1.0 - mVec4Color.a);\n" +
            "     }\n" +
            "     \n" +
            "     mediump float ga;\n" +
            "     if (2.0 * base.g < base.a) {\n" +
            "         ga = 2.0 * mVec4Color.g * base.g + mVec4Color.g * (1.0 - base.a) + base.g * (1.0 - mVec4Color.a);\n" +
            "     } else {\n" +
            "         ga = mVec4Color.a * base.a - 2.0 * (base.a - base.g) * (mVec4Color.a - mVec4Color.g) + mVec4Color.g * (1.0 - base.a) + base.g * (1.0 - mVec4Color.a);\n" +
            "     }\n" +
            "     \n" +
            "     mediump float ba;\n" +
            "     if (2.0 * base.b < base.a) {\n" +
            "         ba = 2.0 * mVec4Color.b * base.b + mVec4Color.b * (1.0 - base.a) + base.b * (1.0 - mVec4Color.a);\n" +
            "     } else {\n" +
            "         ba = mVec4Color.a * base.a - 2.0 * (base.a - base.b) * (mVec4Color.a - mVec4Color.b) + mVec4Color.b * (1.0 - base.a) + base.b * (1.0 - mVec4Color.a);\n" +
            "     }\n" +
            "     \n" +
            "     gl_FragColor = vec4(ra, ga, ba, 1.0);\n" +
            " }";

    private float[] mVec4Color;
    private int mColorLocation;

    public GPUImageOverlayColorBlendFilter(final float[] mColor) {
        super(NO_FILTER_VERTEX_SHADER, OVERLAY_COLOR_BLEND_FRAGMENT_SHADER);
        this.mVec4Color = mColor;
    }

    public GPUImageOverlayColorBlendFilter() {
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
