/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.events;

import java.util.List;

import pl.net.kaw.gomoku_droid.game.BoardField;
import pl.net.kaw.gomoku_droid.game.Player;

/**
 * Zdarzenie: koniec gry
 * @author Maciej Kawecki
 * 
 */
public class GameOverEvent { 
    
  /** Lista pól do zaznaczenia */
  private final List<BoardField> winRow;
  
  /** Zwycięzca */
  private final Player winner;
  
  /** Liczba ruchów */
  private final int moveNo;
  
  
  public GameOverEvent(List<BoardField> winRow, Player winner, int moveNo) {
	this.winRow = winRow;
	this.winner = winner;
	this.moveNo = moveNo;
  }


  public List<BoardField> getWinRow() {
	return winRow;
  }


  public Player getWinner() {
	return winner; 
  }
	
  
  public int getMoveNo() {
	return moveNo;
  }
  
  
    
}
