/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import android.app.Application;


/**
*
* Uzupełnienie bazowej klasy 
* (singleton)
* 
* @author Maciej Kawecki
* 
* 
*/
public class AppBase extends Application {

	/** Instancja klasy */
	private static AppBase INSTANCE;

	/** Ustawienia gry */
	private Settings settings;
	
	
	@Override
	public void onCreate() {
		
	  super.onCreate();
	  INSTANCE = this;	  
	  settings = new Settings(getBaseContext());
	  
	}

	
	public static synchronized AppBase getInstance() {
	   return INSTANCE;
	}
	
	
	public Settings getSettings() {
	  return settings;			
	}
	
}