package jp.co.cyberagent.android.gpuimage.filter;

/**
 * Adjusts the shadows of an image
 * shadows: Increase to lighten shadows, from 0.0 to 2.0, with 1.0 as the default.ult.
 */
public class GPUImageShadowWideRangeFilter extends GPUImageHighlightShadowWideRangeFilter {

    public GPUImageShadowWideRangeFilter() {
        super(1.0f, 1.0f);
    }

    public GPUImageShadowWideRangeFilter(final float shadows) {
        super(shadows, 1.0f);
    }

}

