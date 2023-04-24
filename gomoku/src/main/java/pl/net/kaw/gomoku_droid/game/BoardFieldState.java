/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import java.util.Locale;

import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.Helpers;
import pl.net.kaw.gomoku_droid.app.IConfig;

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
       AppBase context = AppBase.getInstance();
       Locale locale = context.getSettings().getLanguage().getLocale();
       int res = this == WHITE ? R.string.white : R.string.black;
	   return Helpers.getLocaleString(locale, res, context);
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
   public String getWinningRow(int cnt)
   {
	  StringBuilder row = new StringBuilder();
	  for (int i=0; i<cnt; i++) row.append(this.toString());
	  return row.toString();
   }
   
   /**
    * Kolor rozpoczynający
    * @return jw
    */
   public static BoardFieldState getFirst() {
	 return IConfig.BLACK_STARTS ? BoardFieldState.BLACK : BoardFieldState.WHITE;   	   
   }
   
   public static BoardFieldState getSecond() {
	 return !IConfig.BLACK_STARTS ? BoardFieldState.BLACK : BoardFieldState.WHITE;   	   
   }
   
   
   public static BoardFieldState getState(char code) {	   
	 for (BoardFieldState state: BoardFieldState.values()) {
	   if (state.getCode() == code) return state;
	 }   
	 return null;
   }
   
   
}
