package jp.co.cyberagent.android.gpuimage;

import jp.co.cyberagent.android.gpuimage.GPUImageTwoPassTextureSamplingFilter;

public class GPUImageDilationFilter extends GPUImageTwoPassTextureSamplingFilter 
{
  public static final String VERTEX_SHADER_1= 
  		
			"attribute vec4 position;\n"+
					"attribute vec2 inputTextureCoordinate;\n"+
					"\n"+
					"uniform float texelWidthOffset; \n"+
					"uniform float texelHeightOffset; \n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"gl_Position = position;\n"+
					"\n"+
					"vec2 offset = vec2(texelWidthOffset, texelHeightOffset);\n"+
					"\n"+
					"centerTextureCoordinate = inputTextureCoordinate;\n"+
					"oneStepNegativeTextureCoordinate = inputTextureCoordinate - offset;\n"+
					"oneStepPositiveTextureCoordinate = inputTextureCoordinate + offset;\n"+
					"}\n";
	
	public static final String VERTEX_SHADER_2=
			
			"attribute vec4 position;\n"+
					"attribute vec2 inputTextureCoordinate;\n"+
					"\n"+
					"uniform float texelWidthOffset;\n"+
					"uniform float texelHeightOffset;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"gl_Position = position;\n"+
					"\n"+
					"vec2 offset = vec2(texelWidthOffset, texelHeightOffset);\n"+
					"\n"+
					"centerTextureCoordinate = inputTextureCoordinate;\n"+
					"oneStepNegativeTextureCoordinate = inputTextureCoordinate - offset;\n"+
					"oneStepPositiveTextureCoordinate = inputTextureCoordinate + offset;\n"+
					"twoStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 2.0);\n"+
					"twoStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 2.0);\n"+
					"}\n";
	
	public static final String VERTEX_SHADER_3=
			
			"attribute vec4 position;\n"+
					"attribute vec2 inputTextureCoordinate;\n"+
					"\n"+
					"uniform float texelWidthOffset;\n"+
					"uniform float texelHeightOffset;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"varying vec2 threeStepsPositiveTextureCoordinate;\n"+
					"varying vec2 threeStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"gl_Position = position;\n"+
					"\n"+
					"vec2 offset = vec2(texelWidthOffset, texelHeightOffset);\n"+
					"\n"+
					"centerTextureCoordinate = inputTextureCoordinate;\n"+
					"oneStepNegativeTextureCoordinate = inputTextureCoordinate - offset;\n"+
					"oneStepPositiveTextureCoordinate = inputTextureCoordinate + offset;\n"+
					"twoStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 2.0);\n"+
					"twoStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 2.0);\n"+
					"threeStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 3.0);\n"+
					"threeStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 3.0);\n"+
					"}\n";
			
	public static final String VERTEX_SHADER_4=
			
			"attribute vec4 position;\n"+
					"attribute vec2 inputTextureCoordinate;\n"+
					"\n"+
					"uniform float texelWidthOffset;\n"+
					"uniform float texelHeightOffset;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"varying vec2 threeStepsPositiveTextureCoordinate;\n"+
					"varying vec2 threeStepsNegativeTextureCoordinate;\n"+
					"varying vec2 fourStepsPositiveTextureCoordinate;\n"+
					"varying vec2 fourStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"gl_Position = position;\n"+
					"\n"+
					"vec2 offset = vec2(texelWidthOffset, texelHeightOffset);\n"+
					"\n"+
					"centerTextureCoordinate = inputTextureCoordinate;\n"+
					"oneStepNegativeTextureCoordinate = inputTextureCoordinate - offset;\n"+
					"oneStepPositiveTextureCoordinate = inputTextureCoordinate + offset;\n"+
					"twoStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 2.0);\n"+
					"twoStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 2.0);\n"+
					"threeStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 3.0);\n"+
					"threeStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 3.0);\n"+
					"fourStepsNegativeTextureCoordinate = inputTextureCoordinate - (offset * 4.0);\n"+
					"fourStepsPositiveTextureCoordinate = inputTextureCoordinate + (offset * 4.0);\n"+
					"}\n";
			

	public static final String FRAGMENT_SHADER_1=
			
			"precision lowp float;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"\n"+
					"uniform sampler2D inputImageTexture;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"float centerIntensity = texture2D(inputImageTexture, centerTextureCoordinate).r;\n"+
					"float oneStepPositiveIntensity = texture2D(inputImageTexture, oneStepPositiveTextureCoordinate).r;\n"+
					"float oneStepNegativeIntensity = texture2D(inputImageTexture, oneStepNegativeTextureCoordinate).r;\n"+
					"\n"+
					"lowp float maxValue = max(centerIntensity, oneStepPositiveIntensity);\n"+
					"maxValue = max(maxValue, oneStepNegativeIntensity);\n"+
					"\n"+
					"gl_FragColor = vec4(vec3(maxValue), 1.0);\n"+
					"}\n";
	
	public static final String FRAGMENT_SHADER_2=
			
			"precision lowp float;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"uniform sampler2D inputImageTexture;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"float centerIntensity = texture2D(inputImageTexture, centerTextureCoordinate).r;\n"+
					"float oneStepPositiveIntensity = texture2D(inputImageTexture, oneStepPositiveTextureCoordinate).r;\n"+
					"float oneStepNegativeIntensity = texture2D(inputImageTexture, oneStepNegativeTextureCoordinate).r;\n"+
					"float twoStepsPositiveIntensity = texture2D(inputImageTexture, twoStepsPositiveTextureCoordinate).r;\n"+
					"float twoStepsNegativeIntensity = texture2D(inputImageTexture, twoStepsNegativeTextureCoordinate).r;\n"+
					"\n"+
					"lowp float maxValue = max(centerIntensity, oneStepPositiveIntensity);\n"+
					"maxValue = max(maxValue, oneStepNegativeIntensity);\n"+
					"maxValue = max(maxValue, twoStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, twoStepsNegativeIntensity);\n"+
					"\n"+
					"gl_FragColor = vec4(vec3(maxValue), 1.0);\n"+
					"}\n";
			
	public static final String FRAGMENT_SHADER_3=
			
			"precision lowp float;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"varying vec2 threeStepsPositiveTextureCoordinate;\n"+
					"varying vec2 threeStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"uniform sampler2D inputImageTexture;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"float centerIntensity = texture2D(inputImageTexture, centerTextureCoordinate).r;\n"+
					"float oneStepPositiveIntensity = texture2D(inputImageTexture, oneStepPositiveTextureCoordinate).r;\n"+
					"float oneStepNegativeIntensity = texture2D(inputImageTexture, oneStepNegativeTextureCoordinate).r;\n"+
					"float twoStepsPositiveIntensity = texture2D(inputImageTexture, twoStepsPositiveTextureCoordinate).r;\n"+
					"float twoStepsNegativeIntensity = texture2D(inputImageTexture, twoStepsNegativeTextureCoordinate).r;\n"+
					"float threeStepsPositiveIntensity = texture2D(inputImageTexture, threeStepsPositiveTextureCoordinate).r;\n"+
					"float threeStepsNegativeIntensity = texture2D(inputImageTexture, threeStepsNegativeTextureCoordinate).r;\n"+
					"\n"+
					"lowp float maxValue = max(centerIntensity, oneStepPositiveIntensity);\n"+
					"maxValue = max(maxValue, oneStepNegativeIntensity);\n"+
					"maxValue = max(maxValue, twoStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, twoStepsNegativeIntensity);\n"+
					"maxValue = max(maxValue, threeStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, threeStepsNegativeIntensity);\n"+
					"\n"+
					"gl_FragColor = vec4(vec3(maxValue), 1.0);\n"+
					"}\n";
	
	public static final String FRAGMENT_SHADER_4=
			
			"precision lowp float;\n"+
					"\n"+
					"varying vec2 centerTextureCoordinate;\n"+
					"varying vec2 oneStepPositiveTextureCoordinate;\n"+
					"varying vec2 oneStepNegativeTextureCoordinate;\n"+
					"varying vec2 twoStepsPositiveTextureCoordinate;\n"+
					"varying vec2 twoStepsNegativeTextureCoordinate;\n"+
					"varying vec2 threeStepsPositiveTextureCoordinate;\n"+
					"varying vec2 threeStepsNegativeTextureCoordinate;\n"+
					"varying vec2 fourStepsPositiveTextureCoordinate;\n"+
					"varying vec2 fourStepsNegativeTextureCoordinate;\n"+
					"\n"+
					"uniform sampler2D inputImageTexture;\n"+
					"\n"+
					"void main()\n"+
					"{\n"+
					"float centerIntensity = texture2D(inputImageTexture, centerTextureCoordinate).r;\n"+
					"float oneStepPositiveIntensity = texture2D(inputImageTexture, oneStepPositiveTextureCoordinate).r;\n"+
					"float oneStepNegativeIntensity = texture2D(inputImageTexture, oneStepNegativeTextureCoordinate).r;\n"+
					"float twoStepsPositiveIntensity = texture2D(inputImageTexture, twoStepsPositiveTextureCoordinate).r;\n"+
					"float twoStepsNegativeIntensity = texture2D(inputImageTexture, twoStepsNegativeTextureCoordinate).r;\n"+
					"float threeStepsPositiveIntensity = texture2D(inputImageTexture, threeStepsPositiveTextureCoordinate).r;\n"+
					"float threeStepsNegativeIntensity = texture2D(inputImageTexture, threeStepsNegativeTextureCoordinate).r;\n"+
					"float fourStepsPositiveIntensity = texture2D(inputImageTexture, fourStepsPositiveTextureCoordinate).r;\n"+
					"float fourStepsNegativeIntensity = texture2D(inputImageTexture, fourStepsNegativeTextureCoordinate).r;\n"+
					"\n"+
					"lowp float maxValue = max(centerIntensity, oneStepPositiveIntensity);\n"+
					"maxValue = max(maxValue, oneStepNegativeIntensity);\n"+
					"maxValue = max(maxValue, twoStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, twoStepsNegativeIntensity);\n"+
					"maxValue = max(maxValue, threeStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, threeStepsNegativeIntensity);\n"+
					"maxValue = max(maxValue, fourStepsPositiveIntensity);\n"+
					"maxValue = max(maxValue, fourStepsNegativeIntensity);\n"+
					"\n"+
					"gl_FragColor = vec4(vec3(maxValue), 1.0);\n"+
					"}\n";
			

	public GPUImageDilationFilter() 
	{
		this(1);
	}

	public GPUImageDilationFilter(int radius) 
	{
		this(getVertexShader(radius), getFragmentShader(radius));
	}

	private GPUImageDilationFilter(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader, vertexShader, fragmentShader);
	}
	
	private static String getVertexShader(int radius)
	{
		switch (radius)
		{
		case 0:
		case 1:
			return VERTEX_SHADER_1;
		case 2:
			return VERTEX_SHADER_2;
		case 3:
			return VERTEX_SHADER_3;
		default:
			return VERTEX_SHADER_4;
		}
	}
	
	private static String getFragmentShader(int radius)
	{
		switch (radius)
		{
		case 0:
		case 1:
			return FRAGMENT_SHADER_1;
		case 2:
			return FRAGMENT_SHADER_2;
		case 3:
			return FRAGMENT_SHADER_3;
		default:
			return FRAGMENT_SHADER_4;
		}
	}
	
}
