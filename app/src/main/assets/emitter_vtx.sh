// Attributes
attribute float     a_pID;
attribute float     a_pRadiusOffset;
attribute float     a_pVelocityOffset;
attribute float     a_pDecayOffset;
attribute float     a_pSizeOffset;
attribute vec3      a_pColorOffset;

// Uniforms
uniform mat4        u_ProjectionMatrix;
uniform vec2        u_Gravity;
uniform float       u_Time;
uniform float       u_eRadius;
uniform float       u_eVelocity;
uniform float       u_eDecay;
uniform float       u_eSize;
uniform int         u_start;
uniform float       u_centerX;
uniform float       u_centerY;
// Varying
varying vec3        v_pColorOffset;

void main(void)
{
    // 1
    // Convert polar angle to cartesian coordinates and calculate radius
    float x = cos(a_pID) + u_centerX;
    float y = sin(a_pID) + u_centerY;
    float r = u_eRadius * a_pRadiusOffset;

    // 2
    // Lifetime
    float growth = r / (u_eVelocity + a_pVelocityOffset);
    float decay = u_eDecay + a_pDecayOffset;

    // 3
    // If blast is growing
    if(u_Time < growth)
    {
        float time = u_Time / growth;
        x = x * r * time;
        y = y * r * time;
    }

    // 4
    // Else if blast is decaying
    else
    {
        float time = (u_Time - growth) / decay;
        x = (x * r) + (u_Gravity.x * time);
        y = (y * r) + (-u_Gravity.y * time);
    }

    // 5
    // Required OpenGLES 2.0 outputs
    if(u_start == 1){
        gl_Position = u_ProjectionMatrix * vec4(x, y, 0.5, 1.0);
    }
    else{
        gl_Position = u_ProjectionMatrix * vec4(x, y, 0.5, 0.0);
    }
    gl_PointSize = max(0.0, (u_eSize + a_pSizeOffset));

    // Fragment Shader outputs
    v_pColorOffset = a_pColorOffset;
}