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
            listener(createFilterForType(context, filters.filters[item])!!)
        }
        builder.create().show()
    }

    private fun createFilterForType(context: Context, type: FilterType): GPUImageFilter? {
        when (type) {
            GPUImageFilterTools.FilterType.CONTRAST -> return GPUImageContrastFilter(2.0f)
            GPUImageFilterTools.FilterType.GAMMA -> return GPUImageGammaFilter(2.0f)
            GPUImageFilterTools.FilterType.INVERT -> return GPUImageColorInvertFilter()
            GPUImageFilterTools.FilterType.PIXELATION -> return GPUImagePixelationFilter()
            GPUImageFilterTools.FilterType.HUE -> return GPUImageHueFilter(90.0f)
            GPUImageFilterTools.FilterType.BRIGHTNESS -> return GPUImageBrightnessFilter(1.5f)
            GPUImageFilterTools.FilterType.GRAYSCALE -> return GPUImageGrayscaleFilter()
            GPUImageFilterTools.FilterType.SEPIA -> return GPUImageSepiaFilter()
            GPUImageFilterTools.FilterType.SHARPEN -> {
                val sharpness = GPUImageSharpenFilter()
                sharpness.setSharpness(2.0f)
                return sharpness
            }
            GPUImageFilterTools.FilterType.SOBEL_EDGE_DETECTION -> return GPUImageSobelEdgeDetectionFilter()
            GPUImageFilterTools.FilterType.THRESHOLD_EDGE_DETECTION -> return GPUImageThresholdEdgeDetectionFilter()
            GPUImageFilterTools.FilterType.THREE_X_THREE_CONVOLUTION -> {
                val convolution = GPUImage3x3ConvolutionFilter()
                convolution.setConvolutionKernel(
                    floatArrayOf(
                        -1.0f,
                        0.0f,
                        1.0f,
                        -2.0f,
                        0.0f,
                        2.0f,
                        -1.0f,
                        0.0f,
                        1.0f
                    )
                )
                return convolution
            }
            GPUImageFilterTools.FilterType.EMBOSS -> return GPUImageEmbossFilter()
            GPUImageFilterTools.FilterType.POSTERIZE -> return GPUImagePosterizeFilter()
            GPUImageFilterTools.FilterType.FILTER_GROUP -> {
                val filters = LinkedList<GPUImageFilter>()
                filters.add(GPUImageContrastFilter())
                filters.add(GPUImageDirectionalSobelEdgeDetectionFilter())
                filters.add(GPUImageGrayscaleFilter())
                return GPUImageFilterGroup(filters)
            }
            GPUImageFilterTools.FilterType.SATURATION -> return GPUImageSaturationFilter(1.0f)
            GPUImageFilterTools.FilterType.EXPOSURE -> return GPUImageExposureFilter(0.0f)
            GPUImageFilterTools.FilterType.HIGHLIGHT_SHADOW -> return GPUImageHighlightShadowFilter(
                0.0f,
                1.0f
            )
            GPUImageFilterTools.FilterType.MONOCHROME -> return GPUImageMonochromeFilter(
                1.0f,
                floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            GPUImageFilterTools.FilterType.OPACITY -> return GPUImageOpacityFilter(1.0f)
            GPUImageFilterTools.FilterType.RGB -> return GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            GPUImageFilterTools.FilterType.WHITE_BALANCE -> return GPUImageWhiteBalanceFilter(
                5000.0f,
                0.0f
            )
            GPUImageFilterTools.FilterType.VIGNETTE -> {
                val centerPoint = PointF()
                centerPoint.x = 0.5f
                centerPoint.y = 0.5f
                return GPUImageVignetteFilter(
                    centerPoint,
                    floatArrayOf(0.0f, 0.0f, 0.0f),
                    0.3f,
                    0.75f
                )
            }
            GPUImageFilterTools.FilterType.TONE_CURVE -> {
                val toneCurveFilter = GPUImageToneCurveFilter()
                toneCurveFilter.setFromCurveFileInputStream(
                    context.resources.openRawResource(R.raw.tone_cuver_sample)
                )
                return toneCurveFilter
            }
            GPUImageFilterTools.FilterType.BLEND_DIFFERENCE -> return createBlendFilter(
                context,
                GPUImageDifferenceBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SOURCE_OVER -> return createBlendFilter(
                context,
                GPUImageSourceOverBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR_BURN -> return createBlendFilter(
                context,
                GPUImageColorBurnBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR_DODGE -> return createBlendFilter(
                context,
                GPUImageColorDodgeBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DARKEN -> return createBlendFilter(
                context,
                GPUImageDarkenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DISSOLVE -> return createBlendFilter(
                context,
                GPUImageDissolveBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_EXCLUSION -> return createBlendFilter(
                context,
                GPUImageExclusionBlendFilter::class.java
            )


            GPUImageFilterTools.FilterType.BLEND_HARD_LIGHT -> return createBlendFilter(
                context,
                GPUImageHardLightBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LIGHTEN -> return createBlendFilter(
                context,
                GPUImageLightenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_ADD -> return createBlendFilter(
                context,
                GPUImageAddBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_DIVIDE -> return createBlendFilter(
                context,
                GPUImageDivideBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_MULTIPLY -> return createBlendFilter(
                context,
                GPUImageMultiplyBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_OVERLAY -> return createBlendFilter(
                context,
                GPUImageOverlayBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SCREEN -> return createBlendFilter(
                context,
                GPUImageScreenBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_ALPHA -> return createBlendFilter(
                context,
                GPUImageAlphaBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_COLOR -> return createBlendFilter(
                context,
                GPUImageColorBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_HUE -> return createBlendFilter(
                context,
                GPUImageHueBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SATURATION -> return createBlendFilter(
                context,
                GPUImageSaturationBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LUMINOSITY -> return createBlendFilter(
                context,
                GPUImageLuminosityBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN -> return createBlendFilter(
                context,
                GPUImageLinearBurnBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SOFT_LIGHT -> return createBlendFilter(
                context,
                GPUImageSoftLightBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_SUBTRACT -> return createBlendFilter(
                context,
                GPUImageSubtractBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_CHROMA_KEY -> return createBlendFilter(
                context,
                GPUImageChromaKeyBlendFilter::class.java
            )
            GPUImageFilterTools.FilterType.BLEND_NORMAL -> return createBlendFilter(
                context,
                GPUImageNormalBlendFilter::class.java
            )

            GPUImageFilterTools.FilterType.LOOKUP_AMATORKA -> {
                val amatorka = GPUImageLookupFilter()
                amatorka.bitmap =
                        BitmapFactory.decodeResource(context.resources, R.drawable.lookup_amatorka)
                return amatorka
            }
            GPUImageFilterTools.FilterType.GAUSSIAN_BLUR -> return GPUImageGaussianBlurFilter()
            GPUImageFilterTools.FilterType.CROSSHATCH -> return GPUImageCrosshatchFilter()

            GPUImageFilterTools.FilterType.BOX_BLUR -> return GPUImageBoxBlurFilter()
            GPUImageFilterTools.FilterType.CGA_COLORSPACE -> return GPUImageCGAColorspaceFilter()
            GPUImageFilterTools.FilterType.DILATION -> return GPUImageDilationFilter()
            GPUImageFilterTools.FilterType.KUWAHARA -> return GPUImageKuwaharaFilter()
            GPUImageFilterTools.FilterType.RGB_DILATION -> return GPUImageRGBDilationFilter()
            GPUImageFilterTools.FilterType.SKETCH -> return GPUImageSketchFilter()
            GPUImageFilterTools.FilterType.TOON -> return GPUImageToonFilter()
            GPUImageFilterTools.FilterType.SMOOTH_TOON -> return GPUImageSmoothToonFilter()

            GPUImageFilterTools.FilterType.BULGE_DISTORTION -> return GPUImageBulgeDistortionFilter()
            GPUImageFilterTools.FilterType.GLASS_SPHERE -> return GPUImageGlassSphereFilter()
            GPUImageFilterTools.FilterType.HAZE -> return GPUImageHazeFilter()
            GPUImageFilterTools.FilterType.LAPLACIAN -> return GPUImageLaplacianFilter()
            GPUImageFilterTools.FilterType.NON_MAXIMUM_SUPPRESSION -> return GPUImageNonMaximumSuppressionFilter()
            GPUImageFilterTools.FilterType.SPHERE_REFRACTION -> return GPUImageSphereRefractionFilter()
            GPUImageFilterTools.FilterType.SWIRL -> return GPUImageSwirlFilter()
            GPUImageFilterTools.FilterType.WEAK_PIXEL_INCLUSION -> return GPUImageWeakPixelInclusionFilter()
            GPUImageFilterTools.FilterType.FALSE_COLOR -> return GPUImageFalseColorFilter()
            GPUImageFilterTools.FilterType.COLOR_BALANCE -> return GPUImageColorBalanceFilter()
            GPUImageFilterTools.FilterType.LEVELS_FILTER_MIN -> {
                val levelsFilter = GPUImageLevelsFilter()
                levelsFilter.setMin(0.0f, 3.0f, 1.0f)
                return levelsFilter
            }
            GPUImageFilterTools.FilterType.HALFTONE -> return GPUImageHalftoneFilter()

            GPUImageFilterTools.FilterType.BILATERAL_BLUR -> return GPUImageBilateralFilter()

            GPUImageFilterTools.FilterType.TRANSFORM2D -> return GPUImageTransformFilter()

            else -> throw IllegalStateException("No filter of that type!")
        }

    }

    private fun createBlendFilter(
        context: Context,
        filterClass: Class<out GPUImageTwoInputFilter>
    ): GPUImageFilter? {
        return try {
            val filter = filterClass.newInstance()
            filter.bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
            filter
        } catch (e: Exception) {
            e.printStackTrace()
            null
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
        var names: MutableList<String> = LinkedList()
        var filters: MutableList<FilterType> = LinkedList()

        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }

    class FilterAdjuster(filter: GPUImageFilter) {
        private val adjuster: Adjuster<out GPUImageFilter>?

        init {
            when (filter) {
                is GPUImageSharpenFilter -> adjuster = SharpnessAdjuster().filter(filter)
                is GPUImageSepiaFilter -> adjuster = SepiaAdjuster().filter(filter)
                is GPUImageContrastFilter -> adjuster = ContrastAdjuster().filter(filter)
                is GPUImageGammaFilter -> adjuster = GammaAdjuster().filter(filter)
                is GPUImageBrightnessFilter -> adjuster = BrightnessAdjuster().filter(filter)
                is GPUImageSobelEdgeDetectionFilter -> adjuster = SobelAdjuster().filter(filter)
                is GPUImageThresholdEdgeDetectionFilter -> adjuster =
                        ThresholdAdjuster().filter(filter)
                is GPUImageEmbossFilter -> adjuster = EmbossAdjuster().filter(filter)
                is GPUImage3x3TextureSamplingFilter -> adjuster =
                        GPU3x3TextureAdjuster().filter(filter)
                is GPUImageHueFilter -> adjuster = HueAdjuster().filter(filter)
                is GPUImagePosterizeFilter -> adjuster = PosterizeAdjuster().filter(filter)
                is GPUImagePixelationFilter -> adjuster = PixelationAdjuster().filter(filter)
                is GPUImageSaturationFilter -> adjuster = SaturationAdjuster().filter(filter)
                is GPUImageExposureFilter -> adjuster = ExposureAdjuster().filter(filter)
                is GPUImageHighlightShadowFilter -> adjuster =
                        HighlightShadowAdjuster().filter(filter)
                is GPUImageMonochromeFilter -> adjuster = MonochromeAdjuster().filter(filter)
                is GPUImageOpacityFilter -> adjuster = OpacityAdjuster().filter(filter)
                is GPUImageRGBFilter -> adjuster = RGBAdjuster().filter(filter)
                is GPUImageWhiteBalanceFilter -> adjuster = WhiteBalanceAdjuster().filter(filter)
                is GPUImageVignetteFilter -> adjuster = VignetteAdjuster().filter(filter)
                is GPUImageDissolveBlendFilter -> adjuster = DissolveBlendAdjuster().filter(filter)
                is GPUImageGaussianBlurFilter -> adjuster = GaussianBlurAdjuster().filter(filter)
                is GPUImageCrosshatchFilter -> adjuster = CrosshatchBlurAdjuster().filter(filter)
                is GPUImageBulgeDistortionFilter -> adjuster =
                        BulgeDistortionAdjuster().filter(filter)
                is GPUImageGlassSphereFilter -> adjuster = GlassSphereAdjuster().filter(filter)
                is GPUImageHazeFilter -> adjuster = HazeAdjuster().filter(filter)
                is GPUImageSphereRefractionFilter -> adjuster =
                        SphereRefractionAdjuster().filter(filter)
                is GPUImageSwirlFilter -> adjuster = SwirlAdjuster().filter(filter)
                is GPUImageColorBalanceFilter -> adjuster = ColorBalanceAdjuster().filter(filter)
                is GPUImageLevelsFilter -> adjuster = LevelsMinMidAdjuster().filter(filter)
                is GPUImageBilateralFilter -> adjuster = BilateralAdjuster().filter(filter)
                is GPUImageTransformFilter -> adjuster = RotateAdjuster().filter(filter)
                else -> adjuster = null
            }
        }

        fun canAdjust(): Boolean {
            return adjuster != null
        }

        fun adjust(percentage: Int) {
            adjuster?.adjust(percentage)
        }

        private abstract inner class Adjuster<T : GPUImageFilter> {
            var filter: T? = null
                private set

            fun filter(filter: GPUImageFilter): Adjuster<T> {
                this.filter = filter as T
                return this
            }

            abstract fun adjust(percentage: Int)

            protected fun range(percentage: Int, start: Float, end: Float): Float {
                return (end - start) * percentage / 100.0f + start
            }

            protected fun range(percentage: Int, start: Int, end: Int): Int {
                return (end - start) * percentage / 100 + start
            }
        }

        private inner class SharpnessAdjuster : Adjuster<GPUImageSharpenFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setSharpness(range(percentage, -4.0f, 4.0f))
            }
        }

        private inner class PixelationAdjuster : Adjuster<GPUImagePixelationFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setPixel(range(percentage, 1.0f, 100.0f))
            }
        }

        private inner class HueAdjuster : Adjuster<GPUImageHueFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setHue(range(percentage, 0.0f, 360.0f))
            }
        }

        private inner class ContrastAdjuster : Adjuster<GPUImageContrastFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setContrast(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class GammaAdjuster : Adjuster<GPUImageGammaFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setGamma(range(percentage, 0.0f, 3.0f))
            }
        }

        private inner class BrightnessAdjuster : Adjuster<GPUImageBrightnessFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setBrightness(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class SepiaAdjuster : Adjuster<GPUImageSepiaFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setIntensity(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class SobelAdjuster : Adjuster<GPUImageSobelEdgeDetectionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class ThresholdAdjuster : Adjuster<GPUImageThresholdEdgeDetectionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setLineSize(range(percentage, 0.0f, 5.0f))
                filter!!.setThreshold(0.9f)
            }
        }

        private inner class EmbossAdjuster : Adjuster<GPUImageEmbossFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.intensity = range(percentage, 0.0f, 4.0f)
            }
        }

        private inner class PosterizeAdjuster : Adjuster<GPUImagePosterizeFilter>() {
            override fun adjust(percentage: Int) {
                // In theorie to 256, but only first 50 are interesting
                filter!!.setColorLevels(range(percentage, 1, 50))
            }
        }

        private inner class GPU3x3TextureAdjuster : Adjuster<GPUImage3x3TextureSamplingFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class SaturationAdjuster : Adjuster<GPUImageSaturationFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setSaturation(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ExposureAdjuster : Adjuster<GPUImageExposureFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setExposure(range(percentage, -10.0f, 10.0f))
            }
        }

        private inner class HighlightShadowAdjuster : Adjuster<GPUImageHighlightShadowFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setShadows(range(percentage, 0.0f, 1.0f))
                filter!!.setHighlights(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class MonochromeAdjuster : Adjuster<GPUImageMonochromeFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setIntensity(range(percentage, 0.0f, 1.0f))
                //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            }
        }

        private inner class OpacityAdjuster : Adjuster<GPUImageOpacityFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setOpacity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class RGBAdjuster : Adjuster<GPUImageRGBFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRed(range(percentage, 0.0f, 1.0f))
                //getFilter().setGreen(range(percentage, 0.0f, 1.0f));
                //getFilter().setBlue(range(percentage, 0.0f, 1.0f));
            }
        }

        private inner class WhiteBalanceAdjuster : Adjuster<GPUImageWhiteBalanceFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setTemperature(range(percentage, 2000.0f, 8000.0f))
                //getFilter().setTint(range(percentage, -100.0f, 100.0f));
            }
        }

        private inner class VignetteAdjuster : Adjuster<GPUImageVignetteFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setVignetteStart(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class DissolveBlendAdjuster : Adjuster<GPUImageDissolveBlendFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setMix(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class GaussianBlurAdjuster : Adjuster<GPUImageGaussianBlurFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setBlurSize(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class CrosshatchBlurAdjuster : Adjuster<GPUImageCrosshatchFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setCrossHatchSpacing(range(percentage, 0.0f, 0.06f))
                filter!!.setLineWidth(range(percentage, 0.0f, 0.006f))
            }
        }

        private inner class BulgeDistortionAdjuster : Adjuster<GPUImageBulgeDistortionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
                filter!!.setScale(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class GlassSphereAdjuster : Adjuster<GPUImageGlassSphereFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class HazeAdjuster : Adjuster<GPUImageHazeFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setDistance(range(percentage, -0.3f, 0.3f))
                filter!!.setSlope(range(percentage, -0.3f, 0.3f))
            }
        }

        private inner class SphereRefractionAdjuster : Adjuster<GPUImageSphereRefractionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class SwirlAdjuster : Adjuster<GPUImageSwirlFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setAngle(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ColorBalanceAdjuster : Adjuster<GPUImageColorBalanceFilter>() {

            override fun adjust(percentage: Int) {
                filter!!.setMidtones(
                    floatArrayOf(
                        range(percentage, 0.0f, 1.0f),
                        range(percentage / 2, 0.0f, 1.0f),
                        range(percentage / 3, 0.0f, 1.0f)
                    )
                )
            }
        }

        private inner class LevelsMinMidAdjuster : Adjuster<GPUImageLevelsFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f)
            }
        }

        private inner class BilateralAdjuster : Adjuster<GPUImageBilateralFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f))
            }
        }

        private inner class RotateAdjuster : Adjuster<GPUImageTransformFilter>() {
            override fun adjust(percentage: Int) {
                val transform = FloatArray(16)
                Matrix.setRotateM(transform, 0, (360 * percentage / 100).toFloat(), 0f, 0f, 1.0f)
                filter!!.transform3D = transform
            }
        }

    }
}
