/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;


/**
*
* Zakończenie pracy aplikacji
* 
* @author Maciej Kawecki
* 
*/
public class ExitActivity extends Activity {
	
  @Override 
  protected void onCreate(Bundle savedInstanceState) {
      
    super.onCreate(savedInstanceState);
    finish();
      
  }

  
  /**
   * Wyjście z aplikacji
   * @param context Bieżący kontekst
   */
  @SuppressLint("InlinedApi")
  public static void exit(Context context)  {
    	
    Intent intent = new Intent(context, ExitActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
    		| Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    context.startActivity(intent);
      
  }
    
  
  @Override	
  public void onDestroy() {
  	  	  
    super.onDestroy();  	 
    Process.killProcess(Process.myPid());    
      
  }
    
}