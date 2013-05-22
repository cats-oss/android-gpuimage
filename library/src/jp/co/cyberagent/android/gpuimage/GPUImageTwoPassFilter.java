package jp.co.cyberagent.android.gpuimage;

import java.util.ArrayList;

public class GPUImageTwoPassFilter extends GPUImageFilterGroup 
{
	public GPUImageTwoPassFilter(String firstVertexShader,String firstFragmentShader,String secondVertexShader,String secondFragmentShader) 
	{
		super(createFilters(firstVertexShader, firstFragmentShader, secondVertexShader, secondFragmentShader));
	}
	
	public static ArrayList<GPUImageFilter> createFilters(String firstVertexShader,String firstFragmentShader,String secondVertexShader,String secondFragmentShader)
	{
		ArrayList<GPUImageFilter> filters = new ArrayList<GPUImageFilter>();
		
		filters.add(new GPUImageFilter(firstVertexShader,firstFragmentShader));
		filters.add(new GPUImageFilter(secondVertexShader,secondFragmentShader));
		
		return filters;
	}
}
