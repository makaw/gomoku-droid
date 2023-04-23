/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;


import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;


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
   
   /**
    * Konstruktor
    * @param localeIndex Indeks lokalizacji
    */
   public Language(int localeIndex) {
	 setLocale(localeIndex);
   }


   /**
    * Ustawia wskazaną lokalizację
    * @param index Indeks lokalizacji
    */
   public final void setLocale(int index) {
	  try {
		locale = IConfig.LOCALES[index];
		localeIndex = index;
	  }
	  catch (Exception e) {
		  Log.e("Lang", e.getMessage());
	  }
   }

   public void delegateLocales() {
	   LocaleListCompat lc = LocaleListCompat.forLanguageTags(getLocaleSymbol("-"));
	   AppCompatDelegate.setApplicationLocales(lc);
   }

   
   public int getLocaleIndex() {
 	 return localeIndex;
   }

   /**
    * Zwraca symbol lokalizacji (język + kraj)
    * @return Symbol lokalizacji
    */
   public String getLocaleSymbol(String glue) {
	 return locale.getLanguage() + glue + locale.getCountry();
   }

   public String getLocaleSymbol() {
	   return getLocaleSymbol("_");
   }

   public Locale getLocale() {
	   return locale;
   }
   
   /**
    * Zwraca nazwę języka
    * @return Nazwa języka
    */
   public String getName() {
	 return locale.getDisplayLanguage();
   }


	public Context updateBaseContextLocale(Context context)
	{
		Locale.setDefault(locale);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
			return updateResourcesLocale(context, locale);
		}
		return updateResourcesLocaleLegacy(context, locale);
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	private Context updateResourcesLocale(Context context, Locale locale) {
		Configuration configuration = new Configuration(context.getResources().getConfiguration());
		configuration.setLocale(locale);
		return context.createConfigurationContext(configuration);
	}

	private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.locale = locale;
		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
		return context;
	}

}
