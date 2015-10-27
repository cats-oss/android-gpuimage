package jp.co.cyberagent.android.gpuimage.util;

import android.net.Uri;

import static android.content.ContentResolver.SCHEME_FILE;

public class AssetsUtil {
    private static final String ANDROID_ASSET = "android_asset";
    private static final int ASSET_PREFIX_LENGTH =
        (SCHEME_FILE + ":///" + ANDROID_ASSET + "/").length();

    public static boolean isAssetUri(Uri uri) {
        return SCHEME_FILE.equals(uri.getScheme())
            && !uri.getPathSegments().isEmpty()
            && ANDROID_ASSET.equals(uri.getPathSegments().get(0));
    }

    public static String getFilePath(Uri assetUri) {
        return assetUri.toString().substring(ASSET_PREFIX_LENGTH);
    }
}
