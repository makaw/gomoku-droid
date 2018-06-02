/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.events.ZoomChangedEvent;


/**
 *
 * Graficzna reprezentacja planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardGraphics extends View {
    
  /** Dodatkowe marginesy planszy */
  private static final Point PX_BOARD_MARGIN = new Point(15, 28);  
	
  /** Ilość wierszy i kolumn planszy */
  private final int colsAndRows = AppBase.getInstance().getSettings().getColsAndRows();
  /** Bieżący kontekst */
  private final Context context;
  /** Ustawienia rysowania */
  private Paint paint;
  /** Szerokość i wysokość (w pikselach) pojedynczego pola planszy */   
  private int pxField;
  /** Wymiary komponentu planszy */
  private Point pxBoardSize;
  /** Wysokość planszy -1 rząd */
  private int pxBoardSizeDecY;
  /** Współczynnik powiększenia planszy */
  private float zoomFactor = 1.0f;
  /** Rozpoznawanie gestów */
  private final ScaleGestureDetector mScaleGestureDetector;
  /** Czy pierwszy palec w górze (rozpoznawanie gestów) */
  public boolean firstFingerUp = true;
  /** Czy drugi palec w górze (rozpoznawanie gestów) */
  public boolean secondFingerUp = true;
    
  
  public BoardGraphics(Context context, AttributeSet attrs, int defStyle) {
    
	super(context, attrs, defStyle);
    this.context = context;    
    init();    
    
	mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {		  
	   @Override
	   public boolean onScale(ScaleGestureDetector scaleGestureDetector){	    	
		  float factor = scaleGestureDetector.getScaleFactor();
		  boolean out = factor < 1.0;
		  zoom(out, Math.abs(1.0f - factor) * (out ? 10.0f : 6.0f));
		  return true;	    				  
		}	    			
	});
		    

	setOnTouchListener(new View.OnTouchListener() {		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
						  
		  mScaleGestureDetector.onTouchEvent(event);
			
	      switch (event.getAction()) {
	         case MotionEvent.ACTION_DOWN:
	           firstFingerUp = false;
	           break;
	         case MotionEvent.ACTION_POINTER_DOWN:
	           secondFingerUp = false;
	           break;
	         case MotionEvent.ACTION_UP:  
	           if (secondFingerUp) v.performClick();
	           firstFingerUp = true;
	           secondFingerUp = true;
	           break;
	         case MotionEvent.ACTION_POINTER_UP:
	           if (firstFingerUp) v.performClick();
		       firstFingerUp = true;
		       secondFingerUp = true;	           	          	           
	           break;
	      }	        
		  
		  return true;
			
		}
			
	  });	  
		      
    
  }
    

  public BoardGraphics(Context context) {      
    this(context, null, 0);    
  }

  public BoardGraphics(Context context, AttributeSet attrs) {      
	this(context, attrs, 0);    
  }  
  
  
  
  // TODO
  @Override
  public boolean performClick() {
	Log.d(getClass().getSimpleName(), "click!");
	return super.performClick();
  }
	
  
  
  /**
   * Inicjalizacja zmiennych
   */
  private void init() {
    	  
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.parseColor("#3A3A3A"));
    paint.setStyle(Style.FILL); 
    paint.setStrokeWidth(2.5f); 
    
    paint.setTextSize(zoomFactor < 1 ? (zoomFactor <= 0.6 ? 12.0f : 14.0f) : 16.0f);

	DisplayMetrics metrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics); 
    
    pxField = (int)Math.round(metrics.widthPixels / colsAndRows * zoomFactor) * 2;
    pxBoardSize = new Point(pxField * colsAndRows + PX_BOARD_MARGIN.x*2, 
    		pxField * colsAndRows + PX_BOARD_MARGIN.y);
    pxBoardSizeDecY = PX_BOARD_MARGIN.y + (colsAndRows-1)*pxField;
    
    setMinimumWidth(pxBoardSize.x);
    setMinimumHeight(pxBoardSize.y);
    
  }
  
  
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	setMeasuredDimension(pxBoardSize.x, pxBoardSize.y);
	  
  }
     
  
  
  /**
   * Zmiana powiększenia planszy
   * @param out True=zmniejszenie
   * @param zoomFactorStep Krok zmiany
   */
  private void zoom(boolean out, float zoomFactorStep) {
	  
	float factor = zoomFactor + (out ? -1.0f : 1.0f) * zoomFactorStep;  
	if (factor < IConfig.MIN_ZOOM_FACTOR || factor > IConfig.MAX_ZOOM_FACTOR) return;  
	
	zoomFactor = factor;
	init();
	invalidate();	  
	
	AppEventBus.post(new ZoomChangedEvent(zoomFactor));
	
  }
  
  
  /**
   * Zmiana powiększenia planszy (domyślny krok)
   * @param out True=zmniejszenie
   */
  public void zoom(boolean out) {	  
	zoom(out, IConfig.ZOOM_FACTOR_STEP);  	  
  }
    
      

  @SuppressLint("DrawAllocation")
  @Override
  protected void onDraw(Canvas canvas)  {
    
	super.onDraw(canvas);		
		
	int marg2 = (int)Math.round(PX_BOARD_MARGIN.x * zoomFactor * (zoomFactor > 1 ? 2 : 1)) + 3;
	if (zoomFactor <= 0.6) marg2 += 2;
	
    // rysowanie planszy (siatka i podpisy)
    for (int i=0;i<colsAndRows;i++) {
        
      canvas.drawLine(PX_BOARD_MARGIN.x+i*pxField+12, PX_BOARD_MARGIN.y,
    		  PX_BOARD_MARGIN.x+i*pxField+12, pxBoardSizeDecY, paint);
      
      canvas.drawText(Character.toString((char)('A' + i)), PX_BOARD_MARGIN.x+i*pxField+9,
    		  pxBoardSizeDecY + 22 + (zoomFactor <= 0.6 ? -3 : 0), paint);
      
      canvas.drawText(Character.toString((char)('A' + i)), PX_BOARD_MARGIN.x+i*pxField+9,
    		  13, paint);      
    
      canvas.drawLine(PX_BOARD_MARGIN.x+12, PX_BOARD_MARGIN.y+i*pxField,
    		  PX_BOARD_MARGIN.x+(colsAndRows-1)*pxField+12, PX_BOARD_MARGIN.y+i*pxField, paint);
      
      canvas.drawText(Integer.toString(colsAndRows-i), PX_BOARD_MARGIN.x-(colsAndRows-i > 9 ? 14:11), 
    		  PX_BOARD_MARGIN.y+i*pxField+4, paint);
      
      canvas.drawText(Integer.toString(colsAndRows-i), 
    		  pxBoardSize.x - marg2 -(colsAndRows-i > 9 ? 12 : 6),  PX_BOARD_MARGIN.y+i*pxField+4, paint);      

    }
   
    //  kropki 
    int tmp = colsAndRows;
    while (tmp > 11) tmp-=4;
    
    if (tmp == 11) {
      for (int i=3; i<=colsAndRows-4; i+=4) for (int j=3; j<=colsAndRows-4; j+=4) {
        canvas.drawArc(
          new RectF(PX_BOARD_MARGIN.x+pxField*i+8, PX_BOARD_MARGIN.y+pxField*j-4,
        		    PX_BOARD_MARGIN.x+pxField*i+16,PX_BOARD_MARGIN.y+pxField*j+4),
          0, 360, true, paint);
      }
    }
   
  }
  
  
}
