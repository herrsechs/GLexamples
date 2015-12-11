package opengl.glexamples.surfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.TextureRect;

/**
 * Created by LLLLLyj on 2015/10/24.
 */
public class SkyBoxSurfaceView extends GLSurfaceView{
    private final float TOUCH_SCALE_FACTOR = 100.0f/320;
    private SceneRenderer mRenderer;

    private static final float UNIT_SIZE=28f;//天空盒的半边长

    private float mPreviousY;
    private float mPreviousX;
    private float mPreviousDist;

    float cx=0;
    float cy=2;
    float cz=24;
    float cr=24;
    float cAngle=0;

    int[] textureIds = new int[6];

    public SkyBoxSurfaceView(Context context){
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements Renderer{
        TextureRect texRect;
        public void onDrawFrame(GL10 gl10){
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            MatrixState.setCamera(cx,cy,cz,0f,0f,0f,0f,1.0f,0.0f);
            //天空盒六面调整值
            final float tzz=0.4f;
            /*
            //绘制天空盒后面
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -UNIT_SIZE+tzz);
            texRect.drawSelf(textureIds[0]);
            MatrixState.popMatrix();

            //绘制天空盒前面
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, UNIT_SIZE-tzz);
            MatrixState.rotate(180, 0, 1, 0);
            texRect.drawSelf(textureIds[5]);
            MatrixState.popMatrix();
            //绘制天空盒左面
            MatrixState.pushMatrix();
            MatrixState.translate(-UNIT_SIZE+tzz, 0, 0);
            MatrixState.rotate(90, 0, 1, 0);
            texRect.drawSelf(textureIds[1]);
            MatrixState.popMatrix();
            //绘制天空盒右面
            MatrixState.pushMatrix();
            MatrixState.translate(UNIT_SIZE-tzz, 0, 0);
            MatrixState.rotate(-90, 0, 1, 0);
            texRect.drawSelf(textureIds[2]);
            MatrixState.popMatrix();
            //绘制天空盒下面
            MatrixState.pushMatrix();
            MatrixState.translate(0, -UNIT_SIZE+tzz, 0);
            MatrixState.rotate(-90, 1, 0, 0);
            texRect.drawSelf(textureIds[3]);
            MatrixState.popMatrix();
            */
            //绘制天空盒上面
            MatrixState.pushMatrix();
            MatrixState.translate(0, UNIT_SIZE-tzz, 0);
            MatrixState.rotate(90, 1, 0, 0);
            texRect.drawSelf(textureIds[4]);
            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height){
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 1000);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config){
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //创建纹理矩形对对象
            texRect=new TextureRect(SkyBoxSurfaceView.this);
            //加载纹理
            textureIds[0]=initTexture(R.raw.skycubemap_back);
            textureIds[1]=initTexture(R.raw.skycubemap_left);
            textureIds[2]=initTexture(R.raw.skycubemap_right);
            textureIds[3]=initTexture(R.raw.skycubemap_down);
            textureIds[4]=initTexture(R.raw.skycubemap_up);
            textureIds[5]=initTexture(R.raw.skycubemap_front);
        }

        public int initTexture(int drawableID){
            //生成纹理ID
            int[] textures = new int[1];
            GLES20.glGenTextures
                    (
                            1,          //产生的纹理id的数量
                            textures,   //纹理id的数组
                            0           //偏移量
                    );
            int textureId=textures[0];

            //初始化纹理参数
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

            //通过输入流加载图片
            InputStream is = getResources().openRawResource(drawableID);
            Bitmap bitmapTmp;
            try
            {
                bitmapTmp = BitmapFactory.decodeStream(is);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            //实际加载纹理
            GLUtils.texImage2D
                    (
                            GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                            0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
                            bitmapTmp,              //纹理图像
                            0                      //纹理边框尺寸
                    );
            bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
            return textureId;
        }
    }
}
