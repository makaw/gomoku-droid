/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import android.content.Context;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;

/**
 *
 * Stan pola planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public enum BoardFieldState {
    

    /** Puste pole */
   EMPTY('0'), 
	   
   /** Biały kamień */
   WHITE('W'), 
	   
   /** Czarny kamień */
   BLACK('B');
	
	
   private final char code;
   

   
   BoardFieldState(char code) {
	   this.code = code;
   }
   
   
   public char getCode() {
	   return code;
   }
   
   
   /**
    * Kolor kamieni przeciwnika
    * @return Kolor przeciwnika
    */
   public BoardFieldState getOpposite() {
	   
	  switch (this) {
	  
	    default: return EMPTY;
	    case WHITE : return BLACK;
	    case BLACK : return WHITE;
	
	  }
	   	   
   }
   
   
   /**
    * Nazwa koloru/stanu
    * @return Nazwa
    */
   public String getName() {
	   
	   Context context = AppBase.getInstance().getBaseContext();
	   
	   switch (this) {
	     default: return "-";
	     case WHITE: return context.getString(R.string.white);
	     case BLACK: return context.getString(R.string.black);
	   }
	   
   }
   
   
   @Override
   public String toString() {
	   
	 return String.valueOf(code);
	   
   }
   
   
   /**
    * Wygrywający rząd
    * @param cnt Długość rzędu (ustawienia)
    * @return Wygrywający rząd jako ciąg znaków
    */
   public String getWinningRow(int cnt) {
	   
	  String row = ""; 
	  for (int i=0; i<cnt; i++) row += this.toString();
	  return row;
	   
   }
   
   
}
