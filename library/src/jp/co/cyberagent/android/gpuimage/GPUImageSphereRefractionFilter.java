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

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageSphereRefractionFilter extends GPUImageFilter {
    public static final String SPHERE_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float aspectRatio;\n" +
            "uniform highp float refractiveIndex;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n" +
            "highp float distanceFromCenter = distance(center, textureCoordinateToUse);\n" +
            "lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n" +
            "\n" +
            "distanceFromCenter = distanceFromCenter / radius;\n" +
            "\n" +
            "highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\n" +
            "highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n" +
            "\n" +
            "highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n" +
            "\n" +
            "gl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n" +
            "}\n";

    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private float mRefractiveIndex;
    private int mRefractiveIndexLocation;

    public GPUImageSphereRefractionFilter() {
        this(new PointF(0.5f, 0.5f), 0.25f, 0.71f);
    }

    public GPUImageSphereRefractionFilter(PointF center, float radius, float refractiveIndex) {
        super(NO_FILTER_VERTEX_SHADER, SPHERE_FRAGMENT_SHADER);
        mCenter = center;
        mRadius = radius;
        mRefractiveIndex = refractiveIndex;
    }

    @Override
    public void onInit() {
        super.onInit();
        mCenterLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        mAspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        mRefractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setRadius(mRadius);
        setCenter(mCenter);
        setRefractiveIndex(mRefractiveIndex);
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        mAspectRatio = (float) height / width;
        setAspectRatio(mAspectRatio);
        super.onOutputSizeChanged(width, height);
    }

    private void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        setFloat(mAspectRatioLocation, aspectRatio);
    }

    /**
     * The index of refraction for the sphere, with a default of 0.71
     *
     * @param refractiveIndex default 0.71
     */
    public void setRefractiveIndex(float refractiveIndex) {
        mRefractiveIndex = refractiveIndex;
        setFloat(mRefractiveIndexLocation, refractiveIndex);
    }

    /**
     * The center about which to apply the distortion, with a default of (0.5, 0.5)
     *
     * @param center default (0.5, 0.5)
     */
    public void setCenter(PointF center) {
        mCenter = center;
        setPoint(mCenterLocation, center);
    }

    /**
     * The radius of the distortion, ranging from 0.0 to 1.0, with a default of 0.25
     *
     * @param radius from 0.0 to 1.0, default 0.25
     */
    public void setRadius(float radius) {
        mRadius = radius;
        setFloat(mRadiusLocation, radius);
    }
}
