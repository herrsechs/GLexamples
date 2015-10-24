package opengl.glexamples.shape;
import static opengl.glexamples.glUtil.ShaderUtil.createProgram;
import opengl.glexamples.glUtil.ShaderUtil;
import opengl.glexamples.glUtil.MatrixState;
import opengl.glexamples.surfaceView.TriTextureSurfView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;


public class Triangle 
{	
	int mProgram;  //着色器程序的引用
    int muMVPMatrixHandle;  //全局矩阵引用
    int maPositionHandle;   //传入着色器的位置矩阵的引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    String mVertexShader; //顶点着色器程序引用
    String mFragmentShader;  //片元着色器程序引用
	
	FloatBuffer   mVertexBuffer;
	FloatBuffer   mTexCoorBuffer;
    int vCount=0;   
    public float xAngle=0;
    public float yAngle=0;
    public float zAngle=0;
    
    public Triangle(TriTextureSurfView mv)
    {    	
    	initVertexData();
    	initShader(mv);
    }
    
    public void initVertexData()
    {
        vCount=3;
        final float UNIT_SIZE=0.15f;
        float vertices[]=new float[]
        {
        	0*UNIT_SIZE,11*UNIT_SIZE,0,
        	-11*UNIT_SIZE,-11*UNIT_SIZE,0,
        	11*UNIT_SIZE,-11*UNIT_SIZE,0,
        };
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点纹理坐标初始化
        float texCoor[]=new float[]
        {
                //分别为三角形三个顶点坐标
        		0.5f,0,
        		0,1, 
        		1,1        		
        };        

        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);

    }

    public void initShader(TriTextureSurfView mv)
    {
        mVertexShader=ShaderUtil.loadFromAssetsFile("tri_texture_vertex.sh", mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("tri_texture_frag.sh", mv.getResources());
        mProgram = createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    
    public void drawSelf(int texId)
    {        
    	 GLES20.glUseProgram(mProgram);
    	 
    	 MatrixState.setInitStack();
    	 
         MatrixState.translate(0, 0, 1);
         
         MatrixState.rotate(yAngle, 0, 1, 0);
         MatrixState.rotate(zAngle, 0, 0, 1);
         MatrixState.rotate(xAngle, 1, 0, 0);
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
