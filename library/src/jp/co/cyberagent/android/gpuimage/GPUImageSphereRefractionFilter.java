package jp.co.cyberagent.android.gpuimage;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageSphereRefractionFilter extends GPUImageFilter {
  
	public static final String SPHERE_FRAGMENT_SHADER = "" +
	
			"varying highp vec2 textureCoordinate;\n"+
			"\n"+
			"uniform sampler2D inputImageTexture;\n"+
			"\n"+
			"uniform highp vec2 center;\n"+
			"uniform highp float radius;\n"+
			"uniform highp float aspectRatio;\n"+
			"uniform highp float refractiveIndex;\n"+
			"\n"+
			"void main()\n"+
			"{\n"+
			"highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n"+
			"highp float distanceFromCenter = distance(center, textureCoordinateToUse);\n"+
			"lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n"+
			"\n"+
			"distanceFromCenter = distanceFromCenter / radius;\n"+
			"\n"+
			"highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\n"+
			"highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n"+
			"\n"+
			"highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n"+
			"\n"+
			"gl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n"+
			"}\n";
	
	private PointF center;
	private int centerLocation;
	private float radius;
	private int radiusLocation;
	private float aspectRatio;
	private int aspectRatioLocation;
	private float refractiveIndex;
	private int refractiveIndexLocation;
	
	public GPUImageSphereRefractionFilter()
	{
		this(new PointF(0.5f,0.5f),0.25f,0.71f);
	}
	
	public GPUImageSphereRefractionFilter(PointF center, float radius, float refractiveIndex)
	{
		super(NO_FILTER_VERTEX_SHADER, SPHERE_FRAGMENT_SHADER);
		this.center = center;
		this.radius = radius;
		this.refractiveIndex = refractiveIndex;
	}
	
    @Override
    public void onInit() {
        super.onInit();
        centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        refractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
    }
    
    @Override
    public void onInitialized() {
        super.onInitialized();
        setRadius(radius);
        setCenter(center);
        setRefractiveIndex(refractiveIndex);
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

	public void setRefractiveIndex(float refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
		
		setFloat(refractiveIndexLocation, refractiveIndex);
	}

	public void setCenter(PointF center) {
		this.center = center;
		
		setPoint(centerLocation, center);
	}

	public void setRadius(float radius) {
		this.radius = radius;
		
		setFloat(radiusLocation,radius);
	}
}
