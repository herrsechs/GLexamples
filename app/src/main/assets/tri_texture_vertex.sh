uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aTexCoor;
uniform   float uTransX;
uniform   float uTransY;
uniform   float uTransZ;
varying vec2 vTextureCoord;
void main()     
{
   float x = aPosition.x + uTransX;
   float y = aPosition.y + uTransY;
   float z = aPosition.z + uTransZ;
   gl_Position = uMVPMatrix * vec4(x,y,z,1);
   vTextureCoord = aTexCoor;
}                      