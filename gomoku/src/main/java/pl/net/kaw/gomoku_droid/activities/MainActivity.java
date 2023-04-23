/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.Callable;

import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.gui.ModDialog;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.Helpers;
import pl.net.kaw.gomoku_droid.app.Language;


/**
*
* Menu główne - ekran startowy
* 
* @author Maciej Kawecki
* 
*/
public class MainActivity extends AppActivity {

	/** Kod powrotu - zmiana języka */
	private static final int TRANSLATE_CODE = 99;
	
	/** Pozycje menu */
	private TextView startItem, aboutItem, settingsItem, exitItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);		
	  	 
	  setContentView(R.layout.main_activity);
	  
	  Typeface font = Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf");
	  startItem = (TextView) findViewById(R.id.start_item);
	  startItem.setTypeface(font);
	  aboutItem = (TextView) findViewById(R.id.about_item);
	  aboutItem.setTypeface(font);
	  settingsItem = (TextView) findViewById(R.id.settings_item);
	  settingsItem.setTypeface(font);
	  exitItem = (TextView) findViewById(R.id.exit_item);
	  exitItem.setTypeface(font);	

	  startItem.setOnClickListener(view -> {
		  view.startAnimation(BUTTON_CLICK);
		  startActivity(new Intent(MainActivity.this, GameActivity.class));
	  });
	  
	  
	  aboutItem.setOnClickListener(view -> {
		  view.startAnimation(BUTTON_CLICK);
		  startActivity(new Intent(MainActivity.this, AboutActivity.class));
	  });
	  
	  
	  settingsItem.setOnClickListener(view -> {
		 view.startAnimation(BUTTON_CLICK);
		 Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
		 startActivityForResult(intent, TRANSLATE_CODE);
	  });
	  
	  
	  exitItem.setOnClickListener(v -> {
		v.startAnimation(BUTTON_CLICK);
		onBackPressed();
	  });
	  	  	
  }
	
	


    
  @Override
  public void onBackPressed() {
    	
	ModDialog.showConfirmDialog(MainActivity.this, getString(R.string.confirm_quit),
			(Callable<Void>) () -> {
			  ExitActivity.exit(getApplicationContext());
			  return null;
			}
	);
    	
  }
  
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode != TRANSLATE_CODE || resultCode != RESULT_OK)  { return; }

	Language lang = AppBase.getInstance().getSettings().getLanguage();
	if (data.getBooleanExtra("lang", false)) {
	  lang.delegateLocales();
	  startItem.setText(getString(R.string.menu_newgame));
	  aboutItem.setText(getString(R.string.menu_about));
	  settingsItem.setText(getString(R.string.menu_settings));
	  exitItem.setText(getString(R.string.menu_exit));
	}
	message(Helpers.getLocaleString(lang.getLocale(), R.string.settings_changed, this));
	super.onActivityResult(requestCode, resultCode, data);
  }
	

}
