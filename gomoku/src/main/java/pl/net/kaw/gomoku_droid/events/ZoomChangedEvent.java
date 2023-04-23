/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;




/**
 * Zdarzenie: zmiana skalowania planszy
 * @author Maciej Kawecki
 * 
 */
public final class ZoomChangedEvent { 
    
  /** Nowy współczynnik skalowania */
  private final float factor;
  
  
  public ZoomChangedEvent(float factor) {
	this.factor = factor;
  }
	
  
  public float getFactor() {
	return factor;
  }
    
}
