# GPUImage for Android

Idea from: [iOS GPUImage framework](https://github.com/BradLarson/GPUImage)

Goal is to have something as similar to GPUImage as possible. Vertex and fragment shaders are exactly the same. That way it makes it easier to port filters from GPUImage iOS to Android.

## Requirements
* Android 2.2 or higher (OpenGL ES 2.0)

## Usage

### Include in own project
GPUImage can be used as a library project or by copying the following files/folders to your libs folder.

* library/libs/armeabi (only needed for camera live preview)
* library/bin/gpuimage.jar

### Maven dependency

    <dependency>
      <groupId>jp.co.cyberagent.android.gpuimage</groupId>
      <artifactId>gpuimage-library</artifactId>
      <type>apklib</type>
      <version>(use current version here)</version>
    </dependency>

If you want to use it with live camera preview, than you will need to add the following as well:

    <dependency>
      <groupId>jp.co.cyberagent.android.gpuimage</groupId>
      <artifactId>gpuimage-library</artifactId>
      <classifier>armeabi</classifier>
      <type>so</type>
      <version>(use current version here)</version>
    </dependency>


### Sample Code
With preview:

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        Uri imageUri = ...;
        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
        mGPUImage.setImage(imageUri); // this loads image on the current thread, should be run in a thread
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

    cd library
    ndk-build

### Maven
Make sure that you run the clean target when using maven.

    mvn clean install

## License
    Copyright 2012 CyberAgent

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.