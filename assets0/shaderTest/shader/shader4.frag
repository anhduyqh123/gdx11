uniform sampler2D u_texture;
varying vec2 v_texCoords;

uniform vec2 v2_resolution;
uniform float u_dis;

const float radius = 0.01;
const float pi = 3.14159265359;

void main()
{
    vec2 x1 = vec2(v2_resolution.x,0);
    vec2 x2 = normalize(vec2(0,v2_resolution.y) - x1)*u_dis + x1;
    vec4 iMouse = vec4(x2.x,x2.y,x1.x,x1.y);

    //vec4 iMouse = vec4(v2_to.x,v2_to.y,v2_from.x,-v2_from.y);


    float aspect = v2_resolution.x / v2_resolution.y;

    vec2 uv = v_texCoords;
    //vec2 uv = gl_FragCoord.xy * vec2(aspect, 1.0) / v2_resolution.xy;
    vec2 mouse = iMouse.xy  * vec2(aspect, 1.0) / v2_resolution.xy;
    vec2 mouseDir = normalize(abs(iMouse.zw) - iMouse.xy);
    vec2 origin = clamp(mouse - mouseDir * mouse.x / mouseDir.x, 0.0, 1.0);
    
    float mouseDist = clamp(length(mouse - origin) 
        + (aspect - (abs(iMouse.z) / v2_resolution.x) * aspect) / mouseDir.x, 0.0, aspect / mouseDir.x);
    
    if (mouseDir.x < 0.0)
    {
        mouseDist = distance(mouse, origin);
    }
  
    float proj = dot(uv - origin, mouseDir);
    float dist = proj - mouseDist;
    
    vec2 linePoint = uv - dist * mouseDir;
    
    if (dist > radius) 
    {
        gl_FragColor = vec4(0,0,0,0);
        //gl_FragColor = texture2D(u_texture, uv * vec2(1.0 / aspect, 1.0));
        //gl_FragColor.rgb *= pow(clamp(dist - radius, 0.0, 1.0) * 1.5, 0.2);
    }
    else if (dist >= 0.0)
    {
        // map to cylinder point
        float theta = asin(dist / radius);
        vec2 p2 = linePoint + mouseDir * (pi - theta) * radius;
        vec2 p1 = linePoint + mouseDir * theta * radius;
        uv = (p2.x <= aspect && p2.y <= 1.0 && p2.x > 0.0 && p2.y > 0.0) ? p2 : p1;
        gl_FragColor = texture2D(u_texture, uv * vec2(1.0 / aspect, 1.0));
        gl_FragColor.rgb *= pow(clamp((radius - dist) / radius, 0.0, 1.0), 0.2);
    }
    else 
    {
        vec2 p = linePoint + mouseDir * (abs(dist) + pi * radius);
        uv = (p.x <= aspect && p.y <= 1.0 && p.x > 0.0 && p.y > 0.0) ? p : uv;
        gl_FragColor = texture2D(u_texture, uv * vec2(1.0 / aspect, 1.0));
    }
}