/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import java.util.Locale;

/**
*
* Interfejs konfiguracyjny
* 
* @author Maciej Kawecki
* 
*/
public interface IConfig {

	/** Najmniejsza możliwa liczba wierszy (nieparzyste) */
	int MIN_COLS_AND_ROWS = 7;
	/** Największa możliwa liczba wierszy (nieparzyste) */
	int MAX_COLS_AND_ROWS = 15;   
	/** Domyślna liczba wierszy i kolumn planszy */
	int DEFAULT_COLS_AND_ROWS = 15;
	   
	/** Najmniejsza możliwa liczba kamieni w rzędzie wymagana do wygranej */
	int MIN_PIECES_IN_ROW = 3;
	/** Największa możliwa liczba kamieni w rzędzie */
	int MAX_PIECES_IN_ROW = 5;
	/** Domyślna liczba kamieni w rzędzie */
	int DEFAULT_PIECES_IN_ROW = 5;	
	   
	/** Czy komputer zaczyna grę (czarne) */
	boolean DEFAULT_COMPUTER_STARTS = true;  

	/** Domyślnie - czy włączony jest dźwięk */
	boolean DEFAULT_ENABLE_SOUND = true;     	
	
	/** Czy wł. debugowanie */
	boolean DEBUG = false;
	
	/** Dostępne lokalizacje */
	Locale[] LOCALES = { new Locale("en", "US"), new Locale("pl", "PL")};
	/** Domyślna lokalizacja (indeks) */
	int DEFAULT_LOCALE_INDEX = 0;	
	
	/** Min współczynnik powiększenia planszy */
	float MIN_ZOOM_FACTOR = 0.5f;	
	/** Max współczynnik powiększenia planszy */
	float MAX_ZOOM_FACTOR = 2.0f;	
	/** Krok powiększenia planszy */
	float ZOOM_FACTOR_STEP = 0.1f;
	
	/** Wersja aplikacji */
	String VERSION = "0.0.7 (build %s)";
	
}
