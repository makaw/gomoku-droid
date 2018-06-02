/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities.gui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.AboutActivity;
import pl.net.kaw.gomoku_droid.activities.AppActivity;
import pl.net.kaw.gomoku_droid.activities.GameActivity;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.Helpers;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.app.SecTimer;


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
  private final SecTimer timer;
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
	timer = new SecTimer(clockView);
	  
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
		}
	});
    
    zoomOutBtn.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  activity.getBoardGraphics().zoom(true);
		}
	});
    
    Button restartBtn = (Button) activity.findViewById(R.id.gtb_restart_btn);
    restartBtn.setOnClickListener(new View.OnClickListener() {		
	  @Override
	  public void onClick(View v) {
		v.startAnimation(AppActivity.BUTTON_CLICK);
		activity.message(activity.getString(R.string.not_implemented));
	  }
	});
   
  }
  
  
  /**
   * Blokada przycisków zoom +-
   * @param factor Aktualny współczynnik zoom
   */
  public void tryToEnableZoomButtons(float factor) {	  	  	  
	 Button zoomInBtn = (Button) activity.findViewById(R.id.gtb_zoom_in_btn);
	 Button zoomOutBtn = (Button) activity.findViewById(R.id.gtb_zoom_out_btn);
	 enableBtn(zoomInBtn, isZoomEnabled(false, factor));
	 enableBtn(zoomOutBtn, isZoomEnabled(true, factor));
  }
  
  
  
  
  /**
   * Czy można zmienić powiększenie planszy (przyciski)
   * @param out True=zmniejszenie
   * @param factor Bieżący zoom
   * @return j.w.
   */
  private boolean isZoomEnabled(boolean out, float factor) {	  
	float f = factor + (out ? -1.0f : 1.0f) * IConfig.ZOOM_FACTOR_STEP;
	return f >= IConfig.MIN_ZOOM_FACTOR && f <= IConfig.MAX_ZOOM_FACTOR;  	  
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
