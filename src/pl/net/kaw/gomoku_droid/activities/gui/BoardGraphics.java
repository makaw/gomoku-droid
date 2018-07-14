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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.events.PlayerMoveEvent;
import pl.net.kaw.gomoku_droid.events.ZoomChangedEvent;
import pl.net.kaw.gomoku_droid.game.BoardField;


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
  private final ScaleGestureDetector scaleGestureDetector;
  
  private static final int INVALID_POINTER_ID = -1;
  private static final int CLICK_ACTION_THRESHOLD = 200;

  /** Ostatnie dotknięcie X */
  private float touchX;
  /** Ostatnie dotknięcie Y */
  private float touchY;
  
  private int activePointerId = INVALID_POINTER_ID;
    
  
  public BoardGraphics(Context context, AttributeSet attrs, int defStyle) {
    
	super(context, attrs, defStyle);
    this.context = context;    
    init();    
    
	scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {		  
	   @Override
	   public boolean onScale(ScaleGestureDetector scaleGestureDetector){	    			  
		  zoom(scaleGestureDetector.getScaleFactor());
		  return true;	    				  
		}	    			
	});
		    

	setOnTouchListener(new View.OnTouchListener() {		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
						  
		  scaleGestureDetector.onTouchEvent(event);
				    
		  switch (event.getAction() & MotionEvent.ACTION_MASK) {
		  
		    case MotionEvent.ACTION_DOWN: 
		        touchX = event.getX();
		        touchY = event.getY();	
		        activePointerId = event.getPointerId(0);
		        break;		   		        
		    
		        
		    case MotionEvent.ACTION_UP: 
		        
		       activePointerId = INVALID_POINTER_ID;
		       if (isAClick(touchX, event.getX(), touchY, event.getY())) { 
		        	
		          v.performClick();
		        			       
		       }
		        		        
		       break;
		    
		        
		    case MotionEvent.ACTION_CANCEL: 
		        activePointerId = INVALID_POINTER_ID;
		        break;
		    
		    
		    case MotionEvent.ACTION_POINTER_UP: 
		        int currPointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
		                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;		       
		        if (event.getPointerId(currPointerIndex) == activePointerId) {	
		          activePointerId = event.getPointerId(currPointerIndex == 0 ? 1 : 0);		           
		        }
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
  
  
  
  
  @Override
  public boolean performClick() {
		   
    try {
	  AppEventBus.post(new PlayerMoveEvent(getFieldCoords()));
    }
    catch (Exception e) { }
    
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
	zoomFactor = Math.max(IConfig.MIN_ZOOM_FACTOR, Math.min(factor, IConfig.MAX_ZOOM_FACTOR));
	
	init();
	invalidate();	  
	
	AppEventBus.post(new ZoomChangedEvent(zoomFactor));
	
  }
  
  
  /**
   * Dowolna zmiana powiększenia planszy
   * @param factor Czynnik zmiany
   */
  private void zoom(float factor) {
	  
	factor = zoomFactor * factor;
	zoomFactor = Math.max(IConfig.MIN_ZOOM_FACTOR, Math.min(factor, IConfig.MAX_ZOOM_FACTOR));
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
  
  
  /**
   * Wykrywanie kliknięcia
   * @param startX Wsp. X początku akcji
   * @param endX Wsp. X końca akcji
   * @param startY Wsp. Y początku akcji
   * @param endY Wsp. Y końca akcji
   * @return Czy kliknięcie
   */
  private boolean isAClick(float startX, float endX, float startY, float endY) {
      float differenceX = Math.abs(startX - endX);
      float differenceY = Math.abs(startY - endY);
      return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
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
      
      canvas.drawText(BoardField.getLabA(i), PX_BOARD_MARGIN.x+i*pxField+9,
    		  pxBoardSizeDecY + 22 + (zoomFactor <= 0.6 ? -3 : 0), paint);
      
      canvas.drawText(BoardField.getLabA(i), PX_BOARD_MARGIN.x+i*pxField+9,
    		  13, paint);      
    
      canvas.drawLine(PX_BOARD_MARGIN.x+12, PX_BOARD_MARGIN.y+i*pxField,
    		  PX_BOARD_MARGIN.x+(colsAndRows-1)*pxField+12, PX_BOARD_MARGIN.y+i*pxField, paint);
      
      canvas.drawText(BoardField.getLabB(i, colsAndRows), PX_BOARD_MARGIN.x-(colsAndRows-i > 9 ? 14:11), 
    		  PX_BOARD_MARGIN.y+i*pxField+4, paint);
      
      canvas.drawText(BoardField.getLabB(i, colsAndRows), 
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
  
  
  
  /**
   * Zwraca współrzędne ostatnio klikniętego pola planszy
   * @return Współrzędne (punkt)
   * @throws Exception Kliknięcie poza planszą
   */
  private Point getFieldCoords() throws Exception {
    	
	int x = (int) Math.round((touchX - PX_BOARD_MARGIN.x - 12) / (double)pxField) ;
	int y = (int) Math.round((touchY - PX_BOARD_MARGIN.y) / (double)pxField);
  
	if (x < 0 || x >= colsAndRows || y < 0 || y >= colsAndRows) throw new Exception();
	
	return new Point(x, y);
	
  }
  
  
}
