uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aTexCoor;
uniform   float uTransX;
uniform   float uTransY;
uniform   float uTransZ;
uniform   float uAngle;
varying vec2 vTextureCoord;
void main()     
{
   float x = aPosition.x;
   float y = aPosition.y;
   float z = aPosition.z;
   float x1 = cos(uAngle)* x + sin(uAngle) * z + uTransX;
   float y1 = y + uTransY;
   float z1 = cos(uAngle)* z - sin(uAngle) * x + uTransZ;
   gl_Position = uMVPMatrix * vec4(x1, y1, z1, 1);
   vTextureCoord = aTexCoor;
}                      