/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;


/**
*
* Ekran ustawień
* 
* @author Maciej Kawecki
* 
*/
public class SettingsActivity extends AppActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.settings_activity);
        
      TextView backItem = (TextView) findViewById(R.id.back_item);
      backItem.setOnClickListener(new View.OnClickListener() {
			
		@Override
		public void onClick(View v) {
		  v.startAnimation(BUTTON_CLICK);
		  SettingsActivity.this.finish();
		}
	  });
        
      TextView saveItem = (TextView) findViewById(R.id.save_item);
      saveItem.setOnClickListener(new View.OnClickListener() {
			
		@Override
		public void onClick(View v) {
		  v.startAnimation(BUTTON_CLICK);
		  //
		  //SettingsActivity.this.finish();
		}
	  });      
         
  	  backItem.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf"));
  	  saveItem.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf"));
  	  
    }
    
}
