package jp.co.cyberagent.android.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageSwirlFilter extends GPUImageFilter {
  
	public static final String SWIRL_FRAGMENT_SHADER = "" +
			"varying highp vec2 textureCoordinate;\n"+
			"\n"+
			"uniform sampler2D inputImageTexture;\n"+
			"\n"+
			"uniform highp vec2 center;\n"+
			"uniform highp float radius;\n"+
			"uniform highp float angle;\n"+
			"\n"+
			"void main()\n"+
			"{\n"+
			"highp vec2 textureCoordinateToUse = textureCoordinate;\n"+
			"highp float dist = distance(center, textureCoordinate);\n"+
			"if (dist < radius)\n"+
			"{\n"+
			"textureCoordinateToUse -= center;\n"+
			"highp float percent = (radius - dist) / radius;\n"+
			"highp float theta = percent * percent * angle * 8.0;\n"+
			"highp float s = sin(theta);\n"+
			"highp float c = cos(theta);\n"+
			"textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\n"+
			"textureCoordinateToUse += center;\n"+
			"}\n"+
			"\n"+
			"gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n"+
			"\n"+
			"}\n";
	
	private float angle;
	private int angleLocation;
	private float radius;
	private int radiusLocation;
	private PointF center;
	private int centerLocation;
	
	public GPUImageSwirlFilter()
	{
		this(0.5f, 1.0f, new PointF(0.5f,0.5f));
	}
	
	public GPUImageSwirlFilter(float radius, float angle, PointF center)
	{
		super(NO_FILTER_VERTEX_SHADER, SWIRL_FRAGMENT_SHADER);
		this.radius = radius;
		this.angle = angle;
		this.center = center;
	}
	
    @Override
    public void onInit() {
        super.onInit();
        angleLocation = GLES20.glGetUniformLocation(getProgram(), "angle");
        radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
    }
    
    @Override
    public void onInitialized() {
        super.onInitialized();
        setRadius(radius);
        setAngle(angle);
        setCenter(center);
    }
    
    public void setRadius(float radius) {
    	this.radius = radius;
    	
    	setFloat(radiusLocation, radius);
    }
    
    public void setAngle(float angle) {
    	this.angle = angle;
    	
    	setFloat(angleLocation, angle);
    }
    
    public void setCenter(PointF center) {
    	this.center = center;
    	
    	setPoint(centerLocation, center);
    }
}
