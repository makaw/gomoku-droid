/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

/**
 *
 * Gracz - człowiek
 * 
 * @author Maciej Kawecki
 * 
 */
public class PlayerHuman extends Player {
    

   public PlayerHuman(BoardFieldState pieceColor, String name) {
       
     super(pieceColor, name, true);
       
   }  
       
    
}