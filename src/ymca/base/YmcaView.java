package ymca.base;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class YmcaView extends GLSurfaceView{

    private YmcaRenderer renderer;
    public YmcaView(Context context) {
	super(context);
	renderer=new YmcaRenderer(context);
	//renderer=new YmcaRenderer();
	setRenderer(renderer);
    }    

    @Override
    public void onPause(){
	super.onPause();	
	renderer.onPause();
    }
	
    @Override
    public void onResume(){
	
	super.onResume();
	renderer.onResume();
    }
	
	public boolean onTouchEvent(final MotionEvent event){
		
		queueEvent(new Runnable(){

			public void run() {
				
				if(event.getY()<getHeight()/4){
					if(event.getX()<getWidth()/4)
						renderer.siguienteCancion();
					else if(event.getX()<2*getWidth()/4)	
						renderer.siguienteBaile();
					else if(event.getX()<3*getWidth()/4)
						renderer.siguientePersonaje();
					else 
						renderer.siguienteLugar();
				}
			}
			
			
		});
		
		return true;
	}

}
