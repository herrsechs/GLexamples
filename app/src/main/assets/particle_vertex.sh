attribute float aTheta;
attribute vec3  aShade;
varying   vec3  vShade;
uniform   mat4  uMVPMatrix;
uniform   float uk;
uniform   float uTime;
void main(){
    float t = aTheta+uTime;
    float x = cos(uk*t)*sin(t);
    float y = cos(uk*t)*cos(t);
    vShade = aShade;

    gl_Position = uMVPMatrix * vec4(x, y, 0.0, 1.0);
    gl_PointSize = 16.0;
}