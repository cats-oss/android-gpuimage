
package jp.cyberagent.android.gpuimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import android.opengl.GLES20;

public class GPUImageFilterGroup extends GPUImageFilter {

    private final List<GPUImageFilter> mFilters;
    private int[] mFrameBuffers;
    private int[] mFrameBufferTextures;

    private final float mCube[] = {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
    };

    private final float mTexture[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private final short[] mIndeces = {
            0, 1, 2,
            0, 2, 3
    };
    private final FloatBuffer mGLCubeBuffer;
    private final ShortBuffer mGLIndexBuffer;
    private final FloatBuffer mGLTextureBuffer;

    public GPUImageFilterGroup(final List<GPUImageFilter> filters) {
        mFilters = filters;
        mGLCubeBuffer = ByteBuffer.allocateDirect(mCube.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLCubeBuffer.put(mCube).position(0);

        mGLIndexBuffer = ByteBuffer.allocateDirect(mIndeces.length * 4)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        mGLIndexBuffer.put(mIndeces).position(0);

        mGLTextureBuffer = ByteBuffer.allocateDirect(mTexture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureBuffer.put(mTexture).position(0);
    }

    @Override
    public void onInit() {
        super.onInit();
        for (GPUImageFilter filter : mFilters) {
            filter.onInit();
        }
    }

    @Override
    public void onDestroy() {
        destroyFramebuffers();
        for (GPUImageFilter filter : mFilters) {
            filter.onDestroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers() {
        if (mFrameBufferTextures != null) {
            GLES20.glDeleteTextures(mFrameBufferTextures.length, mFrameBufferTextures, 0);
            mFrameBufferTextures = null;
        }
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        if (mFrameBuffers != null) {
            destroyFramebuffers();
        }
        mFrameBuffers = new int[mFilters.size() - 1];
        mFrameBufferTextures = new int[mFilters.size() - 1];

        for (int i = 0; i < mFilters.size() - 1; i++) {
            mFilters.get(i).onOutputSizeChanged(width, height);
            GLES20.glGenFramebuffers(1, mFrameBuffers, i);
            GLES20.glGenTextures(1, mFrameBufferTextures, i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                    GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, mFrameBufferTextures[i], 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        }
        mFilters.get(mFilters.size() - 1).onOutputSizeChanged(width, height);
    }

    @Override
    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
            final FloatBuffer textureBuffer, final ShortBuffer indexBuffer) {
        if (mFrameBuffers == null || mFrameBufferTextures == null) {
            return;
        }
        int previousTexture = textureId;
        for (int i = 0; i < mFilters.size() - 1; i++) {
            GPUImageFilter filter = mFilters.get(i);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
            GLES20.glClearColor(0, 0, 0, 1);
            filter.onDraw(previousTexture, mGLCubeBuffer, mGLTextureBuffer, mGLIndexBuffer);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            previousTexture = mFrameBufferTextures[i];
        }
        mFilters.get(mFilters.size() - 1).onDraw(previousTexture, cubeBuffer, textureBuffer,
                indexBuffer);
    }

    public List<GPUImageFilter> getFilters() {
        return mFilters;
    }
}
