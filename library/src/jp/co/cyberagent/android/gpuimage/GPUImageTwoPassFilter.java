package jp.co.cyberagent.android.gpuimage;

import java.util.ArrayList;

public class GPUImageTwoPassFilter extends GPUImageFilterGroup 
{
  public GPUImageTwoPassFilter(String firstVertexShader,String firstFragmentShader,String secondVertexShader,String secondFragmentShader) 
	{
		super(new ArrayList<GPUImageFilter>());
		
		mFilters.add(new GPUImageFilter(firstVertexShader,firstFragmentShader));
		mFilters.add(new GPUImageFilter(secondVertexShader,secondFragmentShader));
	}
}
