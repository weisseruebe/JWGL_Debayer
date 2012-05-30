varying vec4 vertColor;
uniform sampler2D sampler01;
uniform sampler2D sampler02;

void main(){
    //gl_FragColor = vertColor;
    vec3 theColor=vec3(texture2D(sampler01, (gl_TexCoord[0].st)));
    gl_FragColor = vec4(theColor,1);
}