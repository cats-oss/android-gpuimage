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
 * Selectively replaces a color in the first image with the second image
 */
public class GPUImageChromaKeyBlendFilter extends GPUImageTwoInputFilter {
    public static final String CHROMA_KEY_BLEND_FRAGMENT_SHADER = " precision highp float;\n" +
            " \n" +
            " varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2;\n" +
            "\n" +
            " uniform float thresholdSensitivity;\n" +
            " uniform float smoothing;\n" +
            " uniform vec3 colorToReplace;\n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" +
            "     \n" +
            "     float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n" +
            "     float maskCr = 0.7132 * (colorToReplace.r - maskY);\n" +
            "     float maskCb = 0.5647 * (colorToReplace.b - maskY);\n" +
            "     \n" +
            "     float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;\n" +
            "     float Cr = 0.7132 * (textureColor.r - Y);\n" +
            "     float Cb = 0.5647 * (textureColor.b - Y);\n" +
            "     \n" +
            "     float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));\n" +
            "     gl_FragColor = mix(textureColor, textureColor2, blendValue);\n" +
            " }";

    private int mThresholdSensitivityLocation;
    private int mSmoothingLocation;
    private int mColorToReplaceLocation;
    private float mSmoothing = 0.1f;
    private float mThresholdSensitivity = 0.3f;
    private float[] mColorToReplace = new float[]{0.0f, 1.0f, 0.0f};

    public GPUImageChromaKeyBlendFilter() {
        super(CHROMA_KEY_BLEND_FRAGMENT_SHADER);

    }

    @Override
    public void onInit() {
        super.onInit();
        mThresholdSensitivityLocation = GLES20.glGetUniformLocation(getProgram(), "thresholdSensitivity");
        mSmoothingLocation = GLES20.glGetUniformLocation(getProgram(), "smoothing");
        mColorToReplaceLocation = GLES20.glGetUniformLocation(getProgram(), "colorToReplace");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setSmoothing(mSmoothing);
        setThresholdSensitivity(mThresholdSensitivity);
        setColorToReplace(mColorToReplace[0], mColorToReplace[1], mColorToReplace[2]);
    }

    /**
     * The degree of smoothing controls how gradually similar colors are replaced in the image
     * The default value is 0.1
     */
    public void setSmoothing(final float smoothing) {
        mSmoothing = smoothing;
        setFloat(mSmoothingLocation, mSmoothing);
    }

    /**
     * The threshold sensitivity controls how similar pixels need to be colored to be replaced
     * The default value is 0.3
     */
    public void setThresholdSensitivity(final float thresholdSensitivity) {
        mThresholdSensitivity = thresholdSensitivity;
        setFloat(mThresholdSensitivityLocation, mThresholdSensitivity);
    }

    /** The color to be replaced is specified using individual red, green, and blue components (normalized to 1.0).
     * The default is green: (0.0, 1.0, 0.0).
     *
     * @param redComponent Red component of color to be replaced
     * @param greenComponent Green component of color to be replaced
     * @param blueComponent Blue component of color to be replaced
     */
    public void setColorToReplace(float redComponent, float greenComponent, float blueComponent) {
        mColorToReplace = new float[]{redComponent, greenComponent, blueComponent};
        setFloatVec3(mColorToReplaceLocation, mColorToReplace);
    }
}
