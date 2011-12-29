package ymca.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Icon {
	
	private int[] texturas;
	
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureBuffer;
	
	float[] vertices = {
		     -0.5f, 0f, 0.0f,  // 0, Top Left
		     0.5f, 0.0f, 0.0f,  // 1, Bottom Left
		     0.5f, 1.0f, 0.0f,  // 2, Bottom Right
		     -0.5f,  1.0f, 0.0f,  // 3, Top Right
	};
	
	private float[] texture={
			0f,1f,
			1f,1f,
			1f,0f,
			0f,0f,
	};
	
	private short[] indices={0,1,2,0,2,3};
	
	public Icon(GL10 gl, InputStream textura){
		
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length*2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer=ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length*4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer=tbb.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		loadTexture(textura,gl);
	}
	
	public void draw(GL10 gl){
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturas[0]);
		
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//gl.glColor4f(1, 1, 1, 1);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	private void loadTexture(InputStream textura, GL10 gl){
		
		Bitmap bmp=null;
		bmp=BitmapFactory.decodeStream(textura);
		
		try {
			textura.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textura=null;
		this.texturas=new int[1];
		
		gl.glGenTextures(1, this.texturas, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texturas[0]);
		
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		
		bmp.recycle();
	}
}
