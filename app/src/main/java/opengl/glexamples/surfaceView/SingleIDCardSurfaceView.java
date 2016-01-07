package opengl.glexamples.surfaceView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.UserEntity;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.shape.Explosion;
import opengl.glexamples.shape.IDCard;
import opengl.glexamples.shape.Interstella;
import opengl.glexamples.shape.TextureRect;


public class SingleIDCardSurfaceView extends GLSurfaceView{
    private int[] textureIds;
    private SceneRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private float mPreviousDist;
    private Context mContext;
    public  int   skinID;     // 0 for default, 1 for christmas, 2 for green, 3 for yellow
    int[] ID_card_tex_ids = new int[6];
    int   wallpaper_tex;
    private UserEntity mUser;

    public SingleIDCardSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        textureIds = new int[12];
        this.skinID = 0;      // default skin

        SharedPreferences sp = context.getSharedPreferences("card_data", Context.MODE_WORLD_READABLE);
        this.skinID = sp.getInt("skinID", 0) + 1;

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

            this.mRenderer.texRect.yAngle -= dx * Math.PI / 180;

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
        Interstella interstella;

        public void onDrawFrame(GL10 gl10){

            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            wallPaper.drawSelf(wallpaper_tex);
            texRect.drawSelf(ID_card_tex_ids);
            mExplosion.draw();

        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig config){
            GLES20.glClearColor(0.52f,0.73f,0.91f, 1.0f);

            texRect    = new IDCard(SingleIDCardSurfaceView.this);
            wallPaper  = new TextureRect(SingleIDCardSurfaceView.this);
            mExplosion = new Explosion(SingleIDCardSurfaceView.this);
            interstella= new Interstella(SingleIDCardSurfaceView.this);
            interstella.setAlpha(1f);


            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            initTexture();
            GLES20.glDisable(GLES20.GL_CULL_FACE);
            ID_card_tex_ids = new int[]{
                    textureIds[0], textureIds[1],
                    textureIds[1], textureIds[1],
                    textureIds[1], textureIds[1]
            };
            wallpaper_tex = textureIds[2];
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
        int[] textures = new int[12];
        GLES20.glGenTextures
                (
                        12,          //产生纹理的id数量
                        textures,   //纹理id数组
                        0           //偏移量
                );

        //Bitmap bmp = Bitmap.createBitmap(1200, 600, Bitmap.Config.ARGB_8888);

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

        int front_tex_id = 0;
        int back_tex_id = 0;
        int background_tex_id = 0;
        switch(skinID){
            case 0:
                front_tex_id      = R.drawable.card_front;
                back_tex_id       = R.drawable.card_back;
                background_tex_id = R.drawable.starrysky;
                card_text_pos_y = new int[]{450, 510, 570, 630};
                card_text_pos_x = new int[]{150, 150, 150, 150};
                card_text_size  = 30;
                card_text_color = Color.BLACK;
                break;
            case 1:
                front_tex_id      = R.drawable.front_christmas;
                back_tex_id       = R.drawable.back_christmas;
                background_tex_id = R.drawable.background_christmas;
                card_text_pos_y = new int[]{450, 510, 570, 630};
                card_text_pos_x = new int[]{150, 150, 150, 150};
                card_text_size  = 30;
                card_text_color = Color.WHITE;
                break;
            case 2:
                front_tex_id      = R.drawable.front_green;
                back_tex_id       = R.drawable.back_green;
                background_tex_id = R.drawable.background_green;
                card_text_pos_y = new int[]{450, 510, 570, 630};
                card_text_pos_x = new int[]{150, 150, 150, 150};
                card_text_size  = 30;
                card_text_color = Color.GREEN;
                break;
            case 3:
                front_tex_id      = R.drawable.front_yellow;
                back_tex_id       = R.drawable.back_yellow;
                background_tex_id = R.drawable.background_yellow;
                card_text_pos_y = new int[]{450, 510, 570, 630};
                card_text_pos_x = new int[]{700, 750, 700, 650};
                card_text_size  = 30;
                card_text_color = Color.WHITE;
                break;
            default:
                front_tex_id      = R.drawable.card_front;
                back_tex_id       = R.drawable.card_back;
                background_tex_id = R.drawable.starrysky;
                card_text_pos_y = new int[]{450, 510, 570, 630};
                card_text_pos_x = new int[]{150, 150, 150, 150};
                card_text_size  = 30;
                card_text_color = Color.BLACK;
                break;
        }

        textureIds[0] =textures[0];
        Bitmap front = readBitmap(front_tex_id);
        Bitmap textFront = editIdText(front);
        bindTexture(0, textFront);
        textFront.recycle();
        front.recycle();
        //bmp.recycle(); 		  //纹理加载成功后释放内存中的纹理图
        //bmp = null;

        textureIds[1] = textures[1];
        Bitmap back = readBitmap(back_tex_id);
        bindTexture(1, back);
        back.recycle(); 		  //纹理加载成功后释放内存中的纹理图

        textureIds[2] = textures[2];
        Bitmap wallpaper = readBitmap(background_tex_id);
        bindTexture(2, wallpaper);
        wallpaper.recycle();


    }

    int[] card_text_pos_y;
    int[] card_text_pos_x;
    int   card_text_size;
    int   card_text_color;

    private Bitmap editIdText(Bitmap b){
        Bitmap bmp = Bitmap.createBitmap(1350, 800, Bitmap.Config.ARGB_8888);

        String name = mUser.getName();
        String phone = mUser.getPhone();
        String email = mUser.getEmail();
        String address = mUser.getCategory();

        if(null == name)
            name = "";
        if(null == phone)
            phone = "";
        if(null == email)
            email = "";


        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        canvas.drawBitmap(b, 0, 0, paint);

        paint.setTextSize(card_text_size + 15);
        paint.setColor(card_text_color);
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawText(name, card_text_pos_x[0], card_text_pos_y[0], paint);
        paint.setTextSize(card_text_size);
        canvas.drawText(phone, card_text_pos_x[0], card_text_pos_y[1], paint);
        canvas.drawText(email, card_text_pos_x[0], card_text_pos_y[2], paint);

        return bmp;
    }

    private Bitmap readBitmap(int id){
        InputStream is = getResources().openRawResource(id);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 2;
        return BitmapFactory.decodeStream(is, null, opt);
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

    public void setStartInterstella(){
        this.mRenderer.interstella.explode = true;
    }

    public void resetCardPosition(){
        this.mRenderer.texRect.xShift = 0;
        this.mRenderer.texRect.yShift = 0;
        this.mRenderer.texRect.zShift = 0;
        this.mRenderer.texRect.yAngle = 0;
    }

    public void setUser(UserEntity u){
        this.mUser = u;
    }
}
