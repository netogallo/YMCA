package ymca.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioFormat;

public class MusicPlayer {

	private static final int HEADER_SIZE = 44;
	private static final int scale=16;
	
	private byte[] song;
	private int format, channels,bits,dataSize,rate,bufferSize,currFreq,curr;
	private AudioTrack music;
	private Context context;
	private InputStream track;
	private Timer bufferTimer;
	private int[] playlist;
	private boolean swapping;

	/**Crea un reproductor de musica con capacidad para analizar 
	 * Audio.
	 * @param context Contex de la clase que tendra la instancia
	 * @throws IOException
	 */
	public MusicPlayer(Context context) throws IOException{
		
		this.currFreq=1;
		this.context=context;
		playlist=new int[2];
		playlist[0]=R.raw.usanthem15;
		playlist[1]=R.raw.ymca20;
		curr=0;
		swapping=false;
		this.next();
		//load(R.raw.usanthem);
		//swapping=false;
		
	}
	
	/**Inica la reproduccion de la cancion
	 * 
	 */
	public void play(){
		
		//if(this.music.getPlayState()!=AudioTrack.PLAYSTATE_PLAYING){
			this.bufferTimer = new Timer();
			this.bufferTimer.scheduleAtFixedRate(new addBytes(), 0, 500);
		//}
	}
	
	/**Metodo que detiene la cancion que actualmete se esta reproduciendo
	 * 
	 */
	public void stop(){
		
		if(this.music!=null){
			this.bufferTimer.cancel();
			this.music.stop();
		}
	}
	
	public void nextSong(){
		
		this.swapping=true;
		
	}
	
	/**Cambia a la siguiente cancion
	 * 
	 * @throws IOException
	 */
	public void next() throws IOException{
		
		load(this.playlist[this.curr]);
		
		this.curr=curr+1;
		
		if(this.curr>=this.playlist.length)
				this.curr=0;
		
	}
	
	/**Carga la cancion indicada al buffer de la musica
	 * 
	 * @param file Archivo que se desea cargar
	 * @throws IOException
	 */
	private void load(int file) throws IOException{
		
		stop();
		
		this.track=context.getResources().openRawResource(file);
		ByteBuffer head = ByteBuffer.allocate(HEADER_SIZE);
		head.order(ByteOrder.LITTLE_ENDIAN);
		
		this.track.read(head.array(), head.arrayOffset(), head.capacity());
		
		head.rewind();
		head.position(head.position()+20);
		this.format = head.getShort();
		this.channels = head.getShort();
		this.rate=head.getInt();
		head.position(head.position()+6);
		this.bits = head.getShort();
		this.dataSize=0;
	
		while(head.getInt()!=0x61746164){}
		
		dataSize=head.getInt();
		
		if(this.music!=null){
			this.music.flush();
			this.music.release();
		}
		
		this.bufferSize=rate*channels*2;
		this.song=new byte[this.bufferSize/2];
		
		this.music = new AudioTrack(
				AudioManager.STREAM_MUSIC,
				this.rate,
				this.channels==1?AudioFormat.CHANNEL_OUT_MONO:AudioFormat.CHANNEL_OUT_STEREO,
				AudioFormat.ENCODING_PCM_16BIT,
				this.bufferSize,
				AudioTrack.MODE_STREAM
						);
		
		this.music.play();
		
		for(int veces=0;veces<2;veces++){
			this.track.read(this.song, 0, this.song.length);
			this.music.write(this.song, 0, this.song.length);
		}
		
		this.play();
		
		this.swapping=false;
		
	}
	
	/**Debuelbe la frecuencia de sampleo acutal de la cancion
	 * Relativa al sampleo de la cancion.
	 * 
	 * @return Frecuencia relativa de sampleo
	 */
	public int getCurrentRate(){
		
		if(currFreq<1)
			currFreq*=-1;
		
		return this.currFreq/this.scale;
	}
	
	/**Clase que agrega bytes al buffer cada medio segundo
	 * 
	 * @author neto
	 *
	 */
	private class addBytes extends TimerTask{

		/**Metodo que se ejecuta 4 veces por segundo
		 * 
		 */
		public void run() {
			
			try {
				if(track.read(song, 0, song.length)>0){
					
					if(!swapping){
						music.write(song, 0, song.length);
						currFreq=song[0]-song[song.length/5000];
					}
				}else{
					
					next();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
