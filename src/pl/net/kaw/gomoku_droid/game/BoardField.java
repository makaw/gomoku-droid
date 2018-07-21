/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import java.io.Serializable;

import fr.pixelprose.minimax4j.Move;


/**
 *
 * Pojedyncze pole na planszy 
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardField implements Serializable, Move {    
    
   /** Indeks a (kolumna) pola planszy */ 
   private final int a;
   /** Indeks b (wiersz) pola planszy */
   private final int b;
   /** Stan pola planszy */
   private BoardFieldState state;
   /** Czy zaznaczone */
   private boolean checked = false;
   
   
   private final static long serialVersionUID = 1L;
   
   
   /** 
    * Konstruktor 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    * @param state Początkowy stan pola
    */
   public BoardField(int a, int b, BoardFieldState state) {
       
     this.a = a;
     this.b = b;
     this.state = state;
       
   }   
   
   /** 
    * Konstruktor 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    */
   public BoardField(int a, int b) {
       
     this(a, b, BoardFieldState.EMPTY);
       
   }   
   
   
   public int getA() {
       
     return a;
       
   }
   
   public int getB() {
       
     return b;  
       
   }
   
   public BoardFieldState getState() {
       
      return state; 
       
   }
   
   public void setState(BoardFieldState state) {
       
      this.state = state; 
       
   }
   
   
   public boolean isChecked() {
	 return checked;
   }
   
   public void setChecked(boolean checked) {
	 this.checked = checked;
   }
   
   
   
   
   /**
    * Etykieta pola A(X)
    * @param a Indeks pola A(X)
    * @return Etykieta
    */
   public static String getLabA(int a) {
	 return Character.toString((char)('A' + a));
   }
   
   /**
    * Etykieta pola B(Y)
    * @param b Indeks pola B(Y)
    * @param colsAndRows Liczba wierszy/kolumn
    * @return Etykieta
    */
   public static String getLabB(int b, int colsAndRows) {
	 return String.valueOf(colsAndRows - b);    
   }

   @Override
   public int hashCode() {
	 final int prime = 31;
	 int result = 1;
	 result = prime * result + a;
	 result = prime * result + b;
	 return result;
   }

   @Override
   public boolean equals(Object obj) {
	 if (this == obj)
		return true;
	 if (obj == null)
		return false;
	 if (getClass() != obj.getClass())
		return false;
	 BoardField other = (BoardField) obj;
	 if (a != other.a)
		return false;
	 if (b != other.b)
		return false;
	 return true;
   }
        
   
   
}
