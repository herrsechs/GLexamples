package opengl.glexamples.shape;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.view.SurfaceView;

import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.glUtil.ShaderUtil;

import static opengl.glexamples.glUtil.ShaderUtil.createProgram;
//�������
public class TextureRect 
{	
	int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    String mVertexShader;
    String mFragmentShader;
	static final float UNIT_SIZE = 2f;
	FloatBuffer   mVertexBuffer;
	FloatBuffer   mTexCoorBuffer;
    int vCount=0;   
    
    public TextureRect(SurfaceView mv)
    {    	
    	initVertexData();
    	initShader(mv);
    }
    
    public void initVertexData()
    {
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-UNIT_SIZE*2.5f,UNIT_SIZE*5,-2,
        	-UNIT_SIZE*2.5f,-UNIT_SIZE*5,-2,
        	UNIT_SIZE*2.5f,-UNIT_SIZE*5,-2,
        	  
        	UNIT_SIZE*2.5f,-UNIT_SIZE*5,-2,
        	UNIT_SIZE*2.5f,UNIT_SIZE*5,-2,
        	-UNIT_SIZE*2.5f,UNIT_SIZE*5,-2
        };
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        float texCoor[]=new float[]
        {
        		1,0, 1,1, 0,1,
        		0,1, 0,0, 1,0        		
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);
    }


    public void initShader(SurfaceView mv)
    {

        mVertexShader= ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());

        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  

        mProgram = createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");

        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf(int texId)
    {        
    	 GLES20.glUseProgram(mProgram);
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         GLES20.glVertexAttribPointer
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         GLES20.glVertexAttribPointer
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         GLES20.glEnableVertexAttribArray(maPositionHandle);
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}