package opengl.glexamples.shape;

/**
 * Created by LLLLLyj on 2015/12/27.
 */
public class Explosion {
    final int NUM_PARTICLES = 180;
    float   eRadius;
    float   eVelocity;
    float   eDecay;
    float   eSize;
    float[] eColor;

    float[] gravity;
    float   life;
    float   time;

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

    private void updateLifeCycle(){

    }
}
