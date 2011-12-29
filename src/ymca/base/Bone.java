package ymca.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Scanner;

import javax.microedition.khronos.opengles.GL10;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Bone {
	
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private DocumentBuilder db; 
	
	private int rotx,roty,rotz;
	private float x,y,z,size;
	private Bone parent;
	private Bone[] children = new Bone[20];
	private int children_count = 0;
	private FloatBuffer mesh,colores,texCoords;
	private int[] texturas=null;
	private ShortBuffer indices;
	private int indicesSize;
	private boolean isAlfa;
	private String name;
	private final float cos=(float) Math.cos(-20), sin = (float) Math.sin(-20);

	/** Constructor que crea un hueso que esta unido a otro hueso
	 * 
	 * @param rotx El angulo del hueso en el eje X
	 * @param roty Angluo del hueso en el eje Y
	 * @param rotz Anglulo del hueso en el eje Z
	 * @param size Longitud del Hueso
	 * @param parent Hueso del cual se extiende este hueso
	 * @throws ParserConfigurationException 
	 */
	public Bone(int rotx, int roty, int rotz, float size, Bone parent, String name){
		
		this.rotx=rotx;
		this.roty=roty;
		this.rotz=rotz;
		this.size=size;
		this.parent=parent;
		this.parent.addChild(this);
		this.isAlfa=false;
		this.name=name;
		
		float[] vertices={
				0f,0f,0f,
				0f,this.size,0f,
		};
		
		
		float[] colores = {
				1f,0f,0f,1f,
				0f,0f,1f,1f,
		};
		
		short[] indices={0,1};
		
		makeModel(vertices, colores, null, indices, null, null);
		
	}
	
	/**Constructor que crea un hueso que no tiene padre, es el origen de un esqueleto.
	 */
	public Bone(float x, float y, float z, String name){
		this.parent=null;
		this.x=x;
		this.y=y;
		this.z=z;
		this.isAlfa=true;
		this.name=name;
	}
	
	/**Regresa la coordenada x del origen del esqueleto
	 * 
	 * @return Coordenada x del origen
	 */
	public float x(){return x;}
	
	/**REgresa la coordenada y del origen del esqueleto
	 * 
	 * @return Coordenada y del origen
	 */
	public float y(){return y;}
	
	/**Regresa la coordenada z del origen del esqueleto
	 * 
	 * @return Coordenada z del origen.
	 */
	public float z(){return z;}
	
	 /**Indica si este hueso es el origen de un esqueleto o un hueso ordinario
	  * 
	  * @return Es verdadero si el hueso es el origen
	  */
	public boolean isAlfa(){return isAlfa;}
	
	/**Metodo que debuelve la rotacion en x del hueso 
	 * 
	 * @return Rotacion en x del hueso
	 */
	public int rotx(){return rotx;}
	
	/**Metodo que debuelve la rotacion en y
	 * 
	 * @return rotacion en y del hueso
	 */
	public int roty(){return roty;}
	
	/**Metodo que debuelve la rotacion en z
	 * 
	 * @return Rotacion en z del hueso
	 */
	public int rotz(){return rotz;}
	
	/**Metodo que debuelve el padre de este hueso
	 * 
	 * @return Padre del hueso actual
	 */
	public Bone parent(){return parent;}
	
	/**Metodo que modifica la rotacion en x del hueso
	 * 
	 * @param rotx Valor de la rotacion en x
	 */
	public void rotx(int rotx){this.rotx=rotx;}
	
	/**Metodo que modifica la rotacion en y del hueso
	 * 
	 * @param roty Rotacion en y del hueso
	 */
	public void roty(int roty){this.roty=roty;}
	
	/**Metodo que modifica la rotacion en z del hueso
	 * 
	 * @param rotz Rotacion en z del hueso
	 */
	public void rotz(int rotz){
		this.rotz=rotz;
		}
	
	/**Metodo que debuelve los hijos de este hueso 
	 * 
	 * @return lista de hijos del hueso actual
	 */
	public Bone[] children(){return this.children;}
	
	/**Metodo que debuelve el nombre del hueso
	 * 
	 * @return Nombre del hueso acutal
	 */
	public String name(){return this.name;}
	
	/**Metodo que debuevle la longitud del hueso
	 * 
	 * @return longitud del hueso
	 */
	public float size(){return this.size;}
	
	/**Metodo que debuelve 
	
	/**Agrega un hueso a la lista de hijos del hueso actual
	 * 
	 * @param child Hueso que se desea agregar a la lista, (hueso hijo del hueso actual)
	 */
	
	/**
	 * Metodo que agrega un mesh que se dibujara con el hueso
	 *//*
	public void mesh(float[] mesh){
		this.mesh=mesh;
	}*/
	
	/** Metodo que recibe los colores de cada vertice del mesh
	 * 
	 * @param colors Arreglo de colores del mesh
	 *//*
	public void colors(float[] colors){this.colors=colors;}*/
	
	/** Obtener mesh para dibujar junto al hueso
	 * 
	 * @return Mesh que se quiere dibujar
	 */
	public FloatBuffer mesh(){
		
		this.mesh.position(0);
		return this.mesh;
		}
	
	/** Metodo que debuelve los colores de cada vertice del mesh
	 * 
	 * @return Arreglo de colores en los vertices (Null si no hay colores)
	 */
	public FloatBuffer colors(){
		
		if(this.colores==null)
			return null;
		
		this.colores.position(0);
		return this.colores;
		}
	
	/**Retorna las coordenadas de cada vertice en la textura
	 * 
	 * @return Buffer con las coordenadas (Null si no hay textura)
	 */
	public FloatBuffer textCoords(){
		
		if(this.texCoords==null)
			return null;
			
		this.texCoords.position(0);
		return this.texCoords;
	}
	
	/**Retorna un array con la textura del mesh
	 * 
	 * @return Array con la textura (null si no hay textura)
	 */
	public int[] texturas(){
		
		return this.texturas;
	}
	
	/**Establecer los indices del mesh que se desea dibujar
	 * 
	 * @param indexes Arreglo con los indices de cada vertice del mesh
	 *//*
	public void indexes(short[] indexes){this.indexes=indexes;}*/
	
	/**Obtener los indices de cada vertice del mesh que se dibujara
	 * 
	 * @return indices del mesh
	 */
	public ShortBuffer indices(){
		
		this.indices.position(0);
		return this.indices;
		}
	
	public void loadOGRE(float offset,InputStream modelo, InputStream textura, GL10 gl) throws ParserConfigurationException, SAXException, IOException{
		
		this.db=dbf.newDocumentBuilder();
		Document doc = db.parse(modelo);
		
		doc.getDocumentElement().normalize();
		NodeList faces = doc.getElementsByTagName("face");
		
		short indices[] = new short[faces.getLength()*3];
		
		for(int veces=0; veces<faces.getLength();veces++){
			Node curr = faces.item(veces);
			indices[veces*3]=Short.parseShort(curr.getAttributes().getNamedItem("v1").getTextContent());
			//indices[veces*3+1]=Short.parseShort(curr.getAttributes().getNamedItem("v2").getNodeValue());
			indices[veces*3+1]=Short.parseShort(curr.getAttributes().getNamedItem("v2").getTextContent());
			indices[veces*3+2]=Short.parseShort(curr.getAttributes().getNamedItem("v3").getTextContent());
		}
		
		NodeList vertices = doc.getElementsByTagName("vertex");
		
		float coords[] = new float[vertices.getLength()*3];
		float texCoords[] = new float[vertices.getLength()*2];
		
		for(int veces=0;veces<vertices.getLength();veces++){
			Element curr = (Element)vertices.item(veces);
			Node pos = curr.getElementsByTagName("position").item(0);
			float x=(Float.parseFloat(pos.getAttributes().getNamedItem("x").getTextContent()));
			float y=(Float.parseFloat(pos.getAttributes().getNamedItem("y").getTextContent()))+offset;
			float z=Float.parseFloat(pos.getAttributes().getNamedItem("z").getTextContent());
			
			coords[veces*3]=x;
			coords[veces*3+1]=y;
			coords[veces*3+2]=z;
			Node tex = curr.getElementsByTagName("texcoord").item(0);
			texCoords[veces*2]=Float.parseFloat(tex.getAttributes().getNamedItem("u").getTextContent());
			texCoords[veces*2+1]=Float.parseFloat(tex.getAttributes().getNamedItem("v").getTextContent());
		}
		
		makeModel(coords,null,texCoords, indices, textura, gl);
		
	}
	
	/**Metodo que dibuja el mesh indicado en la posicion del hueso
	 * 
	 * @param model Modelo que se desea dibujar
	 * @param textura Textura del modelo
	 */
	public void load(InputStream model, InputStream textura, GL10 gl){
		
		Scanner scn = new Scanner(model);
		
		String[] tmp = scn.nextLine().split("[\\s]+");
		float[] vertices = new float[tmp.length];
		
		for(int veces=0;veces<vertices.length;veces++)
			vertices[veces]=Float.parseFloat(tmp[veces]);
		
		tmp = scn.nextLine().split("[\\s]+");
		float[] texCoords = new float[tmp.length];
		
		for(int veces=0;veces<texCoords.length;veces++)
			texCoords[veces]=Float.parseFloat(tmp[veces]);
		
		tmp = scn.nextLine().split("[\\s]+");
		short[] indices = new short[tmp.length];
		
		for(int veces=0;veces<indices.length;veces++)
			indices[veces]=(short)Integer.parseInt(tmp[veces]);
		
		makeModel(vertices,null,texCoords, indices, textura, gl);
		
	}
	
	/**Metodo que prepara el hueso para dibujar los indices y coordenadas indicados.
	 * 
	 * @param vertices Vertices del modelo
	 * @param colores Colores de cada vertice del modelo (4 por vertice, null dibuja sin color)
	 * @param texCoords Coordenadas de cada vertice en la textura (null dibuja sin textura)
	 * @param indices Indices de cada vertice
	 * @param textura Textura del modelo
	 */
	private void makeModel(float[] vertices, float[] colores, float[] texCoords, short[] indices, InputStream textura, GL10 gl){
		
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		this.mesh=vbb.asFloatBuffer();
		this.mesh.put(vertices);
		this.mesh.position(0);
		
		ByteBuffer ibb=ByteBuffer.allocateDirect(indices.length*2);
		ibb.order(ByteOrder.nativeOrder());
		this.indices=ibb.asShortBuffer();
		this.indices.put(indices);
		this.indices.position(0);
		this.indicesSize=indices.length;
		
		if(colores!=null){
			
			ByteBuffer cbb=ByteBuffer.allocateDirect(colores.length*4);
			cbb.order(ByteOrder.nativeOrder());
			this.colores=cbb.asFloatBuffer();
			this.colores.put(colores);
			this.colores.position(0);
		}else{
			this.colores=null;
		}
		
		if(texCoords!=null){
			
			ByteBuffer tbb=ByteBuffer.allocateDirect(texCoords.length*4);
			tbb.order(ByteOrder.nativeOrder());
			this.texCoords=tbb.asFloatBuffer();
			this.texCoords.put(texCoords);
			this.texCoords.position(0);
			
			loadTexture(textura,gl);
		}else{
			this.texCoords=null;
		}
	}
	
	/**Metodo que carga una textura para el mesh del hueso seleccionado
	 * 
	 * @param textura Stream que contiene la imagen que se utilizara como textura
	 * @param gl Instancia de opengl donde se desea utilizar la textura.
	 */
	private void loadTexture(InputStream textura, GL10 gl){
		
		Bitmap bmp=null;
		bmp=BitmapFactory.decodeStream(textura);
		/*
		try {
			textura.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textura=null;*/
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
	
	/**Debuelve la longitud del buffer de indices
	 * 
	 * @return longitud del buffer de indices
	 */
	public int indicesSize(){return indicesSize;}
	
	private void addChild(Bone child){
		
		if(children_count<children.length)
			children[children_count++]=child;
		else{
			
			Bone[] tmp = children;
			children=new Bone[tmp.length+20];
			
			for(int count=0; count<tmp.length; count++)
				children[count]=tmp[count];
			
			children[children_count++]=child;
		}
	}
}
