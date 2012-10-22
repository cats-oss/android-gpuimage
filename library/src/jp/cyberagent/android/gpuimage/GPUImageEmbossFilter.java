
package jp.cyberagent.android.gpuimage;

/**
 * Applies an emboss effect to the image.<br />
 * <br />
 * Intensity ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
public class GPUImageEmbossFilter extends GPUImage3x3ConvolutionFilter {
    private float mIntensity;

    public GPUImageEmbossFilter() {
        this(1.0f);
    }

    public GPUImageEmbossFilter(final float intensity) {
        super();
        mIntensity = intensity;
    }

    @Override
    public void onInit() {
        super.onInit();
        setIntensity(mIntensity);
    }

    public void setIntensity(final float intensity) {
        mIntensity = intensity;
        setConvolutionKernel(new float[] {
                intensity * (-2.0f), -intensity, 0.0f,
                -intensity, 1.0f, intensity,
                0.0f, intensity, intensity * 2.0f,
        });
    }

    public float getIntensity() {
        return mIntensity;
    }
}
