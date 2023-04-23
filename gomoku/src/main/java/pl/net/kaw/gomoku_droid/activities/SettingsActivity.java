/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import pl.net.kaw.gomoku_droid.activities.gui.SpinnerList;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.app.Settings;
import pl.net.kaw.gomoku_droid.R;

/**
*
* Ekran ustawień
* 
* @author Maciej Kawecki
* 
*/
public class SettingsActivity extends AppActivity {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.settings_activity);

      final Settings settings = AppBase.getInstance().getSettings();
      
      final SpinnerList boardSizeField = new SpinnerList(this, R.id.st_board_size);
      for (int i=IConfig.MIN_COLS_AND_ROWS; i<=IConfig.MAX_COLS_AND_ROWS; i+=2) 
         boardSizeField.addData(i, getString(R.string.x_fields, i));      
      boardSizeField.setSelectedValue(settings.getColsAndRows());
      
      final SpinnerList winCondField = new SpinnerList(this, R.id.st_win_condition);
      for (int i=IConfig.MIN_PIECES_IN_ROW; i<=IConfig.MAX_PIECES_IN_ROW; i++) 
         winCondField.addData(i, getString(R.string.row_x_stones, i));      
      winCondField.setSelectedValue(settings.getPiecesInRow());   
      
      final SpinnerList langField = new SpinnerList(this, R.id.st_language);
      int i = 0;
      for (Locale locale : IConfig.LOCALES) {
          langField.addData(i++, locale.getDisplayLanguage());
      }
      langField.setSelectedValue(settings.getLanguage().getLocaleIndex());      
      
      final CheckBox computerStartsField = (CheckBox) findViewById(R.id.st_computer_starts);
      computerStartsField.setChecked(settings.isComputerStarts());
      
      final CheckBox soundsEnabledField = (CheckBox) findViewById(R.id.st_sounds_enabled);
      soundsEnabledField.setChecked(settings.isSoundEnabled());
      
      TextView backItem = (TextView) findViewById(R.id.back_item);
      backItem.setOnClickListener(v -> {
        v.startAnimation(BUTTON_CLICK);
        setResult(RESULT_CANCELED, new Intent());
        finish();
      });
        
      TextView saveItem = (TextView) findViewById(R.id.save_item);
      saveItem.setOnClickListener(v -> {
        v.startAnimation(BUTTON_CLICK);
        Intent intent = new Intent();
        intent.putExtra("lang", langField.getSelectedValue() != settings.getLanguage().getLocaleIndex());
        setResult(RESULT_OK, intent);

        AppBase.getInstance().getSettings().save(
          boardSizeField.getSelectedValue(), winCondField.getSelectedValue(),
          computerStartsField.isChecked(),  soundsEnabledField.isChecked(),
          langField.getSelectedValue());

        finish();
      });
         
  	  backItem.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf"));
  	  saveItem.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/vertiup2.ttf"));
  	  
    }
    
    
}
