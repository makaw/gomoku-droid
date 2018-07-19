/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

/*
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
   /** Ostatni ruch gracza */
   protected BoardField lastMove;
   /** Czy to gracz */
   protected final boolean human;
   
   
   /** 
    * Konstruktor (dla rozszerzających klas), przypisanie wartości/referencji do 
    * wewnętrznych pól klasy
    * @param context Kontekst
    * @param pieceColor Kolor kamieni gracza
    * @param name Nazwa gracza
    * @param human Czy to gracz
    */
   protected Player(BoardFieldState pieceColor, String name, boolean human) {
       
     this.pieceColor = pieceColor;
     this.name = name;
     this.human = human;
     
   }  
   
  
   

   public BoardFieldState getPieceColor() {  
       
     return pieceColor; 
   
   }   
  
    
   /**
    * Metoda pobierająca nazwę gracza
    * @return Nazwa gracza
    */
   public String getName() {
       
      return name + " (" + pieceColor.getName() + ")";
       
   }
   

   public BoardField getLastMove() {	   
	   
	  return lastMove; 
	   
   }

   public boolean isHuman() {
	 return human;
   }
      
    
}
