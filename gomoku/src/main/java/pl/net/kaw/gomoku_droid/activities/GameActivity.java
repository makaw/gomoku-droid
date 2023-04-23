/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.Callable;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.gui.BoardGraphics;
import pl.net.kaw.gomoku_droid.activities.gui.GameToolbar;
import pl.net.kaw.gomoku_droid.activities.gui.ModDialog;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.events.BoardClickEvent;
import pl.net.kaw.gomoku_droid.events.GameOverEvent;
import pl.net.kaw.gomoku_droid.events.PlaySoundEvent;
import pl.net.kaw.gomoku_droid.events.ProgressEvent;
import pl.net.kaw.gomoku_droid.events.ZoomChangedEvent;
import pl.net.kaw.gomoku_droid.game.Game;


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
    /** Wątek gry */
    private Game game;

    private AlertDialog progress;
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.game_activity);
      
      boardGraphics = (BoardGraphics) findViewById(R.id.board);
      boardGraphics.init();
      
      toolbar = new GameToolbar(this);
      toolbar.init();
      toolbar.startTimer();    
      
      AppEventBus.register(this);

      game = new Game(new Handler(Looper.getMainLooper()));
      game.start();
      message(getString(R.string.game_started));
      playSound(PlaySoundEvent.SoundType.INFO);
      
      buildProgressDialog();
      
    }   
    
    
    public BoardGraphics getBoardGraphics() {
      return boardGraphics;
    }

    
    private void doInterruptGame() {
     
      try {
    	AppEventBus.unregister(this);
      }
      catch (Exception e) {
          Log.e(tag, e.getMessage());
      }

      game.stopRunning();
      if (game.isAlive()) try {
    	game.interrupt();
      }
      catch (Exception e) {
          Log.e(tag, e.getMessage());
      }
      
      toolbar.stopTimer();
    }
    
    
    private void doRestartGame() {
      
      doInterruptGame();
      boardGraphics.clear();
      toolbar.init();
	  toolbar.startTimer();
	  
	  AppEventBus.register(this);
	  message(getString(R.string.game_started));
	  playSound(PlaySoundEvent.SoundType.INFO);
	  boardGraphics.init();
	  game = new Game(new Handler(Looper.getMainLooper()));
	  game.start();
    }
    
    
    public void restartGame() {
    	
      if (game.isRunning())	{
  	    ModDialog.showConfirmDialog(this, getString(R.string.confirm_restart_game), (Callable<Void>) () -> {
            doRestartGame();
            return null;
          });
      }
      else doRestartGame();
    	
    }
    
    @Subscribe
    public void noticePlaySound(final PlaySoundEvent event) {      
      playSound(event.getType());
    }
    
    
    @Subscribe
    public void noticeZoomChange(final ZoomChangedEvent event) {
      toolbar.tryToEnableZoomButtons(event.getFactor());
    }

    
    @Subscribe
    public void setProgress(final ProgressEvent event) {   
      if (progress == null) return;
      if (progress.isShowing()) progress.dismiss();
      else progress.show();      
    }
    
    
    
    @Subscribe    
    public void noticeBoardClick(final BoardClickEvent event) {
      synchronized(game){
    	if (game.setLastClick(event)) game.notifyAll();
      }
    }
    
    
    @Subscribe
    public void noticeGameOver(final GameOverEvent event) {
    	
      toolbar.stopTimer();	
    	
      if (event.getWinRow() == null) {
    	 ModDialog.showInfoDialog(this, getString(R.string.draw_title), R.drawable.ic_dialog_draw, getString(R.string.draw));
      }
      
      else {    	
    	 int ico = event.getWinner().isHuman() ? R.drawable.ic_dialog_win : R.drawable.ic_dialog_loose;
    	 String title = getString(R.string.winner_title, event.getWinner().getName());
    	 String msg = getString(R.string.winner, event.getWinner().getName(), event.getMoveNo());
    	 ModDialog.showInfoDialog(this, title, ico, msg);
      }
    	
    }
    
    
    
    /**
     * Zakończenie rozgrywki
     */
    public void endGame()
    {
	  ModDialog.showConfirmDialog(this, getString(R.string.confirm_end_game), (Callable<Void>) () -> {
        GameActivity.this.finish();
        return null;
      });
    }
    
    
    private void buildProgressDialog() {

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setCancelable(false);
      builder.setView(R.layout.progress_dialog);
      progress = builder.create();
      progress.setCanceledOnTouchOutside(true);
      progress.setOnCancelListener(dialog -> {
          dialog.dismiss();
          game.sleep();
          ModDialog.showConfirmDialog(GameActivity.this, getString(R.string.confirm_restart_game), () -> {
            doInterruptGame();
            message(getString(R.string.game_interrupted));
            return null;
          }, (Callable<Void>) () -> {
            game.notifyAll();
            AppEventBus.post(new ProgressEvent());
            return null;
          });
      });
      
    }
    
    
    
    
    @Override
    public void onPause() {
      toolbar.pauseTimer();
      super.onPause();
    }
    
    @Override
    public void onResume() {
      if (game.isRunning()) {
          toolbar.resumeTimer();
      }
      super.onResume();
    }    
    
    
    @Override
    public void onBackPressed() {
    	
      if (progress.isShowing()) return;
      endGame();	
    	
    }
    
    
    @Override
    public void onDestroy() {
    	
      boardGraphics.clear();
      game.stopRunning();	
      game.interrupt();      
      toolbar.stopTimer();	
      
      try {
	    AppEventBus.unregister(GameActivity.this);
	  }
	  catch (Exception e) {
        Log.e(tag, e.getMessage());
      }
      
      super.onDestroy();	
      
    }
    
    
    
}
