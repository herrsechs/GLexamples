package opengl.glexamples.surfaceView;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.Explosion;
import opengl.glexamples.shape.PolarRose;

/**
 * Created by LLLLLyj on 2015/10/24.
 * 粒子系统破碎效果场景
 */
public class ParticleSystemSurfaceView extends GLSurfaceView{
    private SceneRenderer mRenderer;

    public ParticleSystemSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * 场景渲染器
     */
    private class SceneRenderer implements Renderer{
        private PolarRose mPolarRose;
        private Explosion mExplosion;
        public void onSurfaceCreated(GL10 gl, EGLConfig config){
            GLES20.glClearColor(0.53f,0.81f,0.92f, 1.0f);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            this.mPolarRose = new PolarRose(ParticleSystemSurfaceView.this);
            this.mExplosion = new Explosion(ParticleSystemSurfaceView.this);
        }

        public void onDrawFrame(GL10 gl){
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            this.mExplosion.draw();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height){
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0, 0, 3, 0f, 0f, 0f, 0.0f, 1.0f, 0f);
        }
    }
}
