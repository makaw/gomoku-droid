/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;


import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;


/**
*
* Wersja językowa - lokalizacja 
* 
* @author Maciej Kawecki
* 
*/
public class Language {

   /** Lokalizacja - indeks */
   private int localeIndex;
   /** Lokalizacja */  
   private Locale locale;
   /** Bazowy kontekst */
   private final Context context;
   
   /**
    * Konstruktor
    * @param context Bazowy kontekst
    */
   public Language(Context context, int localeIndex) {
	   
	 this.context = context;
	 setLocale(localeIndex);
   
   }
   
   

   /**
    * Ustawia wskazaną lokalizację
    * @param index Indeks lokalizacji
    * @return True jeżeli zmieniono
    */
   public final boolean setLocale(int index) {
 	  
	  try {
		locale = IConfig.LOCALES[index];
		localeIndex = index;
		Locale.setDefault(locale);
		Configuration config = context.getResources().getConfiguration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());	
	  }
	  catch (Exception e) {
		setLocale();  
		return false;
	  }
	  
	  return true;
 	 
   }
   
   
   /**
    * Ustawia domyślną lokalizację
    * @param index Indeks lokalizacji
    * @return True jeżeli zmieniono
    */
   private void setLocale() {
	   
	 Locale tmp = context.getResources().getConfiguration().locale;
	 locale = null;
	 
	 for (int i=0; i<IConfig.LOCALES.length; i++) {
		if (IConfig.LOCALES[i].equals(tmp)) {
		   locale = tmp;
		   localeIndex = i;
		   break;
		}
	 }
	 
	 if (locale == null) {
	   locale = IConfig.LOCALES[IConfig.DEFAULT_LOCALE_INDEX];
	   localeIndex = IConfig.DEFAULT_LOCALE_INDEX;
	 }
	   
   }
   
   
   
   public int getLocaleIndex() {
 	 return localeIndex;
   }
   

   /**
    * Zwraca symbol lokalizacji (język + kraj)
    * @return Symbol lokalizacji
    */
   public String getLocaleSymbol() {
	 
	 return locale.getLanguage() + "_" + locale.getCountry();
	   
   }
   
   
   /**
    * Zwraca nazwę języka
    * @return Nazwa języka
    */
   public String getName() {
	   
	 return locale.getDisplayLanguage();  
	   
   }
   
   
	
}
