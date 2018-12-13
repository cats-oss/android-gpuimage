# GPUImage for Android
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/cats-oss/maven/gpuimage/images/download.svg) ](https://bintray.com/cats-oss/maven/gpuimage/_latestVersion)
[![Build Status](https://app.bitrise.io/app/d8d8090a71066e7c/status.svg?token=sJNbvX8CkecWcUA5Z898lQ&branch=master)](https://app.bitrise.io/app/d8d8090a71066e7c)

Idea from: [iOS GPUImage framework](https://github.com/BradLarson/GPUImage2)

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
    implementation 'jp.co.cyberagent.android:gpuimage:2.x.x'
}
```

### Sample Code
#### With preview:

Java:
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

Kotlin:
```kotlin
public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_gallery)

    val imageUri: Uri = ...
    gpuImage = GPUImage(this)
    gpuImage.setGLSurfaceView(findViewById<GLSurfaceView>(R.id.surfaceView))
    gpuImage.setImage(imageUri) // this loads image on the current thread, should be run in a thread
    gpuImage.setFilter(GPUImageSepiaFilter())

    // Later when image should be saved saved:
    gpuImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null)
}
```

#### Using GPUImageView
```xml
<jp.co.cyberagent.android.gpuimage.GPUImageView
    android:id="@+id/gpuimageview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:gpuimage_show_loading="false"
    app:gpuimage_surface_type="texture_view" /> <!-- surface_view or texture_view -->
```

Java:
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

Kotlin:
```kotlin
public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_gallery)

    val imageUri: Uri = ...
    gpuImageView = findViewById<GPUImageView>(R.id.gpuimageview)
    gpuImageView.setImage(imageUri) // this loads image on the current thread, should be run in a thread
    gpuImageView.setFilter(GPUImageSepiaFilter())

    // Later when image should be saved saved:
    gpuImageView.saveToPictures("GPUImage", "ImageWithFilter.jpg", null)
}
```

#### Without preview:

Java:
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

Kotlin:
```kotlin
public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_gallery)
    val imageUri: Uri = ...
    gpuImage = GPUImage(this)
    gpuImage.setFilter(GPUImageSepiaFilter())
    gpuImage.setImage(imageUri)
    gpuImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null)
}
```

### Support status of [GPUImage for iOS](https://github.com/BradLarson/GPUImage2) shaders
- [x] Saturation
- [x] Contrast
- [x] Brightness
- [x] Levels
- [x] Exposure
- [x] RGB
- [x] RGB Diation
- [x] Hue
- [x] White Balance
- [x] Monochrome
- [x] False Color
- [x] Sharpen
- [ ] Unsharp Mask
- [x] Transform Operation
- [ ] Crop
- [x] Gamma
- [x] Highlights and Shadows
- [x] Haze
- [x] Sepia Tone
- [ ] Amatorka
- [ ] Miss Etikate
- [ ] Soft Elegance
- [x] Color Inversion
- [x] Solarize
- [x] Vibrance
- [ ] Highlight and Shadow Tint
- [x] Luminance
- [x] Luminance Threshold
- [ ] Average Color
- [ ] Average Luminance
- [ ] Average Luminance Threshold
- [ ] Adaptive Threshold
- [ ] Polar Pixellate
- [x] Pixellate
- [ ] Polka Dot
- [x] Halftone
- [x] Crosshatch
- [x] Sobel Edge Detection
- [ ] Prewitt Edge Detection
- [ ] Canny Edge Detection
- [x] Threshold Sobel EdgeDetection
- [ ] Harris Corner Detector
- [ ] Noble Corner Detector
- [ ] Shi Tomasi Feature Detector
- [ ] Colour FAST Feature Detector
- [ ] Low Pass Filter
- [ ] High Pass Filter
- [x] Sketch Filter
- [ ] Threshold Sketch Filter
- [x] Toon Filter
- [x] SmoothToon Filter
- [ ] Tilt Shift
- [x] CGA Colorspace Filter
- [x] Posterize
- [x] Convolution 3x3
- [x] Emboss Filter
- [x] Laplacian
- [x] Chroma Keying
- [x] Kuwahara Filter
- [ ] Kuwahara Radius3 Filter
- [x] Vignette
- [x] Gaussian Blur
- [x] Box Blur
- [x] Bilateral Blur
- [ ] Motion Blur
- [x] Zoom Blur
- [ ] iOS Blur
- [ ] Median Filter
- [x] Swirl Distortion
- [x] Bulge Distortion
- [ ] Pinch Distortion
- [x] Sphere Refraction
- [x] Glass Sphere Refraction
- [ ] Stretch Distortion
- [x] Dilation
- [ ] Erosion
- [ ] Opening Filter
- [ ] Closing Filter
- [ ] Local Binary Pattern
- [ ] Color Local Binary Pattern
- [x] Dissolve Blend
- [x] Chroma Key Blend
- [x] Add Blend
- [x] Divide Blend
- [x] Multiply Blend
- [x] Overlay Blend
- [x] Lighten Blend
- [x] Darken Blend
- [x] Color Burn Blend
- [x] Color Dodge Blend
- [x] Linear Burn Blend
- [x] Screen Blend
- [x] Difference Blend
- [x] Subtract Blend
- [x] Exclusion Blend
- [x] HardLight Blend
- [x] SoftLight Blend
- [x] Color Blend
- [x] Hue Blend
- [x] Saturation Blend
- [x] Luminosity Blend
- [x] Normal Blend
- [x] Source Over Blend
- [x] Alpha Blend
- [x] Non Maximum Suppression
- [ ] Thresholded Non Maximum Suppression
- [ ] Directional Non Maximum Suppression
- [x] Opacity
- [x] Weak Pixel Inclusion Filter
- [x] Color Matrix
- [x] Directional Sobel Edge Detection
- [x] Lookup
- [x] Tone Curve (*.acv files) 

## Others
- [x] Texture 3x3
- [x] Gray Scale

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
