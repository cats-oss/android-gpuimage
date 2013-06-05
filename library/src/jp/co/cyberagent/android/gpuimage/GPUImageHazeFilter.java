package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImageHazeFilter extends GPUImageFilter {
  
	public static final String HAZE_FRAGMENT_SHADER = "" +
	
			"varying highp vec2 textureCoordinate;\n"+
			"\n"+
			"uniform sampler2D inputImageTexture;\n"+
			"\n"+
			"uniform lowp float distance;\n"+
			"uniform highp float slope;\n"+
			"\n"+
			"void main()\n"+
			"{\n"+
			"	//todo reconsider precision modifiers	 \n"+
			"	 highp vec4 color = vec4(1.0);//todo reimplement as a parameter\n"+
			"\n"+
			"	 highp float  d = textureCoordinate.y * slope  +  distance; \n"+
			"\n"+
			"	 highp vec4 c = texture2D(inputImageTexture, textureCoordinate) ; // consider using unpremultiply\n"+
			"\n"+
			"	 c = (c - d * color) / (1.0 -d);\n"+
			"\n"+
			"	 gl_FragColor = c; //consider using premultiply(c);\n"+
			"}\n";
	
	private float distance;
	private int distanceLocation;
	private float slope;
	private int slopeLocation;
	
	public GPUImageHazeFilter()
	{
		this(0.2f,0.0f);
	}
	
	public GPUImageHazeFilter(float distance, float slope)
	{
		super(NO_FILTER_VERTEX_SHADER, HAZE_FRAGMENT_SHADER);
		this.distance = distance;
		this.slope = slope;
	}
	
    @Override
    public void onInit() {
        super.onInit();
        distanceLocation = GLES20.glGetUniformLocation(getProgram(), "distance");
        slopeLocation = GLES20.glGetUniformLocation(getProgram(), "slope");
    }
    
    @Override
    public void onInitialized() {
        super.onInitialized();
        setDistance(distance);
        setSlope(slope);
    }
    
    public void setDistance(float distance) {
    	
    	this.distance = distance;
    	
        setFloat(distanceLocation, distance);
    }
    
    public void setSlope(float slope)
    {
    	this.slope = slope;
    	
    	setFloat(slopeLocation,slope);
    }

}
