uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
varying vec2 vTextureCoord;
attribute vec2 aTexCoor;

void main()
{
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vTextureCoord = aTexCoor;
}