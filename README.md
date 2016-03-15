# GPUImage for Android
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/cyberagent/maven/gpuimage-library/images/download.svg) ](https://bintray.com/cyberagent/maven/gpuimage-library/_latestVersion)

Idea from: [iOS GPUImage framework](https://github.com/BradLarson/GPUImage)

Goal is to have something as similar to GPUImage as possible. Vertex and fragment shaders are exactly the same. That way it makes it easier to port filters from GPUImage iOS to Android.

## Requirements
* Android 2.2 or higher (OpenGL ES 2.0)

## Usage

### Gradle dependency

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.4.1'
}
```

### Sample Code
With preview:

```java
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
```

Without preview:

```java
Uri imageUri = ...;
mGPUImage = new GPUImage(context);
mGPUImage.setFilter(new GPUImageSobelEdgeDetection());
mGPUImage.setImage(imageUri);
mGPUImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
```

### Gradle
Make sure that you run the clean target when using maven.

```groovy
gradle clean assemble
```

## License
    Copyright 2012 CyberAgent, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
