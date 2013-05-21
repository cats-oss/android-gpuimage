package jp.co.cyberagent.android.gpuimage;

public class GPUImageKuwaharaRadius3Filter extends GPUImageFilter {
  
	public static final String KUWAHARARADIUS3_FRAGMENT_SHADER = "" +
	
		"varying highp vec2 textureCoordinate;\n"+
		"uniform sampler2D inputImageTexture;\n"+
		"\n"+
		"precision highp float;\n"+
		"\n"+
		"const vec2 src_size = vec2 (1.0 / 768.0, 1.0 / 1024.0);\n"+
		"\n"+
		"void main (void)\n"+
		"{\n"+
		"vec2 uv = textureCoordinate;\n"+
		"float n = float(16); // radius is assumed to be 3\n"+
		"vec3 m0 = vec3(0.0); vec3 m1 = vec3(0.0); vec3 m2 = vec3(0.0); vec3 m3 = vec3(0.0);\n"+
		"vec3 s0 = vec3(0.0); vec3 s1 = vec3(0.0); vec3 s2 = vec3(0.0); vec3 s3 = vec3(0.0);\n"+
		"vec3 c;\n"+
		"vec3 cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,-3) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,-2) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,-1) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,-3) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,-2) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,-1) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,-3) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,-2) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,-1) * src_size).rgb;\n"+
		"m0 += c;\n"+
		"s0 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,-3) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,-2) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,-1) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m0 += c;\n"+
		"s0 += cSq;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,3) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,2) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-3,1) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,3) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,2) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-2,1) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,3) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,2) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(-1,1) * src_size).rgb;\n"+
		"m1 += c;\n"+
		"s1 += c * c;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,3) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,2) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(0,1) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m1 += c;\n"+
		"s1 += cSq;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,3) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,2) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,1) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,3) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,2) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,1) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,3) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,2) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,1) * src_size).rgb;\n"+
		"m2 += c;\n"+
		"s2 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,0) * src_size).rgb;\n"+
		"cSq = c * c;\n"+
		"m2 += c;\n"+
		"s2 += cSq;\n"+
		"m3 += c;\n"+
		"s3 += cSq;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,-3) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,-2) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(3,-1) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,-3) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,-2) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(2,-1) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,-3) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,-2) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"c = texture2D(inputImageTexture, uv + vec2(1,-1) * src_size).rgb;\n"+
		"m3 += c;\n"+
		"s3 += c * c;\n"+
		"\n"+
		"float min_sigma2 = 1e+2;\n"+
		"m0 /= n;\n"+
		"s0 = abs(s0 / n - m0 * m0);\n"+
		"\n"+
		"float sigma2 = s0.r + s0.g + s0.b;\n"+
		"if (sigma2 < min_sigma2) {\n"+
		"min_sigma2 = sigma2;\n"+
		"gl_FragColor = vec4(m0, 1.0);\n"+
		"}\n"+
		"\n"+
		"m1 /= n;\n"+
		"s1 = abs(s1 / n - m1 * m1);\n"+
		"\n"+
		"sigma2 = s1.r + s1.g + s1.b;\n"+
		"if (sigma2 < min_sigma2) {\n"+
		"min_sigma2 = sigma2;\n"+
		"gl_FragColor = vec4(m1, 1.0);\n"+
		"}\n"+
		"\n"+
		"m2 /= n;\n"+
		"s2 = abs(s2 / n - m2 * m2);\n"+
		"\n"+
		"sigma2 = s2.r + s2.g + s2.b;\n"+
		"if (sigma2 < min_sigma2) {\n"+
		"min_sigma2 = sigma2;\n"+
		"gl_FragColor = vec4(m2, 1.0);\n"+
		"}\n"+
		"\n"+
		"m3 /= n;\n"+
		"s3 = abs(s3 / n - m3 * m3);\n"+
		"\n"+
		"sigma2 = s3.r + s3.g + s3.b;\n"+
		"if (sigma2 < min_sigma2) {\n"+
		"min_sigma2 = sigma2;\n"+
		"gl_FragColor = vec4(m3, 1.0);\n"+
		"}\n"+
		"}\n";
	
	public GPUImageKuwaharaRadius3Filter()
	{
		super(NO_FILTER_VERTEX_SHADER, KUWAHARARADIUS3_FRAGMENT_SHADER);
	}
}
