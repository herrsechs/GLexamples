package opengl.glexamples.shape;

import android.opengl.GLES20;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.glUtil.ShaderUtil;
import opengl.glexamples.surfaceView.IDCardSurfaceView;
import opengl.glexamples.surfaceView.TriTextureSurfView;

import static opengl.glexamples.glUtil.ShaderUtil.createProgram;

/**
 * Created by LLLLLyj on 2015/10/18.
 */
public class IDCard {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    int muTransX;
    int muTransY;
    int muTransZ;
    String mVertexShader;
    String mFragmentShader;

    int vCount;
    FloatBuffer[] mVertexBuffer;
    FloatBuffer[] mTextCoorBuffer;

    public float xAngle =0;
    public float yAngle =0;
    public float zAngle =0;
    public float xShift =0;
    public float yShift =0;
    public float zShift =0;
    public float scale  =1;
    //The coordinate of ID card's center
    public float centerX=0;
    public float centerY=0;
    public float centerZ=0;

    private float originX=0;
    private float originY=0;
    private float originZ=0;

    public boolean deleted = false;
    /**
     * If IDCard is in the center of screen, it's highlighted
     */
    private boolean highlighted = false;


    public IDCard(SurfaceView sv){
        initShader(sv);
        initVertexData(0, 0, 0);

    }

    public IDCard(SurfaceView sv, int initX, int initY, int initZ){
        initShader(sv);
        initVertexData(initX, initY, initZ);
    }

    final float UNIT = 0.05f;

    public void initVertexData(int initX, int initY, int initZ){
        float[] left_up_front_v    = new float[]{ 40 * UNIT, -20 * UNIT,  UNIT};
        float[] right_up_front_v   = new float[]{-40 * UNIT, -20 * UNIT,  UNIT};
        float[] right_down_front_v = new float[]{-40 * UNIT,  20 * UNIT,  UNIT};
        float[] left_down_front_v  = new float[]{ 40 * UNIT,  20 * UNIT,  UNIT};
        float[] left_up_back_v     = new float[]{ 40 * UNIT, -20 * UNIT, -UNIT};
        float[] right_up_back_v    = new float[]{-40 * UNIT, -20 * UNIT, -UNIT};
        float[] right_down_back_v  = new float[]{-40 * UNIT,  20 * UNIT, -UNIT};
        float[] left_down_back_v   = new float[]{ 40 * UNIT,  20 * UNIT, -UNIT};

        float[] vertices1 = new float[]{
                /**
                 * Front
                 */
                left_up_front_v[0],    left_up_front_v[1],    left_up_front_v[2],
                right_up_front_v[0],   right_up_front_v[1],   right_up_front_v[2],
                right_down_front_v[0], right_down_front_v[1], right_down_front_v[2],
                right_down_front_v[0], right_down_front_v[1], right_down_front_v[2],
                left_down_front_v[0],  left_down_front_v[1],  left_down_front_v[2],
                left_up_front_v[0],    left_up_front_v[1],    left_up_front_v[2],
        };
        float[] vertices2 = new float[]{
                /**
                 * Back
                 */
                left_up_back_v[0],     left_up_back_v[1],     left_up_back_v[2],
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
                right_down_back_v[0],  right_down_back_v[1],  right_down_back_v[2],
                right_down_back_v[0],  right_down_back_v[1],  right_down_back_v[2],
                left_down_back_v[0],   left_down_back_v[1],   left_down_back_v[2],
                left_up_back_v[0],     left_up_back_v[1],     left_up_back_v[2],
        };
        float[] vertices3 = new float[]{
                /**
                 * Left
                 */
                left_up_front_v[0],    left_up_front_v[1],    left_up_front_v[2],
                left_up_back_v[0],     left_up_back_v[1],     left_up_back_v[2],
                left_down_front_v[0],  left_down_front_v[1],  left_down_front_v[2],
                left_down_front_v[0],  left_down_front_v[1],  left_down_front_v[2],
                left_down_back_v[0],   left_down_back_v[1],   left_down_back_v[2],
                left_up_back_v[0],     left_up_back_v[1],     left_up_back_v[2],
        };
        float[] vertices4 = new float[]{
                /**
                 * Right
                 */
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
                right_down_front_v[0], right_down_front_v[1], right_down_front_v[2],
                right_down_front_v[0], right_down_front_v[1], right_down_front_v[2],
                right_down_back_v[0],  right_down_back_v[1],  right_down_back_v[2],
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
        };
        float[] vertices5 = new float[]{
                /**
                 * Up
                 */
                right_up_front_v[0],   right_up_front_v[1],   right_up_front_v[2],
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
                left_up_front_v[0],    left_up_front_v[1],    left_up_front_v[2],
                left_up_front_v[0],    left_up_front_v[1],    left_up_front_v[2],
                left_up_back_v[0],     left_up_back_v[1],     left_up_back_v[2],
                right_up_back_v[0],    right_up_back_v[1],    right_up_back_v[2],
        };
        float[] vertices6 = new float[]{
                /**
                 * Down
                 */
                right_down_front_v[0], right_down_front_v[1], right_down_front_v[2],
                right_down_back_v[0],  right_down_back_v[1],  right_down_back_v[2],
                left_down_front_v[0],  left_down_front_v[1],  left_down_front_v[2],
                left_down_front_v[0],  left_down_front_v[1],  left_down_front_v[2],
                left_down_back_v[0],   left_down_back_v[1],   left_down_back_v[2],
                right_down_back_v[0],  right_down_back_v[1],  right_down_back_v[2],
        };

        vCount = 36;

        mVertexBuffer   = new FloatBuffer[6];
        mTextCoorBuffer = new FloatBuffer[6];

        this.originX = (float)0.5 * (left_up_front_v[2] + UNIT*initZ*2 + left_up_back_v[2])    + this.zShift;
        this.originY = (float)0.5 * (left_up_front_v[1] + UNIT*initY*2 + right_up_front_v[1])  + this.yShift;
        this.originZ = (float)0.5 * (left_up_front_v[0] + UNIT*initX*2 + left_down_front_v[0]) + this.xShift;

        for(int i = 0; i < 6; i++){
            vertices1[i*3]   += UNIT * initX;
            vertices1[i*3+1] += UNIT * initY;
            vertices1[i*3+2] += UNIT * initZ;
            vertices2[i*3]   += UNIT * initX;
            vertices2[i*3+1] += UNIT * initY;
            vertices2[i*3+2] += UNIT * initZ;
            vertices3[i*3]   += UNIT * initX;
            vertices3[i*3+1] += UNIT * initY;
            vertices3[i*3+2] += UNIT * initZ;
            vertices4[i*3]   += UNIT * initX;
            vertices4[i*3+1] += UNIT * initY;
            vertices4[i*3+2] += UNIT * initZ;
            vertices5[i*3]   += UNIT * initX;
            vertices5[i*3+1] += UNIT * initY;
            vertices5[i*3+2] += UNIT * initZ;
            vertices6[i*3]   += UNIT * initX;
            vertices6[i*3+1] += UNIT * initY;
            vertices6[i*3+2] += UNIT * initZ;
        }

        loadVertexBuffer(0, vertices1);
        loadVertexBuffer(1, vertices2);
        loadVertexBuffer(2, vertices3);
        loadVertexBuffer(3, vertices4);
        loadVertexBuffer(4, vertices5);
        loadVertexBuffer(5, vertices6);

        float[] textCoor1 = new float[]{
                /**
                 * Front
                 */
                0, 0,
                1, 0,
                1, 1,
                1, 1,
                0, 1,
                0, 0,
        };
        float[] textCoor2 = new float[]{
                /**
                 * Back
                 */
                1, 1,
                0, 1,
                0, 0,
                0, 0,
                1, 0,
                1, 1,
        };
        float[] textCoor3 = new float[]{
                /**
                 * Left
                 */
                0, 0,
                1, 0,
                1, 1,
                1, 1,
                0, 1,
                0, 0,
        };
        float[] textCoor4 = new float[]{
                /**
                 * Right
                 */
                0, 0,
                1, 0,
                1, 1,
                1, 1,
                0, 1,
                0, 0,
        };
        float[] textCoor5 = new float[]{
                /**
                 * Up
                 */
                0, 0,
                1, 0,
                1, 1,
                1, 1,
                0, 1,
                0, 0,
        };
        float[] textCoor6 = new float[]{
                /**
                 * Down
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
        };

        loadTexBuffer(0, textCoor1);
        loadTexBuffer(1, textCoor2);
        loadTexBuffer(2, textCoor3);
        loadTexBuffer(3, textCoor4);
        loadTexBuffer(4, textCoor5);
        loadTexBuffer(5, textCoor6);
    }

    public void loadVertexBuffer(int pos, float[] vertices){
        if(mVertexBuffer[pos] != null){
            mVertexBuffer[pos].clear();
            mVertexBuffer[pos] = null;
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer[pos] = vbb.asFloatBuffer();
        mVertexBuffer[pos].put(vertices);
        mVertexBuffer[pos].position(0);
    }

    public void loadTexBuffer(int pos, float[] vertices){
        ByteBuffer cbb = ByteBuffer.allocateDirect(vertices.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTextCoorBuffer[pos] = cbb.asFloatBuffer();
        mTextCoorBuffer[pos].put(vertices);
        mTextCoorBuffer[pos].position(0);
    }

    public void initShader(SurfaceView mv) {
        mVertexShader     = ShaderUtil.loadFromAssetsFile("tri_texture_vertex.sh", mv.getResources());
        mFragmentShader   = ShaderUtil.loadFromAssetsFile("tri_texture_frag.sh", mv.getResources());
        mProgram          = createProgram(mVertexShader, mFragmentShader);
        maPositionHandle  = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle   = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muTransX          = GLES20.glGetUniformLocation(mProgram, "uTransX");
        muTransY          = GLES20.glGetUniformLocation(mProgram, "uTransY");
        muTransZ          = GLES20.glGetUniformLocation(mProgram, "uTransZ");
    }

    public void drawFace(int pos, int texId){
        GLES20.glUseProgram(mProgram);

        MatrixState.setInitStack();

        this.centerX = this.xShift + this.originX;
        this.centerY = this.yShift + this.originY;
        this.centerZ = this.zShift + this.originZ;


        if(this.centerY < -0.75 && this.centerY > -1.25 && !this.highlighted) {
            this.highlighted = true;
            this.zShift += 30 * UNIT;
        }
        else if(this.centerY > -0.5 || this.centerY < -1.5){
            if(this.highlighted)
                this.zShift -= 30 *UNIT;
            this.highlighted = false;

        }

        if(this.deleted){
            this.zShift -= 0.05 * UNIT;
        }

        //this.translate(xShift, yShift, zShift);

        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.scale(scale, scale, scale);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniform1f(muTransX, this.xShift);
        GLES20.glUniform1f(muTransY, this.yShift);
        GLES20.glUniform1f(muTransZ, this.zShift);
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer[pos]
                );
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTextCoorBuffer[pos]
                );

        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount/6);

    }

    public void drawSelf(int[] texIds) {
        for(int i = 0; i < 6; i++){
            drawFace(i, texIds[i]);
        }
    }

}
