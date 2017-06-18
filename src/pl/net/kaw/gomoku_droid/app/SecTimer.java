/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import android.os.Handler;
import android.widget.TextView;


/**
*
* Licznik czasu mm:ss
* 
* @author Maciej Kawecki
* 
*/
public class SecTimer extends Handler {

  /** Pole licznika */
  private final TextView timeView;
  /** Uruchamiane zadanie */
  private SecTimerTask task;
  
  
  /** Uruchamiane zadanie */
  private class SecTimerTask implements Runnable {
	  
	private long seconds;
	
	public SecTimerTask(long seconds) {
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
	  SecTimer.this.postDelayed(this, 1000);
	}
			
	public long getSeconds() {
	  return seconds;
	}	  
	  	  
  }
  
  
	
  /**
   * Konstruktor
   * @param timeView Pole licznika
   */
  public SecTimer(TextView timeView) {	
	this.timeView = timeView;
  }
  
  
  /**
   * Uruchomienie zadania
   * @param initTime Początkowy czas w sek.
   */
  public void start(final long initTime) {
	  
	 stop();
	 timeView.setText(Helpers.timeSecondsToString(initTime)); 	  
	 task = new SecTimerTask(initTime);	 
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
