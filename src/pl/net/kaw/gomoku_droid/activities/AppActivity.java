/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.events.PlaySoundEvent;

/**
 *
 * Ogólny szablon aktywności aplikacji
 * 
 * @author Maciej Kawecki
 * 
 */
public abstract class AppActivity extends Activity {

	/** Animacja po kliknięciu przycisku */
	public static AlphaAnimation BUTTON_CLICK = new AlphaAnimation(1F, 0.7F);	

	/** Okienko oczekiwania */
	protected ProgressDialog pDialog;
	/** Tag do logów (nazwa dziedziczącej klasy) */
	protected final String tag = getClass().getSimpleName();
	/** Czy aktywność jest widoczna */
	private boolean inFront;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		inFront = true;

	}

	public String getTag() {
		return tag;
	}

	/**
	 * Odegranie dźwięku jeżeli aktywność jest widoczna
	 */
	protected void playSound(PlaySoundEvent.SoundType type) {

		if (!inFront)
			return;
		
		if (!AppBase.getInstance().getSettings().isSoundEnabled()) return;
	      
		int resId;
	    switch (type) {
	      default: return;
	      case INFO: resId = R.raw.info; break;
	      case MOVE: resId = R.raw.move; break;
	      case SUCCESS: resId = R.raw.success; break;
	    }

		MediaPlayer mp = MediaPlayer.create(this, resId);

		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});

		mp.start();

	}

	/**
	 * Zamyka okienko oczekiwania
	 */
	public void pDialogDismiss() {

		if (pDialog.isShowing()) pDialog.dismiss();

	}

	
	/**
	 * Wyświetla okienko z komunikatem
	 * 
	 * @param msg
	 *            Komunikat
	 */
	public void message(String msg) {

		pDialogDismiss();
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

	}

	

	public boolean isInFront() {
		return inFront;
	}

	@Override
	public void onResume() {

		super.onResume();
		inFront = true;

	}

	@Override
	public void onPause() {

		super.onPause();
		inFront = false;

	}

	
	/**
	 * Zwraca wymiary ekranu
	 * @return j.w.
	 */
	public Point getDisplaySize() {
		  
	  DisplayMetrics displayMetrics = new DisplayMetrics();
	  getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	  return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
	}
	
	

	/**
	 * Ukrycie klawiatury
	 * 
	 * @param view
	 *            Widok
	 */
	protected void hideKeyboard(View view) {

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

	}

	/**
	 * Pomocnicza klasa: listener do ukrycia klawiatury
	 */
	protected class HideKbOnFocusListener implements View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				hideKeyboard(v);
			}
		}
	};

	
}
