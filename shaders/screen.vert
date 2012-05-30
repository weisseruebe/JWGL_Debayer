varying vec4 vertColor;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    vertColor = vec4(0.6, 0.9, 0.4, 1.0);
    gl_TexCoord[0]=gl_MultiTexCoord0;
}