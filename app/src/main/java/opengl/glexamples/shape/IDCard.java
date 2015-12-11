package opengl.glexamples.shape;

import android.opengl.GLES20;

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
    String mVertexShader;
    String mFragmentShader;

    int vCount;
    FloatBuffer[] mVertexBuffer = new FloatBuffer[6];
    FloatBuffer[] mTextCoorBuffer = new FloatBuffer[6];

    public float xAngle=0;
    public float yAngle=0;
    public float zAngle=0;
    public float xShift=0;
    public float yShift=0;
    public float scale =1;

    public IDCard(IDCardSurfaceView sv){
        initShader(sv);
        initVertexData();
    }


    public void initVertexData(){
        vCount = 36;
        final float UNIT = 0.05f;
        float[] vertices1 = new float[]{
                /**
                 * Front
                 */
                -40 * UNIT, 20 * UNIT, UNIT,
                40 * UNIT, 20 * UNIT, UNIT,
                40 * UNIT, -20 * UNIT, UNIT,
                40 * UNIT, -20 * UNIT, UNIT,
                -40 * UNIT, -20 * UNIT, UNIT,
                -40 * UNIT, 20 * UNIT, UNIT,
        };
        float[] vertices2 = new float[]{
                /**
                 * Back
                 */
                -40 * UNIT, 20 * UNIT, -UNIT,
                40 * UNIT, 20 * UNIT, -UNIT,
                40 * UNIT, -20 * UNIT, -UNIT,
                40 * UNIT, -20 * UNIT, -UNIT,
                -40 * UNIT, -20 * UNIT, -UNIT,
                -40 * UNIT, 20 * UNIT, -UNIT,
        };
        float[] vertices3 = new float[]{
                /**
                 * Left
                 */
                -40 * UNIT, 20 * UNIT, UNIT,
                -40 * UNIT, 20 * UNIT, -UNIT,
                -40 * UNIT, -20 * UNIT, UNIT,
                -40 * UNIT, -20 * UNIT, UNIT,
                -40 * UNIT, -20 * UNIT, -UNIT,
                -40 * UNIT, 20 * UNIT, -UNIT,
        };
        float[] vertices4 = new float[]{
                /**
                 * Right
                 */
                40 * UNIT, 20 * UNIT, UNIT,
                40 * UNIT, 20 * UNIT, -UNIT,
                40 * UNIT, -20 * UNIT, UNIT,
                40 * UNIT, -20 * UNIT, UNIT,
                40 * UNIT, -20 * UNIT, -UNIT,
                40 * UNIT, 20 * UNIT, -UNIT,
        };
        float[] vertices5 = new float[]{
                /**
                 * Up
                 */
                40 * UNIT, 20 * UNIT, UNIT,
                40 * UNIT, 20 * UNIT, -UNIT,
                -40 * UNIT, 20 * UNIT, UNIT,
                -40 * UNIT, 20 * UNIT, UNIT,
                -40 * UNIT, 20 * UNIT, -UNIT,
                40 * UNIT, 20 * UNIT, -UNIT,
        };
        float[] vertices6 = new float[]{
                /**
                 * Down
                 */
                40*UNIT, -20*UNIT, UNIT,
                40*UNIT, -20*UNIT, -UNIT,
                -40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, -20*UNIT, -UNIT,
                40*UNIT, -20*UNIT, -UNIT,
        };

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
                0, 0,
                1, 0,
                1, 1,
                1, 1,
                0, 1,
                0, 0,
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

    public void initShader(IDCardSurfaceView mv)
    {
        mVertexShader= ShaderUtil.loadFromAssetsFile("tri_texture_vertex.sh", mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("tri_texture_frag.sh", mv.getResources());
        mProgram = createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawFace(int pos, int texId){
        GLES20.glUseProgram(mProgram);

        MatrixState.setInitStack();


        MatrixState.translate(xShift, yShift, 1);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.scale(scale, scale, scale);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
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
