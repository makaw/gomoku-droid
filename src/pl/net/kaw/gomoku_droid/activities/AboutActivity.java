/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipFile;

import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.IConfig;


/**
*
* Ekran informacyjny
* 
* @author Maciej Kawecki
* 
*/
public class AboutActivity extends AppActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        
        TextView backItem = (TextView) findViewById(R.id.back_item);
        backItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
         
  	    backItem.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf"));
        
  	    String version = String.format(IConfig.VERSION, getBuildDate());
  	    ((TextView) findViewById(R.id.txt_about)).setText(getString(R.string.credits, version));
       
        try {
          ((TextView) findViewById(R.id.txt_about_rules)).setText(loadRules());
        }
        catch (Exception e) {}
        
    }
    
    
    
    /**
     * Metoda wczytująca tekst z pliku 
     * @return Tekst pobrany z pliku 
     * @throws IOException Błąd wejścia-wyjścia przy próbie odczytu
     */
    private String loadRules() throws IOException {
        
       AssetManager assetManager = getAssets();

       //String fName = "/Rules_" + Lang.getLocaleSymbol() + ".txt";
       String fName = "Rules_pl_PL.txt";
       InputStream input = assetManager.open(fName);	
       BufferedReader reader;
       
       try {
         reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
       }
       catch (NullPointerException e) {
         System.err.println(e);//Lang.get("FileNotFound", "/resources" + fName));
         return null;
       }
       
       StringBuilder strBuilder = new StringBuilder();
       String line;
       
       while((line = reader.readLine()) != null) {
           
          strBuilder.append(line);
          strBuilder.append(System.getProperty("line.separator"));
           
       }
  
       reader.close();              
       
       return strBuilder.toString();
        
    }
        
    
    
    /**
     * Zwraca datę zbudowania pakietu
     * @return j.w.
     */
    private String getBuildDate() {
 	  
 	 String build = "n/a";  
 	   
      try {
 	   ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
 	   ZipFile zf = new ZipFile(ai.sourceDir);
 	   Date modDate = new Date(zf.getEntry("classes.dex").getTime());
 	   build = new SimpleDateFormat("yyMMdd.HHmm", Locale.getDefault()).format(modDate);
 	   zf.close();
 	 }
      catch(Exception e) {}
      
      return build;
 	   
    }    
    
    
    
}
