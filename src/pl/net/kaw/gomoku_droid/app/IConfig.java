/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

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
	
	/** Domyślna minimalna wielkość pola planszy (w px) */
	int DEFAULT_MIN_PX_FIELD = 30;
	   
	/** Czy komputer zaczyna grę (czarne) */
	boolean DEFAULT_COMPUTER_STARTS = true;  

	/** Domyślnie - czy włączony jest dźwięk */
	boolean DEFAULT_ENABLE_SOUND = true;     	
	
	
	/** Wersja aplikacji */
	String VERSION = "0.6 (build %s)";
	
}
