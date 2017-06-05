/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;


import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;


/**
*
* Odczyt właściwości z assets
* 
* @author Maciej Kawecki
* 
* 
*/
public class PropertyReader {

  /** Właściwości */
  private Properties properties = new Properties();

  /**
   * Konstruktor
   * @param context Kontekst aplikacji
   */
  public PropertyReader(Context context, String fileName) {
 
    try {
    	
      AssetManager assetManager = context.getAssets();
      InputStream inputStream = assetManager.open(fileName);
      properties.load(inputStream);  
      if (properties.get("services.url_host") == null  
    		  || properties.get("services.url_path") == null) throw new Exception(); 
      
    } catch (Exception e){
    	
      //properties.put("services.url_host", IConfig.DEFAULT_URL_HOST);
      //properties.put("services.url_path", IConfig.DEFAULT_URL_PATH);
      
    }
		
  }
  

  public Properties getProperties() {
    return properties;
  }

}