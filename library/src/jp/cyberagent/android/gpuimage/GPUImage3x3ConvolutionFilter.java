
package jp.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImage3x3ConvolutionFilter extends GPUImage3x3TextureSamplingFilter {
    public static final String THREE_X_THREE_TEXTURE_SAMPLING_FRAGMENT_SHADER = "" +
            "precision highp float;\n" + 
            "\n" + 
            "uniform sampler2D inputImageTexture;\n" + 
            "\n" + 
            "uniform mediump mat3 convolutionMatrix;\n" + 
            "\n" + 
            "varying vec2 textureCoordinate;\n" + 
            "varying vec2 leftTextureCoordinate;\n" + 
            "varying vec2 rightTextureCoordinate;\n" + 
            "\n" + 
            "varying vec2 topTextureCoordinate;\n" + 
            "varying vec2 topLeftTextureCoordinate;\n" + 
            "varying vec2 topRightTextureCoordinate;\n" + 
            "\n" + 
            "varying vec2 bottomTextureCoordinate;\n" + 
            "varying vec2 bottomLeftTextureCoordinate;\n" + 
            "varying vec2 bottomRightTextureCoordinate;\n" + 
            "\n" + 
            "void main()\n" + 
            "{\n" + 
            "    mediump vec4 bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate);\n" + 
            "    mediump vec4 bottomLeftColor = texture2D(inputImageTexture, bottomLeftTextureCoordinate);\n" + 
            "    mediump vec4 bottomRightColor = texture2D(inputImageTexture, bottomRightTextureCoordinate);\n" + 
            "    mediump vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
            "    mediump vec4 leftColor = texture2D(inputImageTexture, leftTextureCoordinate);\n" + 
            "    mediump vec4 rightColor = texture2D(inputImageTexture, rightTextureCoordinate);\n" + 
            "    mediump vec4 topColor = texture2D(inputImageTexture, topTextureCoordinate);\n" + 
            "    mediump vec4 topRightColor = texture2D(inputImageTexture, topRightTextureCoordinate);\n" + 
            "    mediump vec4 topLeftColor = texture2D(inputImageTexture, topLeftTextureCoordinate);\n" + 
            "\n" + 
            "    mediump vec4 resultColor = topLeftColor * convolutionMatrix[0][0] + topColor * convolutionMatrix[0][1] + topRightColor * convolutionMatrix[0][2];\n" + 
            "    resultColor += leftColor * convolutionMatrix[1][0] + centerColor * convolutionMatrix[1][1] + rightColor * convolutionMatrix[1][2];\n" + 
            "    resultColor += bottomLeftColor * convolutionMatrix[2][0] + bottomColor * convolutionMatrix[2][1] + bottomRightColor * convolutionMatrix[2][2];\n" + 
            "\n" + 
            "    gl_FragColor = resultColor;\n" + 
            "}"; 
 
    private float[] mConvolutionKernel;
    private int mUniformConvolutionMatrix;

    public GPUImage3x3ConvolutionFilter() {
        super(THREE_X_THREE_TEXTURE_SAMPLING_FRAGMENT_SHADER);
        mConvolutionKernel = new float[] {
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f
        };
    }

    @Override
    public void onInit() {
        super.onInit();
        mUniformConvolutionMatrix = GLES20.glGetUniformLocation(getProgram(), "convolutionMatrix");
        setConvolutionKernel(mConvolutionKernel);
    }

    public void setConvolutionKernel(final float[] convolutionKernel) {
        mConvolutionKernel = convolutionKernel;
        setUniformMatrix3f(mUniformConvolutionMatrix, mConvolutionKernel);
    }
}
