package opengl.glexamples.glUtil;

import android.opengl.Matrix;

//�洢ϵͳ����״̬����
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];//4x4���� ͶӰ��
    private static float[] mVMatrix = new float[16];//�����λ�ó���9��������
    private static float[] mMVPMatrix;//��������õ��ܱ任����
    static float[] mMMatrix=new float[16] ;//����������ƶ���ת����
    
    
    
    public static void setInitStack()//��ȡ���任��ʼ����
    {
    	Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void translate(float x,float y,float z)//������xyz���ƶ�
    {
    	Matrix.translateM(mMMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//������xyz��ת��
    {
    	Matrix.rotateM(mMMatrix,0,angle,x,y,z);
    }

	public static void scale(float x, float y, float z)
	{
		Matrix.scaleM(mMMatrix,0,x,y,z);
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
