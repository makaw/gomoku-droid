/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * Ustawienia gry przechowywane w bazie SQLite
 * 
 * @author Maciej Kawecki
 * 
 */
public class Settings extends SQLiteOpenHelper {

	/** Tag: nazwa klasy */
	private static final String TAG = Settings.class.getSimpleName();
	/** Wersja bazy */
	private static final int DATABASE_VERSION = 1;
	/** Nazwa bazy */
	private static final String DATABASE_NAME = "gomoku";
	
	/** Ilość wierszy i kolumn planszy */  
	private int colsAndRows = IConfig.DEFAULT_COLS_AND_ROWS;
	/** Ilość kamieni w rzędzie wymagana do wygranej */
	private int piecesInRow = IConfig.DEFAULT_PIECES_IN_ROW;
	/** Czy komputer zaczyna grę */
	private boolean computerStarts = IConfig.DEFAULT_COMPUTER_STARTS;
	/** Czy włączony dźwięk */
	private boolean soundEnabled = IConfig.DEFAULT_ENABLE_SOUND;
	/** Wersja językowa */
	private Language language;
	
	
	/**
	 * Konstruktor
	 * @param context Bieżący kontekst
	 */
	public Settings(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  language = new Language(context, IConfig.DEFAULT_LOCALE_INDEX);
	  load();
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
	  db.execSQL("CREATE TABLE settings (name TEXT UNIQUE, value INTEGER)");
	  if (IConfig.DEBUG) Log.d(TAG, "Database tables created");		
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	  db.execSQL("DROP TABLE IF EXISTS settings");
	  onCreate(db);		
	}

	

	public int getColsAndRows() {
	  return colsAndRows;
	}

	public int getPiecesInRow() {
	  return piecesInRow;
	}


	public void setPiecesInRow(int piecesInRow) {
	  this.piecesInRow = piecesInRow;
	}


	public boolean isComputerStarts() {
	  return computerStarts;
	}


	public boolean isSoundEnabled() {
	  return soundEnabled;
	}
	
	
	public Language getLanguage() {
	  return language;
	}

	
	/**
	 * Załadowanie ustawień z bazy
	 */
	private void load() {
		
	  String query = "SELECT name, value FROM settings;";

	  SQLiteDatabase db = this.getReadableDatabase();
	  Cursor cursor = db.rawQuery(query, null);
	  cursor.moveToFirst();
	  if (cursor.getCount() > 0) do {
		
		try {
		  String name = cursor.getString(0);		
		  int value = cursor.getInt(1);
		  if (name.equals("colsAndRows")) colsAndRows = value;
		  else if (name.equals("piecesInRow")) piecesInRow = value;
		  else if (name.equals("computerStarts")) computerStarts = (value == 1);
		  else if (name.equals("soundEnabled")) soundEnabled = (value == 1);
		  else if (name.equals("language")) language.setLocale(value);
		}
		catch (Exception e) {
		  if (IConfig.DEBUG) Log.d(TAG, "loading settings: " + e.getMessage());	
		}
		
	  } while (cursor.moveToNext());
			
	  cursor.close();
	  db.close();
	  
		
	}
	
	
	/**
	 * Zapis ustawień do bazy SQLite
	 * @param colsAndRows Ilość wierszy i kolumn planszy
	 * @param piecesInRow Ilość kamieni w rzędzie wymagana do wygranej
	 * @param computerStarts Czy komputer zaczyna grę 
	 * @param soundEnabled Czy dźwięk włączony
	 * @param localeIndex Indeks wersji językowej
	 */
	public void save(int colsAndRows, int piecesInRow, boolean computerStarts, boolean soundEnabled, int localeIndex) {
		
	  this.colsAndRows = colsAndRows;
	  this.piecesInRow = piecesInRow;
	  this.computerStarts = computerStarts;
	  this.soundEnabled = soundEnabled;
	  language.setLocale(localeIndex);
	  
	  SQLiteDatabase db = this.getWritableDatabase();
	  db.delete("settings", null, null);
	  
	  dbInsert(db, "colsAndRows", colsAndRows);
	  dbInsert(db, "piecesInRow", piecesInRow);
	  dbInsert(db, "computerStarts", computerStarts ? 1 : 0);
	  dbInsert(db, "soundEnabled", soundEnabled ? 1 : 0);
	  dbInsert(db, "language", language.getLocaleIndex());
	  
	  db.close(); 

	  if (IConfig.DEBUG) Log.d(TAG, "Settings saved into SQLite");
	  
	}
	
	
	/**
	 * Wstawienie pojedynczego rekordu do bazy SQLite
	 * @param db Ref do bazy
	 * @param name Nazwa - klucz
	 * @param value Wartość
	 */
	private void dbInsert(SQLiteDatabase db, String name, int value) {
		
	  ContentValues values = new ContentValues();
	  values.put("name", name);		
	  values.put("value", value);
	  db.insert("settings", null, values);	
		
	}
	
	
	/**
	 * Zapis ustawień do bazy SQLite
	 * @param soundEnabled Czy dźwięk włączony
	 */
	public void save(boolean soundEnabled) {
		
	  save(this.colsAndRows, this.piecesInRow, this.computerStarts,
			  soundEnabled, this.language.getLocaleIndex());	
		
	}
	

}
