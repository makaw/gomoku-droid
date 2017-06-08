/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.R;



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
   * @param listener Listener przycisku "Tak"
   */
  private ModDialog(Context context, String title, String message, int icon, View.OnClickListener listener) {
	  
    super(context);

    if (icon != -1) requestWindowFeature(Window.FEATURE_LEFT_ICON);
    if (title == null) requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    setContentView(R.layout.info_dialog);
	
    if (title != null) setTitle(title);
    if (icon !=-1) setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, icon);
	
	((TextView) findViewById(R.id.text)).setText(message);

	if (listener != null) 
	  ((Button) findViewById(R.id.dialogButtonYes)).setOnClickListener(listener);
	else {
	  ((Button) findViewById(R.id.dialogButtonYes)).setVisibility(View.GONE);
	  ((Button) findViewById(R.id.dialogButtonNo)).setText(context.getString(R.string.ok));
	  findViewById(R.id.dialogView1).setLayoutParams(new LinearLayout.LayoutParams(0, 30,  0.5f));
	}	
	
	((Button) findViewById(R.id.dialogButtonNo)).setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
		  v.startAnimation(AppActivity.BUTTON_CLICK);
		  ModDialog.this.dismiss();
		}
	});
	
  }
  
  
  
  /**
   * Pokazuje okienko z potwierdzeniem
   * @param context Bieżący kontekst
   * @param message Treść wiadomości
   * @param listener Listener dla przycisku "Tak"
   */
  public static void showConfirmDialog(Context context, String message, View.OnClickListener listener) {
	  
	 new ModDialog(context, context.getString(R.string.confirm), message,
			 R.drawable.ic_dialog_question, listener).show(); 
	  
  }
  
  
  /**
   * Pokazuje okienko informacyjne
   * @param context Bieżący kontekst
   * @param title Nagłówek (tytuł)
   * @param icon Ikona nagłówka
   * @param message Treść wiadomości
   */
  public static void showInfoDialog(Context context, String title, int icon, String message) {
	  
	 new ModDialog(context, title, message, icon, null).show(); 
	  
  }
  
  
  
}