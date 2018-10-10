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
 * Adjusts the individual RGB channels of an image
 * red: Normalized values by which each color channel is multiplied. The range is from 0.0 up, with 1.0 as the default.
 * green:
 * blue:
 */
public class GPUImageRGBFilter extends GPUImageFilter {
    public static final String RGB_FRAGMENT_SHADER = "" +
    		"  varying highp vec2 textureCoordinate;\n" +
    		"  \n" +
    		"  uniform sampler2D inputImageTexture;\n" +
    		"  uniform highp float red;\n" +
    		"  uniform highp float green;\n" +
    		"  uniform highp float blue;\n" +
    		"  \n" +
    		"  void main()\n" +
    		"  {\n" +
    		"      highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
    		"      \n" +
    		"      gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n" +
    		"  }\n";

    private int mRedLocation;
    private float mRed;
    private int mGreenLocation;
    private float mGreen;
    private int mBlueLocation;
    private float mBlue;
    private boolean mIsInitialized = false;

    public GPUImageRGBFilter() {
        this(1.0f, 1.0f, 1.0f);
    }

    public GPUImageRGBFilter(final float red, final float green, final float blue) {
        super(NO_FILTER_VERTEX_SHADER, RGB_FRAGMENT_SHADER);
        mRed = red;
        mGreen = green;
        mBlue = blue;
    }

    @Override
    public void onInit() {
        super.onInit();
        mRedLocation = GLES20.glGetUniformLocation(getProgram(), "red");
        mGreenLocation = GLES20.glGetUniformLocation(getProgram(), "green");
        mBlueLocation = GLES20.glGetUniformLocation(getProgram(), "blue");
        mIsInitialized = true;
        setRed(mRed);
        setGreen(mGreen);
        setBlue(mBlue);
    }

    public void setRed(final float red) {
        mRed = red;
        if (mIsInitialized) {
            setFloat(mRedLocation, mRed);
        }
    }
    
    public void setGreen(final float green) {
        mGreen = green;
        if (mIsInitialized) {
            setFloat(mGreenLocation, mGreen);
        }
    }
    
    public void setBlue(final float blue) {
        mBlue = blue;
        if (mIsInitialized) {
            setFloat(mBlueLocation, mBlue);
        }
    }
}
