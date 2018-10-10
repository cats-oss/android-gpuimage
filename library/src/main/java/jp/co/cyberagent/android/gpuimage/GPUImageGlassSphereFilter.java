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

public class GPUImageGlassSphereFilter extends GPUImageFilter {
    public static final String SPHERE_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float aspectRatio;\n" +
            "uniform highp float refractiveIndex;\n" +
            "// uniform vec3 lightPosition;\n" +
            "const highp vec3 lightPosition = vec3(-0.5, 0.5, 1.0);\n" +
            "const highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0);\n" +
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
            "highp vec3 refractedVector = 2.0 * refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n" +
            "refractedVector.xy = -refractedVector.xy;\n" +
            "\n" +
            "highp vec3 finalSphereColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5).rgb;\n" +
            "\n" +
            "// Grazing angle lighting\n" +
            "highp float lightingIntensity = 2.5 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));\n" +
            "finalSphereColor += lightingIntensity;\n" +
            "\n" +
            "// Specular lighting\n" +
            "lightingIntensity  = clamp(dot(normalize(lightPosition), sphereNormal), 0.0, 1.0);\n" +
            "lightingIntensity  = pow(lightingIntensity, 15.0);\n" +
            "finalSphereColor += vec3(0.8, 0.8, 0.8) * lightingIntensity;\n" +
            "\n" +
            "gl_FragColor = vec4(finalSphereColor, 1.0) * checkForPresenceWithinSphere;\n" +
            "}\n";

    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private float mRefractiveIndex;
    private int mRefractiveIndexLocation;

    public GPUImageGlassSphereFilter() {
        this(new PointF(0.5f, 0.5f), 0.25f, 0.71f);
    }

    public GPUImageGlassSphereFilter(PointF center, float radius, float refractiveIndex) {
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

    public void setRefractiveIndex(float refractiveIndex) {
        mRefractiveIndex = refractiveIndex;
        setFloat(mRefractiveIndexLocation, refractiveIndex);
    }

    public void setCenter(PointF center) {
        mCenter = center;
        setPoint(mCenterLocation, center);
    }

    public void setRadius(float radius) {
        mRadius = radius;
        setFloat(mRadiusLocation, radius);
    }
}
