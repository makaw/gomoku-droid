/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.Language;
import pl.net.kaw.gomoku_droid.events.PlaySoundEvent;

/**
 *
 * Ogólny szablon aktywności aplikacji
 * 
 * @author Maciej Kawecki
 * 
 */
public abstract class AppActivity extends AppCompatActivity {

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

		mp.setOnCompletionListener(mp1 -> mp1.release());

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
	public void message(String msg)
	{
		pDialogDismiss();
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}


	@Override
	public void onResume()
	{
		super.onResume();
		inFront = true;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		inFront = false;
	}


	@Override
	protected void attachBaseContext(Context base) {
		Language lang = AppBase.getInstance().getSettings().getLanguage();
		super.attachBaseContext(lang.updateBaseContextLocale(base));
	}

}
