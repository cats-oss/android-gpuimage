package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by vashisthg 30/05/14.
 */
public class GPUImageLevelsFilter extends GPUImageFilter{

    private static final String LOGTAG = GPUImageLevelsFilter.class.getSimpleName();

    public static final String LEVELS_FRAGMET_SHADER =

            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform mediump vec3 levelMinimum;\n" +
            " uniform mediump vec3 levelMiddle;\n" +
            " uniform mediump vec3 levelMaximum;\n" +
            " uniform mediump vec3 minOutput;\n" +
            " uniform mediump vec3 maxOutput;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n" +
            " }\n";

    private int mMinLocation;
    private float[] mMin;
    private int mMidLocation;
    private float[] mMid;
    private int mMaxLocation;
    private float[] mMax;
    private int mMinOutputLocation;
    private float[] mMinOutput;
    private int mMaxOutputLocation;
    private float[] mMaxOutput;

    public GPUImageLevelsFilter() {
        this(new float[] {0.0f,0.0f,0.0f}, new float[] {1.0f, 1.0f, 1.0f }, new float[] {1.0f, 1.0f ,1.0f}, new float[] {0.0f, 0.0f, 0.0f}, new float[] {1.0f,1.0f,1.0f});
    }

    private GPUImageLevelsFilter(final float[] min, final float[] mid, final float[] max, final float[] minOUt, final float[] maxOut) {
        super(NO_FILTER_VERTEX_SHADER, LEVELS_FRAGMET_SHADER);

        mMin = min;
        mMid = mid;
        mMax = max;
        mMinOutput = minOUt;
        mMaxOutput = maxOut;
        setMin(0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void onInit() {
        super.onInit();
        mMinLocation = GLES20.glGetUniformLocation(getProgram(), "levelMinimum");
        mMidLocation = GLES20.glGetUniformLocation(getProgram(), "levelMiddle");
        mMaxLocation = GLES20.glGetUniformLocation(getProgram(), "levelMaximum");
        mMinOutputLocation = GLES20.glGetUniformLocation(getProgram(), "minOutput");
        mMaxOutputLocation = GLES20.glGetUniformLocation(getProgram(), "maxOutput");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        updateUniforms();
    }


    public void updateUniforms () {
        setFloatVec3(mMinLocation, mMin);
        setFloatVec3(mMidLocation, mMid);
        setFloatVec3(mMaxLocation, mMax);
        setFloatVec3(mMinOutputLocation, mMinOutput);
        setFloatVec3(mMaxOutputLocation, mMaxOutput);
    }

    public void setMin(float min, float mid , float max ,float minOut , float maxOut) {
        setRedMin(min, mid, max, minOut, maxOut);
        setGreenMin(min, mid, max, minOut, maxOut);
        setBlueMin(min, mid, max, minOut, maxOut);
    }

    public void setMin(float min, float mid , float max ) {
        setMin(min, mid, max, 0.0f, 1.0f);
    }

    public void setRedMin(float min, float mid , float max ,float minOut , float maxOut) {
        mMin[0] = min;
        mMid[0] = mid;
        mMax[0] = max;
        mMinOutput[0] = minOut;
        mMaxOutput[0] = maxOut;
        updateUniforms();
    }

    public void setRedMin(float min, float mid , float max ){
        setRedMin(min, mid, max, 0, 1);
    }

    public void setGreenMin(float min, float mid , float max ,float minOut , float maxOut) {
        mMin[1] = min;
        mMid[1] = mid;
        mMax[1] = max;
        mMinOutput[1] = minOut;
        mMaxOutput[1] = maxOut;
        updateUniforms();
    }

    public void setGreenMin(float min, float mid , float max ){
        setGreenMin(min, mid, max, 0, 1);
    }

    public void setBlueMin(float min, float mid , float max ,float minOut , float maxOut) {
        mMin[2] = min;
        mMid[2] = mid;
        mMax[2] = max;
        mMinOutput[2] = minOut;
        mMaxOutput[2] = maxOut;
        updateUniforms();
    }

    public void setBlueMin(float min, float mid , float max ){
        setBlueMin(min, mid, max, 0, 1);
    }
}
