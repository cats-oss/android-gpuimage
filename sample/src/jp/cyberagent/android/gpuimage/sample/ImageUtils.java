/* 
 * Copyright (C) 2012 CyberAgent 
 */

package jp.cyberagent.android.gpuimage.sample;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    public final static int IMAGE_MAX_SIZE = 1200000; // 1.2MP
    public final static int IMAGE_MAX_SIZE_HALF = 600000; // 0.6MP

    /**
     * Convets dp to pixels.
     * 
     * @param dp Dp value to be converted to pixels.
     * @param context The Context
     * @return pixels equivalent to the given dp value
     */
    public static float dpToPixel(final float dp, final Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * Load a resized image with default max size of 1.2MP (height*width).
     * 
     * @param imageFile the image file
     * @return the bitmap
     */
    public static Bitmap loadResizedImage(final File imageFile) {
        return loadResizedImage(imageFile, IMAGE_MAX_SIZE);
    }

    /**
     * This will load a resized image. maxSize is defined by width*height. This
     * way of loading will not generate a OutOfMemoryException. Except if the
     * requested size is too big.
     * 
     * @param imageFile the image file
     * @param maxSize the max size (width * height)
     * @return the bitmap
     */
    public static Bitmap loadResizedImage(final File imageFile, final int maxSize) {
        return loadResizedImage(imageFile, new ImageResizeMaxSize(maxSize));
    }

    /**
     * This will load a resized image where the new images size is newImageWidth
     * >= minWidth and newImageHeight >= minHeight. One of those two will always
     * be ==.
     * 
     * @param imageFile the image file
     * @param minWidth the min width
     * @param minHeight the min height
     * @return the resized bitmap
     */
    public static Bitmap loadResizedImageMin(final File imageFile, final int minWidth,
            final int minHeight) {
        return loadResizedImage(imageFile, new ImageResizeMinSide(minWidth, minHeight));
    }

    /**
     * This will load a resized image where the new images size is newImageWidth
     * <= maxWidth and newImageHeight <= maxHeight. One of those two will always
     * be ==.
     * 
     * @param imageFile the image file
     * @param maxWidth the max width
     * @param maxHeight the max height
     * @return the resized bitmap
     */
    public static Bitmap loadResizedImageMax(final File imageFile, final int maxWidth,
            final int maxHeight) {
        return loadResizedImage(imageFile, new ImageResizeMaxSide(maxWidth, maxHeight));
    }

    private static Bitmap loadResizedImage(final File imageFile, final ImageResizeStrategy strategy) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        DecodingTools.decodeFile(imageFile.getAbsolutePath(), options);
        int scale = 1;
        while (strategy.isAcceptableSize(options.outWidth / scale, options.outHeight / scale)) {
            scale++;
        }
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;
        if (scale > 1) {
            scale--;
            options = new BitmapFactory.Options();
            options.inSampleSize = scale;
            DecodingTools.useOptimizedOptions(options);
            bitmap = DecodingTools.decodeFile(imageFile.getAbsolutePath(), options);
            if (bitmap == null) {
                return null;
            }

            // resize to desired dimensions
            int[] size = strategy.getScaleSize(bitmap.getWidth(), bitmap.getHeight());

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], true);
                bitmap.recycle();
                bitmap = scaledBitmap;
            } catch (OutOfMemoryError e) {
                bitmap = DecodingTools.free(bitmap);
                scaledBitmap = DecodingTools.free(scaledBitmap);
                System.gc();
                e.printStackTrace();
                return null;
            }

            System.gc();
        } else {
            bitmap = DecodingTools.decodeFile(imageFile.getAbsolutePath());
        }

        return rotateImage(bitmap, imageFile);
    }

    private static Bitmap rotateImage(final Bitmap bitmap, final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        int orientation = 0;
        try {
            orientation = ImageUtils.getImageOrientation(fileWithExifInfo.getAbsolutePath());
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }
        } catch (IOException e) {
            Log.i(TAG, "Could not get orientation", e);
        } catch (OutOfMemoryError e) {
            rotatedBitmap = DecodingTools.free(rotatedBitmap);
            System.gc();
            e.printStackTrace();
            return null;
        }
        return rotatedBitmap;
    }

    public static Bitmap cropBitmap(final Bitmap bitmap, final int size) {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final int minSide = Math.min(width, height);
        Bitmap scaled = null;
        Bitmap cropped = null;
        try {
            cropped = Bitmap.createBitmap(bitmap,
                    (width - minSide) / 2, (height - minSide) / 2,
                    minSide, minSide);
            if (cropped == null || minSide <= size) {
                return cropped;
            }
            scaled = Bitmap.createScaledBitmap(cropped, size, size, true);
            cropped.recycle();
            cropped = null;
        } catch (OutOfMemoryError e) {
            scaled = DecodingTools.free(scaled);
            cropped = DecodingTools.free(cropped);
            System.gc();
            e.printStackTrace();
            return null;
        }
        return scaled;
    }

    public static Bitmap cropImage(final File imageFile, final int size) {
        Bitmap bitmap = ImageUtils.loadResizedImageMin(imageFile, size, size);
        if (bitmap == null) {
            return null;
        }
        return cropBitmap(bitmap, size);
    }

    public static int getImageOrientation(final Context context, final Uri uri)
            throws IOException {
        String[] projection = {
                MediaStore.Images.Media.DATA,
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int orientation = 0;
        if (cursor.moveToFirst()) {
            String file = cursor.getString(0);
            orientation = getImageOrientation(file);
        }
        cursor.close();
        return orientation;
    }

    public static int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static class DecodingTools {
        public static Bitmap decodeFile(final String pathName) {
            return decodeFile(pathName, null);
        }

        private static Bitmap decodeFile(final String pathName, final Options options) {
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeFile(pathName, options);
            } catch (OutOfMemoryError e) {
                bmp = free(bmp);
                e.printStackTrace();
                System.gc();
            }
            return bmp;
        }

        public static Bitmap decodeStream(final InputStream is) {
            Bitmap bmp = null;
            try {

                Options options = useOptimizedOptions(new BitmapFactory.Options());
                bmp = BitmapFactory.decodeStream(is, null, options);
            } catch (OutOfMemoryError e) {
                bmp = free(bmp);
                e.printStackTrace();
                System.gc();
            }
            return bmp;
        }

        private static Options useOptimizedOptions(final Options options) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inTempStorage = new byte[32 * 1024];
            return options;
        }

        private static Bitmap free(final Bitmap bmp) {
            if (bmp != null) {
                if (!bmp.isRecycled()) {
                    bmp.recycle();
                }
            }
            return null;
        }
    }

    private interface ImageResizeStrategy {
        boolean isAcceptableSize(int width, int height);

        int[] getScaleSize(int width, int height);
    }

    private static class ImageResizeMinSide implements ImageResizeStrategy {

        private final int mMinWidth;
        private final int mMinHeigth;

        public ImageResizeMinSide(final int minSide) {
            this(minSide, minSide);
        }

        public ImageResizeMinSide(final int minWidth, final int minHeight) {
            mMinWidth = minWidth;
            mMinHeigth = minHeight;
        }

        @Override
        public boolean isAcceptableSize(final int width, final int height) {
            return width > mMinWidth && height > mMinHeigth;
        }

        @Override
        public int[] getScaleSize(final int width, final int height) {
            double newWidth;
            double newHeight;
            if ((double) width / mMinWidth < (double) height / mMinHeigth) {
                newWidth = mMinWidth;
                newHeight = (newWidth / width) * height;
            } else {
                newHeight = mMinHeigth;
                newWidth = (newHeight / height) * width;
            }
            return new int[] {
                    Math.round((float) newWidth), Math.round((float) newHeight)
            };
        }
    }

    private static class ImageResizeMaxSide implements ImageResizeStrategy {

        private final int mMaxWidth;
        private final int mMaxHeigth;

        public ImageResizeMaxSide(final int maxSide) {
            this(maxSide, maxSide);
        }

        public ImageResizeMaxSide(final int maxWidth, final int maxHeight) {
            mMaxWidth = maxWidth;
            mMaxHeigth = maxHeight;
        }

        @Override
        public boolean isAcceptableSize(final int width, final int height) {
            return width > mMaxWidth || height > mMaxHeigth;
        }

        @Override
        public int[] getScaleSize(final int width, final int height) {
            double newWidth;
            double newHeight;
            if ((double) width / mMaxWidth < (double) height / mMaxHeigth) {
                newHeight = mMaxHeigth;
                newWidth = (newHeight / height) * width;
            } else {
                newWidth = mMaxWidth;
                newHeight = (newWidth / width) * height;
            }
            return new int[] {
                    Math.round((float) newWidth), Math.round((float) newHeight)
            };
        }
    }

    private static class ImageResizeMaxSize implements ImageResizeStrategy {

        private final int mMaxSize;

        public ImageResizeMaxSize(final int maxSize) {
            mMaxSize = maxSize;
        }

        @Override
        public boolean isAcceptableSize(final int width, final int height) {
            return width * height > mMaxSize;
        }

        @Override
        public int[] getScaleSize(final int width, final int height) {
            double y = Math.sqrt(mMaxSize / (((double) width) / height));
            double x = (y / height) * width;
            return new int[] {
                    (int) x, (int) y,
            };
        }
    }
}
