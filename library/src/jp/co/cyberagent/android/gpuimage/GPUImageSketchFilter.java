package jp.co.cyberagent.android.gpuimage;

import java.util.ArrayList;
import java.util.List;

public class GPUImageSketchFilter extends GPUImageFilterGroup {

    public static final String SKETCH_FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
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
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
            "float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
            "float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
            "float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
            "float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
            "float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
            "float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
            "float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
            "float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
            "float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
            "\n" +
            "float mag = 1.0 - length(vec2(h, v));\n" +
            "\n" +
            "gl_FragColor = vec4(vec3(mag), 1.0);\n" +
            "}\n";

    public GPUImageSketchFilter() {
        super(createFilters());
    }

    private static List<GPUImageFilter> createFilters() {
        List<GPUImageFilter> filters = new ArrayList<GPUImageFilter>(2);
        filters.add(new GPUImageGrayscaleFilter());
        filters.add(new GPUImage3x3TextureSamplingFilter(SKETCH_FRAGMENT_SHADER));
        return filters;
    }
}
