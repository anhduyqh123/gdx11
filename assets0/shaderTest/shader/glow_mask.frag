uniform sampler2D u_texture;
varying vec2 v_texCoords;//between uv,uv2
uniform vec2 uv,uv2;
uniform vec2 resolution;

const float textAlpha = 0.8;
//our different texture units
uniform sampler2D i_mask;
uniform vec4 v4_region_mask;//uv,u2vu2
uniform vec4 v4_bound_mask;//local position of this

vec2 UV()//0->1
{
  return (v_texCoords-uv)/(uv2-uv);
}
vec4 GetTxtBound(vec4 bound)
{
  bound.y = resolution.y-bound.w-bound.y;
  return bound;
}

float Alpha()
{
  vec2 point = UV()*resolution;
  vec4 bound_mask = GetTxtBound(v4_bound_mask);
  if (point.x< bound_mask.x || point.x> bound_mask.x+ bound_mask.z) return textAlpha;
  if (point.y< bound_mask.y || point.y> bound_mask.y+ bound_mask.w) return textAlpha;
  vec2 mask_uv = (point- bound_mask.xy)/ bound_mask.zw;
  mask_uv = mask_uv*(v4_region_mask.zw-v4_region_mask.xy)+v4_region_mask.xy;
  return 1.0-texture2D(i_mask, mask_uv).a;
}

void main() {
  vec4 color = vec4(0.0,0.0,0.0,textAlpha);
  color.a = min(Alpha(),textAlpha);
  gl_FragColor = color;
}