package de.rettig.jwgl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.sourceforge.fastpng.PNGDecoder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

/**
 * The vertex and fragment shaders are setup when the box object is
 * constructed. They are applied to the GL state prior to the box
 * being drawn, and released from that state after drawing.
 * @author Stephen Jones
 */
public class Box {

	/*
	 * if the shaders are setup ok we can use shaders, otherwise we just
	 * use default settings
	 */
	private boolean useShader=true;

	/*
	 * program shader, to which is attached a vertex and fragment shaders.
	 * They are set to 0 as a check because GL will assign unique int
	 * values to each
	 */
	private int shader=0;
	private int vertShader=0;
	private int fragShader=0;
	private int tex01=0;//first texture
	private int tex02=0;//second texture

	private float zoom = -5;

	public Box(){

		try {
			tex01=setupTextures("res/weltgrund.png");
			tex02=setupTextures("res/rgb.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/*
		 * create the shader program. If OK, create vertex
		 * and fragment shaders
		 */
		shader=ARBShaderObjects.glCreateProgramObjectARB();

		if(shader!=0){
			vertShader=createVertShader("shaders/screen.vert");
			fragShader=createFragShader("shaders/screen.frag");
		}
		else {
			useShader=false;
		}

		/*
		 * if the vertex and fragment shaders setup sucessfully,
		 * attach them to the shader program, link the shader program
		 * (into the GL context I suppose), and validate
		 */
		if(vertShader !=0 && fragShader !=0){
			ARBShaderObjects.glAttachObjectARB(shader, vertShader);
			ARBShaderObjects.glAttachObjectARB(shader, fragShader);

			ARBShaderObjects.glLinkProgramARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
				printLogInfo(shader);
				useShader=false;
			}
			ARBShaderObjects.glValidateProgramARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
				printLogInfo(shader);
				useShader=false;
			}
		} else {
			useShader=false;
		}

		if(useShader){
			ARBShaderObjects.glUseProgramObjectARB(shader);
		}
		int sampler01 = ARBShaderObjects.glGetUniformLocationARB(shader, "sampler01");

		if(sampler01<1){
			System.out.println("Error accessing sampler01");
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex01);
		ARBShaderObjects.glUniform1iARB(sampler01, 0);

	}

	private int setupTextures(String fileName) throws IOException {
		IntBuffer tmp=BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();

		InputStream in = new FileInputStream(fileName);
		PNGDecoder decoder=new PNGDecoder(in);
		ByteBuffer data=ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
		decoder.decode(data, decoder.getWidth()*4, PNGDecoder.TextureFormat.RGBA);
		data.rewind();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,decoder.getWidth(),decoder.getHeight(),0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,data);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);

		tmp.rewind();
		return tmp.get(0);
	}

	public void setTexFilter(int filter){
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER, filter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER, filter);
	}
	
	/*
	 * If the shader was setup succesfully, we use the shader. Otherwise
	 * we run normal drawing code.
	 */
	public void draw(){
		if(useShader) {
			ARBShaderObjects.glUseProgramObjectARB(shader);
		}
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, 0.0f, zoom);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);//white

		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
		
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1.0f, 1.0f, 0.0f);

		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1.0f, -1.0f, 0.0f);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
		
		GL11.glEnd();

		//release the shader
		ARBShaderObjects.glUseProgramObjectARB(0);

	}

	/*
	 * With the exception of syntax, setting up vertex and fragment shaders
	 * is the same.
	 * @param the name and path to the vertex shader
	 */
	private int createVertShader(String filename){
		//vertShader will be non zero if succefully created

		vertShader=ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		//if created, convert the vertex shader code to a String
		if(vertShader==0){return 0;}
		String vertexCode="";
		String line;
		try{
			BufferedReader reader=new BufferedReader(new FileReader(filename));
			while((line=reader.readLine())!=null){
				vertexCode+=line + "\n";
			}
		}catch(Exception e){
			System.out.println("Fail reading vertex shading code");
			return 0;
		}
		/*
		 * associate the vertex code String with the created vertex shader
		 * and compile
		 */
		ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
		ARBShaderObjects.glCompileShaderARB(vertShader);
		//if there was a problem compiling, reset vertShader to zero
		if (ARBShaderObjects.glGetObjectParameteriARB(vertShader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
			printLogInfo(vertShader);
			vertShader=0;
		}
		//if zero we won't be using the shader
		return vertShader;
	}
	
	public void zoom(float f){
		zoom+=f;
	}

	//same as per the vertex shader except for method syntax
	private int createFragShader(String filename){

		fragShader=ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		if(fragShader==0){return 0;}
		String fragCode="";
		String line;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while((line=reader.readLine())!=null){
				fragCode+=line + "\n";
			}
		}catch(Exception e){
			System.out.println("Fail reading fragment shading code");
			return 0;
		}
		ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
		ARBShaderObjects.glCompileShaderARB(fragShader);
		if (ARBShaderObjects.glGetObjectParameteriARB(fragShader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
			printLogInfo(fragShader);
			fragShader=0;
		}
		return fragShader;
	}

	private static boolean printLogInfo(int obj){
		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		ARBShaderObjects.glGetObjectParameterARB(obj,ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

		int length = iVal.get();
		if (length > 1) {
			// We have some info we need to output.
			ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
			iVal.flip();
			ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			String out = new String(infoBytes);
			System.out.println("Info log:\n"+out);
		}
		else return true;
		return false;
	}

}