/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;

import android.graphics.Point;

/**
 * Zdarzenie: kliknięcie - ruch gracza
 * @author Maciej Kawecki
 * 
 */
public final class PlayerMoveEvent { 
    
  /** Współrzędne */
  private final Point coords;
  
  
  public PlayerMoveEvent(Point coords) {
	this.coords = coords;
  }
	
  
  public int getX() {
	return coords.x;
  }
  
  public int getY() {
	return coords.y;
  }  
    
}
