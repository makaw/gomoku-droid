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
import android.view.View;
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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);		
	  	 
	  setContentView(R.layout.main_activity);
	  
	  Typeface font = Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf");
	  TextView startItem = (TextView) findViewById(R.id.start_item);
	  startItem.setTypeface(font);
	  TextView aboutItem = (TextView) findViewById(R.id.about_item);
	  aboutItem.setTypeface(font);
	  TextView settingsItem = (TextView) findViewById(R.id.settings_item);
	  settingsItem.setTypeface(font);
	  final TextView exitItem = (TextView) findViewById(R.id.exit_item);
	  exitItem.setTypeface(font);	
	  
	  
	  startItem.setOnClickListener(new View.OnClickListener() {
			
		  @Override
		  public void onClick(View view) {
			  
			  view.startAnimation(BUTTON_CLICK);
			  startActivity(new Intent(MainActivity.this, GameActivity.class));
			 
			}
		  
	  });	  
	  
	  
	  aboutItem.setOnClickListener(new View.OnClickListener() {
		
		  @Override
		  public void onClick(View view) {
			  
			  view.startAnimation(BUTTON_CLICK);
			  startActivity(new Intent(MainActivity.this, AboutActivity.class));
			 
			}
		  
	  });
	  
	  
	  exitItem.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {			
			
			  
		  v.startAnimation(BUTTON_CLICK);
	
			
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
