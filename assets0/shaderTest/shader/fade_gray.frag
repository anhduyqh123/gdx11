#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float f_grayness;

void main() {
  vec4 c = v_color * texture2D(u_texture, v_texCoords);
  float grey = (c.r + c.g + c.b) / 3.0;
  vec3 blendedColor = mix(c.rgb, vec3(grey), f_grayness);
  gl_FragColor = vec4(blendedColor.rgb, c.a);
}