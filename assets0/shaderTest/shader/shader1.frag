//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 v_texCoords;//uv

uniform vec2 size;

const vec2 mask_size = vec2(333.0,333.0);
const vec2 mask_pos = vec2(50.0,100.0);
//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D i_mask;

vec2 UV()
{
  vec2 uv = v_texCoords;
  uv.y = 1.0-uv.y;
  return uv;
}
vec2 uv = UV();


float Point(float alpha)
{
  vec2 point = uv*size;
  if (point.x<mask_pos.x || point.x>mask_pos.x+mask_size.x) return alpha;
  if (point.y<mask_pos.y || point.y>mask_pos.y+mask_size.y) return alpha;
  vec2 mask_uv = (point-mask_pos)/mask_size;
  return 1.0-texture2D(i_mask, mask_uv).a;
}

void main() {
  vec4 color = texture2D(u_texture, uv);
  color.a = Point(color.a);
  gl_FragColor = color;
}