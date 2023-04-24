/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;

import pl.net.kaw.gomoku_droid.game.BoardField;

/**
 * Zdarzenie: ruch gracza
 * @author Maciej Kawecki
 * 
 */
public class PlayerMoveEvent { 
    
  /** Pole planszy */
  private final BoardField field;
  
  /** Nazwa gracza */
  private final String playerName;
  
  
  public PlayerMoveEvent(BoardField field, String playerName) {
	this.field = field;
	this.playerName = playerName;
  }
	
  
  public BoardField getField() {
	return field;
  }  
  
  
  public String getPlayerName() {
	return playerName;
  }  
  
  
  @Override
  public String toString() {
	try {
	  return playerName + " " + field.getState() + " / " + field.getA() + "/" + field.getB();  
	}
	catch (NullPointerException e) {
	  return "null";	
	}  
  }
    
}
