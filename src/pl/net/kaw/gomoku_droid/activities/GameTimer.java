package pl.net.kaw.gomoku_droid.activities;

import android.os.Handler;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.app.Helpers;


/**
*
* Licznik czasu obsługi zlecenia
* 
* @author Maciej Kawecki
* 
*/
public class GameTimer extends Handler {

  /** Pole licznika */
  private final TextView timeView;
  /** Uruchamiane zadanie */
  private GameTimerTask task;
  
  
  /** Klasa uruchamianego zadania */
  private class GameTimerTask implements Runnable {
	  
	private long seconds;
	
	public GameTimerTask(long seconds) {
	  this.seconds = seconds;
	}
			 
	@Override
	public void run() {
	  seconds++;
	  try {
		synchronized(timeView) {
	      timeView.setText(Helpers.timeSecondsToString(seconds));
		}
	  }
	  catch (Exception e) {}
	  GameTimer.this.postDelayed(this, 1000);
	}
			
	public long getSeconds() {
	  return seconds;
	}	  
	  	  
  }
  
  
	
  /**
   * Konstruktor
   * @param timeView Pole licznika
   */
  public GameTimer(TextView timeView) {	
	this.timeView = timeView;
  }
  
  
  /**
   * Uruchomienie zadania
   * @param initTime Początkowy czas w sek.
   */
  public void start(final long initTime) {
	  
	 stop();
	 timeView.setText(Helpers.timeSecondsToString(initTime)); 	  
	 task = new GameTimerTask(initTime);	 
	 postDelayed(task, 1000);
	  
  }
	
  
  /**
   * Zatrzymanie zadania
   */
  public void stop() {
	
	removeCallbacks(task);
	  
  }
  
  
  /**
   * Zwraca bieżący stan licznika
   * @return j.w.
   */
  public long getSeconds() {
	  
	return task.getSeconds();
	
  }
	
	
}
