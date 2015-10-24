package opengl.glexamples.surfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.IDCard;
import opengl.glexamples.shape.Triangle;

/**
 * Created by LLLLLyj on 2015/10/18.
 */
public class IDCardSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 100.0f/320;
    private SceneRenderer mRenderer;

    private float mPreviousY;
    private float mPreviousX;
    private float mPreviousDist;

    int textureId;

    public IDCardSurfaceView(Context context) {
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
        int pointerCount = e.getPointerCount();
        if(pointerCount == 1) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float dy = y - mPreviousY;
                    float dx = x - mPreviousX;
                    if (dy < 5) {
                        mRenderer.texRect.yAngle += dx * TOUCH_SCALE_FACTOR;
                    }
                    if (dx < 5) {
                        mRenderer.texRect.zAngle += -dy * TOUCH_SCALE_FACTOR;
                    }

                    break;
            }
            mPreviousY = y;
            mPreviousX = x;
        }
        else if(pointerCount == 2){
            float dist = getDist(e);
            if(dist > mPreviousDist)
                mRenderer.texRect.scale += 0.01;
            else
                mRenderer.texRect.scale -= 0.01;

            mPreviousDist = getDist(e);
        }
            return true;

    }

    private float getDist(MotionEvent e){
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float)Math.sqrt(x*x + y*y);
    }


    private class SceneRenderer implements Renderer
    {
        IDCard texRect;

        public void onDrawFrame(GL10 gl)
        {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            texRect.drawSelf(textureId);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0,0,5,0f,0f,0f,0.0f,1.0f,0.0f);

        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);
            texRect =new IDCard(IDCardSurfaceView.this);
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
        /*
        Bitmap bitmapTmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.wall);
        Bitmap bmp = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        //Drawable doge = getResources().getDrawable(R.drawable.doge, null);
        Bitmap doge = BitmapFactory.decodeResource(getResources(), R.drawable.doge);
        canvas.drawBitmap(doge, 0, 0, paint);
        */
        Bitmap bmp = Bitmap.createBitmap(1200, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setTextSize(60);
        //Drawable doge = getResources().getDrawable(R.drawable.doge, null);
        Bitmap doge = BitmapFactory.decodeResource(getResources(), R.drawable.doge);
        Rect photo = new Rect(0,0, 400, 400);
        String name = "姓名：Doge";
        String gender = "性别：雄";
        String position = "职业：娱乐明星";
        String contact = "联系方式：与Kabosu酱一起散步";

        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(doge, null, photo, paint);
        canvas.drawText(name, 450, 60, paint);
        canvas.drawText(gender, 450, 120, paint);
        canvas.drawText(position, 450, 180, paint);
        canvas.drawText(contact, 450, 240, paint);
        //加载纹理进入显存
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型
                        0,                      //纹理层次，0代表直接贴图
                        bmp,              //纹理图像
                        0                      //纹理边框尺寸
                );
        bmp.recycle(); 		  //纹理加载成功后释放内存中的纹理图
    }
}
