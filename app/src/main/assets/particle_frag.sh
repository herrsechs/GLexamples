varying highp vec3 vShade;
uniform highp vec3 uColor;
uniform sampler2D  uTexture;
void main(){
    highp vec4 color = vec4((uColor+vShade), 1.0);
    highp vec4 texture = texture2D(uTexture, gl_PointCoord);
    color.rgb = clamp(color.rgb, vec3(0.0), vec3(1.0));
    gl_FragColor = texture * color;
}