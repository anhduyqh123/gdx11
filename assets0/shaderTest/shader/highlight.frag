//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 v_texCoords;//uv

uniform vec2 size;

const float textAlpha = 0.8;
//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D i_mask;

uniform float f_maskX;
uniform float f_maskY;
uniform vec2 v2_maskSize;

vec2 UV()
{
  vec2 uv = v_texCoords;
  uv.y = 1.0-uv.y;
  return uv;
}
vec2 uv = UV();


float Alpha()
{
  vec2 mask_pos = vec2(f_maskX,f_maskY);
  vec2 point = uv*size;
  if (point.x<mask_pos.x || point.x>mask_pos.x+v2_maskSize.x) return textAlpha;
  if (point.y<mask_pos.y || point.y>mask_pos.y+v2_maskSize.y) return textAlpha;
  vec2 mask_uv = (point-mask_pos)/v2_maskSize;
  return 1.0-texture2D(i_mask, mask_uv).a;
}

void main() {
  vec4 color = vec4(0,0,0,textAlpha);
  color.a = min(Alpha(),textAlpha);
  gl_FragColor = color;
}