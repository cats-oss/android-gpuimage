package jp.co.cyberagent.android.gpuimage;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;

public class GPUImageBlendFilter extends GPUImageTwoInputFilter{
	
	private int mixUniform = -1; //for alpha blending
	private float alpha = 0.5f; //for alpha blending
	private boolean needSetAlpha = false; //for alpha blending
	
	
	private String mCurrentBlendMode = null;
	
	public GPUImageBlendFilter( String blendMode, Context context ){
		super(context, blendMode );
		mCurrentBlendMode = blendMode;
	}
	
	@Override
	public void onInit(){
		super.onInit();
		mixUniform = GLES20.glGetUniformLocation(mGLProgId, "mixturePercent");
	}
	
	@Override
	public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer){
		super.onDraw(textureId, cubeBuffer, textureBuffer);
		if(needSetAlpha){
			needSetAlpha = false;
			setAlphaBlendOpacity( alpha );
		}
	}
	
	public void setAlphaBlendOpacity(float mix){
		if(mCurrentBlendMode != BLEND_MODE_ALPHA){
			return;
		}
		alpha = mix;
		if(mixUniform == -1){
			needSetAlpha = true;
		}else{
			this.setFloat(mixUniform, mix);
		}
	}
	
	public void setColor(final int r, final int g, final int b, final int alpha){
		runOnDraw(new Runnable(){
			public void run(){
				int[] textureId = new int[1];
		        byte[] pixels = {  
		            (byte)r,  (byte) g,  (byte) b,  (byte) alpha 
		        };
		        
		        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(1*4);
		        pixelBuffer.put(pixels).position(0);

		        GLES20.glPixelStorei ( GLES20.GL_UNPACK_ALIGNMENT, 1 );
		        textureId[0] = filterSourceTexture2;
		        GLES20.glActiveTexture( GLES20.GL_TEXTURE3 );
		        GLES20.glGenTextures ( 1, textureId, 0 );
		        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );
		        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 1, 1, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer );
		        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );
		        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );
		        filterSourceTexture2 = textureId[0];
			}
		});
	}
	
	
	public static final String BLEND_MODE_SOURCE_OVER = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(baseColor.rgb, lum(overlayColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_COLOR_BURN = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"    mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"    mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"    mediump vec4 whiteColor = vec4(1.0);\n" + 
			"    gl_FragColor = whiteColor - (whiteColor - textureColor) / textureColor2;\n" + 
			" }";
	public static final String BLEND_MODE_COLOR_DODGE = "precision mediump float;\n" + 
			" \n" + 
			" varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     vec3 baseOverlayAlphaProduct = vec3(overlay.a * base.a);\n" + 
			"     vec3 rightHandProduct = overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a);\n" + 
			"     \n" + 
			"     vec3 firstBlendColor = baseOverlayAlphaProduct + rightHandProduct;\n" + 
			"     vec3 overlayRGB = clamp((overlay.rgb / clamp(overlay.a, 0.01, 1.0)) * step(0.0, overlay.a), 0.0, 0.99);\n" + 
			"     \n" + 
			"     vec3 secondBlendColor = (base.rgb * overlay.a) / (1.0 - overlayRGB) + rightHandProduct;\n" + 
			"     \n" + 
			"     vec3 colorChoice = step((overlay.rgb * base.a + base.rgb * overlay.a), baseOverlayAlphaProduct);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(mix(firstBlendColor, secondBlendColor, colorChoice), 1.0);\n" + 
			" }";
	public static final String BLEND_MODE_DARKEN = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"    lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"    lowp vec4 overlayer = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"    \n" + 
			"    gl_FragColor = vec4(min(overlayer.rgb * base.a, base.rgb * overlayer.a) + overlayer.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlayer.a), 1.0);\n" + 
			" }";
	public static final String BLEND_MODE_DIFFERENCE = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     gl_FragColor = vec4(abs(textureColor2.rgb - textureColor.rgb), textureColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_DISSOLVE = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" uniform lowp float mixturePercent;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"    \n" + 
			"    gl_FragColor = mix(textureColor, textureColor2, mixturePercent);\n" + 
			" }";
	public static final String BLEND_MODE_EXCLUSION = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     //     Dca = (Sca.Da + Dca.Sa - 2.Sca.Dca) + Sca.(1 - Da) + Dca.(1 - Sa)\n" + 
			"     \n" + 
			"     gl_FragColor = vec4((overlay.rgb * base.a + base.rgb * overlay.a - 2.0 * overlay.rgb * base.rgb) + overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a), base.a);\n" + 
			" }";
	public static final String BLEND_MODE_HARD_LIGHT = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			"\n" + 
			" const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" + 
			"\n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"\n" + 
			"     highp float ra;\n" + 
			"     if (2.0 * overlay.r < overlay.a) {\n" + 
			"         ra = 2.0 * overlay.r * base.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ra = overlay.a * base.a - 2.0 * (base.a - base.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     highp float ga;\n" + 
			"     if (2.0 * overlay.g < overlay.a) {\n" + 
			"         ga = 2.0 * overlay.g * base.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ga = overlay.a * base.a - 2.0 * (base.a - base.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     highp float ba;\n" + 
			"     if (2.0 * overlay.b < overlay.a) {\n" + 
			"         ba = 2.0 * overlay.b * base.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ba = overlay.a * base.a - 2.0 * (base.a - base.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(ra, ga, ba, 1.0);\n" + 
			" }\n" + 
			"";
	public static final String BLEND_MODE_LIGHTEN = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"    \n" + 
			"    gl_FragColor = max(textureColor, textureColor2);\n" + 
			" }";
	public static final String BLEND_MODE_ADD = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 lowp vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"	 \n" + 
			"   mediump float r;\n" + 
			"   if (overlay.r * base.a + base.r * overlay.a >= overlay.a * base.a) {\n" + 
			"     r = overlay.a * base.a + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"   } else {\n" + 
			"     r = overlay.r + base.r;\n" + 
			"   }\n" + 
			"\n" + 
			"   mediump float g;\n" + 
			"   if (overlay.g * base.a + base.g * overlay.a >= overlay.a * base.a) {\n" + 
			"     g = overlay.a * base.a + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"   } else {\n" + 
			"     g = overlay.g + base.g;\n" + 
			"   }\n" + 
			"\n" + 
			"   mediump float b;\n" + 
			"   if (overlay.b * base.a + base.b * overlay.a >= overlay.a * base.a) {\n" + 
			"     b = overlay.a * base.a + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"   } else {\n" + 
			"     b = overlay.b + base.b;\n" + 
			"   }\n" + 
			"\n" + 
			"   mediump float a = overlay.a + base.a - overlay.a * base.a;\n" + 
			"   \n" + 
			"	 gl_FragColor = vec4(r, g, b, a);\n" + 
			" }\n" + 
			"";
	public static final String BLEND_MODE_DIVIDE = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"   \n" + 
			"   mediump float ra;\n" + 
			"   if (overlay.a == 0.0 || ((base.r / overlay.r) > (base.a / overlay.a)))\n" + 
			"     ra = overlay.a * base.a + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"   else\n" + 
			"     ra = (base.r * overlay.a * overlay.a) / overlay.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"   \n" + 
			"\n" + 
			"   mediump float ga;\n" + 
			"   if (overlay.a == 0.0 || ((base.g / overlay.g) > (base.a / overlay.a)))\n" + 
			"     ga = overlay.a * base.a + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"   else\n" + 
			"     ga = (base.g * overlay.a * overlay.a) / overlay.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"\n" + 
			"   \n" + 
			"   mediump float ba;\n" + 
			"   if (overlay.a == 0.0 || ((base.b / overlay.b) > (base.a / overlay.a)))\n" + 
			"     ba = overlay.a * base.a + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"   else\n" + 
			"     ba = (base.b * overlay.a * overlay.a) / overlay.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"\n" + 
			"   mediump float a = overlay.a + base.a - overlay.a * base.a;\n" + 
			"   \n" + 
			"	 gl_FragColor = vec4(ra, ga, ba, a);\n" + 
			" }";
	public static final String BLEND_MODE_MULTIPLY = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(baseColor.rgb, lum(overlayColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_OVERLAY = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     mediump float ra;\n" + 
			"     if (2.0 * base.r < base.a) {\n" + 
			"         ra = 2.0 * overlay.r * base.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ra = overlay.a * base.a - 2.0 * (base.a - base.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     mediump float ga;\n" + 
			"     if (2.0 * base.g < base.a) {\n" + 
			"         ga = 2.0 * overlay.g * base.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ga = overlay.a * base.a - 2.0 * (base.a - base.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     mediump float ba;\n" + 
			"     if (2.0 * base.b < base.a) {\n" + 
			"         ba = 2.0 * overlay.b * base.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"     } else {\n" + 
			"         ba = overlay.a * base.a - 2.0 * (base.a - base.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n" + 
			"     }\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(ra, ga, ba, 1.0);\n" + 
			" }\n" + 
			"";
	public static final String BLEND_MODE_SCREEN = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     mediump vec4 whiteColor = vec4(1.0);\n" + 
			"     gl_FragColor = whiteColor - ((whiteColor - textureColor2) * (whiteColor - textureColor));\n" + 
			" }";
	
	public static final String BLEND_MODE_ALPHA = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" uniform lowp float mixturePercent;\n" + 
			"\n" + 
			" void main()\n" + 
			" {\n" + 
			"	 lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"	 \n" + 
			"	 gl_FragColor = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a * mixturePercent), textureColor.a);\n" + 
			" }";
	
	
	public static final String BLEND_MODE_COLOR = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			"\n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"\n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(overlayColor.rgb, lum(baseColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }\n" + 
			"";
	public static final String BLEND_MODE_HUE = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" highp float sat(lowp vec3 c) {\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     return x - n;\n" + 
			" }\n" + 
			" \n" + 
			" lowp float mid(lowp float cmin, lowp float cmid, lowp float cmax, highp float s) {\n" + 
			"     return ((cmid - cmin) * s) / (cmax - cmin);\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setsat(lowp vec3 c, highp float s) {\n" + 
			"     if (c.r > c.g) {\n" + 
			"         if (c.r > c.b) {\n" + 
			"             if (c.g > c.b) {\n" + 
			"                 /* g is mid, b is min */\n" + 
			"                 c.g = mid(c.b, c.g, c.r, s);\n" + 
			"                 c.b = 0.0;\n" + 
			"             } else {\n" + 
			"                 /* b is mid, g is min */\n" + 
			"                 c.b = mid(c.g, c.b, c.r, s);\n" + 
			"                 c.g = 0.0;\n" + 
			"             }\n" + 
			"             c.r = s;\n" + 
			"         } else {\n" + 
			"             /* b is max, r is mid, g is min */\n" + 
			"             c.r = mid(c.g, c.r, c.b, s);\n" + 
			"             c.b = s;\n" + 
			"             c.r = 0.0;\n" + 
			"         }\n" + 
			"     } else if (c.r > c.b) {\n" + 
			"         /* g is max, r is mid, b is min */\n" + 
			"         c.r = mid(c.b, c.r, c.g, s);\n" + 
			"         c.g = s;\n" + 
			"         c.b = 0.0;\n" + 
			"     } else if (c.g > c.b) {\n" + 
			"         /* g is max, b is mid, r is min */\n" + 
			"         c.b = mid(c.r, c.b, c.g, s);\n" + 
			"         c.g = s;\n" + 
			"         c.r = 0.0;\n" + 
			"     } else if (c.b > c.g) {\n" + 
			"         /* b is max, g is mid, r is min */\n" + 
			"         c.g = mid(c.r, c.g, c.b, s);\n" + 
			"         c.b = s;\n" + 
			"         c.r = 0.0;\n" + 
			"     } else {\n" + 
			"         c = vec3(0.0);\n" + 
			"     }\n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(setsat(overlayColor.rgb, sat(baseColor.rgb)), lum(baseColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_SATURATION = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" highp float sat(lowp vec3 c) {\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     return x - n;\n" + 
			" }\n" + 
			" \n" + 
			" lowp float mid(lowp float cmin, lowp float cmid, lowp float cmax, highp float s) {\n" + 
			"     return ((cmid - cmin) * s) / (cmax - cmin);\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setsat(lowp vec3 c, highp float s) {\n" + 
			"     if (c.r > c.g) {\n" + 
			"         if (c.r > c.b) {\n" + 
			"             if (c.g > c.b) {\n" + 
			"                 /* g is mid, b is min */\n" + 
			"                 c.g = mid(c.b, c.g, c.r, s);\n" + 
			"                 c.b = 0.0;\n" + 
			"             } else {\n" + 
			"                 /* b is mid, g is min */\n" + 
			"                 c.b = mid(c.g, c.b, c.r, s);\n" + 
			"                 c.g = 0.0;\n" + 
			"             }\n" + 
			"             c.r = s;\n" + 
			"         } else {\n" + 
			"             /* b is max, r is mid, g is min */\n" + 
			"             c.r = mid(c.g, c.r, c.b, s);\n" + 
			"             c.b = s;\n" + 
			"             c.r = 0.0;\n" + 
			"         }\n" + 
			"     } else if (c.r > c.b) {\n" + 
			"         /* g is max, r is mid, b is min */\n" + 
			"         c.r = mid(c.b, c.r, c.g, s);\n" + 
			"         c.g = s;\n" + 
			"         c.b = 0.0;\n" + 
			"     } else if (c.g > c.b) {\n" + 
			"         /* g is max, b is mid, r is min */\n" + 
			"         c.b = mid(c.r, c.b, c.g, s);\n" + 
			"         c.g = s;\n" + 
			"         c.r = 0.0;\n" + 
			"     } else if (c.b > c.g) {\n" + 
			"         /* b is max, g is mid, r is min */\n" + 
			"         c.g = mid(c.r, c.g, c.b, s);\n" + 
			"         c.b = s;\n" + 
			"         c.r = 0.0;\n" + 
			"     } else {\n" + 
			"         c = vec3(0.0);\n" + 
			"     }\n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(setsat(baseColor.rgb, sat(overlayColor.rgb)), lum(baseColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_LUMINOSITY = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" highp float lum(lowp vec3 c) {\n" + 
			"     return dot(c, vec3(0.3, 0.59, 0.11));\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 clipcolor(lowp vec3 c) {\n" + 
			"     highp float l = lum(c);\n" + 
			"     lowp float n = min(min(c.r, c.g), c.b);\n" + 
			"     lowp float x = max(max(c.r, c.g), c.b);\n" + 
			"     \n" + 
			"     if (n < 0.0) {\n" + 
			"         c.r = l + ((c.r - l) * l) / (l - n);\n" + 
			"         c.g = l + ((c.g - l) * l) / (l - n);\n" + 
			"         c.b = l + ((c.b - l) * l) / (l - n);\n" + 
			"     }\n" + 
			"     if (x > 1.0) {\n" + 
			"         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n" + 
			"         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n" + 
			"     }\n" + 
			"     \n" + 
			"     return c;\n" + 
			" }\n" + 
			" \n" + 
			" lowp vec3 setlum(lowp vec3 c, highp float l) {\n" + 
			"     highp float d = l - lum(c);\n" + 
			"     c = c + vec3(d);\n" + 
			"     return clipcolor(c);\n" + 
			" }\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 highp vec4 overlayColor = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(baseColor.rgb, lum(overlayColor.rgb)) * overlayColor.a, baseColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_LINEAR_BURN = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = vec4(clamp(textureColor.rgb + textureColor2.rgb - vec3(1.0), vec3(0.0), vec3(1.0)), textureColor.a);\n" + 
			" }";
	
	public static final String BLEND_MODE_SOFT_LIGHT = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     mediump vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     mediump vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     gl_FragColor = base * (overlay.a * (base / base.a) + (2.0 * overlay * (1.0 - (base / base.a)))) + overlay * (1.0 - base.a) + base * (1.0 - overlay.a);\n" + 
			" }";
	public static final String BLEND_MODE_SUBSTRACT = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"	 lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"	 \n" + 
			"	 gl_FragColor = vec4(textureColor.rgb - textureColor2.rgb, textureColor.a);\n" + 
			" }";
	public static final String BLEND_MODE_CHROMAKEY = "precision highp float;\n" + 
			" \n" + 
			" varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			"\n" + 
			" uniform float thresholdSensitivity;\n" + 
			" uniform float smoothing;\n" + 
			" uniform vec3 colorToReplace;\n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     float maskY = 0.2989 * colorToReplace.r + 0.5866 * colorToReplace.g + 0.1145 * colorToReplace.b;\n" + 
			"     float maskCr = 0.7132 * (colorToReplace.r - maskY);\n" + 
			"     float maskCb = 0.5647 * (colorToReplace.b - maskY);\n" + 
			"     \n" + 
			"     float Y = 0.2989 * textureColor.r + 0.5866 * textureColor.g + 0.1145 * textureColor.b;\n" + 
			"     float Cr = 0.7132 * (textureColor.r - Y);\n" + 
			"     float Cb = 0.5647 * (textureColor.b - Y);\n" + 
			"     \n" + 
			"//     float blendValue = 1.0 - smoothstep(thresholdSensitivity - smoothing, thresholdSensitivity , abs(Cr - maskCr) + abs(Cb - maskCb));\n" + 
			"     float blendValue = 1.0 - smoothstep(thresholdSensitivity, thresholdSensitivity + smoothing, distance(vec2(Cr, Cb), vec2(maskCr, maskCb)));\n" + 
			"     gl_FragColor = mix(textureColor, textureColor2, blendValue);\n" + 
			" }\n" + 
			"";
	
	public static final String BLEND_MODE_NORMAL = "varying highp vec2 textureCoordinate;\n" + 
			" varying highp vec2 textureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" void main()\n" + 
			" {\n" + 
			"     lowp vec4 c2 = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"	 lowp vec4 c1 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     \n" + 
			"     lowp vec4 outputColor;\n" + 
			"     \n" + 
			"     outputColor.r = c1.r + c2.r * c2.a * (1.0 - c1.a);\n" + 
			"\n" + 
			"     outputColor.g = c1.g + c2.g * c2.a * (1.0 - c1.a);\n" + 
			"     \n" + 
			"     outputColor.b = c1.b + c2.b * c2.a * (1.0 - c1.a);\n" + 
			"     \n" + 
			"     outputColor.a = c1.a + c2.a * (1.0 - c1.a);\n" + 
			"     \n" + 
			"     gl_FragColor = outputColor;\n" + 
			" }";
	
	
	
	public static final String BLEND_MODE_POISSON = "precision mediump float;\n" + 
			" \n" + 
			" varying vec2 textureCoordinate;\n" + 
			" varying vec2 leftTextureCoordinate;\n" + 
			" varying vec2 rightTextureCoordinate;\n" + 
			" varying vec2 topTextureCoordinate;\n" + 
			" varying vec2 bottomTextureCoordinate;\n" + 
			" \n" + 
			" varying vec2 textureCoordinate2;\n" + 
			" varying vec2 leftTextureCoordinate2;\n" + 
			" varying vec2 rightTextureCoordinate2;\n" + 
			" varying vec2 topTextureCoordinate2;\n" + 
			" varying vec2 bottomTextureCoordinate2;\n" + 
			" \n" + 
			" uniform sampler2D inputImageTexture;\n" + 
			" uniform sampler2D inputImageTexture2;\n" + 
			" \n" + 
			" uniform lowp float mixturePercent;\n" + 
			"\n" + 
			" void main()\n" + 
			" {\n" + 
			"     vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\n" + 
			"     vec3 bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n" + 
			"     vec3 leftColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n" + 
			"     vec3 rightColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n" + 
			"     vec3 topColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n" + 
			"\n" + 
			"     vec4 centerColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n" + 
			"     vec3 bottomColor2 = texture2D(inputImageTexture2, bottomTextureCoordinate2).rgb;\n" + 
			"     vec3 leftColor2 = texture2D(inputImageTexture2, leftTextureCoordinate2).rgb;\n" + 
			"     vec3 rightColor2 = texture2D(inputImageTexture2, rightTextureCoordinate2).rgb;\n" + 
			"     vec3 topColor2 = texture2D(inputImageTexture2, topTextureCoordinate2).rgb;\n" + 
			"\n" + 
			"     vec3 meanColor = (bottomColor + leftColor + rightColor + topColor) / 4.0;\n" + 
			"     vec3 diffColor = centerColor.rgb - meanColor;\n" + 
			"\n" + 
			"     vec3 meanColor2 = (bottomColor2 + leftColor2 + rightColor2 + topColor2) / 4.0;\n" + 
			"     vec3 diffColor2 = centerColor2.rgb - meanColor2;\n" + 
			"     \n" + 
			"     vec3 gradColor = (meanColor + diffColor2);\n" + 
			"     \n" + 
			"	 gl_FragColor = vec4(mix(centerColor.rgb, gradColor, centerColor2.a * mixturePercent), centerColor.a);\n" + 
			" }"; //TODO implement methods and uniforms
	
}
