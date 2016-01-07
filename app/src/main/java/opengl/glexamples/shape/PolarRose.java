package opengl.glexamples.shape;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.view.SurfaceView;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import opengl.glexamples.R;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.glUtil.ShaderUtil;

import static opengl.glexamples.glUtil.ShaderUtil.createProgram;

public class PolarRose {
    //private Particle[] mParticles;
    private int PARTICLECOUNT = 360;
    private float k = 4;
    private float[] color;
    private Random rand = new Random(System.currentTimeMillis());
    private float _timeCurrent;
    private float _timeDirectoin;
    private float _timeMax;
    private int[] textureIds;
    /**
     * Handle obtained from shader file
     */
    int mProgram;
    int muMVPMatrixHandle;
    int maThetaHandle;
    int mUKHandle;
    int mThetasHandleIndex;  //Handle for vbo
    int mShadesHandleIndex;
    int maShade;  //Color for each particle
    int muColor;  //Color for the whole particle system
    int muTime;
    int muTexture;
    String mVertexShader;
    String mFragmentShader;

    private ShortBuffer mIndexBuffer;

    public PolarRose(SurfaceView sv){
        initShader(sv);
        initTexture(sv);
        this._timeCurrent = 0f;
        this._timeDirectoin = 1f;
        this._timeMax = 3f;

        float[] thetas = new float[PARTICLECOUNT];
        float[] shades = new float[PARTICLECOUNT*3];
        FloatBuffer thetaBuffer;
        FloatBuffer shadesBuffer;
        //this.mParticles = new Particle[PARTICLECOUNT];
        this.color = new float[]{0.13f, 0.36f, 0.67f};
        for(int i = 0; i < PARTICLECOUNT; i++){
            //mParticles[i] = new Particle();

            thetas[i] = (float)(i * Math.PI / 180);
            shades[i*3]   = randomFloatBetween(-0.25f, 0.25f);
            shades[i*3+1] = randomFloatBetween(-0.25f, 0.25f);
            shades[i*3+2] = randomFloatBetween(-0.25f, 0.25f);
        }

        thetaBuffer = makeFloatBuffer(thetas);
        shadesBuffer = makeFloatBuffer(shades);
        /**
         * Load data into GPU and obtain the ref
         */
        final int buffers[] = new int[2];
        GLES20.glGenBuffers(2, buffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, thetaBuffer.capacity() * 4,
                thetaBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, shadesBuffer.capacity() * 4,
                shadesBuffer, GLES20.GL_STATIC_DRAW);

        this.mThetasHandleIndex = buffers[0];
        this.mShadesHandleIndex = buffers[1];

    }

    private void initShader(SurfaceView mv){
        mVertexShader= ShaderUtil.loadFromAssetsFile("particle_vertex.sh", mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("particle_frag.sh", mv.getResources());
        mProgram = createProgram(mVertexShader, mFragmentShader);
        maThetaHandle = GLES20.glGetAttribLocation(mProgram, "aTheta");
        mUKHandle = GLES20.glGetUniformLocation(mProgram, "uk");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muColor = GLES20.glGetUniformLocation(mProgram, "uColor");
        maShade = GLES20.glGetAttribLocation(mProgram, "aShade");
        muTime = GLES20.glGetUniformLocation(mProgram, "uTime");
        muTexture = GLES20.glGetUniformLocation(mProgram, "uTexture");
    }

    private FloatBuffer makeFloatBuffer(float[] arr){
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    private ShortBuffer makeShortBuffer(short[] arr){
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void draw(){
        GLES20.glUseProgram(mProgram);
        MatrixState.setInitStack();

        /**
         * Update time
         */
        /*
        if(this._timeCurrent > this._timeMax)
            this._timeDirectoin = -1;
        else if(this._timeCurrent < 0)
            this._timeDirectoin = 1;
        */
        _timeCurrent += _timeDirectoin * 0.05;

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniform1f(mUKHandle, this.k);
        GLES20.glUniform1f(muTime, (_timeCurrent / _timeMax));
        GLES20.glUniform3f(muColor, this.color[0], this.color[1], this.color[2]);
        GLES20.glUniform1i(muTexture, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.mThetasHandleIndex);
        GLES20.glEnableVertexAttribArray(this.maThetaHandle);
        GLES20.glVertexAttribPointer(this.maThetaHandle,     // Handle of attribute variable in .sh
                1,                      // Data size
                GLES20.GL_FLOAT,
                false,
                0,
                0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.mShadesHandleIndex);
        GLES20.glEnableVertexAttribArray(this.maShade);
        GLES20.glVertexAttribPointer(this.maShade,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                0);



        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, PARTICLECOUNT);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDisableVertexAttribArray(this.maThetaHandle);
        GLES20.glDisableVertexAttribArray(this.maShade);
    }

    private float randomFloatBetween(float min, float max){
        float range = max - min;

        return rand.nextFloat() * range + min;
    }

    private void initTexture(SurfaceView sv){
        this.textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        Bitmap snow = BitmapFactory.decodeResource(sv.getResources(), R.drawable.snow);
        bindTexture(0, snow);
        snow.recycle();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
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
