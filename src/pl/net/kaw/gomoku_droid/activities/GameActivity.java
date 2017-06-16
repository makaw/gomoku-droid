/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import java.util.concurrent.Callable;

import android.os.Bundle;
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
    	
	  ModDialog.showConfirmDialog(this, getString(R.string.confirm_end_game), new Callable<Void>() {			
		  @Override
		  public Void call() {
		    GameActivity.this.finish();
		    return null;
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
