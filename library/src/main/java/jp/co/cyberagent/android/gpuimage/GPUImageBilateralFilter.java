/**
 * @author wysaid
 * @mail admin@wysaid.org
 *
*/

package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;


public class GPUImageBilateralFilter extends GPUImageFilter {
	public static final String BILATERAL_VERTEX_SHADER = "" +
			"attribute vec4 position;\n" + 
			"attribute vec4 inputTextureCoordinate;\n" + 
	 
			"const int GAUSSIAN_SAMPLES = 9;\n" + 
	 
			"uniform vec2 singleStepOffset;\n" +  
	 
			"varying vec2 textureCoordinate;\n" + 
			"varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" + 
	 
			"void main()\n" + 
			"{\n" + 
	     	"	gl_Position = position;\n" + 
			"	textureCoordinate = inputTextureCoordinate.xy;\n" + 
	     
			"	int multiplier = 0;\n" + 
			"	vec2 blurStep;\n" +  
	     
			"	for (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n" + 
	     	"	{\n" + 
	        "		multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n" + 
	        
	        "		blurStep = float(multiplier) * singleStepOffset;\n" + 
	        "		blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n" + 
	     	"	}\n" + 
	 		"}";

	public static final String BILATERAL_FRAGMENT_SHADER = "" + 
			"uniform sampler2D inputImageTexture;\n" +

			" const lowp int GAUSSIAN_SAMPLES = 9;\n" +

			" varying highp vec2 textureCoordinate;\n" +
			" varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +

			" uniform mediump float distanceNormalizationFactor;\n" +

			" void main()\n" +
			" {\n" +
			"     lowp vec4 centralColor;\n" +
			"     lowp float gaussianWeightTotal;\n" +
			"     lowp vec4 sum;\n" +
			"     lowp vec4 sampleColor;\n" +
			"     lowp float distanceFromCentralColor;\n" +
			"     lowp float gaussianWeight;\n" +
			"     \n" +
			"     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n" +
			"     gaussianWeightTotal = 0.18;\n" +
			"     sum = centralColor * 0.18;\n" +
			"     \n" +
			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.15 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.12 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +

			"     sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n" +
			"     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
			"     gaussianWeight = 0.05 * (1.0 - distanceFromCentralColor);\n" +
			"     gaussianWeightTotal += gaussianWeight;\n" +
			"     sum += sampleColor * gaussianWeight;\n" +
			"     gl_FragColor = sum / gaussianWeightTotal;\n" +
//			" gl_FragColor.r = distanceNormalizationFactor / 20.0;" + 
			" }";

	private float mDistanceNormalizationFactor;
	private int mDisFactorLocation;
	private int mSingleStepOffsetLocation;
	
	public GPUImageBilateralFilter() {
		this(8.0f);
	}
	
	public GPUImageBilateralFilter(final float distanceNormalizationFactor) {
		super(BILATERAL_VERTEX_SHADER, BILATERAL_FRAGMENT_SHADER);
		mDistanceNormalizationFactor = distanceNormalizationFactor;
	}
	
	@Override
	public void onInit() {
		super.onInit();
		mDisFactorLocation = GLES20.glGetUniformLocation(getProgram(), "distanceNormalizationFactor");
		mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
	}
	
	@Override
	public void onInitialized() {
		super.onInitialized();
		setDistanceNormalizationFactor(mDistanceNormalizationFactor);
	}
	
	public void setDistanceNormalizationFactor(final float newValue) {
		mDistanceNormalizationFactor = newValue;
		setFloat(mDisFactorLocation, newValue);
	}
	
	private void setTexelSize(final float w, final float h) {
		setFloatVec2(mSingleStepOffsetLocation, new float[] {1.0f / w, 1.0f / h});
	}
	
	@Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        setTexelSize(width, height);
    }
}
