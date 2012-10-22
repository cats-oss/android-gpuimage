
package jp.cyberagent.android.gpuimage;

import android.opengl.GLES20;

/**
 * Sharpens the picture. <br />
 * <br />
 * sharpness: from -4.0 to 4.0, with 0.0 as the normal level
 */
public class GPUImageSharpenFilter extends GPUImageFilter {
    public static final String SHARPEN_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "\n" +
            "uniform float imageWidthFactor; \n" +
            "uniform float imageHeightFactor; \n" +
            "uniform float sharpness;\n" +
            "\n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate; \n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            "\n" +
            "varying float centerMultiplier;\n" +
            "varying float edgeMultiplier;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    \n" +
            "    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n" +
            "    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n" +
            "    \n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n" +
            "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n" +
            "    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n" +
            "    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n" +
            "    \n" +
            "    centerMultiplier = 1.0 + 4.0 * sharpness;\n" +
            "    edgeMultiplier = sharpness;\n" +
            "}";

    public static final String SHARPEN_FRAGMENT_SHADER = "" +
            "precision highp float;\n" + 
            "\n" + 
            "varying highp vec2 textureCoordinate;\n" + 
            "varying highp vec2 leftTextureCoordinate;\n" + 
            "varying highp vec2 rightTextureCoordinate; \n" + 
            "varying highp vec2 topTextureCoordinate;\n" + 
            "varying highp vec2 bottomTextureCoordinate;\n" + 
            "\n" + 
            "varying highp float centerMultiplier;\n" + 
            "varying highp float edgeMultiplier;\n" + 
            "\n" + 
            "uniform sampler2D inputImageTexture;\n" + 
            "\n" + 
            "void main()\n" + 
            "{\n" + 
            "    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n" + 
            "    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n" + 
            "    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n" + 
            "    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n" + 
            "    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n" + 
            "\n" + 
            "    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n" + 
            "}";

    private int mSharpnessLocation;
    private float mSharpness;
    private int mImageWidthFactorLocation;
    private int mImageHeightFactorLocation;

    public GPUImageSharpenFilter() {
        this(0.0f);
    }
    
    public GPUImageSharpenFilter(final float sharpness) {
        super(SHARPEN_VERTEX_SHADER, SHARPEN_FRAGMENT_SHADER);
        mSharpness = sharpness;
    }

    @Override
    public void onInit() {
        super.onInit();
        mSharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        mImageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        mImageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
        setSharpness(mSharpness);
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(mImageWidthFactorLocation, 1.0f / width);
        setFloat(mImageHeightFactorLocation, 1.0f / height);
    }

    public void setSharpness(final float sharpness) {
        mSharpness = sharpness;
        setFloat(mSharpnessLocation, mSharpness);
    }
}
