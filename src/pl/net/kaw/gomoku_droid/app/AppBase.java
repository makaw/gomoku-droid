/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import java.util.Properties;

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

	/** Ustawienia z pliku properties */
	private Properties properties;
	
	
	@Override
	public void onCreate() {
		
	  super.onCreate();
	  INSTANCE = this;	  
	  properties = new PropertyReader(getBaseContext(), "app.properties").getProperties();	
	  
	}

	
	public static synchronized AppBase getInstance() {
	   return INSTANCE;
	}
	

	
	
	public Properties getProperties() {
	  return properties;
	}
	
	
		
	
}