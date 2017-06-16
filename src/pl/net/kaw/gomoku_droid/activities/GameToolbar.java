/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;

import com.example.android.cheatsheet.CheatSheet;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.Helpers;


/**
*
* Zarządzanie paskiem narzędzi ekranu gry
* 
* @author Maciej Kawecki
* 
*/
public class GameToolbar {

  /** Bieżąca aktywność */
  private final GameActivity activity;
  /** Licznik czasu gry */
  private final GameTimer timer;
  /** Zatrzymany stan licznika czasu */
  private long timerSecTmp = 0;
  
  
  /**
   * Konstruktor
   * @param activity Bieżąca aktywność
   */
  public GameToolbar(GameActivity activity) {
	  
	this.activity = activity;	
	TextView clockView = (TextView) activity.findViewById(R.id.gtb_clock);
	clockView.setText("00:00:00");
	timer = new GameTimer(clockView);
	  
  }
  
  /**
   * Inicjalizacja paska narzędzi
   */
  public void init() {
	  
	Button backBtn = (Button) activity.findViewById(R.id.gtb_back_btn);
    backBtn.setOnClickListener(new View.OnClickListener() {		
	  @Override
	  public void onClick(View v) {
		v.startAnimation(AppActivity.BUTTON_CLICK);
		activity.endGame();
	  }
	});
    
    Button helpBtn = (Button) activity.findViewById(R.id.gtb_help_btn);
    helpBtn.setOnClickListener(new View.OnClickListener() {		
	  @Override
	  public void onClick(View v) {
		 v.startAnimation(AppActivity.BUTTON_CLICK);
		 activity.startActivity(new Intent(activity, AboutActivity.class));	
	  }
	});      
      
    
    final Button soundBtn = (Button) activity.findViewById(R.id.gtb_sound_btn);
    boolean snd = AppBase.getInstance().getSettings().isSoundEnabled();
    soundBtn.setBackgroundResource(snd ? R.drawable.btn_sound : R.drawable.btn_sound_off); 
    
    soundBtn.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  boolean snd = AppBase.getInstance().getSettings().isSoundEnabled();
		  AppBase.getInstance().getSettings().save(!snd);
		  soundBtn.setBackgroundResource(snd ? R.drawable.btn_sound_off : R.drawable.btn_sound);
		}
	});
    
    final Button zoomInBtn = (Button) activity.findViewById(R.id.gtb_zoom_in_btn);
    final Button zoomOutBtn = (Button) activity.findViewById(R.id.gtb_zoom_out_btn);
    
    zoomInBtn.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  activity.getBoardGraphics().zoom(false);
		  enableZoomBtn(zoomInBtn, zoomOutBtn);
		}
	});
    
    zoomOutBtn.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  activity.getBoardGraphics().zoom(true);
		  enableZoomBtn(zoomInBtn, zoomOutBtn);
		}
	});
    
    // tooltips
    CheatSheet.setup(zoomInBtn);
    CheatSheet.setup(zoomOutBtn);
    CheatSheet.setup(helpBtn);
    CheatSheet.setup(soundBtn);
    CheatSheet.setup(activity.findViewById(R.id.gtb_restart_btn));
    CheatSheet.setup(backBtn);    
	  	  
  }
  
  /**
   * Blokada przycisków powiększenia
   * @param zoomInBtn Przycisk zoom+
   * @param zoomOutBtn Przycisk zoom-
   */
  private void enableZoomBtn(Button zoomInBtn, Button zoomOutBtn) {

	enableBtn(zoomInBtn, activity.getBoardGraphics().isZoomEnabled(false));
	enableBtn(zoomOutBtn, activity.getBoardGraphics().isZoomEnabled(true));
	
  }
  
  /**
   * Blokada przycisku
   * @param button Przycisk
   * @param enabled Dostępny (true), czy zablokowany
   */
  private void enableBtn(Button button, boolean enabled)  {

    Helpers.setBrightness(button.getBackground(), enabled ? 0 : -90);
    button.setEnabled(enabled);
    
  } 
  
  
  
  /**
   * Uruchomienie licznika
   */
  public void startTimer() {
	timer.start(0);
  }
  
  /**
   * Wstrzymanie licznika
   */
  public void pauseTimer() {
	timerSecTmp = timer.getSeconds();
	timer.stop();
  }
  
  /**
   * Wznowienie licznika
   */
  public void resumeTimer() {
	timer.start(timerSecTmp);
  }
  
  /**
   * Zatrzymanie licznika
   */
  public void stopTimer() {
	 timerSecTmp = 0; 
	 timer.stop();
  }	  
  
  
}
