/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;

/**
 * Zdarzenie: odegranie dźwięku
 * @author Maciej Kawecki
 * 
 */
public class PlaySoundEvent { 
    
  public enum SoundType {
	  MOVE, SUCCESS, INFO;
  }
  
  	
  /** Rodzaj dźwięku */
  private final SoundType type;
  
  
  public PlaySoundEvent(SoundType type) {
	this.type = type;
  }
	
  
  public SoundType getType() {
	return type;
  }
    
}
