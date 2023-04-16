//"in" attributes from our vertex shader
varying vec2 v_texCoords;//uv

uniform vec2 resolution;
//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D i_mask;

uniform vec4 v4_mask;//bound mask (x,y,w,h) x,y in u_texture

vec2 GetPoint()
{
  return v_texCoords*resolution;
}
vec4 GetTxtBound(vec4 bound)
{
  bound.y = resolution.y-bound.w-bound.y;
  return bound;
}
vec4 GetColor(sampler2D txt,vec4 bound,vec4 defaultColor)
{
  vec2 point = GetPoint();
  if (point.x<bound.x || point.x>bound.x+bound.z) return defaultColor;
  if (point.y<bound.y || point.y>bound.y+bound.w) return defaultColor;
  vec2 txt_uv = (point-bound.xy)/bound.zw;
  return texture2D(txt, txt_uv);
}

void main() {
  vec4 color = vec4(1,1,1,0);
  vec4 maskColor = GetColor(i_mask, GetTxtBound(v4_mask), color);
  vec4 txtColor = texture2D(u_texture,v_texCoords);
  txtColor.a*=maskColor.a;

  gl_FragColor = txtColor;
}