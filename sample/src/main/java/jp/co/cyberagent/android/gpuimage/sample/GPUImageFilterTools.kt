/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.opengl.Matrix
import jp.co.cyberagent.android.gpuimage.filter.*
import java.util.*

object GPUImageFilterTools {
    fun showDialog(
        context: Context,
        listener: (filter: GPUImageFilter) -> Unit
    ) {
        val filters = FilterList().apply {
            addFilter("Contrast", FilterType.CONTRAST)
            addFilter("Invert", FilterType.INVERT)
            addFilter("Pixelation", FilterType.PIXELATION)
            addFilter("Hue", FilterType.HUE)
            addFilter("Gamma", FilterType.GAMMA)
            addFilter("Brightness", FilterType.BRIGHTNESS)
            addFilter("Sepia", FilterType.SEPIA)
            addFilter("Grayscale", FilterType.GRAYSCALE)
            addFilter("Sharpness", FilterType.SHARPEN)
            addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION)
            addFilter("Threshold Edge Detection", FilterType.THRESHOLD_EDGE_DETECTION)
            addFilter("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION)
            addFilter("Emboss", FilterType.EMBOSS)
            addFilter("Posterize", FilterType.POSTERIZE)
            addFilter("Grouped filters", FilterType.FILTER_GROUP)
            addFilter("Saturation", FilterType.SATURATION)
            addFilter("Exposure", FilterType.EXPOSURE)
            addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW)
            addFilter("Monochrome", FilterType.MONOCHROME)
            addFilter("Opacity", FilterType.OPACITY)
            addFilter("RGB", FilterType.RGB)
            addFilter("White Balance", FilterType.WHITE_BALANCE)
            addFilter("Vignette", FilterType.VIGNETTE)
            addFilter("ToneCurve", FilterType.TONE_CURVE)

            addFilter("Luminance", FilterType.LUMINANCE)
            addFilter("Luminance Threshold", FilterType.LUMINANCE_THRESHSOLD)

            addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE)
            addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER)
            addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN)
            addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE)
            addFilter("Blend (Darken)", FilterType.BLEND_DARKEN)
            addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE)
            addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION)
            addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT)
            addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN)
            addFilter("Blend (Add)", FilterType.BLEND_ADD)
            addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE)
            addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY)
            addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY)
            addFilter("Blend (Screen)", FilterType.BLEND_SCREEN)
            addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA)
            addFilter("Blend (Color)", FilterType.BLEND_COLOR)
            addFilter("Blend (Hue)", FilterType.BLEND_HUE)
            addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION)
            addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY)
            addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN)
            addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT)
            addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT)
            addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY)
            addFilter("Blend (Normal)", FilterType.BLEND_NORMAL)

            addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA)
            addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR)
            addFilter("Crosshatch", FilterType.CROSSHATCH)

            addFilter("Box Blur", FilterType.BOX_BLUR)
            addFilter("CGA Color Space", FilterType.CGA_COLORSPACE)
            addFilter("Dilation", FilterType.DILATION)
            addFilter("Kuwahara", FilterType.KUWAHARA)
            addFilter("RGB Dilation", FilterType.RGB_DILATION)
            addFilter("Sketch", FilterType.SKETCH)
            addFilter("Toon", FilterType.TOON)
            addFilter("Smooth Toon", FilterType.SMOOTH_TOON)
            addFilter("Halftone", FilterType.HALFTONE)

            addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION)
            addFilter("Glass Sphere", FilterType.GLASS_SPHERE)
            addFilter("Haze", FilterType.HAZE)
            addFilter("Laplacian", FilterType.LAPLACIAN)
            addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION)
            addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION)
            addFilter("Swirl", FilterType.SWIRL)
            addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION)
            addFilter("False Color", FilterType.FALSE_COLOR)

            addFilter("Color Balance", FilterType.COLOR_BALANCE)

            addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN)

            addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR)

            addFilter("Zoom Blur", FilterType.ZOOM_BLUR)

            addFilter("Transform (2-D)", FilterType.TRANSFORM2D)

            addFilter("Solarize", FilterType.SOLARIZE)

            addFilter("Vibrance", FilterType.VIBRANCE)
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose a filter")
        builder.setItems(filters.names.toTypedArray()) { _, item ->
            listener(createFilterForType(context, filters.filters[item]))
        }
        builder.create().show()
    }

    private fun createFilterForType(context: Context, type: FilterType): GPUImageFilter {
        return when (type) {
            FilterType.CONTRAST -> GPUImageContrastFilter(2.0f)
            FilterType.GAMMA -> GPUImageGammaFilter(2.0f)
            FilterType.INVERT -> GPUImageColorInvertFilter()
            FilterType.PIXELATION -> GPUImagePixelationFilter()
            FilterType.HUE -> GPUImageHueFilter(90.0f)
            FilterType.BRIGHTNESS -> GPUImageBrightnessFilter(1.5f)
            FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            FilterType.SEPIA -> GPUImageSepiaToneFilter()
            FilterType.SHARPEN -> GPUImageSharpenFilter()
            FilterType.SOBEL_EDGE_DETECTION -> GPUImageSobelEdgeDetectionFilter()
            FilterType.THRESHOLD_EDGE_DETECTION -> GPUImageThresholdEdgeDetectionFilter()
            FilterType.THREE_X_THREE_CONVOLUTION -> GPUImage3x3ConvolutionFilter()
            FilterType.EMBOSS -> GPUImageEmbossFilter()
            FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            FilterType.FILTER_GROUP -> GPUImageFilterGroup(
                listOf(
                    GPUImageContrastFilter(),
                    GPUImageDirectionalSobelEdgeDetectionFilter(),
                    GPUImageGrayscaleFilter()
                )
            )
            FilterType.SATURATION -> GPUImageSaturationFilter(1.0f)
            FilterType.EXPOSURE -> GPUImageExposureFilter(0.0f)
            FilterType.HIGHLIGHT_SHADOW -> GPUImageHighlightShadowFilter(
                0.0f,
                1.0f
            )
            FilterType.MONOCHROME -> GPUImageMonochromeFilter(
                1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            FilterType.OPACITY -> GPUImageOpacityFilter(1.0f)
            FilterType.RGB -> GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            FilterType.WHITE_BALANCE -> GPUImageWhiteBalanceFilter(
                5000.0f,
                0.0f
            )
            FilterType.VIGNETTE -> GPUImageVignetteFilter(
                PointF(0.5f, 0.5f),
                floatArrayOf(0.0f, 0.0f, 0.0f),
                0.3f,
                0.75f
            )
            FilterType.TONE_CURVE -> GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.resources.openRawResource(R.raw.tone_cuver_sample))
            }
            FilterType.LUMINANCE -> GPUImageLuminanceFilter()
            FilterType.LUMINANCE_THRESHSOLD -> GPUImageLuminanceThresholdFilter(0.5f)
            FilterType.BLEND_DIFFERENCE -> createBlendFilter(
                context,
                GPUImageDifferenceBlendFilter::class.java
            )
            FilterType.BLEND_SOURCE_OVER -> createBlendFilter(
                context,
                GPUImageSourceOverBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_BURN -> createBlendFilter(
                context,
                GPUImageColorBurnBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_DODGE -> createBlendFilter(
                context,
                GPUImageColorDodgeBlendFilter::class.java
            )
            FilterType.BLEND_DARKEN -> createBlendFilter(
                context,
                GPUImageDarkenBlendFilter::class.java
            )
            FilterType.BLEND_DISSOLVE -> createBlendFilter(
                context,
                GPUImageDissolveBlendFilter::class.java
            )
            FilterType.BLEND_EXCLUSION -> createBlendFilter(
                context,
                GPUImageExclusionBlendFilter::class.java
            )

            FilterType.BLEND_HARD_LIGHT -> createBlendFilter(
                context,
                GPUImageHardLightBlendFilter::class.java
            )
            FilterType.BLEND_LIGHTEN -> createBlendFilter(
                context,
                GPUImageLightenBlendFilter::class.java
            )
            FilterType.BLEND_ADD -> createBlendFilter(
                context,
                GPUImageAddBlendFilter::class.java
            )
            FilterType.BLEND_DIVIDE -> createBlendFilter(
                context,
                GPUImageDivideBlendFilter::class.java
            )
            FilterType.BLEND_MULTIPLY -> createBlendFilter(
                context,
                GPUImageMultiplyBlendFilter::class.java
            )
            FilterType.BLEND_OVERLAY -> createBlendFilter(
                context,
                GPUImageOverlayBlendFilter::class.java
            )
            FilterType.BLEND_SCREEN -> createBlendFilter(
                context,
                GPUImageScreenBlendFilter::class.java
            )
            FilterType.BLEND_ALPHA -> createBlendFilter(
                context,
                GPUImageAlphaBlendFilter::class.java
            )
            FilterType.BLEND_COLOR -> createBlendFilter(
                context,
                GPUImageColorBlendFilter::class.java
            )
            FilterType.BLEND_HUE -> createBlendFilter(
                context,
                GPUImageHueBlendFilter::class.java
            )
            FilterType.BLEND_SATURATION -> createBlendFilter(
                context,
                GPUImageSaturationBlendFilter::class.java
            )
            FilterType.BLEND_LUMINOSITY -> createBlendFilter(
                context,
                GPUImageLuminosityBlendFilter::class.java
            )
            FilterType.BLEND_LINEAR_BURN -> createBlendFilter(
                context,
                GPUImageLinearBurnBlendFilter::class.java
            )
            FilterType.BLEND_SOFT_LIGHT -> createBlendFilter(
                context,
                GPUImageSoftLightBlendFilter::class.java
            )
            FilterType.BLEND_SUBTRACT -> createBlendFilter(
                context,
                GPUImageSubtractBlendFilter::class.java
            )
            FilterType.BLEND_CHROMA_KEY -> createBlendFilter(
                context,
                GPUImageChromaKeyBlendFilter::class.java
            )
            FilterType.BLEND_NORMAL -> createBlendFilter(
                context,
                GPUImageNormalBlendFilter::class.java
            )

            FilterType.LOOKUP_AMATORKA -> GPUImageLookupFilter().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lookup_amatorka)
            }
            FilterType.GAUSSIAN_BLUR -> GPUImageGaussianBlurFilter()
            FilterType.CROSSHATCH -> GPUImageCrosshatchFilter()
            FilterType.BOX_BLUR -> GPUImageBoxBlurFilter()
            FilterType.CGA_COLORSPACE -> GPUImageCGAColorspaceFilter()
            FilterType.DILATION -> GPUImageDilationFilter()
            FilterType.KUWAHARA -> GPUImageKuwaharaFilter()
            FilterType.RGB_DILATION -> GPUImageRGBDilationFilter()
            FilterType.SKETCH -> GPUImageSketchFilter()
            FilterType.TOON -> GPUImageToonFilter()
            FilterType.SMOOTH_TOON -> GPUImageSmoothToonFilter()
            FilterType.BULGE_DISTORTION -> GPUImageBulgeDistortionFilter()
            FilterType.GLASS_SPHERE -> GPUImageGlassSphereFilter()
            FilterType.HAZE -> GPUImageHazeFilter()
            FilterType.LAPLACIAN -> GPUImageLaplacianFilter()
            FilterType.NON_MAXIMUM_SUPPRESSION -> GPUImageNonMaximumSuppressionFilter()
            FilterType.SPHERE_REFRACTION -> GPUImageSphereRefractionFilter()
            FilterType.SWIRL -> GPUImageSwirlFilter()
            FilterType.WEAK_PIXEL_INCLUSION -> GPUImageWeakPixelInclusionFilter()
            FilterType.FALSE_COLOR -> GPUImageFalseColorFilter()
            FilterType.COLOR_BALANCE -> GPUImageColorBalanceFilter()
            FilterType.LEVELS_FILTER_MIN -> GPUImageLevelsFilter()
            FilterType.HALFTONE -> GPUImageHalftoneFilter()
            FilterType.BILATERAL_BLUR -> GPUImageBilateralBlurFilter()
            FilterType.ZOOM_BLUR -> GPUImageZoomBlurFilter()
            FilterType.TRANSFORM2D -> GPUImageTransformFilter()
            FilterType.SOLARIZE -> GPUImageSolarizeFilter()
            FilterType.VIBRANCE -> GPUImageVibranceFilter()
        }
    }

    private fun createBlendFilter(
        context: Context,
        filterClass: Class<out GPUImageTwoInputFilter>
    ): GPUImageFilter {
        return try {
            filterClass.newInstance().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GPUImageFilter()
        }
    }

    private enum class FilterType {
        CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN,
        BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR, HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }

    private class FilterList {
        val names: MutableList<String> = LinkedList()
        val filters: MutableList<FilterType> = LinkedList()

        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }

    class FilterAdjuster(filter: GPUImageFilter) {
        private val adjuster: Adjuster<out GPUImageFilter>?

        init {
            adjuster = when (filter) {
                is GPUImageSharpenFilter -> SharpnessAdjuster(filter)
                is GPUImageSepiaToneFilter -> SepiaAdjuster(filter)
                is GPUImageContrastFilter -> ContrastAdjuster(filter)
                is GPUImageGammaFilter -> GammaAdjuster(filter)
                is GPUImageBrightnessFilter -> BrightnessAdjuster(filter)
                is GPUImageSobelEdgeDetectionFilter -> SobelAdjuster(filter)
                is GPUImageThresholdEdgeDetectionFilter -> ThresholdAdjuster(filter)
                is GPUImage3x3ConvolutionFilter -> ThreeXThreeConvolutionAjuster(filter)
                is GPUImageEmbossFilter -> EmbossAdjuster(filter)
                is GPUImage3x3TextureSamplingFilter -> GPU3x3TextureAdjuster(filter)
                is GPUImageHueFilter -> HueAdjuster(filter)
                is GPUImagePosterizeFilter -> PosterizeAdjuster(filter)
                is GPUImagePixelationFilter -> PixelationAdjuster(filter)
                is GPUImageSaturationFilter -> SaturationAdjuster(filter)
                is GPUImageExposureFilter -> ExposureAdjuster(filter)
                is GPUImageHighlightShadowFilter -> HighlightShadowAdjuster(filter)
                is GPUImageMonochromeFilter -> MonochromeAdjuster(filter)
                is GPUImageOpacityFilter -> OpacityAdjuster(filter)
                is GPUImageRGBFilter -> RGBAdjuster(filter)
                is GPUImageWhiteBalanceFilter -> WhiteBalanceAdjuster(filter)
                is GPUImageVignetteFilter -> VignetteAdjuster(filter)
                is GPUImageLuminanceThresholdFilter -> LuminanceThresholdAdjuster(filter)
                is GPUImageDissolveBlendFilter -> DissolveBlendAdjuster(filter)
                is GPUImageGaussianBlurFilter -> GaussianBlurAdjuster(filter)
                is GPUImageCrosshatchFilter -> CrosshatchBlurAdjuster(filter)
                is GPUImageBulgeDistortionFilter -> BulgeDistortionAdjuster(filter)
                is GPUImageGlassSphereFilter -> GlassSphereAdjuster(filter)
                is GPUImageHazeFilter -> HazeAdjuster(filter)
                is GPUImageSphereRefractionFilter -> SphereRefractionAdjuster(filter)
                is GPUImageSwirlFilter -> SwirlAdjuster(filter)
                is GPUImageColorBalanceFilter -> ColorBalanceAdjuster(filter)
                is GPUImageLevelsFilter -> LevelsMinMidAdjuster(filter)
                is GPUImageBilateralBlurFilter -> BilateralAdjuster(filter)
                is GPUImageTransformFilter -> RotateAdjuster(filter)
                is GPUImageSolarizeFilter -> SolarizeAdjuster(filter)
                is GPUImageVibranceFilter -> VibranceAdjuster(filter)
                else -> null
            }
        }

        fun canAdjust(): Boolean {
            return adjuster != null
        }

        fun adjust(percentage: Int) {
            adjuster?.adjust(percentage)
        }

        private abstract inner class Adjuster<T : GPUImageFilter>(protected val filter: T) {

            abstract fun adjust(percentage: Int)

            protected fun range(percentage: Int, start: Float, end: Float): Float {
                return (end - start) * percentage / 100.0f + start
            }

            protected fun range(percentage: Int, start: Int, end: Int): Int {
                return (end - start) * percentage / 100 + start
            }
        }

        private inner class SharpnessAdjuster(filter: GPUImageSharpenFilter) :
            Adjuster<GPUImageSharpenFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setSharpness(range(percentage, -4.0f, 4.0f))
            }
        }

        private inner class PixelationAdjuster(filter: GPUImagePixelationFilter) :
            Adjuster<GPUImagePixelationFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setPixel(range(percentage, 1.0f, 100.0f))
            }
        }

        private inner class HueAdjuster(filter: GPUImageHueFilter) :
            Adjuster<GPUImageHueFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setHue(range(percentage, 0.0f, 360.0f))
            }
        }

        private inner class ContrastAdjuster(filter: GPUImageContrastFilter) :
            Adjuster<GPUImageContrastFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setContrast(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class GammaAdjuster(filter: GPUImageGammaFilter) :
            Adjuster<GPUImageGammaFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setGamma(range(percentage, 0.0f, 3.0f))
            }
        }

        private inner class BrightnessAdjuster(filter: GPUImageBrightnessFilter) :
            Adjuster<GPUImageBrightnessFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setBrightness(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class SepiaAdjuster(filter: GPUImageSepiaToneFilter) :
            Adjuster<GPUImageSepiaToneFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setIntensity(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class SobelAdjuster(filter: GPUImageSobelEdgeDetectionFilter) :
            Adjuster<GPUImageSobelEdgeDetectionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class ThresholdAdjuster(filter: GPUImageThresholdEdgeDetectionFilter) :
            Adjuster<GPUImageThresholdEdgeDetectionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
                filter.setThreshold(0.9f)
            }
        }

        private inner class ThreeXThreeConvolutionAjuster(filter: GPUImage3x3ConvolutionFilter) :
            Adjuster<GPUImage3x3ConvolutionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setConvolutionKernel(
                    floatArrayOf(-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f)
                )
            }
        }

        private inner class EmbossAdjuster(filter: GPUImageEmbossFilter) :
            Adjuster<GPUImageEmbossFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.intensity = range(percentage, 0.0f, 4.0f)
            }
        }

        private inner class PosterizeAdjuster(filter: GPUImagePosterizeFilter) :
            Adjuster<GPUImagePosterizeFilter>(filter) {
            override fun adjust(percentage: Int) {
                // In theorie to 256, but only first 50 are interesting
                filter.setColorLevels(range(percentage, 1, 50))
            }
        }

        private inner class GPU3x3TextureAdjuster(filter: GPUImage3x3TextureSamplingFilter) :
            Adjuster<GPUImage3x3TextureSamplingFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class SaturationAdjuster(filter: GPUImageSaturationFilter) :
            Adjuster<GPUImageSaturationFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setSaturation(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ExposureAdjuster(filter: GPUImageExposureFilter) :
            Adjuster<GPUImageExposureFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setExposure(range(percentage, -10.0f, 10.0f))
            }
        }

        private inner class HighlightShadowAdjuster(filter: GPUImageHighlightShadowFilter) :
            Adjuster<GPUImageHighlightShadowFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setShadows(range(percentage, 0.0f, 1.0f))
                filter.setHighlights(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class MonochromeAdjuster(filter: GPUImageMonochromeFilter) :
            Adjuster<GPUImageMonochromeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setIntensity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class OpacityAdjuster(filter: GPUImageOpacityFilter) :
            Adjuster<GPUImageOpacityFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setOpacity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class RGBAdjuster(filter: GPUImageRGBFilter) :
            Adjuster<GPUImageRGBFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRed(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class WhiteBalanceAdjuster(filter: GPUImageWhiteBalanceFilter) :
            Adjuster<GPUImageWhiteBalanceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setTemperature(range(percentage, 2000.0f, 8000.0f))
            }
        }

        private inner class VignetteAdjuster(filter: GPUImageVignetteFilter) :
            Adjuster<GPUImageVignetteFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setVignetteStart(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class LuminanceThresholdAdjuster(filter: GPUImageLuminanceThresholdFilter) :
            Adjuster<GPUImageLuminanceThresholdFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setThreshold(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class DissolveBlendAdjuster(filter: GPUImageDissolveBlendFilter) :
            Adjuster<GPUImageDissolveBlendFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMix(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class GaussianBlurAdjuster(filter: GPUImageGaussianBlurFilter) :
            Adjuster<GPUImageGaussianBlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setBlurSize(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class CrosshatchBlurAdjuster(filter: GPUImageCrosshatchFilter) :
            Adjuster<GPUImageCrosshatchFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setCrossHatchSpacing(range(percentage, 0.0f, 0.06f))
                filter.setLineWidth(range(percentage, 0.0f, 0.006f))
            }
        }

        private inner class BulgeDistortionAdjuster(filter: GPUImageBulgeDistortionFilter) :
            Adjuster<GPUImageBulgeDistortionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
                filter.setScale(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class GlassSphereAdjuster(filter: GPUImageGlassSphereFilter) :
            Adjuster<GPUImageGlassSphereFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class HazeAdjuster(filter: GPUImageHazeFilter) :
            Adjuster<GPUImageHazeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setDistance(range(percentage, -0.3f, 0.3f))
                filter.setSlope(range(percentage, -0.3f, 0.3f))
            }
        }

        private inner class SphereRefractionAdjuster(filter: GPUImageSphereRefractionFilter) :
            Adjuster<GPUImageSphereRefractionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class SwirlAdjuster(filter: GPUImageSwirlFilter) :
            Adjuster<GPUImageSwirlFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setAngle(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ColorBalanceAdjuster(filter: GPUImageColorBalanceFilter) :
            Adjuster<GPUImageColorBalanceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMidtones(
                    floatArrayOf(
                        range(percentage, 0.0f, 1.0f),
                        range(percentage / 2, 0.0f, 1.0f),
                        range(percentage / 3, 0.0f, 1.0f)
                    )
                )
            }
        }

        private inner class LevelsMinMidAdjuster(filter: GPUImageLevelsFilter) :
            Adjuster<GPUImageLevelsFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f)
            }
        }

        private inner class BilateralAdjuster(filter: GPUImageBilateralBlurFilter) :
            Adjuster<GPUImageBilateralBlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f))
            }
        }

        private inner class RotateAdjuster(filter: GPUImageTransformFilter) :
            Adjuster<GPUImageTransformFilter>(filter) {
            override fun adjust(percentage: Int) {
                val transform = FloatArray(16)
                Matrix.setRotateM(transform, 0, (360 * percentage / 100).toFloat(), 0f, 0f, 1.0f)
                filter.transform3D = transform
            }
        }

        private inner class SolarizeAdjuster(filter: GPUImageSolarizeFilter) :
            Adjuster<GPUImageSolarizeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setThreshold(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class VibranceAdjuster(filter: GPUImageVibranceFilter) :
            Adjuster<GPUImageVibranceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setVibrance(range(percentage, -1.2f, 1.2f))
            }
        }
    }
}
