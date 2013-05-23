package jp.co.cyberagent.android.gpuimage;

import java.util.ArrayList;

public class GPUImageTwoPassFilter extends GPUImageFilterGroup {
    public GPUImageTwoPassFilter(String firstVertexShader, String firstFragmentShader,
                                 String secondVertexShader, String secondFragmentShader) {
        super(null);
        addFilter(new GPUImageFilter(firstVertexShader, firstFragmentShader));
        addFilter(new GPUImageFilter(secondVertexShader, secondFragmentShader));
    }
}
