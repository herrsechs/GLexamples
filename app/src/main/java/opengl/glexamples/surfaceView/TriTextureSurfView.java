package opengl.glexamples.surfaceView;


import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.Triangle;

public class TriTextureSurfView extends GLSurfaceView
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;
    private SceneRenderer mRenderer;
	
	private float mPreviousY;
    private float mPreviousX;
    
    int textureId;
	
	public TriTextureSurfView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
	
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mRenderer.texRect.yAngle += dx * TOUCH_SCALE_FACTOR;
            mRenderer.texRect.zAngle+= dy * TOUCH_SCALE_FACTOR;
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }

	private class SceneRenderer implements Renderer
    {   
    	Triangle texRect;
    	
        public void onDrawFrame(GL10 gl) 
        { 
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            texRect.drawSelf(textureId);
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
            
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);
            texRect=new Triangle(TriTextureSurfView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            initTexture();
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }
    }
	
	public void initTexture()//textureId
	{
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生纹理的id数量
				textures,   //纹理id数组
				0           //偏移量
		);    
		textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

        Bitmap bitmapTmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall);

        //加载纹理进入显存
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型
        		0, 					  //纹理层次，0代表直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放内存中的纹理图
	}
}
