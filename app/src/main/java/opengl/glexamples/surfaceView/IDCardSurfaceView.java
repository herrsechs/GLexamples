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
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.IDCard;
import opengl.glexamples.shape.TextureRect;
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

    int[] textureIds;

    public IDCardSurfaceView(Context context) {
        super(context);
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
                    if (dy < 5) {
                        /*
                        mRenderer.texRect.yAngle += dx * TOUCH_SCALE_FACTOR;
                        for(IDCard i : mRenderer.texRects){
                            i.yAngle += dx * TOUCH_SCALE_FACTOR;
                        }

                        mRenderer.texRect.yShift += 0.01 * dx;
                        for(IDCard i : mRenderer.texRects){
                            i.yShift += dx * 0.01;
                        }
                        */
                    }
                    if (dx < 5) {
                        /*
                        mRenderer.texRect.zAngle += -dy * TOUCH_SCALE_FACTOR;
                        for(IDCard i : mRenderer.texRects){
                            i.zAngle -= dy * TOUCH_SCALE_FACTOR;
                        }
                        */
                        mRenderer.texRect.yShift += 0.01 * dy;
                        int num = 0;
                        for(IDCard i : mRenderer.texRects){
                            num++;
                            i.yShift = dy * 0.01f;
                            //Log.d("Card" + num, "X: " + i.centerX + " Y: " + i.centerY + " Z: " + i.centerZ);

                        }
                        //Log.d("zShift", 0.01*dy + "");
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
        else if(pointerCount == 3){
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mRenderer.texRect.xShift += 0.01*dx;
            mRenderer.texRect.yShift -= 0.01*dy;

            mPreviousX = x;
            mPreviousY = y;
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
        IDCard[] texRects = new IDCard[6];
        TextureRect wallPaper;
        public void onDrawFrame(GL10 gl)
        {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            int[] ids = new int[]{textureIds[0], textureIds[1], textureIds[1], textureIds[1],
            textureIds[1], textureIds[1]};
            texRect.drawSelf(ids);

            for(IDCard i : texRects){
                i.drawSelf(ids);
            }

            wallPaper.drawSelf(textureIds[2]);

        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0, 0, 5, 0f, 0f, 0f, 0.0f, -1.0f, 1.0f);

        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);

            texRect =new IDCard(IDCardSurfaceView.this);
            for(int i = 0; i < 6; i++){
                texRects[i] = new IDCard(IDCardSurfaceView.this, 0, 50*(i+1), 0);
            }

            wallPaper = new TextureRect(IDCardSurfaceView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            initTexture();
            GLES20.glDisable(GLES20.GL_CULL_FACE);
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

        Bitmap bmp = Bitmap.createBitmap(1200, 600, Bitmap.Config.ARGB_8888);
        Bitmap front = BitmapFactory.decodeResource(getResources(), R.drawable.card_front);

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
        //加载纹理进入显存
        bindTexture(0, bmp);
        front.recycle();
        bmp.recycle(); 		  //纹理加载成功后释放内存中的纹理图

        textureIds[1] = textures[1];
        Bitmap wall = BitmapFactory.decodeResource(getResources(), R.drawable.card_back);
        bindTexture(1, wall);
        wall.recycle(); 		  //纹理加载成功后释放内存中的纹理图

        textureIds[2] = textures[2];
        Bitmap wallpaper = BitmapFactory.decodeResource(getResources(), R.drawable.starrysky);
        bindTexture(2, wallpaper);
        wallpaper.recycle();
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
}
