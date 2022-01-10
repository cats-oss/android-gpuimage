package jp.co.cyberagent.android.gpuimage.view;

import android.opengl.GLSurfaceView;

import jp.co.cyberagent.android.gpuimage.GLTextureView;

/*
 * Copyright (C) 2022 MichaelX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public interface GLRenderView extends ViewCompat {
    /**
     * @see android.opengl.GLSurfaceView#setEGLContextClientVersion(int)
     * @see jp.co.cyberagent.android.gpuimage.GLTextureView#setEGLContextClientVersion(int)
     */
    void setEGLContextClientVersion(int glVersion);

    /**
     * @see android.opengl.GLSurfaceView#setEGLConfigChooser(int, int, int, int, int, int)
     * @see jp.co.cyberagent.android.gpuimage.GLTextureView#setEGLConfigChooser(int, int, int, int, int, int)
     */
    void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize,
                             int depthSize, int stencilSize);

    /**
     * adapter for {@link android.opengl.GLSurfaceView#setRenderer(GLSurfaceView.Renderer)}
     * and {@link jp.co.cyberagent.android.gpuimage.GLTextureView#setRenderer(GLTextureView.Renderer)}
     */
    void setRender(Renderer render);

    /**
     * @see GLSurfaceView#setRenderMode(int)
     * @see GLTextureView#setRenderMode(int)
     */
    void setRenderMode(int mode);

    /**
     * @see GLSurfaceView#requestRender()
     * @see GLTextureView#requestRender()
     */
    void requestRender();

    /**
     * {@link GLSurfaceView.Renderer}
     * {@link GLTextureView.Renderer}
     */
    interface Renderer {
    }
}
