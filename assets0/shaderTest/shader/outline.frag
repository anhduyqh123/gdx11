varying vec4 v_color; 
varying vec2 v_texCoords;

uniform sampler2D u_texture;

uniform vec2  v2_imageSize;
uniform vec4  cl_borderColor; 
uniform float f_borderSize;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    vec2 pixelToTextureCoords = 1. / v2_imageSize;
    bool isInteriorPoint = true;
    bool isExteriorPoint = true;

	for (float dx = -f_borderSize; dx < f_borderSize; dx++)     
	{
	    for (float dy = -f_borderSize; dy < f_borderSize; dy++){
	        vec2 point = v_texCoords + vec2(dx,dy) * pixelToTextureCoords;

	        // range check
	        if (point.x < 0.0 || point.x > 1.0 || point.y < 0.0 || point.y > 1.0)
	            continue;

	        float alpha = texture2D(u_texture, point).a;
	        if ( alpha < 0.5 )
	            isInteriorPoint = false;
	        if ( alpha > 0.5 )
	            isExteriorPoint = false;
	    }
	}

    if (!isInteriorPoint && !isExteriorPoint && color.a < 0.5)
        gl_FragColor = cl_borderColor;    
        else
        gl_FragColor = v_color * color;
}