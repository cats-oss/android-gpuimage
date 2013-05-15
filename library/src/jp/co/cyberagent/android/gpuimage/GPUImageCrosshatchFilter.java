package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImageCrosshatchFilter extends GPUImageFilter {
  
	public static final String CROSSHATCH_FRAGMENT_SHADER = "" +
	
			"varying highp vec2 textureCoordinate;\n"+
			"uniform sampler2D inputImageTexture;\n"+
			"uniform highp float crossHatchSpacing;\n"+
			"uniform highp float lineWidth;\n"+
			"const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n"+
			"void main()\n"+
			"{\n"+
			"highp float luminance = dot(texture2D(inputImageTexture, textureCoordinate).rgb, W);\n"+
			"lowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\n"+
			"if (luminance < 1.00)\n"+
			"{\n"+
			"if (mod(textureCoordinate.x + textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n"+
			"{\n"+
			"colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"+
			"}\n"+
			"}\n"+
			"if (luminance < 0.75)\n"+
			"{\n"+
			"if (mod(textureCoordinate.x - textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n"+
			"{\n"+
			"colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"+
			"}\n"+
			"}\n"+
			"if (luminance < 0.50)\n"+
			"{\n"+
			"if (mod(textureCoordinate.x + textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n"+
			"{\n"+
            "colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"+
			"}\n"+
			"}\n"+
			"if (luminance < 0.3)\n"+
			"{\n"+
			"if (mod(textureCoordinate.x - textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n"+
			"{\n"+
            "colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n"+
         	"}\n"+
     		"}\n"+
     		"gl_FragColor = colorToDisplay;\n"+
			"}\n";
	
	private float mCrossHatchSpacing;
	private int mCrossHatchSpacingLocation;
	private float mLineWidth;
	private int mLineWidthLocation;
	
	public GPUImageCrosshatchFilter()
	{
		this(0.03f,0.003f);
	}
	
	public GPUImageCrosshatchFilter(float crossHatchSpacing, float lineWidth)
	{
		super(NO_FILTER_VERTEX_SHADER, CROSSHATCH_FRAGMENT_SHADER);
		mCrossHatchSpacing = crossHatchSpacing;
		mLineWidth = lineWidth;
	}
	
    @Override
    public void onInit() {
        super.onInit();
        mCrossHatchSpacingLocation = GLES20.glGetUniformLocation(getProgram(), "crossHatchSpacing");
        mLineWidthLocation = GLES20.glGetUniformLocation(getProgram(), "lineWidth");
    }
    
    @Override
    public void onInitialized() {
        super.onInitialized();
        setCrossHatchSpacing(mCrossHatchSpacing);
        setLineWidth(mLineWidth);
    }
    
    public void setCrossHatchSpacing(final float crossHatchSpacing) {
    	
    	float singlePixelSpacing = 0;
    	if (getOutputWidth()!=0)
    		singlePixelSpacing = 1.0f/(float)getOutputWidth();
    	else
    		singlePixelSpacing = 1.0f/2048.0f;
    	
    	if (crossHatchSpacing < singlePixelSpacing)
    		mCrossHatchSpacing = singlePixelSpacing;
    	else
    		mCrossHatchSpacing = crossHatchSpacing;
    	
        setFloat(mCrossHatchSpacingLocation, mCrossHatchSpacing);
    }
    
    public void setLineWidth(final float lineWidth)
    {
    	mLineWidth = lineWidth;
    	
    	setFloat(mLineWidthLocation,mLineWidth);
    }

}
