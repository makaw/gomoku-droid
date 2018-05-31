/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import pl.net.kaw.gomoku_droid.activities.gui.BoardGraphics;

/**
 *
 * Ogólny szablon gracza
 * 
 * @author Maciej Kawecki
 * 
 */
public abstract class Player {
    
   /** Kolor kamieni gracza - biały lub czarny */
   protected final BoardFieldState pieceColor;  
   /** Nazwa gracza (klucz) */
   protected final String name;
   /** Referencja do obiektu będącego graficzną reprezentacją planszy */
   protected final BoardGraphics gBoard;
   /** Referencja do obiektu logicznej warstwy planszy */
   protected final Board lBoard;
   /** Zmienna wymuszająca zakończenie ruchu w razie zakończenia gry */
   protected boolean gameRestarted;
   /** Ostatni ruch gracza */
   protected BoardField lastMove;
   /** Parametry (nazwa) */
   private final Object[] nameParams;
   
   
   /** 
    * Konstruktor (dla rozszerzających klas), przypisanie wartości/referencji do 
    * wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    * @param nameParams Parametry (nazwa)
    */
   public Player(BoardFieldState pieceColor, BoardGraphics gBoard, Board lBoard,
		   String name, Object... nameParams) {
       
     this.pieceColor = pieceColor;
     this.gBoard = gBoard;
     this.lBoard = lBoard;
     this.name = name;
     gameRestarted = false;
     this.nameParams = nameParams;
       
   }  
   
   
   /**
    * Metoda wymuszająca zakończenie ruchu gracza
    */
   public void forceEndTurn() {
       
      gameRestarted = true; 
       
   }
   
   /**
    * Metoda abstrakcyjna, wykonanie ruchu, specyficzne dla graczy różnych typów
    */
   public abstract void makeMove();

   

   public BoardFieldState getPieceColor() {  
       
     return pieceColor; 
   
   }   
  
    
   /**
    * Metoda pobierająca nazwę gracza
    * @return Nazwa gracza
    */
   public String getName() {
       
      //return Lang.get(name, nameParams) + " (" + pieceColor.getName() + ")";
      return name + " (" + pieceColor.getName() + ")";
       
   }
   
   
   
   
   public BoardField getLastMove() {	   
	   
	  return lastMove; 
	   
   }
      
    
}
