/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities.gui;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.activities.AppActivity;


/**
*
* Komponent rozwijanej listy
* 
* @author Maciej Kawecki
* 
*/
public class SpinnerList {

  /** Adapter listy */	
  private final ArrayAdapter<SpinData> adapter;
  /** Komponent - spinner */
  private final Spinner spinner;
  
  /** Wewn. klasa - rekord listy (int, string) */
  private class SpinData {
		
	int id;
	String name;
	
	public SpinData(int id, String name) {
	  this.id = id;
  	  this.name = name;
    }
		  
	@Override
	public String toString() {
	  return name;  
	}

  }
  
  
  /**
   * Konstruktor 
   * @param activity Bieżąca aktywność
   * @param layoutId Id komponentu (spinner)
   */
  public SpinnerList(AppActivity activity, int layoutId) {
	  
	spinner = (Spinner) activity.findViewById(layoutId);
	adapter = new ArrayAdapter<SpinData>(activity, R.layout.spinner_item);
	adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);	
	spinner.setAdapter(adapter);  
	
  }
  
  /**
   * Dodanie danych
   * @param id Wartość liczbowa
   * @param name Napis
   */
  public void addData(int id, String name) {
	  
	adapter.add(new SpinData(id, name)); 
	adapter.notifyDataSetChanged();
	  
  }
  
  /**
   * Zwraca Wybraną wartość liczbową
   * @return j.w.
   */
  public int getSelectedValue() {
	  
	try {
	  return ((SpinData) spinner.getSelectedItem()).id;
	}
	catch (Exception e) {
	  return -1;
	}
	  
  }
  
  
  /**
   * Ustawia wybraną wartość 
   * @param id Wartość liczbowa
   * @return True jeżeli się udało
   */
  public boolean setSelectedValue(int id) {
	  
	for (int i=0; i<adapter.getCount(); i++) {
	  if (adapter.getItem(i).id == id) {
		 spinner.setSelection(i);
		 return true;
	  }
	}
	
	return false;
	  	  
  }
  
	
	
}
