package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GPUImageTwoInputFilter extends GPUImageFilter {
    private static final String VERTEX_SHADER = "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "attribute vec4 inputTextureCoordinate2;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 textureCoordinate2;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    textureCoordinate2 = inputTextureCoordinate2.xy;\n" +
            "}";


    public int filterSecondTextureCoordinateAttribute;
    public int filterInputTextureUniform2;
    public int inputRotation2;
    public int filterSourceTexture2 = OpenGlUtils.NO_TEXTURE;
    public boolean mIsInitialized = false;
    private Bitmap mBitmap = null;

    private Context mContext;

    public GPUImageTwoInputFilter(Context context, String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
    }

    public GPUImageTwoInputFilter(Context context, String fragmentShader) {
        super(VERTEX_SHADER, fragmentShader);
    }

    @Override
    public void onInit() {
        super.onInit();

        filterSecondTextureCoordinateAttribute = GLES20.glGetAttribLocation(mGLProgId, "inputTextureCoordinate2");
        filterInputTextureUniform2 = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture2"); // This does assume a name of "inputImageTexture2" for second input texture in the fragment shader
        GLES20.glEnableVertexAttribArray(filterSecondTextureCoordinateAttribute);

        mIsInitialized = true;

    }

    public void setBitmap(final Bitmap bm) {
        runOnDraw(new Runnable() {
            public void run() {
                if (filterSourceTexture2 == OpenGlUtils.NO_TEXTURE) {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
                    filterSourceTexture2 = OpenGlUtils.loadTexture(bm, OpenGlUtils.NO_TEXTURE);
                }
            }
        });
    }

    public void onDestroy() {
        GLES20.glDeleteProgram(mGLProgId);
        mIsInitialized = false;
    }


    @Override
    public void onDraw(final int textureId, final FloatBuffer cubeBuffer, final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(mGLProgId);
        runPendingOnDrawTasks();
        if (!mIsInitialized) {
            return;
        }

        if (mBitmap != null && !mBitmap.isRecycled()) {
            setBitmap(mBitmap);
        }

        cubeBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }

        GLES20.glEnableVertexAttribArray(filterSecondTextureCoordinateAttribute);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, filterSourceTexture2);
        GLES20.glUniform1i(filterInputTextureUniform2, 3);

        GLES20.glVertexAttribPointer(filterSecondTextureCoordinateAttribute, 2, GLES20.GL_FLOAT, false, 0, textureCoordinatesForRotation(kGPUImageNoRotation));

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public static float[] noRotationTextureCoordinates = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };

    static float[] rotateLeftTextureCoordinates = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
    };

    static float[] rotateRightTextureCoordinates = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    static float[] verticalFlipTextureCoordinates = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };

    static float[] horizontalFlipTextureCoordinates = {
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    };

    static float[] rotateRightVerticalFlipTextureCoordinates = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    static float[] rotate180TextureCoordinates = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private static final int kGPUImageNoRotation = 1;
    private static final int kGPUImageRotateLeft = 2;
    private static final int kGPUImageRotateRight = 3;
    private static final int kGPUImageFlipVertical = 4;
    private static final int kGPUImageFlipHorizonal = 5;
    private static final int kGPUImageRotateRightFlipVertical = 6;
    private static final int kGPUImageRotate180 = 7;


    public ByteBuffer textureCoordinatesForRotation(int rotationMode) {
        float[] buffer = null;
        switch (rotationMode) {
            case kGPUImageNoRotation:
                buffer = noRotationTextureCoordinates;
            case kGPUImageRotateLeft:
                buffer = rotateLeftTextureCoordinates;
            case kGPUImageRotateRight:
                buffer = rotateRightTextureCoordinates;
            case kGPUImageFlipVertical:
                buffer = verticalFlipTextureCoordinates;
            case kGPUImageFlipHorizonal:
                buffer = horizontalFlipTextureCoordinates;
            case kGPUImageRotateRightFlipVertical:
                buffer = rotateRightVerticalFlipTextureCoordinates;
            case kGPUImageRotate180:
                buffer = rotate180TextureCoordinates;
        }

        buffer = noRotationTextureCoordinates;
        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();

        return bBuffer;
    }
}
