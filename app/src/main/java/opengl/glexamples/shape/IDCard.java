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
    FloatBuffer mVertexBuffer;
    FloatBuffer mTextCoorBuffer;

    public float xAngle=0;
    public float yAngle=0;
    public float zAngle=0;
    public float scale =1;

    public IDCard(IDCardSurfaceView sv){
        initShader(sv);
        initVertexData();
    }


    public void initVertexData(){
        vCount = 36;
        final float UNIT = 0.05f;
        float[] vertices = new float[]{
                /**
                * Front
                */
                -40*UNIT, 20*UNIT, UNIT,
                40*UNIT, 20*UNIT, UNIT,
                40*UNIT, -20*UNIT, UNIT,
                40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, 20*UNIT, UNIT,
                /**
                * Back
                */
                -40*UNIT, 20*UNIT, -UNIT,
                40*UNIT, 20*UNIT, -UNIT,
                40*UNIT, -20*UNIT, -UNIT,
                40*UNIT, -20*UNIT, -UNIT,
                -40*UNIT, -20*UNIT, -UNIT,
                -40*UNIT, 20*UNIT, -UNIT,
                /**
                 * Left
                 */
                -40*UNIT, 20*UNIT, UNIT,
                -40*UNIT, 20*UNIT, -UNIT,
                -40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, -20*UNIT, UNIT,
                -40*UNIT, -20*UNIT, -UNIT,
                -40*UNIT, 20*UNIT, -UNIT,
                /**
                 * Right
                 */
                40*UNIT, 20*UNIT, UNIT,
                40*UNIT, 20*UNIT, -UNIT,
                40*UNIT, -20*UNIT, UNIT,
                40*UNIT, -20*UNIT, UNIT,
                40*UNIT, -20*UNIT, -UNIT,
                40*UNIT, 20*UNIT, -UNIT,
                /**
                 * Up
                 */
                40*UNIT, 20*UNIT, UNIT,
                40*UNIT, 20*UNIT, -UNIT,
                -40*UNIT, 20*UNIT, UNIT,
                -40*UNIT, 20*UNIT, UNIT,
                -40*UNIT, 20*UNIT, -UNIT,
                40*UNIT, 20*UNIT, -UNIT,
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

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float[] textCoor = new float[]{
                /**
                 * Front
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
                /**
                 * Back
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
                /**
                 * Left
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
                /**
                 * Right
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
                /**
                 * Up
                 */
                0,0,
                1,0,
                1,1,
                1,1,
                0,1,
                0,0,
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
        ByteBuffer cbb = ByteBuffer.allocateDirect(textCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTextCoorBuffer = cbb.asFloatBuffer();
        mTextCoorBuffer.put(textCoor);
        mTextCoorBuffer.position(0);
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

    public void drawSelf(int texId) {
        GLES20.glUseProgram(mProgram);

        MatrixState.setInitStack();

        MatrixState.translate(0, 0, 1);

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
                        mVertexBuffer
                );
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTextCoorBuffer
                );
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
