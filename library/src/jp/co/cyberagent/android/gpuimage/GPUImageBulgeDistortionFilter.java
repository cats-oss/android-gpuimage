package jp.co.cyberagent.android.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageBulgeDistortionFilter extends GPUImageFilter {
	
	public static final String BULGE_FRAGMENT_SHADER = "" +
			"varying highp vec2 textureCoordinate;\n"+
			"\n"+
			"uniform sampler2D inputImageTexture;\n"+
			"\n"+
			"uniform highp float aspectRatio;\n"+
			"uniform highp vec2 center;\n"+
			"uniform highp float radius;\n"+
			"uniform highp float scale;\n"+
			"\n"+
			"void main()\n"+
			"{\n"+
			"highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n"+
			"highp float dist = distance(center, textureCoordinateToUse);\n"+
			"textureCoordinateToUse = textureCoordinate;\n"+
			"\n"+
			"if (dist < radius)\n"+
			"{\n"+
			"textureCoordinateToUse -= center;\n"+
			"highp float percent = 1.0 - ((radius - dist) / radius) * scale;\n"+
			"percent = percent * percent;\n"+
			"\n"+
			"textureCoordinateToUse = textureCoordinateToUse * percent;\n"+
			"textureCoordinateToUse += center;\n"+
			"}\n"+
			"\n"+
			"gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );    \n"+
			"}\n";
	
	private float scale;
	private int scaleLocation;
	private float radius;
	private int radiusLocation;
	private PointF center;
	private int centerLocation;
	private float aspectRatio;
	private int aspectRatioLocation;
	
	public GPUImageBulgeDistortionFilter()
	{
		this(0.25f, 0.5f, new PointF(0.5f,0.5f));
	}
	
	public GPUImageBulgeDistortionFilter(float radius, float scale, PointF center)
	{
		super(NO_FILTER_VERTEX_SHADER, BULGE_FRAGMENT_SHADER);
		this.radius = radius;
		this.scale = scale;
		this.center = center;
	}
	
    @Override
    public void onInit() {
        super.onInit();
        scaleLocation = GLES20.glGetUniformLocation(getProgram(), "scale");
        radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
    }
    
    @Override
    public void onInitialized() {
        super.onInitialized();
        setRadius(radius);
        setScale(scale);
        setCenter(center);
    }
    
    @Override
    public void onOutputSizeChanged(int width, int height) {
        aspectRatio = (float)height/width;
        setAspectRatio(aspectRatio);
    	super.onOutputSizeChanged(width, height);
    }
    
    private void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		
		setFloat(aspectRatioLocation, aspectRatio);
	}
    
    public void setRadius(float radius) {
    	this.radius = radius;
    	
    	setFloat(radiusLocation, radius);
    }
    
    public void setScale(float scale) {
    	this.scale = scale;
    	
    	setFloat(scaleLocation, scale);
    }
    
    public void setCenter(PointF center) {
    	this.center = center;
    	
    	setPoint(centerLocation, center);
    }
}
