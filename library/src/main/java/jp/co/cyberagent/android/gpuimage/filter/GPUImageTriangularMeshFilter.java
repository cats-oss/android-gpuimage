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

package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;

public class GPUImageTriangularMeshFilter extends GPUImageFilter {
    private final FloatBuffer glVertexBuffer;
    private final FloatBuffer glTextureBuffer;

    public GPUImageTriangularMeshFilter(float[] vertices, float[] textureVertices) {
        super(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
        glVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glVertexBuffer.put(vertices).position(0);

        glTextureBuffer = ByteBuffer.allocateDirect(textureVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glTextureBuffer.put(textureVertices).position(0);
    }

    public void onDraw(final int textureId, final FloatBuffer vertexBuffer,
                       final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(getProgram());
        runPendingOnDrawTasks();

        if (!isInitialized()) {
            return;
        }

        glVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(getAttribPosition(), 2, GLES20.GL_FLOAT, true, 0, glVertexBuffer);
        GLES20.glEnableVertexAttribArray(getAttribPosition());
        glTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(getAttribTextureCoordinate(), 2, GLES20.GL_FLOAT, true, 0,
                glTextureBuffer);
        GLES20.glEnableVertexAttribArray(getAttribTextureCoordinate());
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(getUniformTexture(), 0);
        }

        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, glVertexBuffer.capacity()/2);
        GLES20.glDisableVertexAttribArray(getAttribPosition());
        GLES20.glDisableVertexAttribArray(getAttribTextureCoordinate());
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
