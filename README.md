# GPUImage for Android
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/cats-oss/maven/gpuimage/images/download.svg) ](https://bintray.com/cats-oss/maven/gpuimage/_latestVersion)
[![Build Status](https://app.bitrise.io/app/d8d8090a71066e7c/status.svg?token=sJNbvX8CkecWcUA5Z898lQ&branch=master)](https://app.bitrise.io/app/d8d8090a71066e7c)

Idea from: [iOS GPUImage framework](https://github.com/BradLarson/GPUImage)

Goal is to have something as similar to GPUImage as possible. Vertex and fragment shaders are exactly the same. That way it makes it easier to port filters from GPUImage iOS to Android.

## Requirements
* Android 2.2 or higher (OpenGL ES 2.0)

## Milestone
For Core Library
- [x] GLTextureView support

For Sample
- [x] Migrate to AndroidX
- [ ] Use Camera2 for Oreo or Higher

## Usage

### Gradle dependency

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'jp.co.cyberagent.android:gpuimage:2.0.0'
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
    gpuImage = new GPUImage(this);
    gpuImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
    gpuImage.setImage(imageUri); // this loads image on the current thread, should be run in a thread
    gpuImage.setFilter(new GPUImageSepiaFilter());

    // Later when image should be saved saved:
    gpuImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
}
```

Using GPUImageView
```xml
<jp.co.cyberagent.android.gpuimage.GPUImageView
    android:id="@+id/gpuimageview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:surface_type="texture_view" /> <!-- surface_view or texture_view -->
```

```java
@Override
public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity);

    Uri imageUri = ...;
    gpuImageView = findViewById(R.id.gpuimageview);
    gpuImageView.setImage(imageUri); // this loads image on the current thread, should be run in a thread
    gpuImageView.setFilter(new GPUImageSepiaFilter());

    // Later when image should be saved saved:
    gpuImageView.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
}
```

Without preview:

```java
public void onCreate(final Bundle savedInstanceState) {
    public void onCreate(final Bundle savedInstanceState) {
    Uri imageUri = ...;
    gpuImage = new GPUImage(context);
    gpuImage.setFilter(new GPUImageSobelEdgeDetection());
    gpuImage.setImage(imageUri);
    gpuImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
}
```

### Gradle
Make sure that you run the clean target when using maven.

```groovy
gradle clean assemble
```

## License
    Copyright 2018 CyberAgent, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
