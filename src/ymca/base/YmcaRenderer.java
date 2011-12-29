package ymca.base;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10; 
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.opengl.GLU;
import android.opengl.GLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
//import javax.microedition.khronos.opengles.
import java.util.TimerTask;

public class YmcaRenderer implements GLSurfaceView.Renderer{

	private Playa back;
	int speed;
	short piso;
	private Sky cielo,espacio;
	private Icon cancion,baile,subzero,reptile,terrain,lbocina;
	private Context context;
	private Bone Bones1=null;
	private Bailarin Bailarin1;
	private Timer dance;
	private float rot;
	MusicPlayer player;
	boolean character,lugar,slugar;
	boolean swap;
	GLU glu;
	
	/**Crea un Renderizador que dibuja toda la escena y los bailarines segun los parametors
	 * 
	 * @param context Ambito donde se encuantra el escenario
	 */
	public YmcaRenderer(Context context){
		
		this.rot=0;
		this.piso=0;
		this.character=true;
		this.lugar=true;
		this.swap=false;
		this.context=context;
		back=new Playa();
		glu = new GLU();
		Bailarin1 = new Bailarin();
		makeBones();
		Bailarin1.addSkeleton(this.Bones1);
		this.speed=1;
		
		dance=new Timer();
		dance.scheduleAtFixedRate(new danceStep(), 0, 250);
		
		try {
			this.player = new MusicPlayer(context);
			//this.player.play();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void makeBones(){
		
		Bones1=new Bone(0,-0.1f,0,"Bailarin1");
		
		Bone back = new Bone(0,0,0,10,Bones1,"back");
		Bone backdummy = new Bone(180,0,0,2f,back,"dummy1");
		Bone neck = new Bone(0,0,0,1f,back,"neck");
		Bone head = new Bone(0,0,0,1f,neck,"head");
		Bone larmdummy = new Bone(0,0,-90,4f,backdummy,"larmdummy");
		Bone rarmdummy = new Bone(0,0,90,4f,backdummy,"rarmdummy");
		Bone larm = new Bone(0,0,90,6f,larmdummy,"larm");
		Bone rarm = new Bone(0,0,0,6f,rarmdummy,"rarm");
		Bone lfarm =new Bone(0,0,0,3f,larm,"lfarm");
		Bone rfarm =new Bone(0,0,90,3f,rarm,"rfarm");
		Bone backdummy1 = new Bone(0,0,0,9f,backdummy,"dummy2");
		Bone llegdummy = new Bone(0,0,-90,2f,backdummy1,"llegdummy");
		Bone rlegdummy = new Bone(0,0,90,2f,backdummy1,"rrlegdummy");
		Bone lleg = new Bone(0,0,90,8f,llegdummy,"lleg");
		Bone lfleg = new Bone(0,0,0,6f,lleg,"lfleg");
		Bone rleg = new Bone(0,0,-90,8f,rlegdummy,"rleg");
		Bone rfleg = new Bone(0,0,0,6f,rleg,"rfleg");
		Bone lfoot =new Bone(0,0,0,1,lfleg,"lfoot");
		Bone rfoot =new Bone(0,0,0,1,rfleg,"rfoot");
		Bone lhand =new Bone(0,0,0,1,lfarm,"lhand");
		Bone rhand =new Bone(0,0,0,1,rfarm,"rhand");
		
	}
	
	public void onPause(){
	    
	    //super.onPause();
	    player.stop();
	}
	
	public void onResume(){
	    
	    //super.onResume();
	    try {
		player.next();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
	public void onDrawFrame(GL10 gl) {
		
		//gl.glClearColor(0.5f, 0.5f, 0f, 0.6f);
		
		//float vertices[] = {1,0,0,0,1,0,-1,0,0};
		//gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
		
		//gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT |
                GL10.GL_DEPTH_BUFFER_BIT);
		
		//gl.glClearColor(0.5f, 0.5f, 0f, 0.6f);
		
		gl.glLoadIdentity();
		//gl.glTranslatef(0, 0, -4);
		
		//back.draw(gl);
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -50);
		//gl.glRotatef(rot, 1, 0, 0);
		Bailarin1.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(10, 0, -50);
		gl.glScalef(0.2f, 0.2f, 0.2f);
		gl.glScalef(this.speed+10, this.speed+20, this.speed+10);
		this.lbocina.draw(gl);
		gl.glTranslatef(-7, 0, 0);
		this.lbocina.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0f,0f,-20f);
		gl.glScalef(20,100,20);
		gl.glRotatef(rot, 0, 1, 0);
		if(this.lugar)
			this.cielo.draw(gl);
		else
			this.espacio.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		
		gl.glTranslatef(-13, 16+this.speed/5, -55);
		gl.glScalef(9f, 5, 0);
		this.cancion.draw(gl);
		gl.glTranslatef(1f, 0, 0);
		this.baile.draw(gl);
		gl.glTranslatef(1f, 0, 0);
		if(!this.character)
			this.subzero.draw(gl);
		else
			this.reptile.draw(gl);
		gl.glTranslatef(1f, 0, 0);
		this.terrain.draw(gl);
		gl.glPopMatrix();
		
		if(this.swap){
			this.swap=false;
			try {
				this.subzero(gl);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				500.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
		
	}
	
	public void siguienteBaile(){
		
		this.Bailarin1.siguienteBaile();
	}
	
	public void siguienteLugar(){
		
		this.slugar=true;
	}
	
	public void siguientePersonaje(){
		
		this.swap=true;
	}
	
	public void siguienteCancion(){

		this.player.nextSong();
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		back.loadTexture(gl,this.context);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		this.cancion = new Icon(gl,context.getResources().openRawResource(R.drawable.music));
		this.subzero = new Icon(gl,context.getResources().openRawResource(R.drawable.subzero));
		this.reptile = new Icon(gl,context.getResources().openRawResource(R.drawable.reptile));
		this.baile = new Icon(gl,context.getResources().openRawResource(R.drawable.dance));
		this.cielo = new Sky(gl, context.getResources().openRawResource(R.drawable.skydome1),16);
		this.terrain = new Icon(gl,context.getResources().openRawResource(R.drawable.terrain));
		this.espacio = new Sky(gl, context.getResources().openRawResource(R.drawable.universe),16);
		this.lbocina = new Icon(gl,context.getResources().openRawResource(R.drawable.bocina));
		/*
		try {
			subzero(gl);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		gl.glFlush();
		
	}
	
	public void subzero(GL10 gl) throws NotFoundException, ParserConfigurationException, SAXException, IOException{
		InputStream image;
		if(character)
			 	image = context.getResources().openRawResource(R.drawable.rep);
		else
				image = context.getResources().openRawResource(R.drawable.subz);
		
		this.character=!this.character;
		
		Bailarin1.getBone("head").loadOGRE(0f,context.getResources().openRawResource(R.raw.head), image, gl);
		Bailarin1.getBone("rarm").loadOGRE(3f,context.getResources().openRawResource(R.raw.rarm), image, gl);
		Bailarin1.getBone("back").loadOGRE(7f,context.getResources().openRawResource(R.raw.back), image, gl);
		Bailarin1.getBone("larm").loadOGRE(3f,context.getResources().openRawResource(R.raw.larm), image, gl);
		Bailarin1.getBone("rleg").loadOGRE(4f,context.getResources().openRawResource(R.raw.rleg), image, gl);
		Bailarin1.getBone("lleg").loadOGRE(4f,context.getResources().openRawResource(R.raw.lleg), image, gl);
		Bailarin1.getBone("lfleg").loadOGRE(4f,context.getResources().openRawResource(R.raw.lfleg), image, gl);
		Bailarin1.getBone("rfleg").loadOGRE(4f,context.getResources().openRawResource(R.raw.rfleg), image, gl);
		Bailarin1.getBone("rfarm").loadOGRE(2f,context.getResources().openRawResource(R.raw.lfarm), image, gl);
		Bailarin1.getBone("lfarm").loadOGRE(2f,context.getResources().openRawResource(R.raw.rfarm), image, gl);
		Bailarin1.getBone("dummy2").loadOGRE(5f,context.getResources().openRawResource(R.raw.pelvis), image, gl);
		Bailarin1.getBone("lhand").loadOGRE(2f,context.getResources().openRawResource(R.raw.lhand), image, gl);
		Bailarin1.getBone("rhand").loadOGRE(2f,context.getResources().openRawResource(R.raw.rhand), image, gl);
		
		try {
			image.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class danceStep extends TimerTask{

		/**Metodo que se ejecuta 4 veces por segundo
		 * 
		 */
		public void run() {
			
			try {
				if(slugar){
					lugar=!lugar;
					slugar=false;
				}
				if(player!=null){
					Bailarin1.dance(player.getCurrentRate());
					rot=2*player.getCurrentRate()+rot;
					speed=player.getCurrentRate();
					piso=(short)(player.getCurrentRate()/(Short.MAX_VALUE/16));
				}
				//Bailarin1.dance(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
