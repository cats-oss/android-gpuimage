# GPUImage for Android

Idea from: [iOS GPUImage framework](https://github.com/BradLarson/GPUImage)

Goal is to have something as similar to GPUImage as possible. Vertex and fragment shaders are exactly the same. That way it makes it easier to port filters from GPUImage iOS to Android.

## Usage

### Library project
Add gpuimage as a library project.

### Copy paste only
Copy the the following files/folders to your projects libs folder.

* libs/armeabi
* bin/gpuimage.jar

### Code
With preview:

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        
        Uri imageUri = ...;
        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
        mGPUImage.setImage(imageUri);
        mGPUImage.setFilter(new GPUImageSepiaFilter());
        
        // Later when image should be saved saved:
        mGPUImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
    }

Without preview:

    Uri imageUri = ...;
    mGPUImage = new GPUImage(context);
    mGPUImage.setFilter(new GPUImageSobelEdgeDetection());
    mGPUImage.setImage(imageUri);
    mGPUImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);

## Create libs/armeabi
Run the following command in the library folder. Make sure you have android-ndk in your PATH.

    ndk-build