/**
 * @author wysaid
 * @mail admin@wysaid.org
 * @refer https://github.com/wysaid/android-gpuimage-plus
 */



package jp.co.cyberagent.android.gpuimage;


import android.opengl.GLES20;

import java.nio.ByteBuffer;

public class GPUImageCurveFilter extends GPUImageFilter {
    public static final String CURVE_ADJUST_FRAGMENT_SHADER = "" +
            "precision mediump float;" +
            "varying vec2 textureCoordinate;\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "uniform sampler2D curveTexture; //We do not use sampler1D because GLES dosenot support that.\n" +

            "void main()\n" +
            "{\n" +
            "   vec4 src = texture2D(inputImageTexture, textureCoordinate);\n" +
            "   gl_FragColor = vec4(texture2D(curveTexture, vec2(src.r, 0.0)).r," +
            "                       texture2D(curveTexture, vec2(src.g, 0.0)).g," +
            "                       texture2D(curveTexture, vec2(src.b, 0.0)).b," +
            "                       src.a);\n" +
//            "gl_FragColor = texture2D(curveTexture, textureCoordinate);" + //for testing
            "}";
    private static int CURVE_PRECISION = 256;

    private int mCurveTexLocation;
    private int mCurveTextureID = 0;
    ByteBuffer mCurveBuffer;

    public GPUImageCurveFilter() {
        super(NO_FILTER_VERTEX_SHADER, CURVE_ADJUST_FRAGMENT_SHADER);
    }

    @Override
    public void onInit() {
        super.onInit();
        GLES20.glUseProgram(getProgram());
        mCurveTexLocation = GLES20.glGetUniformLocation(getProgram(), "curveTexture");
        GLES20.glUniform1i(mCurveTexLocation, 1);  //Curve Texture Location is never changed!

        //Generate curve texture
        int[] texID = {0};
        GLES20.glGenTextures(1, texID, 0);
        mCurveTextureID = texID[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCurveTextureID);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        mCurveBuffer = ByteBuffer.allocate(CURVE_PRECISION * 3);
        resetCurve();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCurveTextureID);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 256, 1, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, mCurveBuffer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, new int[]{mCurveTextureID}, 0);
    }

    @Override
    public void onDrawArraysPre() {

        //As mCurveTexLocation is already set to 1, just bind the tex id to GL_TEXTURE1.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCurveTextureID);
    }

    // 'points[RGB]' are paired with x and y, and both range from 0 to 1.
    // If any channel shall stay, pass 'null'.
    public void setCurveByPoints(float[] pointsR, float[] pointsG, float[] pointsB) {
        if(pointsR != null && pointsR.length >= 4)
            setSingleCurve(pointsR, 0);

        if(pointsG != null && pointsG.length >= 4)
            setSingleCurve(pointsG, 1);

        if(pointsB != null && pointsB.length >= 4)
            setSingleCurve(pointsB, 2);

        assignCurve();
    }


    // Algorithm from: https://github.com/wysaid/android-gpuimage-plus
    private void setSingleCurve(float[] pnts, int stride) {

        mCurveBuffer.position(0);

        int cnt = pnts.length / 2;
        float[] u = new float[cnt - 1];
        float[] ypp = new float[cnt];
        ypp[0] = u[0] = 0.0f;

        for(int i=1; i != cnt - 1; ++i)
        {
            int pre = (i - 1) * 2, cur = i * 2, nxt = (i + 1) * 2;

            float sig = (pnts[cur] - pnts[pre]) / (pnts[nxt] - pnts[pre]);
            float p = sig * ypp[i - 1] + 2.0f;
            ypp[i] = (sig - 1.0f) / p;
            u[i] = ((pnts[nxt + 1] - pnts[cur + 1]) / (pnts[nxt] - pnts[cur]) - (pnts[cur + 1] - pnts[pre + 1]) / (pnts[cur] - pnts[pre]));
            u[i] = (6.0f * u[i] / (pnts[nxt] - pnts[pre]) - sig * u[i - 1]) / p;
        }

        ypp[cnt - 1] = 0.0f;
        for(int i = cnt - 2; i >= 0; --i)
        {
            ypp[i] = ypp[i] * ypp[i+1] + u[i];
        }

        int kL = -1, kH = 0;
        byte[] buffer = mCurveBuffer.array();
        for(int i = 0; i != CURVE_PRECISION; ++i)
        {
            float t = (float)i/(CURVE_PRECISION - 1);

            while(kH < cnt && t > pnts[kH * 2])
            {
                kL = kH;
                ++kH;
            }

            if(kH == cnt)
            {
                buffer[i * 3 + stride] = (byte) (pnts[(cnt-1) * 2 + 1] * 255);
                continue;
            }

            if(kL == -1)
            {
                buffer[i * 3 + stride] = (byte) (pnts[1] * 255);
                continue;
            }

            float h = pnts[kH * 2] - pnts[kL * 2];
            float a = (pnts[kH * 2] - t) / h;
            float b = (t - pnts[kL * 2]) / h;
            float g = a * pnts[kL * 2 + 1] + b*pnts[kH * 2 + 1] + ((a*a*a - a)*ypp[kL] + (b*b*b - b) * ypp[kH]) * (h*h) / 6.0f;
            float result = Math.min(Math.max(g, 0.0f), 1.0f);
            buffer[i * 3 + stride] = (byte) (result * 255.0f);
        }

    }

    private void resetCurve() {
        mCurveBuffer.position(0);

        byte[] bytes = mCurveBuffer.array();
        for(int i = 0; i < CURVE_PRECISION * 3; i += 3) {
            byte tmp = (byte) (i * 255 / (CURVE_PRECISION * 3));
            bytes[i] = bytes[i + 1] = bytes[i + 2] = tmp;
        }
    }

    private void assignCurve() {
        mCurveBuffer.position(0);

        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCurveTextureID);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 256, 1, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, mCurveBuffer);
            }
        });
    }
}
