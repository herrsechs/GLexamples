package opengl.glexamples.shape;

import java.util.Random;

/**
 * Created by LLLLLyj on 2015/12/26.
 */
public class Particle {
    public float   x;
    public float   y;
    public float   z;
    public float   theta;
    public float[] shade;
    public float   pID;
    public float   pRadiusOffset;
    public float   pVelocityOffset;
    public float   pDecayOffset;
    public float   pSizeOffset;
    public float[] pColorOffset;


    public Particle(){
        Random gen = new Random(System.currentTimeMillis());
        this.x = gen.nextFloat();
        this.y = gen.nextFloat();
        this.z = gen.nextFloat();

        this.shade = new float[3];
    }
}
