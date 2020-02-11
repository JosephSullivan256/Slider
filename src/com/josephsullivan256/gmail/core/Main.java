package com.josephsullivan256.gmail.core;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.josephsullivan256.gmail.gl.BufferObject;
import com.josephsullivan256.gmail.gl.FrameBuffer;
import com.josephsullivan256.gmail.gl.Shader;
import com.josephsullivan256.gmail.gl.Texture;
import com.josephsullivan256.gmail.gl.Uniform;
import com.josephsullivan256.gmail.gl.UniformPasser;
import com.josephsullivan256.gmail.gl.Utils;
import com.josephsullivan256.gmail.gl.VertexArrayObject;
import com.josephsullivan256.gmail.gl.VertexAttributes;
import com.josephsullivan256.gmail.gl.Window;
import com.josephsullivan256.gmail.gl.camera.Camera;
import com.josephsullivan256.gmail.gl.camera.CameraCallback;
import com.josephsullivan256.gmail.gl.input.CollectionCursorPosCallback;
import com.josephsullivan256.gmail.gl.input.CollectionKeyCallback;
import com.josephsullivan256.gmail.math.linalg.Matrix;
import com.josephsullivan256.gmail.math.linalg.Vec2;
import com.josephsullivan256.gmail.math.linalg.Vec2i;
import com.josephsullivan256.gmail.math.linalg.Vec3;
import com.josephsullivan256.gmail.render.DirectionalLight;
import com.josephsullivan256.gmail.render.SceneInfo;
import com.josephsullivan256.gmail.util.Pair;
import com.josephsullivan256.gmail.util.Procedure;

public class Main {
	
	public Main() throws IOException {
		System.out.println("LWJGL " + Utils.getVersion());
		
		int width = 1600;
		int height = 900;
		
		Utils.initGLFW();
		Window window = new Window("hello world",width,height);
		window.hideCursor();
		CollectionKeyCallback callback = new CollectionKeyCallback();
		CollectionCursorPosCallback callback1 = new CollectionCursorPosCallback();
		window.setCallback(callback);
		window.setCallback(callback1);
		
		Camera camera = new Camera(new Vec3(0,0,10));
		CameraCallback cameraCallback = CameraCallback.mouseRotationCallback(camera);
		callback.addCallback(cameraCallback);
		callback1.addCallback(cameraCallback);
		
		ExtraKeyCallback keys = new ExtraKeyCallback();
		callback.addCallback(keys);
		
		Level3D lvl = new Level3D(20,20,20,200);
		
		float far = 1000f;
		float near = 0.1f;
		Matrix perspective = Matrix.perspective((float)width/((float)height), (float)Math.PI/2f, far, near);
		
		Utils.initGL();
		
		Shader blockShader = new Shader(Utils.readFile("shaders/shader0.vsh"),Utils.readFile("shaders/shader0.fsh"));
		Shader wallShader = new Shader(Utils.readFile("shaders/shader1.vsh"),Utils.readFile("shaders/shader1.fsh"));
		Shader shaderF0 = new Shader(Utils.readFile("shaders/shaderF0.vsh"),Utils.readFile("shaders/shaderF0.fsh"));
		
		Texture tex = new Texture(ImageIO.read(new File("textures/noise2.png")));
		
		SceneInfo sceneInfo = new SceneInfo(new DirectionalLight(
				new Vec3(-2,-3,-4).normalized(),
				new Vec3(0.1f,0.5f,1).scaledBy(0.4f),
				new Vec3(1f,0.8f,0.7f).scaledBy(0.9f),
				new Vec3(1f,0.8f,0.7f).scaledBy(0.5f)));
		
		LevelRenderer lr = new LevelRenderer(lvl, sceneInfo, blockShader, wallShader, tex);
		
		Uniform<Vec2> nearFarUniform = new Uniform<Vec2>("nf",UniformPasser.uniform2f);
		Uniform<Vec2> dimensionsUniform = new Uniform<Vec2>("dimensions",UniformPasser.uniform2f);
		Uniform<Integer> texF0Uniform0 = new Uniform<Integer>("scene",UniformPasser.uniform1i);
		Uniform<Integer> texF0Uniform1 = new Uniform<Integer>("depth",UniformPasser.uniform1i);
		
		Pair<FrameBuffer,Texture[]> pair = FrameBuffer.withColorDepth(width, height);
		FrameBuffer fbo = pair.a;
		
		ScreenRenderProcedure drawScreen = new ScreenRenderProcedure(
				shaderF0,
				pair.b,
				()->{
						texF0Uniform0.uniform(0, shaderF0);
						texF0Uniform1.uniform(1, shaderF0);
						nearFarUniform.uniform(new Vec2(near,far), shaderF0);
						dimensionsUniform.uniform(new Vec2(width,height), shaderF0);
					}
				);
		
		//for screenshotting
		Pair<FrameBuffer,Texture[]> pair2 = FrameBuffer.withColorDepth(width, height);
		FrameBuffer fbo2 = pair2.a;
		
		//pre-render state settings
		GL11.glViewport(0, 0, width, height);
		//GL11.glEnable(GL11.GL_CULL_FACE); 
		while(!window.shouldClose()) {
			
			//bind vao
			//use shader
			//assign textures to units
			//do uniforms
			//draw vao
			//unbind vao
			
			fbo.bind();
			
			GL11.glClearColor(0f, 1f, 1f, 0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			lr.render(camera.getTransform(), perspective);
			
			fbo.unbind();
			
			GL11.glClearColor(0f, 0f, 0f, 0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			drawScreen.run();
			
			if(keys.isPDown()){
				fbo2.bind();
				GL11.glClearColor(0f, 0f, 0f, 0f);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				drawScreen.run();
				
				pair.b[0].assignToUnit(0);
				
				GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
				int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
				ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
				GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
				fbo2.unbind();
				
				String name = "save"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
				String format = "png";
				File file = new File(name+"."+format);// The file to save to.
				
				try {
					ImageIO.write(bufferToImage(buffer,width,height), format, file);
				} catch (IOException e) { e.printStackTrace(); }
			}
			
			camera.move(cameraCallback.getMovement().scaledBy(0.1f));
			camera.rotate(cameraCallback.getRotation().scaledBy(0.001f));
			
			window.swapBuffers();
			Utils.pollGLFWEvents();
		}
		
		window.destroy();
		Utils.terminateGLFW();
	}
	
	public static BufferedImage bufferToImage(ByteBuffer buffer, int width, int height){
		return bufferToImage(buffer,width,height,4);
	}
	
	public static BufferedImage bufferToImage(ByteBuffer buffer, int width, int height, int bpp){ //bpp = bytes per pixel
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		   
		for(int x = 0; x < width; x++) 
		{
		    for(int y = 0; y < height; y++)
		    {
		        int i = (x + (width * y)) * bpp;
		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		
		return image;
	}
	
	public static Vec3[] offsetsFromLevel(boolean[][][] level){
		List<Vec3> offsets = new ArrayList<Vec3>();
		
		for(int x = 0; x < level.length; x++){
			for(int y = 0; y < level[0].length; y++){
				for(int z = 0; z < level[0][0].length; z++){
					if(level[x][y][z]) offsets.add(new Vec3(x,y,z).scaledBy(1.2f));
				}
			}
		}
		
		return offsets.toArray(new Vec3[offsets.size()]);
	}
	
	public static void main(String[] args) throws IOException {
		new Main();
	}
}
