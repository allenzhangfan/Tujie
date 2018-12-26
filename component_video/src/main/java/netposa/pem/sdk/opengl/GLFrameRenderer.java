package netposa.pem.sdk.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLFrameRenderer implements Renderer {

    private GLSurfaceView mTargetSurface;
    private GLProgram prog = new GLProgram(0);
    private int mScreenWidth, mScreenHeight;
    private int mVideoWidth, mVideoHeight;
    private ByteBuffer y = null;
    private ByteBuffer u = null;
    private ByteBuffer v = null;

    public GLFrameRenderer(GLSurfaceView surface) {
        mTargetSurface = surface;
//        mScreenWidth = dm.widthPixels;
//        mScreenHeight = dm.heightPixels;
//        mScreenWidth = 1920;
//        mScreenHeight = 1088;
        mScreenWidth = 704;
        mScreenHeight = 576;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (!prog.isProgramBuilt()) {
            prog.buildProgram();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mScreenWidth = width;
        mScreenHeight = height;
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (y != null) {
                // reset position, have to be done
                y.position(0);
                u.position(0);
                v.position(0);
                prog.buildTextures(y, u, v, mVideoWidth, mVideoHeight);
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                prog.drawFrame();
            }
        }

    }

    /**
     * this method will be called from native code, it happens when the video is about to play or
     * the video size changes.
     */
    public void update(int w, int h) {
        if (w > 0 && h > 0) {
            // 调整比例
            if (mScreenWidth > 0 && mScreenHeight > 0) {
                float f1 = 1f * mScreenHeight / mScreenWidth;
                float f2 = 1f * h / w;
                if (f1 == f2) {
                    prog.createBuffers(GLProgram.squareVertices);
                } else if (f1 < f2) {
                    float widScale = f1 / f2;
                    prog.createBuffers(new float[]{-widScale, -1.0f, widScale, -1.0f, -widScale, 1.0f, widScale,
                            1.0f,});
                } else {
                    float heightScale = f2 / f1;
                    prog.createBuffers(new float[]{-1.0f, -heightScale, 1.0f, -heightScale, -1.0f, heightScale, 1.0f,
                            heightScale,});
                }
            }
            allocateYUVbuffer(w, h);
        }
    }

    private void allocateYUVbuffer(int w, int h) {
        // 初始化容器
        if (w != mVideoWidth && h != mVideoHeight) {
            this.mVideoWidth = w;
            this.mVideoHeight = h;
            int yarraySize = w * h;
            int uvarraySize = yarraySize / 4;
            synchronized (this) {
                y = ByteBuffer.allocate(yarraySize);
                u = ByteBuffer.allocate(uvarraySize);
                v = ByteBuffer.allocate(uvarraySize);
            }
        }
    }

    public void ResetSolution() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        yuvPlanes = null;
    }

    /**
     * this method will be called from native code, it's used for passing yuv data to me.
     */
    public void update(byte[] ydata, byte[] udata, byte[] vdata) {
        if (y == null || u == null || v == null)
            return;
        synchronized (this) {
            y.clear();
            u.clear();
            v.clear();
            y.put(ydata, 0, ydata.length);
            u.put(udata, 0, udata.length);
            v.put(vdata, 0, vdata.length);
        }

        // request to
        if (mTargetSurface != null) {
            mTargetSurface.requestRender();
        }
    }

    public void openGlDraw(byte[] yuvData, int width, int height) {
        update(width, height);

        copyFrom(yuvData, width, height);

        byte[] y = new byte[yuvPlanes[0].remaining()];
        yuvPlanes[0].get(y, 0, y.length);

        byte[] u = new byte[yuvPlanes[1].remaining()];
        yuvPlanes[1].get(u, 0, u.length);

        byte[] v = new byte[yuvPlanes[2].remaining()];
        yuvPlanes[2].get(v, 0, v.length);

        update(y, u, v);

    }

    public ByteBuffer[] yuvPlanes = null;

    public void copyFrom(byte[] yuvData, int width, int height) {

        int[] yuvStrides = {width, width / 2, width / 2};

        if (yuvPlanes == null) {
            yuvPlanes = new ByteBuffer[3];
            yuvPlanes[0] = ByteBuffer.allocate(yuvStrides[0] * height);
            yuvPlanes[1] = ByteBuffer.allocate(yuvStrides[1] * height / 2);
            yuvPlanes[2] = ByteBuffer.allocate(yuvStrides[2] * height / 2);
        }

        if (yuvData.length < width * height * 3 / 2) {
            throw new RuntimeException("Wrong arrays size: " + yuvData.length);
        }

        int planeSize = width * height;

        ByteBuffer[] planes = new ByteBuffer[3];

        planes[0] = ByteBuffer.wrap(yuvData, 0, planeSize);
        planes[1] = ByteBuffer.wrap(yuvData, planeSize, planeSize / 4);
        planes[2] = ByteBuffer.wrap(yuvData, planeSize + planeSize / 4, planeSize / 4);

        for (int i = 0; i < 3; i++) {
            yuvPlanes[i].position(0);
            yuvPlanes[i].put(planes[i]);
            yuvPlanes[i].position(0);
            yuvPlanes[i].limit(yuvPlanes[i].capacity());
        }
    }

    public void clear() {
        if (yuvPlanes != null) {
            yuvPlanes[0].clear();
            yuvPlanes[1].clear();
            yuvPlanes[2].clear();
            yuvPlanes = null;
        }
    }
}
