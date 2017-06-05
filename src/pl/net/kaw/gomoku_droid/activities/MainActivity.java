/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;


/**
*
* Menu główne - ekran startowy
* 
* @author Maciej Kawecki
* 
*/
public class MainActivity extends AppActivity {

	
	private DisplayMetrics mMetrics = new DisplayMetrics();
	//private float mScreenDensity;


	/** Animacja po kliknięciu przycisku */
	private static AlphaAnimation BUTTON_CLICK = new AlphaAnimation(1F, 0.7F);	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);		
	  	 
	  getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
	 // mScreenDensity = mMetrics.density;
	    
	  setContentView(R.layout.main_activity);
	  
	  Typeface font = Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf");
	  TextView startItem = (TextView) findViewById(R.id.start_item);
	  startItem.setTypeface(font);
	  final TextView aboutItem = (TextView) findViewById(R.id.about_item);
	  aboutItem.setTypeface(font);
	  TextView settingsItem = (TextView) findViewById(R.id.settings_item);
	  settingsItem.setTypeface(font);
	  final TextView exitItem = (TextView) findViewById(R.id.exit_item);
	  exitItem.setTypeface(font);	
	  
	  aboutItem.setOnClickListener(new View.OnClickListener() {
		
		  @Override
		  public void onClick(View view) {
			  
			  aboutItem.startAnimation(BUTTON_CLICK);
			  startActivity(new Intent(MainActivity.this, AboutActivity.class));
			 
			}
		  
	  });
	  
	  
	  exitItem.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {			
			
			  
		 exitItem.startAnimation(BUTTON_CLICK);
	
			
		  new AlertDialog.Builder(MainActivity.this)
	        .setCancelable(false)
	        .setMessage(getString(R.string.confirm_quit))
	        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {	        	
	           @Override
	           public void onClick(DialogInterface dialog, int which) {	            	    	   
	    	     ExitActivity.exit(getApplicationContext());
	           }
	       })
	       .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {	        	
	           @Override
	           public void onClick(DialogInterface dialog, int which) {	            	    	   
	        	   exitItem.clearAnimation();
	           }
	       })
	       .show();
						
		}
	});
	  	  
	 
	}
	

}
