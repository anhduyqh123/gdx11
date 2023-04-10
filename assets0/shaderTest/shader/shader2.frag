  // this is the resolution of the window
  uniform vec2 v2_resolution;

  // this is a count in seconds.
  uniform float u_time;

  void main() {
      // gl_FragCoord is the position of the pixel being drawn
      // so this code makes p a value that goes from -1 to +1 
      // x and y
      vec2 p = -1.0 + 2.0 * gl_FragCoord.xy / v2_resolution.xy;

      // a = the time speed up by 40
      float a = u_time*40.0;

      // declare a bunch of variables.
      float d,e,f,g=1.0/40.0,h,i,r,q;

      // e goes from 0 to 400 across the screen
      e=400.0*(p.x*0.5+0.5);

      // f goes from 0 to 400 down the screen
      f=400.0*(p.y*0.5+0.5);

      // i goes from 200 + or - 20 based
      // on the sin of e * 1/40th + the slowed down time / 150
      // or in other words slow down even more.
      // e * 1/40 means e goes from 0 to 1
      i=200.0+sin(e*g+a/150.0)*20.0;

      // d is 200 + or - 18.0 + or - 7
      // the first +/- is cos of 0.0 to 0.5 down the screen
      // the second +/i is cos of 0.0 to 1.0 across the screen
      d=200.0+cos(f*g/2.0)*18.0+cos(e*g)*7.0;

      // I'm stopping here. You can probably figure out the rest
      // see answer
      r=sqrt(pow(i-e,2.0)+pow(d-f,2.0));
      q=f/r;
      e=(r*cos(q))-a/2.0;f=(r*sin(q))-a/2.0;
      d=sin(e*g)*176.0+sin(e*g)*164.0+r;
      h=((f+d)+a/2.0)*g;
      i=cos(h+r*p.x/1.3)*(e+e+a)+cos(q*g*6.0)*(r+h/3.0);
      h=sin(f*g)*144.0-sin(e*g)*212.0*p.x;
      h=(h+(f-e)*q+sin(r-(a+h)/7.0)*10.0+i/4.0)*g;
      i+=cos(h*2.3*sin(a/350.0-q))*184.0*sin(q-(r*4.3+a/12.0)*g)+tan(r*g+h)*184.0*cos(r*g+h);
      i=mod(i/5.6,256.0)/64.0;
      if(i<0.0) i+=4.0;
      if(i>=2.0) i=4.0-i;
      d=r/350.0;
      d+=sin(d*d*8.0)*0.52;
      f=(sin(a*g)+1.0)/2.0;
      gl_FragColor=vec4(vec3(f*i/1.6,i/2.0+d/13.0,i)*d*p.x+vec3(i/1.3+d/8.0,i/2.0+d/18.0,i)*d*(1.0-p.x),1.0);
}