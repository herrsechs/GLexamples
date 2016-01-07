// Attributes
attribute float     a_pID;
attribute float     a_pRadiusOffset;
attribute float     a_pSizeOffset;
attribute vec3      a_pColorOffset;

uniform mat4        u_ProjectionMatrix;
uniform float       u_Time;
uniform float       u_eRadius;
uniform float       u_eSize;
uniform int         u_start;
uniform float       u_alpha;
// Varying
varying vec3        v_pColorOffset;

void main(void){
    float r = u_eRadius * a_pRadiusOffset;
    r = r * ( u_Time + 1.0);
    float x = r * cos( u_Time * 6.28);
    r = x * ( u_Time + 1.0);
    x = r * cos( u_Time * 6.28);
    float y = r * sin( u_Time * 6.28);

    float time = r * u_Time;
    time = mod(time, 10.0);


    if(u_start == 1){
        gl_Position = u_ProjectionMatrix * vec4(x, y, time, u_alpha);
    }
    else{
        gl_Position = u_ProjectionMatrix * vec4(x, y, time, 0.0);
    }
    gl_PointSize = max(0.0, (u_eSize + a_pSizeOffset));

    // Fragment Shader outputs
    v_pColorOffset = a_pColorOffset;

}