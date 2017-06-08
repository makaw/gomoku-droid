/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.os.Bundle;
import android.view.View;
import pl.net.kaw.gomoku_droid.R;


/**
*
* Aktywność rozgrywki
* 
* @author Maciej Kawecki
* 
*/
public class GameActivity extends AppActivity {
	
	/** Pasek narzędziowy */
	private GameToolbar toolbar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.game_activity);
      
      toolbar = new GameToolbar(this);
      toolbar.init();
      toolbar.startTimer();
        
    }    
    
    
    public void endGame() {
    	
	  ModDialog.showConfirmDialog(this, getString(R.string.confirm_end_game), 
		new View.OnClickListener() {			
		  @Override
		  public void onClick(View v) {
		    v.startAnimation(BUTTON_CLICK);
		    GameActivity.this.finish();
		  }
		}
	  );	
    	
    }
    
    
    @Override
    public void onPause() {
      toolbar.pauseTimer();
      super.onPause();
    }
    
    @Override
    public void onResume() {
      toolbar.resumeTimer();
      super.onResume();
    }    
    
    
    @Override
    public void onBackPressed() {
    	
      endGame();	
    	
    }
    
    
    @Override
    public void onDestroy() {
    	
      toolbar.stopTimer();	
      super.onDestroy();	
      
    }
    
    
    
}
