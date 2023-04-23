/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import java.util.Locale;


/**
*
* Pomocnicze metody statyczne
* 
* @author Maciej Kawecki
* 
*/
@SuppressLint("DefaultLocale")
public class Helpers {

	
  private Helpers() {}

  
  /**
   * Formatowanie czasu 
   * @param secTime Czas w sekundach
   * @return Czas w formacie HH:mm:ss
   */
  public static String timeSecondsToString(long secTime) {
	 	 	 
    int seconds = (int) (secTime % 60);
    secTime /= 60;
    int minutes = (int) (secTime % 60);
    secTime /= 60;
    int hours = (int) secTime;
 
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	 
  }
   
   
   /**
    * Zmiana jasności rysunku
    * @param d Rysunek
    * @param bright Jasność
    */
   public static void setBrightness(Drawable d, int bright) {
	   
	 ColorMatrix cm = new ColorMatrix(new float[]
		{
		    1, 0, 0, 0, bright,
		    0, 1, 0, 0, bright,
		    0, 0, 1, 0, bright,
		    0, 0, 0, 1, 0
		});
		    
	 d.setColorFilter(new ColorMatrixColorFilter(cm));
	
   }



	public static String getLocaleString(Locale locale, int resourceId, Context context) {
		Configuration config = new Configuration(context.getResources().getConfiguration());
		config.setLocale(locale);
		return context.createConfigurationContext(config).getText(resourceId).toString();
	}


	
}
