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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.InputStream;
import java.util.jar.Attributes;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.Explosion;
import opengl.glexamples.shape.IDCard;
import opengl.glexamples.shape.TextureRect;


public class SingleIDCardSurfaceView extends GLSurfaceView{
    private int[] textureIds;
    private SceneRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private float mPreviousDist;


    public SingleIDCardSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        textureIds = new int[6];
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
                    if (mRenderer.texRect.centerX < 4
                               && mRenderer.texRect.centerX > -1) {
                        mRenderer.texRect.xShift -= 0.01 * dx;
                    }else if(mRenderer.texRect.centerX > 3 && dx > 0){
                        mRenderer.texRect.xShift -= 0.01 * dx;
                    }else if(mRenderer.texRect.centerX < -1 && dx < 0){
                        mRenderer.texRect.xShift -= 0.01 * dx;
                    }
                    if (mRenderer.texRect.centerY < 4.5
                               && mRenderer.texRect.centerY > -5) {
                        mRenderer.texRect.yShift += 0.01 * dy;
                    }else if(mRenderer.texRect.centerY > 4.5 && dy < 0){
                        mRenderer.texRect.yShift += 0.01 * dy;
                    }else if(mRenderer.texRect.centerY < -5 && dy > 0){
                        mRenderer.texRect.yShift += 0.01 * dy;
                    }

                    break;
            }
            mPreviousY = y;
            mPreviousX = x;
        }
        else if(pointerCount == 2){
            float dist = getDist(e);
            if(dist > mPreviousDist && mRenderer.texRect.scale < 1.5)
                mRenderer.texRect.scale += 0.01;
            else if(mRenderer.texRect.scale > 1)
                mRenderer.texRect.scale -= 0.01;
            //Log.d("TAG", "scale:" + mRenderer.texRect.scale);
            mPreviousDist = getDist(e);
        }
        else if(pointerCount == 3){
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mRenderer.texRect.xShift += 0.01*dx;
            mRenderer.texRect.yShift -= 0.01*dy;

            mPreviousX = x;
            mPreviousY = y;
        }
        this.requestRender();

        return true;

    }

    private float getDist(MotionEvent e){
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float)Math.sqrt(x*x + y*y);
    }

    private class SceneRenderer implements Renderer{
        IDCard texRect;
        TextureRect wallPaper;
        Explosion mExplosion;

        public void onDrawFrame(GL10 gl10){

            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            int[] ids = new int[]{textureIds[0], textureIds[1], textureIds[1], textureIds[1],
                    textureIds[1], textureIds[1]};

            wallPaper.drawSelf(textureIds[2]);
            texRect.drawSelf(ids);
            mExplosion.draw();
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig config){
            GLES20.glClearColor(0.52f,0.73f,0.91f, 1.0f);

            texRect    = new IDCard(SingleIDCardSurfaceView.this);
            wallPaper  = new TextureRect(SingleIDCardSurfaceView.this);
            mExplosion = new Explosion(SingleIDCardSurfaceView.this);

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            initTexture();
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        public void onSurfaceChanged(GL10 gl10, int width, int height){
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0, 0, 5, 0f, 0f, 0f, 0.0f, -1.0f, 1.0f);
        }

    }

    public void initTexture()//textureId
    {
        int[] textures = new int[3];
        GLES20.glGenTextures
                (
                        3,          //产生纹理的id数量
                        textures,   //纹理id数组
                        0           //偏移量
                );
        textureIds[0] =textures[0];

        //Bitmap bmp = Bitmap.createBitmap(1200, 600, Bitmap.Config.ARGB_8888);
        Bitmap front = readBitmap(R.drawable.card_front);
        /*
        String name = "Doge";
        String gender = "雄";
        String position = "娱乐明星";
        String contact = "与Kabosu酱一起散步";


        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        Rect rect = new Rect(0,0, 1250, 600);
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(front, null, rect, paint);

        Rect blank_for_info = new Rect(120, 300, 700, 700);
        Rect blank_src = new Rect(500, 0, 800, 400);
        canvas.drawBitmap(front, blank_src, blank_for_info, paint);
        paint.setTextSize(60);
        paint.setColor(Color.BLACK);
        canvas.drawText(name, 150, 400, paint);
        paint.setTextSize(30);
        canvas.drawText(gender, 150, 460, paint);
        canvas.drawText(position, 150, 520, paint);
        canvas.drawText(contact, 150, 580, paint);

        canvas.drawBitmap(bmp, null, rect, paint);
        */
        //加载纹理进入显存
        bindTexture(0, front);
        front.recycle();
        front = null;
        //bmp.recycle(); 		  //纹理加载成功后释放内存中的纹理图
        //bmp = null;

        textureIds[1] = textures[1];
        Bitmap wall = readBitmap(R.drawable.card_back);
        bindTexture(1, wall);
        wall.recycle(); 		  //纹理加载成功后释放内存中的纹理图
        wall = null;

        textureIds[2] = textures[2];
        Bitmap wallpaper = readBitmap(R.drawable.starrysky);
        bindTexture(2, wallpaper);
        wallpaper.recycle();
        wallpaper = null;
    }

    private Bitmap readBitmap(int id){
        InputStream is = getResources().openRawResource(id);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeStream(is, null, opt);
        return bmp;

    }

    private void bindTexture(int pos, Bitmap bmp){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[pos]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型
                        0,                      //纹理层次，0代表直接贴图
                        bmp,              //纹理图像
                        0                      //纹理边框尺寸
                );
    }

    public void setStartExplosion(){
        this.mRenderer.mExplosion.explode = true;
        this.mRenderer.mExplosion.centerX = this.mRenderer.texRect.centerX;
        this.mRenderer.mExplosion.centerY = this.mRenderer.texRect.centerY;
        this.mRenderer.texRect.deleted = true;
    }
}
