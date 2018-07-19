/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities.gui;

import java.util.concurrent.Callable;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.AppActivity;
import pl.net.kaw.gomoku_droid.app.IConfig;



/**
*
* Okienko modalne
* 
* @author Maciej Kawecki
* 
*/
public class ModDialog extends Dialog {
	
  /**
   * Konstruktor
   * @param context Bieżący kontekst
   * @param title Nagłówek okna
   * @param message Wiadomość
   * @param icon ID ikony nagłówka
   * @param callable Metoda wykonywana w listenerze przycisku "Tak"
   * @param callableNo Metoda wykonywana dodatkowo w listenerze przycisku "Nie"
   * @param <T> Typ zwracany przez metodę callable
   */
  private <T> ModDialog(final AppActivity context, String title, String message,
		  int icon, final Callable<T> callable, final Callable<T> callableNo) {
	  
    super(context);

    if (icon != -1) requestWindowFeature(Window.FEATURE_LEFT_ICON);
    if (title == null) requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    setContentView(R.layout.info_dialog);
	setCanceledOnTouchOutside(false);
	
    if (title != null) setTitle(title);
    if (icon !=-1) setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, icon);
	
	((TextView) findViewById(R.id.text)).setText(message);

	if (callable != null) {
		
	
	  ((Button) findViewById(R.id.dialogButtonYes)).setOnClickListener(new View.OnClickListener() {
		  
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);		  
		  ModDialog.this.dismiss();
		  try {
		    callable.call();
		  }
		  catch (Exception e) {
			if (IConfig.DEBUG) Log.e(context.getTag(), "Callable e: " + e.getMessage());  
		  }
		}
		
	  });
	  
	}
	
	else {
		
	  ((Button) findViewById(R.id.dialogButtonYes)).setVisibility(View.GONE);
	  ((Button) findViewById(R.id.dialogButtonNo)).setText(context.getString(R.string.ok));
	  findViewById(R.id.dialogView1).setLayoutParams(new LinearLayout.LayoutParams(0, 30,  0.5f));
	  
	}	
	
	
	((Button) findViewById(R.id.dialogButtonNo)).setOnClickListener(new View.OnClickListener() {		
		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  if (callableNo != null) {
			try {
			  callableNo.call();
			}
			catch (Exception e) {
			  if (IConfig.DEBUG) Log.e(context.getTag(), "Callable[no] e: " + e.getMessage());  
			}
		  }
		  ModDialog.this.dismiss();
		}
		
	});
	
  }
  
  
  private <T> ModDialog(final AppActivity context, String title, String message,
		  int icon, final Callable<T> callable) {
	this(context, title, message, icon, callable, null);
  }
  
  
  /**
   * Pokazuje okienko z potwierdzeniem
   * @param context Bieżący kontekst
   * @param message Treść wiadomości
   * @param callable Metoda wykonywana w listenerze przycisku "Tak"
   * @param <T> Typ zwracany przez metodę callable
   */
  public static <T> void showConfirmDialog(AppActivity context, String message, Callable<T> callable) {	  
	showConfirmDialog(context, message, callable, null);	  
  }
  
  
  /**
   * Pokazuje okienko z potwierdzeniem
   * @param context Bieżący kontekst
   * @param message Treść wiadomości
   * @param callable Metoda wykonywana w listenerze przycisku "Tak"
   * @param callable Dodatkowa metoda wykonywana w listenerze przycisku "Nie"
   * @param <T> Typ zwracany przez metodę callable
   */
  public static <T> void showConfirmDialog(AppActivity context, String message, Callable<T> callable, Callable<T> callableNo) {
	  
	 new ModDialog(context, context.getString(R.string.confirm), message,
			 R.drawable.ic_dialog_question, callable, callableNo).show(); 
	  
  }  
  
  
  /**
   * Pokazuje okienko informacyjne
   * @param context Bieżący kontekst
   * @param title Nagłówek (tytuł)
   * @param icon Ikona nagłówka
   * @param message Treść wiadomości
   */
  public static void showInfoDialog(AppActivity context, String title, int icon, String message) {
	  
	 new ModDialog(context, title, message, icon, null).show(); 
	  
  }
  
  
  
}
