package com.josephsullivan256.gmail.core;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

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
import com.josephsullivan256.gmail.util.Pair;
import com.josephsullivan256.gmail.util.Procedure;

public class Main {
	
	public Main() throws IOException {
		System.out.println("LWJGL " + Utils.getVersion());
		
		int width = 800;
		int height = 800;
		
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
		
		Level3D lvl = new Level3D(20,20,20,50);
		
		float far = 1000f;
		float near = 0.1f;
		Matrix perspective = Matrix.perspective((float)width/(float)height, (float)Math.PI/2f, far, near);
		
		Utils.initGL();
		
		Shader shader0 = new Shader(Utils.readFile("shaders/shader0.vsh"),Utils.readFile("shaders/shader0.fsh"));
		Shader shader1 = new Shader(Utils.readFile("shaders/shader1.vsh"),Utils.readFile("shaders/shader1.fsh"));
		Shader shaderF0 = new Shader(Utils.readFile("shaders/shaderF0.vsh"),Utils.readFile("shaders/shaderF0.fsh"));
		
		Texture tex0 = new Texture(ImageIO.read(new File("textures/noise0.png")));
		Texture tex1 = new Texture(ImageIO.read(new File("textures/noise1.png")));
		Texture tex2 = new Texture(ImageIO.read(new File("textures/noise2.png")));
		
		int[] indices = new int[]{
				0,1,2,
				1,3,2,
				1,5,3,
				5,7,3,
				5,4,7,
				4,6,7,
				4,0,6,
				0,2,6,
				4,5,0,
				5,1,0,
				2,3,6,
				3,7,6
		};
		
		float[] cubeVertices = new float[]{
				 0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,
			     0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  1.0f,
			     0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,

			    -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
			     0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
			    -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  1.0f,
			    -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,

			    -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
			    -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
			    -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			    -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			    -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
			    -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

			     0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
			     0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
			     0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			     0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
			     0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			     0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

			    -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,
			     0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  1.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
			     0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
			    -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  0.0f,
			    -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,

			     0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  1.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f,
			     0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
			    -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  0.0f,
			     0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
			    -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f
		};
		
		VertexArrayObject vao0 = new VertexArrayObject();
		Vec3[] offsets0 = offsetsFromLevel(lvl.getInterior());
		{
			vao0.initialize(
					Pair.init(
							BufferObject.vbo().bind().bufferData(cubeVertices, GL15.GL_STATIC_DRAW),
							new VertexAttributes()
							.with(3, GL11.GL_FLOAT)
							.with(3, GL11.GL_FLOAT)
							.with(2, GL11.GL_FLOAT)
							),
					Pair.init(
							BufferObject.vbo().bind().bufferData(offsets0, GL15.GL_STATIC_DRAW),
							new VertexAttributes()
							.withInstanced(3, GL11.GL_FLOAT)
							)
					);
		}
		
		VertexArrayObject vao1 = new VertexArrayObject();
		Vec3[] offsets1 = offsetsFromLevel(lvl.getWalls());
		{
			vao1.initialize(
					Pair.init(
							BufferObject.vbo().bind().bufferData(cubeVertices, GL15.GL_STATIC_DRAW),
							new VertexAttributes()
							.with(3, GL11.GL_FLOAT)
							.with(3, GL11.GL_FLOAT)
							.with(2, GL11.GL_FLOAT)
							),
					Pair.init(
							BufferObject.vbo().bind().bufferData(offsets1, GL15.GL_STATIC_DRAW),
							new VertexAttributes()
							.withInstanced(3, GL11.GL_FLOAT)
							)
					);
		}
		
		//instantiate uniform classes
		Uniform<Vec3> sunDirection, sunAmbient, sunDiffuse, sunSpecular;
		sunDirection = new Uniform<Vec3>("sun.direction",UniformPasser.uniform3f);
		sunAmbient = new Uniform<Vec3>("sun.ambient",UniformPasser.uniform3f);
		sunDiffuse = new Uniform<Vec3>("sun.diffuse",UniformPasser.uniform3f);
		sunSpecular = new Uniform<Vec3>("sun.specular",UniformPasser.uniform3f);
		Vec3 sunDir = new Vec3(-2,-3,-4).normalized();
		
		Uniform<Matrix> transformUniform = new Uniform<Matrix>("transform",UniformPasser.uniformMatrix4);
		Uniform<Matrix> perspectiveUniform = new Uniform<Matrix>("perspective",UniformPasser.uniformMatrix4);
		Uniform<Integer> texUniform = new Uniform<Integer>("noise",UniformPasser.uniform1i);
		
		Uniform<Vec2> nearFarUniform = new Uniform<Vec2>("nf",UniformPasser.uniform2f);
		Uniform<Vec2> dimensionsUniform = new Uniform<Vec2>("dimensions",UniformPasser.uniform2f);
		Uniform<Integer> texF0Uniform0 = new Uniform<Integer>("scene",UniformPasser.uniform1i);
		Uniform<Integer> texF0Uniform1 = new Uniform<Integer>("depth",UniformPasser.uniform1i);
		
		//render procedures
		Procedure drawVao0 = ()->{
			vao0.bind();
			shader0.use();
			
			//uniforms
			transformUniform.uniform(camera.getTransform(), shader0);
			perspectiveUniform.uniform(perspective, shader0);
			
			//assign texture to unit
			tex2.assignToUnit(0);
			
			//glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
			vao0.drawArraysInstanced(indices.length, offsets0.length);
			vao0.unbind();
		};
		
		Procedure drawVao1 = ()->{
			vao1.bind();
			shader1.use();
			
			//uniforms
			transformUniform.uniform(camera.getTransform(), shader1);
			perspectiveUniform.uniform(perspective, shader1);
			
			//assign texture to unit
			tex2.assignToUnit(0);
			
			//glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
			vao1.drawArraysInstanced(indices.length, offsets1.length);
			vao1.unbind();
		};
		
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
		
		//initial uniforms
		shader0.use();
		texUniform.uniform(0, shader0);
		sunDirection.uniform(sunDir, shader0);
		sunAmbient.uniform(new Vec3(0.1f,0.5f,1).scaledBy(0.4f), shader0);
		sunDiffuse.uniform(new Vec3(1f,0.8f,0.7f).scaledBy(0.9f), shader0);
		sunSpecular.uniform(new Vec3(1f,0.8f,0.7f).scaledBy(0.5f), shader0);
		
		shader1.use();
		texUniform.uniform(0, shader1);
		sunDirection.uniform(sunDir, shader1);
		sunAmbient.uniform(new Vec3(0.1f,0.5f,1).scaledBy(0.1f), shader1);
		sunDiffuse.uniform(new Vec3(1f,0.9f,0.8f).scaledBy(0.8f), shader1);
		sunSpecular.uniform(new Vec3(1f,0.9f,0.8f).scaledBy(0.6f), shader1);
		
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
			
			drawVao0.run();
			drawVao1.run();
			
			fbo.unbind();
			
			GL11.glClearColor(0f, 0f, 0f, 0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			drawScreen.run();
			
			camera.move(cameraCallback.getMovement().scaledBy(0.1f));
			camera.rotate(cameraCallback.getRotation().scaledBy(0.001f));
			
			window.swapBuffers();
			Utils.pollGLFWEvents();
		}
		
		window.destroy();
		Utils.terminateGLFW();
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
