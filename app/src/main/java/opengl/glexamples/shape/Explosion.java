package opengl.glexamples.shape;

import android.opengl.GLES20;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.glUtil.ShaderUtil;

/**
 * Created by LLLLLyj on 2015/12/27.
 */
public class Explosion {
    final int NUM_PARTICLES = 180;
    private Random rand = new Random(System.currentTimeMillis());
    float   eRadius;
    float   eVelocity;
    float   eDecay;
    float   eSize;
    float[] eColor;

    float[] gravity;
    float   life;
    float   time;

    int mProgram;
    String mVertexShader;
    String mFragShader;

    int a_pIDIndex;
    int a_pRadiusOffsetIndex;
    int a_pVelocityOffsetIndex;
    int a_pDecayOffsetIndex;
    int a_pSizeOffsetIndex;
    int a_pColorOffsetIndex;

    /**
     * Attribute Handles
     */
    int a_pID;
    int a_pRadiusOffset;
    int a_pVelocityOffset;
    int a_pDecayOffset;
    int a_pSizeOffset;
    int a_pColorOffset;

    /**
     * Uniform Handles
     */
    int u_ProjectionMatrix;
    int u_Gravity;
    int u_Time;
    int u_eRadius;
    int u_eVelocity;
    int u_eDecay;
    int u_eSize;
    int u_eColor;

    public Explosion(SurfaceView sv){
        initShader(sv);
        loadParticleSystem();
    }

    private void initShader(SurfaceView sv){
        mVertexShader     = ShaderUtil.loadFromAssetsFile("emitter_vtx.sh", sv.getResources());
        mFragShader       = ShaderUtil.loadFromAssetsFile("emitter_frag.sh",sv.getResources());
        mProgram          = ShaderUtil.createProgram(mVertexShader, mFragShader);

        a_pID             = GLES20.glGetAttribLocation(mProgram, "a_pID");
        a_pRadiusOffset   = GLES20.glGetAttribLocation(mProgram, "a_pRadiusOffset");
        a_pVelocityOffset = GLES20.glGetAttribLocation(mProgram, "a_pVelocityOffset");
        a_pDecayOffset    = GLES20.glGetAttribLocation(mProgram, "a_pDecayOffset");
        a_pSizeOffset     = GLES20.glGetAttribLocation(mProgram, "a_pSizeOffset");
        a_pColorOffset    = GLES20.glGetAttribLocation(mProgram, "a_pColorOffset");

        u_ProjectionMatrix= GLES20.glGetUniformLocation(mProgram, "u_ProjectionMatrix");
        u_Gravity         = GLES20.glGetUniformLocation(mProgram, "u_Gravity");
        u_Time            = GLES20.glGetUniformLocation(mProgram, "u_Time");
        u_eRadius         = GLES20.glGetUniformLocation(mProgram, "u_eRadius");
        u_eVelocity       = GLES20.glGetUniformLocation(mProgram, "u_eVelocity");
        u_eDecay          = GLES20.glGetUniformLocation(mProgram, "u_eDecay");
        u_eSize           = GLES20.glGetUniformLocation(mProgram, "u_eSize");
        u_eColor          = GLES20.glGetUniformLocation(mProgram, "u_eColor");
    }

    private void updateLifeCycle(){
        this.time += 0.05;
        if(this.time > this.life)
            this.time = 0f;
    }

    private void loadParticleSystem(){
        // Offset bounds
        float oRadius   = 0.10f;      // 0.0 = circle; 1.0 = ring
        float oVelocity = 0.50f;    // Speed
        float oDecay    = 1f;       // Time
        float oSize     = 8.00f;        // Pixels
        float oColor    = 0.25f;       // 0.5 = 50% shade offset

        float[] a_pIDs             = new float[NUM_PARTICLES];
        float[] a_pRadiusOffsets   = new float[NUM_PARTICLES];
        float[] a_pVelocityOffsets = new float[NUM_PARTICLES];
        float[] a_pDecayOffsets    = new float[NUM_PARTICLES];
        float[] a_pSizeOffsets     = new float[NUM_PARTICLES];
        float[] a_pColorOffsets    = new float[NUM_PARTICLES * 3];

        for(int i = 0; i < NUM_PARTICLES; i++){
            a_pIDs[i]             = ( (float)i/(float)NUM_PARTICLES ) * 360f * (float)Math.PI / 180f;
            a_pRadiusOffsets[i]   = randomFloatBetween(oRadius, 1.0f);
            a_pVelocityOffsets[i] = randomFloatBetween(-oVelocity, oVelocity);
            a_pDecayOffsets[i]    = randomFloatBetween(-oDecay, oDecay);
            a_pSizeOffsets[i]     = randomFloatBetween(-oSize, oSize);
            a_pColorOffsets[i*3]  = randomFloatBetween(-oColor, oColor);
            a_pColorOffsets[i*3+1]= randomFloatBetween(-oColor, oColor);
            a_pColorOffsets[i*3+2]= randomFloatBetween(-oColor, oColor);
        }

        FloatBuffer IDBuffer       = makeFloatBuffer(a_pIDs);
        FloatBuffer radiusBuffer   = makeFloatBuffer(a_pRadiusOffsets);
        FloatBuffer velocityBuffer = makeFloatBuffer(a_pVelocityOffsets);
        FloatBuffer decayBuffer    = makeFloatBuffer(a_pDecayOffsets);
        FloatBuffer sizeBuffer     = makeFloatBuffer(a_pSizeOffsets);
        FloatBuffer colorBuffer    = makeFloatBuffer(a_pColorOffsets);

        final int[] buffers = new int[6];
        GLES20.glGenBuffers(6, buffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, radiusBuffer.capacity() * 4,
                radiusBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, velocityBuffer.capacity() * 4,
                velocityBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, decayBuffer.capacity() * 4,
                decayBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[3]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sizeBuffer.capacity() * 4,
                sizeBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[4]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * 4,
                colorBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[5]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, IDBuffer.capacity() * 4,
                IDBuffer, GLES20.GL_STATIC_DRAW);

        this.a_pRadiusOffsetIndex   = buffers[0];
        this.a_pVelocityOffsetIndex = buffers[1];
        this.a_pDecayOffsetIndex    = buffers[2];
        this.a_pSizeOffsetIndex     = buffers[3];
        this.a_pColorOffsetIndex    = buffers[4];
        this.a_pIDIndex             = buffers[5];

        this.eRadius   = 3f;
        this.eVelocity = 3.00f;
        this.eDecay    = 2.00f;
        this.eSize     = 32.00f;
        this.eColor    = new float[]{1f, 0.5f, 0f};

        float growth   = this.eRadius / this.eVelocity;
        this.life      = growth + eDecay + oDecay;

        float drag     = 10f;
        this.gravity   = new float[]{0f, -9.81f*(1.0f/drag)};

    }

    public void draw(){
        GLES20.glUseProgram(mProgram);
        MatrixState.setInitStack();

        this.updateLifeCycle();
        /**
         * Uniform
         */
        GLES20.glUniformMatrix4fv(this.u_ProjectionMatrix, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniform2f(this.u_Gravity, this.gravity[0], this.gravity[1]);
        GLES20.glUniform1f(this.u_Time, this.time);
        GLES20.glUniform1f(this.u_eRadius, this.eRadius);
        GLES20.glUniform1f(this.u_eVelocity, this.eVelocity);
        GLES20.glUniform1f(this.u_eDecay, this.eDecay);
        GLES20.glUniform1f(this.u_eSize, this.eSize);
        GLES20.glUniform3f(this.u_eColor, this.eColor[0], this.eColor[1], this.eColor[2]);

        /**
         * Attrib
         */

        this.bindVBO(this.a_pID, this.a_pIDIndex, 1);
        this.bindVBO(this.a_pRadiusOffset, this.a_pVelocityOffsetIndex, 1);
        this.bindVBO(this.a_pVelocityOffset, this.a_pVelocityOffsetIndex, 1);
        this.bindVBO(this.a_pDecayOffset, this.a_pDecayOffsetIndex, 1);
        this.bindVBO(this.a_pSizeOffset, this.a_pSizeOffsetIndex, 1);
        this.bindVBO(this.a_pColorOffset, this.a_pColorOffsetIndex, 3);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_PARTICLES);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDisableVertexAttribArray(this.a_pID);
        GLES20.glDisableVertexAttribArray(this.a_pRadiusOffset);
        GLES20.glDisableVertexAttribArray(this.a_pVelocityOffset);
        GLES20.glDisableVertexAttribArray(this.a_pDecayOffset);
        GLES20.glDisableVertexAttribArray(this.a_pSizeOffset);
        GLES20.glDisableVertexAttribArray(this.a_pColorOffset);
    }

    private void bindVBO(int targetHandle, int targetIndex, int gap){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, targetIndex);
        GLES20.glEnableVertexAttribArray(targetHandle);
        GLES20.glVertexAttribPointer(targetHandle,
                1 * gap,
                GLES20.GL_FLOAT,
                false,
                0,
                0);
    }

    private FloatBuffer makeFloatBuffer(float[] arr){
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    private float randomFloatBetween(float min, float max){
        float range = max - min;

        return rand.nextFloat() * range + min;
    }
}
