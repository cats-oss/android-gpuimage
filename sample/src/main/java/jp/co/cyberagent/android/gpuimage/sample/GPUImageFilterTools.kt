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
        val filters = FilterList()
        filters.addFilter("Contrast", FilterType.CONTRAST)
        filters.addFilter("Invert", FilterType.INVERT)
        filters.addFilter("Pixelation", FilterType.PIXELATION)
        filters.addFilter("Hue", FilterType.HUE)
        filters.addFilter("Gamma", FilterType.GAMMA)
        filters.addFilter("Brightness", FilterType.BRIGHTNESS)
        filters.addFilter("Sepia", FilterType.SEPIA)
        filters.addFilter("Grayscale", FilterType.GRAYSCALE)
        filters.addFilter("Sharpness", FilterType.SHARPEN)
        filters.addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION)
        filters.addFilter("Threshold Edge Detection", FilterType.THRESHOLD_EDGE_DETECTION)
        filters.addFilter("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION)
        filters.addFilter("Emboss", FilterType.EMBOSS)
        filters.addFilter("Posterize", FilterType.POSTERIZE)
        filters.addFilter("Grouped filters", FilterType.FILTER_GROUP)
        filters.addFilter("Saturation", FilterType.SATURATION)
        filters.addFilter("Exposure", FilterType.EXPOSURE)
        filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW)
        filters.addFilter("Monochrome", FilterType.MONOCHROME)
        filters.addFilter("Opacity", FilterType.OPACITY)
        filters.addFilter("RGB", FilterType.RGB)
        filters.addFilter("White Balance", FilterType.WHITE_BALANCE)
        filters.addFilter("Vignette", FilterType.VIGNETTE)
        filters.addFilter("ToneCurve", FilterType.TONE_CURVE)

        filters.addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE)
        filters.addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER)
        filters.addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN)
        filters.addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE)
        filters.addFilter("Blend (Darken)", FilterType.BLEND_DARKEN)
        filters.addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE)
        filters.addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION)
        filters.addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT)
        filters.addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN)
        filters.addFilter("Blend (Add)", FilterType.BLEND_ADD)
        filters.addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE)
        filters.addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY)
        filters.addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY)
        filters.addFilter("Blend (Screen)", FilterType.BLEND_SCREEN)
        filters.addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA)
        filters.addFilter("Blend (Color)", FilterType.BLEND_COLOR)
        filters.addFilter("Blend (Hue)", FilterType.BLEND_HUE)
        filters.addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION)
        filters.addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY)
        filters.addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN)
        filters.addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT)
        filters.addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT)
        filters.addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY)
        filters.addFilter("Blend (Normal)", FilterType.BLEND_NORMAL)

        filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA)
        filters.addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR)
        filters.addFilter("Crosshatch", FilterType.CROSSHATCH)

        filters.addFilter("Box Blur", FilterType.BOX_BLUR)
        filters.addFilter("CGA Color Space", FilterType.CGA_COLORSPACE)
        filters.addFilter("Dilation", FilterType.DILATION)
        filters.addFilter("Kuwahara", FilterType.KUWAHARA)
        filters.addFilter("RGB Dilation", FilterType.RGB_DILATION)
        filters.addFilter("Sketch", FilterType.SKETCH)
        filters.addFilter("Toon", FilterType.TOON)
        filters.addFilter("Smooth Toon", FilterType.SMOOTH_TOON)
        filters.addFilter("Halftone", FilterType.HALFTONE)

        filters.addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION)
        filters.addFilter("Glass Sphere", FilterType.GLASS_SPHERE)
        filters.addFilter("Haze", FilterType.HAZE)
        filters.addFilter("Laplacian", FilterType.LAPLACIAN)
        filters.addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION)
        filters.addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION)
        filters.addFilter("Swirl", FilterType.SWIRL)
        filters.addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION)
        filters.addFilter("False Color", FilterType.FALSE_COLOR)

        filters.addFilter("Color Balance", FilterType.COLOR_BALANCE)

        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN)

        filters.addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR)

        filters.addFilter("Transform (2-D)", FilterType.TRANSFORM2D)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose a filter")
        builder.setItems(filters.names.toTypedArray()) { _, item ->
            listener(createFilterForType(context, filters.filters[item]))
        }
        builder.create().show()
    }

    private fun createFilterForType(context: Context, type: FilterType): GPUImageFilter {
        return when (type) {
            GPUImageFilterTools.FilterType.CONTRAST -> GPUImageContrastFilter(2.0f)
            GPUImageFilterTools.FilterType.GAMMA -> GPUImageGammaFilter(2.0f)
            GPUImageFilterTools.FilterType.INVERT -> GPUImageColorInvertFilter()
            GPUImageFilterTools.FilterType.PIXELATION -> GPUImagePixelationFilter()
            GPUImageFilterTools.FilterType.HUE -> GPUImageHueFilter(90.0f)
            GPUImageFilterTools.FilterType.BRIGHTNESS -> GPUImageBrightnessFilter(1.5f)
            GPUImageFilterTools.FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            GPUImageFilterTools.FilterType.SEPIA -> GPUImageSepiaFilter()
            GPUImageFilterTools.FilterType.SHARPEN -> GPUImageSharpenFilter().apply {
                setSharpness(2.0f)
            }
            GPUImageFilterTools.FilterType.SOBEL_EDGE_DETECTION -> GPUImageSobelEdgeDetectionFilter()
            GPUImageFilterTools.FilterType.THRESHOLD_EDGE_DETECTION -> GPUImageThresholdEdgeDetectionFilter()
            GPUImageFilterTools.FilterType.THREE_X_THREE_CONVOLUTION -> GPUImage3x3ConvolutionFilter().apply {
                setConvolutionKernel(
                    floatArrayOf(-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f)
                )
            }
            GPUImageFilterTools.FilterType.EMBOSS -> GPUImageEmbossFilter()
            GPUImageFilterTools.FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            GPUImageFilterTools.FilterType.FILTER_GROUP -> GPUImageFilterGroup(
                listOf(
                    GPUImageContrastFilter(),
                    GPUImageDirectionalSobelEdgeDetectionFilter(),
                    GPUImageGrayscaleFilter()
                )
            )
            GPUImageFilterTools.FilterType.SATURATION -> GPUImageSaturationFilter(1.0f)
            GPUImageFilterTools.FilterType.EXPOSURE -> GPUImageExposureFilter(0.0f)
            GPUImageFilterTools.FilterType.HIGHLIGHT_SHADOW -> GPUImageHighlightShadowFilter(
                0.0f,
                1.0f
            )
            GPUImageFilterTools.FilterType.MONOCHROME -> GPUImageMonochromeFilter(
                1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            GPUImageFilterTools.FilterType.OPACITY -> GPUImageOpacityFilter(1.0f)
            GPUImageFilterTools.FilterType.RGB -> GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            GPUImageFilterTools.FilterType.WHITE_BALANCE -> GPUImageWhiteBalanceFilter(
                5000.0f,
                0.0f
            )
            GPUImageFilterTools.FilterType.VIGNETTE -> GPUImageVignetteFilter(
                PointF(0.5f, 0.5f),
                floatArrayOf(0.0f, 0.0f, 0.0f),
                0.3f,
                0.75f
            )
            GPUImageFilterTools.FilterType.TONE_CURVE -> GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.resources.openRawResource(R.raw.tone_cuver_sample))
            }
            GPUImageFilterTools.FilterType.BLEND_DIFFERENCE -> createBlendFilter(
                context,
                GPUImageDifferenceBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SOURCE_OVER -> createBlendFilter(
                context,
                GPUImageSourceOverBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR_BURN -> createBlendFilter(
                context,
                GPUImageColorBurnBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR_DODGE -> createBlendFilter(
                context,
                GPUImageColorDodgeBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DARKEN -> createBlendFilter(
                context,
                GPUImageDarkenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DISSOLVE -> createBlendFilter(
                context,
                GPUImageDissolveBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_EXCLUSION -> createBlendFilter(
                context,
                GPUImageExclusionBlendFilter::class.java
            )


            GPUImageFilterTools.FilterType.BLEND_HARD_LIGHT -> createBlendFilter(
                context,
                GPUImageHardLightBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LIGHTEN -> createBlendFilter(
                context,
                GPUImageLightenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_ADD -> createBlendFilter(
                context,
                GPUImageAddBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DIVIDE -> createBlendFilter(
                context,
                GPUImageDivideBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_MULTIPLY -> createBlendFilter(
                context,
                GPUImageMultiplyBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_OVERLAY -> createBlendFilter(
                context,
                GPUImageOverlayBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SCREEN -> createBlendFilter(
                context,
                GPUImageScreenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_ALPHA -> createBlendFilter(
                context,
                GPUImageAlphaBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR -> createBlendFilter(
                context,
                GPUImageColorBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_HUE -> createBlendFilter(
                context,
                GPUImageHueBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SATURATION -> createBlendFilter(
                context,
                GPUImageSaturationBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LUMINOSITY -> createBlendFilter(
                context,
                GPUImageLuminosityBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN -> createBlendFilter(
                context,
                GPUImageLinearBurnBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SOFT_LIGHT -> createBlendFilter(
                context,
                GPUImageSoftLightBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SUBTRACT -> createBlendFilter(
                context,
                GPUImageSubtractBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_CHROMA_KEY -> createBlendFilter(
                context,
                GPUImageChromaKeyBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_NORMAL -> createBlendFilter(
                context,
                GPUImageNormalBlendFilter::class.java
            )

            GPUImageFilterTools.FilterType.LOOKUP_AMATORKA -> GPUImageLookupFilter().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lookup_amatorka)
            }
            GPUImageFilterTools.FilterType.GAUSSIAN_BLUR -> GPUImageGaussianBlurFilter()
            GPUImageFilterTools.FilterType.CROSSHATCH -> GPUImageCrosshatchFilter()

            GPUImageFilterTools.FilterType.BOX_BLUR -> GPUImageBoxBlurFilter()
            GPUImageFilterTools.FilterType.CGA_COLORSPACE -> GPUImageCGAColorspaceFilter()
            GPUImageFilterTools.FilterType.DILATION -> GPUImageDilationFilter()
            GPUImageFilterTools.FilterType.KUWAHARA -> GPUImageKuwaharaFilter()
            GPUImageFilterTools.FilterType.RGB_DILATION -> GPUImageRGBDilationFilter()
            GPUImageFilterTools.FilterType.SKETCH -> GPUImageSketchFilter()
            GPUImageFilterTools.FilterType.TOON -> GPUImageToonFilter()
            GPUImageFilterTools.FilterType.SMOOTH_TOON -> GPUImageSmoothToonFilter()

            GPUImageFilterTools.FilterType.BULGE_DISTORTION -> GPUImageBulgeDistortionFilter()
            GPUImageFilterTools.FilterType.GLASS_SPHERE -> GPUImageGlassSphereFilter()
            GPUImageFilterTools.FilterType.HAZE -> GPUImageHazeFilter()
            GPUImageFilterTools.FilterType.LAPLACIAN -> GPUImageLaplacianFilter()
            GPUImageFilterTools.FilterType.NON_MAXIMUM_SUPPRESSION -> GPUImageNonMaximumSuppressionFilter()
            GPUImageFilterTools.FilterType.SPHERE_REFRACTION -> GPUImageSphereRefractionFilter()
            GPUImageFilterTools.FilterType.SWIRL -> GPUImageSwirlFilter()
            GPUImageFilterTools.FilterType.WEAK_PIXEL_INCLUSION -> GPUImageWeakPixelInclusionFilter()
            GPUImageFilterTools.FilterType.FALSE_COLOR -> GPUImageFalseColorFilter()
            GPUImageFilterTools.FilterType.COLOR_BALANCE -> GPUImageColorBalanceFilter()
            GPUImageFilterTools.FilterType.LEVELS_FILTER_MIN -> GPUImageLevelsFilter().apply {
                setMin(0.0f, 3.0f, 1.0f)
            }
            GPUImageFilterTools.FilterType.HALFTONE -> GPUImageHalftoneFilter()

            GPUImageFilterTools.FilterType.BILATERAL_BLUR -> GPUImageBilateralBlurFilter()

            GPUImageFilterTools.FilterType.TRANSFORM2D -> GPUImageTransformFilter()
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
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE,
        BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, HALFTONE, TRANSFORM2D
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
            when (filter) {
                is GPUImageSharpenFilter -> adjuster = SharpnessAdjuster(filter)
                is GPUImageSepiaFilter -> adjuster = SepiaAdjuster(filter)
                is GPUImageContrastFilter -> adjuster = ContrastAdjuster(filter)
                is GPUImageGammaFilter -> adjuster = GammaAdjuster(filter)
                is GPUImageBrightnessFilter -> adjuster = BrightnessAdjuster(filter)
                is GPUImageSobelEdgeDetectionFilter -> adjuster = SobelAdjuster(filter)
                is GPUImageThresholdEdgeDetectionFilter -> adjuster = ThresholdAdjuster(filter)
                is GPUImageEmbossFilter -> adjuster = EmbossAdjuster(filter)
                is GPUImage3x3TextureSamplingFilter -> adjuster = GPU3x3TextureAdjuster(filter)
                is GPUImageHueFilter -> adjuster = HueAdjuster(filter)
                is GPUImagePosterizeFilter -> adjuster = PosterizeAdjuster(filter)
                is GPUImagePixelationFilter -> adjuster = PixelationAdjuster(filter)
                is GPUImageSaturationFilter -> adjuster = SaturationAdjuster(filter)
                is GPUImageExposureFilter -> adjuster = ExposureAdjuster(filter)
                is GPUImageHighlightShadowFilter -> adjuster = HighlightShadowAdjuster(filter)
                is GPUImageMonochromeFilter -> adjuster = MonochromeAdjuster(filter)
                is GPUImageOpacityFilter -> adjuster = OpacityAdjuster(filter)
                is GPUImageRGBFilter -> adjuster = RGBAdjuster(filter)
                is GPUImageWhiteBalanceFilter -> adjuster = WhiteBalanceAdjuster(filter)
                is GPUImageVignetteFilter -> adjuster = VignetteAdjuster(filter)
                is GPUImageDissolveBlendFilter -> adjuster = DissolveBlendAdjuster(filter)
                is GPUImageGaussianBlurFilter -> adjuster = GaussianBlurAdjuster(filter)
                is GPUImageCrosshatchFilter -> adjuster = CrosshatchBlurAdjuster(filter)
                is GPUImageBulgeDistortionFilter -> adjuster = BulgeDistortionAdjuster(filter)
                is GPUImageGlassSphereFilter -> adjuster = GlassSphereAdjuster(filter)
                is GPUImageHazeFilter -> adjuster = HazeAdjuster(filter)
                is GPUImageSphereRefractionFilter -> adjuster = SphereRefractionAdjuster(filter)
                is GPUImageSwirlFilter -> adjuster = SwirlAdjuster(filter)
                is GPUImageColorBalanceFilter -> adjuster = ColorBalanceAdjuster(filter)
                is GPUImageLevelsFilter -> adjuster = LevelsMinMidAdjuster(filter)
                is GPUImageBilateralBlurFilter -> adjuster = BilateralAdjuster(filter)
                is GPUImageTransformFilter -> adjuster = RotateAdjuster(filter)
                else -> adjuster = null
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

        private inner class SepiaAdjuster(filter: GPUImageSepiaFilter) :
            Adjuster<GPUImageSepiaFilter>(filter) {
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

    }
}
