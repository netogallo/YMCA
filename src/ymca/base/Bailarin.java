package ymca.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.util.Map;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class Bailarin {

	private Bone skeleton;
	private Map <String,Bone> bones;
	private int baile,nextBaile;
	private int step;
	private final int znlarm=90,znrarm=-90,znlfarm=0,znrfarm=0,znlleg=90,znrleg=-90,znlfleg=0,znrfleg=0; 
	private final int ynlarm=0,ynrarm=0,ynlfarm=0,ynrfarm=0,ynlleg=0,ynrleg=0,ynlfleg=0,ynrfleg=0;
	private final int xnlarm=0,xnrarm=0,xnlfarm=0,xnrfarm=0,xnlleg=0,xnrleg=0,xnlfleg=0,xnrfleg=0;
	
	private nBone nlarm = new nBone(0,0,90);
	private nBone nrarm = new nBone(0,0,-90);
	private nBone nlfarm = new nBone(0,0,0);
	private nBone nrfarm = new nBone(0,0,0);
	private nBone nlleg = new nBone(0,0,90);
	private nBone nrleg = new nBone(0,0,-90);
	private nBone nlfleg = new nBone(0,0,0);
	private nBone nrfleg = new nBone(0,0,0);
	
	//Clase que representara un brazo neutral.
	class nBone{
		
		public final int rotx,roty,rotz;
		public nBone(int rotx,int roty, int rotz){
			this.rotx=rotx;
			this.roty=roty;
			this.rotz=rotz;
		}
	}
	
	/**Metodo que Crea un bailarin
	 * 
	 * @param skeleton Esqueleto que utilizara el Bailarin.
	 */
	public Bailarin(){
		
		this.bones = new HashMap<String,Bone>();
		this.baile=0;
		this.nextBaile=3;
		this.step=0;
	}
	/** Metodo que le agrega un esqueleto al bailarin
	 * 
	 * @param skeleton Esqueleto del bailarin
	 */
	public void addSkeleton(Bone skeleton){
		if(skeleton.isAlfa()){
			this.skeleton=skeleton;
			addBones(skeleton);
		}
	}
	
	/**Metodo que indexea los huesos del esqueleto del bailarin para
	 * un acesso rapido.
	 * @param bone Hueso que se desea indexear
	 */
	private void addBones(Bone bone){
		
		this.bones.put(bone.name(), bone);
		
		for(int count=0; count<bone.children().length && bone.children()[count]!=null; count++)
			addBones(bone.children()[count]);
	}
	
	/**Metodo que permite obtener un hueso del bailarin utilizando
	 * el nombre del hueso
	 * @param name Nobre del hueso que se desea obtener
	 * @return Hueso con el nombre enviado.
	 */
	public Bone getBone(String name){return bones.get(name);}
	
	/**Metodo que mueve al bailarin la cantidad de pasos especificados
	 * en su baile
	 * @param pace numero de pasos que desea mover al bailarin.
	 * @throws InterruptedException 
	 */
	
	public void dance(int pace) throws InterruptedException{
		
		//this.getBone("larm").rotz(pace*8);
		
		for(int veces=0; veces<pace*2; veces++){
			stepOnce();
			stepOnce();
			//Thread.currentThread().sleep(5);
		}
	}
	
	public void siguienteBaile(){
		
		switch(this.nextBaile){
		
		case 1:
			this.nextBaile=2;
			break;
		case 2:
			this.nextBaile=3;
			break;
			
		default:
			this.nextBaile=1;
		}
		
		//this.nextBaile=2;
		this.baile=0;
		this.step=0;
	}
	
	/**Avanza un paso en el baile actualmete seleccionado
	 * 
	 */
	public void stepOnce(){
		
		Bone tmp;
		Bone tmp1;
		nBone curr;
		boolean finished=true;
		int speed = 3;
		
		switch(this.baile){
		
		//Bailes disponibles del muñeco.
		//Regresar a la posicion neutral
		case 0:
			
			if(this.isNeutral()){
				this.baile=this.nextBaile;
				this.step=0;
			}
			else{
				
				tmp=this.getBone("rarm");
				curr=nrarm;
				if(tmp.rotz()!=curr.rotz)
					tmp.rotz(tmp.rotz()+(tmp.rotz()>curr.rotz?-1:1));
				if(tmp.rotx()!=curr.rotx)
					tmp.rotx(tmp.rotx()+(tmp.rotx()>curr.rotx?-1:1));
				if(tmp.roty()!=curr.roty)
					tmp.roty(tmp.roty()+(tmp.roty()>curr.roty?-1:1));
				
				tmp=this.getBone("larm");
				curr=nlarm;
				if(tmp.rotz()!=curr.rotz)
					tmp.rotz(tmp.rotz()+(tmp.rotz()>curr.rotz?-1:1));
				if(tmp.rotx()!=curr.rotx)
					tmp.rotx(tmp.rotx()+(tmp.rotx()>curr.rotx?-1:1));
				if(tmp.roty()!=curr.roty)
					tmp.roty(tmp.roty()+(tmp.roty()>curr.roty?-1:1));
				
				tmp=this.getBone("rfarm");
				curr=nrfarm;
				if(tmp.rotz()!=curr.rotz)
					tmp.rotz(tmp.rotz()+(tmp.rotz()>curr.rotz?-1:1));
				if(tmp.rotx()!=curr.rotx)
					tmp.rotx(tmp.rotx()+(tmp.rotx()>curr.rotx?-1:1));
				if(tmp.roty()!=curr.roty)
					tmp.roty(tmp.roty()+(tmp.roty()>curr.roty?-1:1));
				
				tmp=this.getBone("lfarm");
				curr=nlfarm;
				if(tmp.rotz()!=curr.rotz)
					tmp.rotz(tmp.rotz()+(tmp.rotz()>curr.rotz?-1:1));
				if(tmp.rotx()!=curr.rotx)
					tmp.rotx(tmp.rotx()+(tmp.rotx()>curr.rotx?-1:1));
				if(tmp.roty()!=curr.roty)
					tmp.roty(tmp.roty()+(tmp.roty()>curr.roty?-1:1));
			}
			break;
			
		//Greased Lightning	
		case 1:
			
			if(this.step==0){
				
				tmp=this.getBone("rarm");
				
				if(tmp.roty()<180){
					tmp.roty(tmp.roty()+speed);
				}else{
					this.step=1;
				}
			}
			else if(this.step==1){
				
				tmp=this.getBone("rarm");
				
				if(tmp.rotz()<90){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				
				if(tmp.rotz()>-120){
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=2;
				
			}else if(this.step==2){
				
				tmp=this.getBone("rarm");
				
				if(tmp.rotz()>0){
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				
				if(tmp.rotz()<0){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(finished)
					this.step=3;
					
			}else if(this.step==3){
				
				tmp=this.getBone("rarm");
				
				if(tmp.rotz()<70){
					
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				
				if(tmp.rotz()>-120){
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=4;
				
			}else if(this.step==4){
				
				tmp=this.getBone("rarm");
				
				if(tmp.rotz()>-90){
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				
				if(tmp.rotz()<0){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(finished)
					this.step=1;
			}
			
			break;
		
	    //YMCA
		case 2:
			
			if(this.step==0){
				
				tmp = this.getBone("rarm");
				tmp1 = this.getBone("larm");
				
				if(tmp.roty()<180 || tmp1.roty()>-180){
					tmp.roty(tmp.roty()+speed);
					tmp1.roty(tmp1.roty()-speed);
					finished=false;
				}
				
				if(tmp.rotz()<-70 || tmp1.rotz()>70){
					
					tmp.rotz(tmp.rotz()+speed);
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=1;
			}
			
			else if(this.step==1){
				tmp1=this.getBone("larm");
				tmp=this.getBone("rarm");
				
				if(tmp1.roty()>150 || tmp.roty()<-150){
					
					tmp.roty(tmp.roty()+speed);
					tmp1.roty(tmp1.roty()-speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				tmp1=this.getBone("lfarm");
				
				if(tmp.rotz()>-130 || tmp1.rotz()<130){
					
					tmp.rotz(tmp.rotz()-speed);
					tmp1.rotz(tmp1.rotz()+speed);
					finished=false;
				}
				
				if(finished)
					this.step=3;
				
			}
			
			else if(this.step==3){
				
				tmp=this.getBone("rarm");
				tmp1=this.getBone("larm");
				
				if(tmp.rotz()>-85){
					
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
				
				if(tmp1.rotz()>-70){
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				tmp1=this.getBone("lfarm");
				
				if(tmp.rotz()<-60){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(tmp1.rotz()>60){
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=4;
			}
			
			else if(this.step==4){
				
				//tmp=this.getBone("rarm");
				tmp1=this.getBone("larm");
				
				if(tmp1.rotz()<85){
					
					tmp1.rotz(tmp1.rotz()+speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
					
				if(tmp.rotz()<-40){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				tmp1=this.getBone("rfarm");
				
				if(tmp1.rotz()>50){
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=5;
				
			}
			
			else if(this.step==5){
				
				tmp=this.getBone("rarm");
				tmp1=this.getBone("larm");
				
				if(tmp.rotz()<-70){
					
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(tmp1.rotz()>70){
					
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				tmp=this.getBone("rfarm");
				tmp1=this.getBone("lfarm");
				
				if(tmp.rotz()<0){
					
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(tmp1.rotz()>0){
					
					tmp1.rotz(tmp1.rotz()-speed);
					finished=false;
				}
				
				if(finished)
					this.step=1;
				
			}
			
			break;
			
		case 3:
			
			if(this.step==0){
				tmp=this.getBone("rarm");
			
				if(tmp.roty()<10){
					tmp.roty(tmp.roty()+speed);
					finished=false;
				}
			
				if(tmp.rotz()>-170){
					tmp.rotz(tmp.rotz()-speed);
					finished=false;
				}
			
				if(finished)
					this.step=1;
				}
			
			
			if(this.step==1){
				
				tmp=this.getBone("rarm");
				
				if(tmp.roty()<120){
					
					tmp.roty(tmp.roty()+speed);
					finished=false;
				}
				
				if(tmp.rotz()<80){
					tmp.rotz(tmp.rotz()+speed);
					finished=false;
				}
				
				if(finished)
					this.siguienteBaile();
			}
			
		}
			
	}
	
	/**Metodo que dibuja al bailarin en la escena
	 * 
	 * @param gl renderizador que se desea utilizar
	 */
	public void draw(GL10 gl){
		
		drawBones(gl,skeleton);
	}
	
	private boolean isNeutral(){
		
		boolean ret=true;
		Bone tmp;
		
		tmp=this.getBone("rarm");
		if(tmp.rotx()!=xnrarm || tmp.roty()!=ynrarm || tmp.rotz()!=znrarm)
			ret=false;
		
		tmp=this.getBone("rfarm");
		if(tmp.rotx()!=xnrfarm || tmp.roty()!=ynrfarm || tmp.rotz()!=znrfarm)
			ret=false;
		
		tmp=this.getBone("lfarm");
		if(tmp.rotx()!=xnlfarm || tmp.roty()!=ynlfarm || tmp.rotz()!=znlfarm)
			ret=false;
		
		tmp=this.getBone("larm");
		if(tmp.rotx()!=xnlarm || tmp.roty()!=ynlarm || tmp.rotz()!=znlarm)
			ret=false;
		
		return ret;
	}
	
	/**Dibuja un hueso y los hijos del hueso
	 * 
	 * @param gl Renderizador que se desea utilizar
	 * @param bone Hueso que se desea dibujar
	 */
	private void drawBones(GL10 gl, Bone bone){
		
		
		gl.glPushMatrix();
		
		if(bone.isAlfa()){
			gl.glTranslatef(bone.x(), bone.y(), bone.z());
		}else{
			
			
			gl.glRotatef(bone.rotx(), 1, 0, 0);
			gl.glRotatef(bone.roty(), 0, 1, 0);
			gl.glRotatef(bone.rotz(), 0, 0, 1);
			
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			if(bone.colors()!=null){
				gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, bone.colors());
			}
			
			if(bone.textCoords()!=null){
				gl.glBindTexture(GL10.GL_TEXTURE_2D, bone.texturas()[0]);
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bone.textCoords());
			}
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bone.mesh());
			
			if(bone.textCoords()!=null)
				gl.glDrawElements(GL10.GL_TRIANGLES, bone.indicesSize(), GL10.GL_UNSIGNED_SHORT, bone.indices());
			else
				gl.glDrawElements(GL10.GL_LINES, bone.indicesSize(), GL10.GL_UNSIGNED_SHORT, bone.indices());
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			
			gl.glTranslatef(0, bone.size(), 0);
			}
		
		for(int count=0; count<bone.children().length && bone.children()[count]!=null; count++)
			drawBones(gl, bone.children()[count]);
		
		gl.glPopMatrix();
	}
}
