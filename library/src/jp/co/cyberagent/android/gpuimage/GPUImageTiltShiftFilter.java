package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

/**
 * A simulated tilt shift lens effect
 */
public class GPUImageTiltShiftFilter extends GPUImageTwoInputFilter {
    public static final String SHADER = "" +
            "varying vec2 textureCoordinate;\n" +
            " varying vec2 textureCoordinate2;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " \n" +
            " uniform float topFocusLevel;\n" +
            " uniform float bottomFocusLevel;\n" +
            " uniform float mFocusFallOffRate;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     vec4 sharpImageColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     vec4 blurredImageColor = texture2D(inputImageTexture2, textureCoordinate2);\n" +
            "     \n" +
            "     float blurIntensity = 1.0 - smoothstep(topFocusLevel - mFocusFallOffRate, topFocusLevel, textureCoordinate2.y);\n" +
            "     blurIntensity += smoothstep(bottomFocusLevel, bottomFocusLevel + mFocusFallOffRate, textureCoordinate2.y);\n" +
            "     \n" +
            "     gl_FragColor = mix(sharpImageColor, blurredImageColor, blurIntensity);\n" +
            " }\n";

    private int mTopFocusLevelLocation;
    private int mBottomFocusLevelLocation;
    private int mFocusFallOffRate;

    private GPUImageGaussianBlurFilter mBlurFilter;

    private float mTilt;

    public GPUImageTiltShiftFilter(){
        this(0.5f);
    }

    public GPUImageTiltShiftFilter(float tilt){
        super(SHADER);

        mTilt = tilt;

        mBlurFilter = new GPUImageGaussianBlurFilter();
        mBlurFilter.setBlurSize(2);
    }

    @Override
    public void onInit() {
        super.onInit();

        mTopFocusLevelLocation = GLES20.glGetUniformLocation(getProgram(), "topFocusLevel");
        mBottomFocusLevelLocation = GLES20.glGetUniformLocation(getProgram(), "bottomFocusLevel");
        mFocusFallOffRate = GLES20.glGetUniformLocation(getProgram(), "mFocusFallOffRate");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setTilt(mTilt);
        setFocusFallOffRate(0.3f);
    }

    public void setTilt(final float tilt){
        mTilt = tilt;

        setFloat(mTopFocusLevelLocation, tilt);
        setFloat(mBottomFocusLevelLocation, tilt);
    }

    public void setFocusFallOffRate(final float rate){
        setFloat(mFocusFallOffRate, rate);
    }

    public void setBitmap(Context context, final Bitmap bitmap){
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(mBlurFilter);

        super.setBitmap(gpuImage.getBitmapWithFilterApplied());
    }

}
