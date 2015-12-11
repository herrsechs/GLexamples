package opengl.glexamples.glUtil;

import android.opengl.Matrix;

//�洢ϵͳ����״̬����
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];//投影矩阵
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private static float[] mMVPMatrix;//总变换矩阵
    static float[] mMMatrix=new float[16] ;//当前变换矩阵
    
    static float[][] mStack = new float[10][16];
	static int stackTop = -1;
    
    public static void setInitStack()//初始化
    {
    	Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void translate(float x,float y,float z)//平移变换
    {
    	Matrix.translateM(mMMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//旋转变换
    {
    	Matrix.rotateM(mMMatrix,0,angle,x,y,z);
    }

	public static void scale(float x, float y, float z)//缩放变换
	{
		Matrix.scaleM(mMMatrix,0,x,y,z);
	}

	public static void pushMatrix() //保存变换矩阵
	{
		stackTop++;
		for(int i=0; i < 16; i++){
			mStack[stackTop][i] = mMMatrix[i];
		}
	}

	public static void popMatrix()  //恢复变换矩阵
	{
		for(int i=0; i<16; i++){
			mMMatrix[i] = mStack[stackTop][i];
		}
		stackTop--;
	}

    public static void setCamera
    (
    		float cx,
    		float cy,
    		float cz,
    		float tx,
    		float ty,
    		float tz,
    		float upx,
    		float upy,
    		float upz
    )
    {
    	Matrix.setLookAtM
        (
        		mVMatrix, 
        		0, 
        		cx,
        		cy,
        		cz,
        		tx,
        		ty,
        		tz,
        		upx,
        		upy,
        		upz
        );
    }
    
    //����͸��ͶӰ����
    public static void setProjectFrustum
    (
    	float left,		//near���left
    	float right,    //near���right
    	float bottom,   //near���bottom
    	float top,      //near���top
    	float near,		//near�����
    	float far       //far�����
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
   
    //��ȡ����������ܱ任����
    public static float[] getFinalMatrix()
    {
    	mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
}
