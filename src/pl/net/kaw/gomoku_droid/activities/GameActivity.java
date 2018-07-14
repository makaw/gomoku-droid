/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import android.os.Bundle;
import android.widget.Toast;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.gui.BoardGraphics;
import pl.net.kaw.gomoku_droid.activities.gui.GameToolbar;
import pl.net.kaw.gomoku_droid.activities.gui.ModDialog;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.events.PlayerMoveEvent;
import pl.net.kaw.gomoku_droid.events.ZoomChangedEvent;
import pl.net.kaw.gomoku_droid.game.BoardField;


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
	/** Komponent planszy */
	private BoardGraphics boardGraphics;

    /** Ilość wierszy i kolumn planszy */
    private int colsAndRows;
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.game_activity);
      
      boardGraphics = (BoardGraphics) findViewById(R.id.board);
      colsAndRows = AppBase.getInstance().getSettings().getColsAndRows();
      
      toolbar = new GameToolbar(this);
      toolbar.init();
      toolbar.startTimer();      
      
      AppEventBus.register(this);
        
    }   
    
    
    public BoardGraphics getBoardGraphics() {
      return boardGraphics;
    }
    
    
    public GameToolbar getGameToolbar() {
      return toolbar;
    }
    
    
    @Subscribe
    public void noticeZoomChange(final ZoomChangedEvent event) {
      toolbar.tryToEnableZoomButtons(event.getFactor());
    }
    
    // TODO
    @Subscribe
    public void noticePlayerMove(final PlayerMoveEvent event) {
      Toast.makeText(this, "field " + BoardField.getLabA(event.getX())
      	+ ":" + BoardField.getLabB(event.getY(), colsAndRows), Toast.LENGTH_SHORT).show();            
    }
    
    
    /**
     * Zakończenie rozgrywki
     */
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
      AppEventBus.unregister(this);
      super.onDestroy();	
      
    }
    
    
    
}
