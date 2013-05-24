package jp.co.cyberagent.android.gpuimage;

public class GPUImageBoxBlurFilter extends GPUImageTwoPassTextureSamplingFilter {
    public static final String VERTEX_SHADER =
            "attribute vec4 position;\n" +
                    "attribute vec2 inputTextureCoordinate;\n" +
                    "\n" +
                    "uniform float texelWidthOffset; \n" +
                    "uniform float texelHeightOffset; \n" +
                    "\n" +
                    "varying vec2 centerTextureCoordinate;\n" +
                    "varying vec2 oneStepLeftTextureCoordinate;\n" +
                    "varying vec2 twoStepsLeftTextureCoordinate;\n" +
                    "varying vec2 oneStepRightTextureCoordinate;\n" +
                    "varying vec2 twoStepsRightTextureCoordinate;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "gl_Position = position;\n" +
                    "\n" +
                    "vec2 firstOffset = vec2(1.5 * texelWidthOffset, 1.5 * texelHeightOffset);\n" +
                    "vec2 secondOffset = vec2(3.5 * texelWidthOffset, 3.5 * texelHeightOffset);\n" +
                    "\n" +
                    "centerTextureCoordinate = inputTextureCoordinate;\n" +
                    "oneStepLeftTextureCoordinate = inputTextureCoordinate - firstOffset;\n" +
                    "twoStepsLeftTextureCoordinate = inputTextureCoordinate - secondOffset;\n" +
                    "oneStepRightTextureCoordinate = inputTextureCoordinate + firstOffset;\n" +
                    "twoStepsRightTextureCoordinate = inputTextureCoordinate + secondOffset;\n" +
                    "}\n";

    public static final String FRAGMENT_SHADER =
            "precision highp float;\n" +
                    "\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "\n" +
                    "varying vec2 centerTextureCoordinate;\n" +
                    "varying vec2 oneStepLeftTextureCoordinate;\n" +
                    "varying vec2 twoStepsLeftTextureCoordinate;\n" +
                    "varying vec2 oneStepRightTextureCoordinate;\n" +
                    "varying vec2 twoStepsRightTextureCoordinate;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec4 fragmentColor = texture2D(inputImageTexture, centerTextureCoordinate) * 0.2;\n" +
                    "fragmentColor += texture2D(inputImageTexture, oneStepLeftTextureCoordinate) * 0.2;\n" +
                    "fragmentColor += texture2D(inputImageTexture, oneStepRightTextureCoordinate) * 0.2;\n" +
                    "fragmentColor += texture2D(inputImageTexture, twoStepsLeftTextureCoordinate) * 0.2;\n" +
                    "fragmentColor += texture2D(inputImageTexture, twoStepsRightTextureCoordinate) * 0.2;\n" +
                    "\n" +
                    "gl_FragColor = fragmentColor;\n" +
                    "}\n";

    private float blurSize = 1f;

    public GPUImageBoxBlurFilter() {
        this(1f);
    }

    public GPUImageBoxBlurFilter(float blurSize) {
        super(VERTEX_SHADER, FRAGMENT_SHADER, VERTEX_SHADER, FRAGMENT_SHADER);
        this.blurSize = blurSize;
    }

    public void setBlurSize(float blurSize) {
        this.blurSize = blurSize;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                initTexelOffsets();
            }
        });
    }

    @Override
    public float getVerticalTexelOffsetRatio() {
        return blurSize;
    }

    @Override
    public float getHorizontalTexelOffsetRatio() {
        return blurSize;
    }
}
