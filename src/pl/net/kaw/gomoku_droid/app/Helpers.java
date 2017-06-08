/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;


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
   * Zamiana sformatowanego czasu na liczbę sekund
   * @param timeString Czas w formacie HH:mm:ss
   * @return Czas w sekundach
   */
  public static long timeStringToSeconds(String timeString) {
	
	long seconds = 0;
	String time[] = timeString.split(":");
	
	try {
	  seconds = Long.parseLong(time[0])*3600L  + Long.parseLong(time[1])*60L + Long.parseLong(time[2]);
	}
	catch (Exception e) {
	  if (IConfig.DEBUG) Log.e(Helpers.class.getSimpleName(), e.getMessage());
	}
	
	return seconds;
		  
  }
  
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
    * Zwraca datę zbudowania pakietu
    * @param context Bieżący kontekst
    * @return j.w.
    */
   public static String getBuildDate(Context context) {
	  
	 String build = "n/a";  
	   
     try {
	   ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
	   ZipFile zf = new ZipFile(ai.sourceDir);
	   Date modDate = new Date(zf.getEntry("classes.dex").getTime());
	   build = new SimpleDateFormat("yyMMdd.HHmm", Locale.getDefault()).format(modDate);
	   zf.close();
	 }
     catch(Exception e) {}
     
     return build;
	   
   }
   
   
   
	
}
