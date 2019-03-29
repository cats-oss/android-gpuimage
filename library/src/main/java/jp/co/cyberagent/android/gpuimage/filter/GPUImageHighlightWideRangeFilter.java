package jp.co.cyberagent.android.gpuimage.filter;

/**
 * Adjusts the highlights of an image
 * highlights: Decrease to darken highlights, from 0.0 to 1.0, with 1.0 as the default.
 */
public class GPUImageHighlightWideRangeFilter extends GPUImageHighlightShadowWideRangeFilter {

    public GPUImageHighlightWideRangeFilter() {
        super(1.0f, 1.0f);
    }

    public GPUImageHighlightWideRangeFilter(final float highlights) {
        super(1.0f, highlights);
    }

}
