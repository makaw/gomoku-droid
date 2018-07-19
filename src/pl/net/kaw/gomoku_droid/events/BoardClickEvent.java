/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;

import android.graphics.Point;

/**
 * Zdarzenie: kliknięcie na planszy
 * @author Maciej Kawecki
 * 
 */
public final class BoardClickEvent { 
    
  /** Współrzędne */
  private final Point coords;
  
  
  public BoardClickEvent(Point coords) {
	this.coords = coords;
  }
	
  
  public int getA() {
	return coords.x;
  }
  
  public int getB() {
	return coords.y;
  }  
    
}
