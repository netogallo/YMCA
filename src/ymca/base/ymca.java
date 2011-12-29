package ymca.base;

import android.app.Activity;
import android.os.Bundle;

public class ymca extends Activity {
	
    private YmcaView view;
    public void onCreate(Bundle instance){
		
	super.onCreate(instance);
	this.view = new YmcaView(this);
	this.setContentView(view);
    }	

    @Override
	protected void onResume(){
	
	super.onResume();
	this.view.onResume();
    }

    @Override
	protected void onPause(){
		
	super.onPause();
	this.view.onPause();
	//super.finish();
    }       

    @Override
    protected void onStop(){

	super.onStop();
	this.view.onPause();
	//super.finish();
    }
        
        
}
