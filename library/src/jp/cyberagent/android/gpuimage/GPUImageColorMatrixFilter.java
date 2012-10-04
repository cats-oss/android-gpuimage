
package jp.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImageColorMatrixFilter extends GPUImageFilter {
    public static final String COLOR_MATRIX_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "uniform lowp mat4 colorMatrix;\n" +
            "uniform lowp float intensity;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "    lowp vec4 outputColor = textureColor * colorMatrix;\n" +
            "    \n" +
            "    gl_FragColor = (intensity * outputColor) + ((1.0 - intensity) * textureColor);\n" +
            "}";

    private float mIntensity;
    private float[] mColorMatrix;
    private int mColorMatrixLocation;
    private int mIntensityLocation;
    private boolean mIsInitialized = false;

    public GPUImageColorMatrixFilter() {
        super(NO_FILTER_VERTEX_SHADER, COLOR_MATRIX_FRAGMENT_SHADER);
        mIntensity = 1.0f;
        mColorMatrix = new float[] {
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };
    }

    @Override
    public void onInit() {
        super.onInit();
        mColorMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "colorMatrix");
        mIntensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
        mIsInitialized = true;
        setIntensity(mIntensity);
        setColorMatrix(mColorMatrix);
    }

    public void setIntensity(final float intensity) {
        mIntensity = intensity;
        if (mIsInitialized) {
            setFloat(mIntensityLocation, intensity);
        }
    }

    public void setColorMatrix(final float[] colorMatrix) {
        mColorMatrix = colorMatrix;
        if (mIsInitialized) {
            setUniformMatrix4f(mColorMatrixLocation, colorMatrix);
        }
    }
}
