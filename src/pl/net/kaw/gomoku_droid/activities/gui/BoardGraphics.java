/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.activities.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import pl.net.kaw.gomoku_droid.R;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.events.BoardClickEvent;
import pl.net.kaw.gomoku_droid.events.GameOverEvent;
import pl.net.kaw.gomoku_droid.events.PlayerMoveEvent;
import pl.net.kaw.gomoku_droid.events.ZoomChangedEvent;
import pl.net.kaw.gomoku_droid.game.BoardField;
import pl.net.kaw.gomoku_droid.game.BoardFieldState;


/**
 *
 * Graficzna reprezentacja planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardGraphics extends View {
    
  private static final String TAG = BoardGraphics.class.getSimpleName();	
	
  /** Marginesy planszy */
  private Point pxBoardMargin;  
  /** Ilość wierszy i kolumn planszy */
  private final int colsAndRows = AppBase.getInstance().getSettings().getColsAndRows();
  /** Ustawienia rysowania */
  private Paint paint;
  /** Szerokość i wysokość (w pikselach) pojedynczego pola planszy */   
  private int pxField;
  /** Wymiary komponentu planszy */
  private Point pxBoardSize;
  /** Szerokość planszy -1 rząd */
  private int pxBoardSizeDecX;
  /** Wysokość planszy -1 rząd */
  private int pxBoardSizeDecY;
  /** Współczynnik powiększenia planszy */
  private float zoomFactor;
  /** Środkowy (początkowy) współczynnik powiększenia */
  private final float midZoomFactor;
  /** Dodatkowy współczynnik przeskalowania z uwzgl. metryki ekranu */
  private final double extraZoomFactor;  
  /** Rozpoznawanie gestów */
  private final ScaleGestureDetector scaleGestureDetector;
  
  private static final int INVALID_POINTER_ID = -1;

  private int activePointerId = INVALID_POINTER_ID;
  
  /** Ostatnie dotknięcie X */
  private float touchX;
  /** Ostatnie dotknięcie Y */
  private float touchY;  
  
  /** Ustawione kamienie */
  private final Set<BoardField> pieces = new HashSet<>();
  /** Mapa przygotowanych bitmap kamieni */
  private Map<Integer, Bitmap> pieceBitmaps = new HashMap<>(); 
  /** Aktualna wielkość kamienia w pikselach */
  private int piecePx;
  
  /** Czy aktywna rozgrywka */
  private boolean gameRunning = false;
  
  
  public BoardGraphics(Context context, AttributeSet attrs, int defStyle) {
    
	super(context, attrs, defStyle);
    
    DisplayMetrics metrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);     
    
	midZoomFactor = (float) colsAndRows / (float)IConfig.MAX_COLS_AND_ROWS;
	extraZoomFactor = (double)metrics.densityDpi / (double)DisplayMetrics.DENSITY_XHIGH / midZoomFactor;    
	zoomFactor = midZoomFactor;
	initForZoom();        	
    
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
		 
	if (gameRunning)  
    try {
	  AppEventBus.post(new BoardClickEvent(getFieldCoords()));
    }
    catch (Exception e) { }
    
	return super.performClick();
	
  }
	
  
  public void init() {
    AppEventBus.register(this);	
    gameRunning = true;
  }
  
  
  /**
   * Inicjalizacja zmiennych po zmianie powiększenia
   */
  private void initForZoom() {
    	  	    	
    float factor = zoomFactor / IConfig.MIN_ZOOM_FACTOR;
    piecePx = (int) (IConfig.MIN_PIECE_SIZE_PX * factor * extraZoomFactor);

	loadBitmaps();
	  
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.parseColor("#3A3A3A"));
    paint.setStyle(Style.FILL); 
    paint.setStrokeWidth(2.5f); 
    
    float ts = 16.0f * zoomFactor;
    paint.setTextSize(ts < 12.0f ? 12.0f : ts);

    pxField = (int)Math.round(piecePx * 1.5f);

	pxBoardMargin = new Point(pxField/2, pxField/2);  
	   
    pxBoardSize = new Point(pxField * colsAndRows + pxBoardMargin.x*2,  pxField * colsAndRows + pxBoardMargin.y);
    pxBoardSizeDecX = pxBoardMargin.x + colsAndRows * pxField;
    pxBoardSizeDecY = pxBoardMargin.y + (colsAndRows-1) * pxField;
    
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
	zoomFactor = Math.max(IConfig.MIN_ZOOM_FACTOR * midZoomFactor, Math.min(factor, IConfig.MAX_ZOOM_FACTOR * midZoomFactor));
		
	initForZoom();
	invalidate();	  
	
	AppEventBus.post(new ZoomChangedEvent(zoomFactor));
	
  }
  
  
  /**
   * Dowolna zmiana powiększenia planszy
   * @param factor Czynnik zmiany
   */
  private void zoom(float factor) {
	  
	factor = zoomFactor * factor;	
	zoomFactor = Math.max(IConfig.MIN_ZOOM_FACTOR * midZoomFactor, Math.min(factor, IConfig.MAX_ZOOM_FACTOR * midZoomFactor));
	initForZoom();
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
    float diffX = Math.abs(startX - endX);
    float diffY = Math.abs(startY - endY);
    return diffX <= IConfig.CLICK_ACTION_THRESHOLD && diffY <= IConfig.CLICK_ACTION_THRESHOLD;
  } 
    
      

  @SuppressLint("DrawAllocation")
  @Override
  protected void onDraw(Canvas canvas)  {
    
	super.onDraw(canvas);		
			
	Point coordsDn = getCanvasCoords(colsAndRows - 1, 0);
	
    // rysowanie planszy (siatka i podpisy)
    for (int i=0; i<colsAndRows; i++) {
    	
      Point coords = getCanvasCoords(i, i);
        
      canvas.drawLine(coords.x, pxBoardMargin.y, coords.x, pxBoardSizeDecY, paint);
      
      canvas.drawText(BoardField.getLabA(i), coords.x - 3,
    		  pxBoardSizeDecY + (zoomFactor <= 0.6 ? 7 : 10) + pxField/2, paint);
      
      canvas.drawText(BoardField.getLabA(i), coords.x - 3, 11, paint);      
    
      canvas.drawLine(pxBoardMargin.x + 12, coords.y, coordsDn.x, coords.y, paint);
      
      canvas.drawText(BoardField.getLabB(i, colsAndRows), 3 + (colsAndRows-i <= 9 ? 3:0), 
    		  coords.y + 4, paint);
      
      canvas.drawText(BoardField.getLabB(i, colsAndRows), pxBoardSizeDecX - pxField/3,  coords.y + 4, paint);      

    }
   
    //  kropki 
    int tmp = colsAndRows;
    while (tmp > 11) tmp-=4;
    
    if (tmp == 11) {
      for (int i=3; i<=colsAndRows-4; i+=4) for (int j=3; j<=colsAndRows-4; j+=4) {
    	Point coords = getCanvasCoords(i, j);
        canvas.drawArc(new RectF(coords.x - 4, coords.y-4, coords.x + 4, coords.y + 4), 0, 360, true, paint);
      }
    }
        
    // kamienie
    for (BoardField field: pieces) {
      if (field.getState() == BoardFieldState.EMPTY) continue;
      Bitmap bmp = getFieldBitmap(field);
      if (bmp == null) continue;
      Point coords = getCanvasCoords(field.getA(), field.getB());
      canvas.drawBitmap(bmp, coords.x - piecePx/2, coords.y - piecePx/2, paint);
    }
   
  }
  
  
  /**
   * Wyczyszczenie planszy
   */
  public void clear() {
	pieces.clear();
	gameRunning = false;
	try {
	  AppEventBus.unregister(this);	
	}
	catch (Exception e) { }
	invalidate();
  }
  
    
  
  @Subscribe
  public void noticePlayerMove(final PlayerMoveEvent event) {	
	 if (pieces.contains(event.getField()) || !gameRunning) return; 
	 Log.d(TAG, event.toString());
	 pieces.add(event.getField());	 
	 invalidate();
  }
  
  
  @Subscribe
  public void noticeGameOver(final GameOverEvent event) {
	 
	 if (!gameRunning) return; 
	  
	 if (event.getWinRow() != null) {
	   for (BoardField bf: event.getWinRow()) {
		 bf.setChecked(true);
		 pieces.add(bf);   	
	   }
	   gameRunning = false;
	   invalidate();
	 }	 
	    
  }
  
  
  
  /**
   * Zwraca współrzędne ostatnio klikniętego pola planszy
   * @return Współrzędne (punkt)
   * @throws Exception Kliknięcie poza planszą
   */
  private Point getFieldCoords() throws Exception {
    	
	int a = (int) Math.round((touchX - pxBoardMargin.x - 12) / (double)pxField) ;
	int b = (int) Math.round((touchY - pxBoardMargin.y) / (double)pxField);
  
	if (a < 0 || a >= colsAndRows || b < 0 || b >= colsAndRows) throw new Exception();
	
	return new Point(a, b);
	
  }
  
  
  /**
   * Zwraca współrzędne layoutu (przecięcie linii)
   * @param a Współrzędna A planszy
   * @param b Współrzędna B planszy
   * @return Współrzędne layoutu (punkt)  
   */  
  private Point getCanvasCoords(int a, int b) {
	  
	int x = pxBoardMargin.x + a * pxField + 12;
	int y = pxBoardMargin.y + b *pxField;
  
	return new Point(x, y);
	
  }
  
  
  /**
   * Przygotowanie bitmap (kamienie)
   */
  private void loadBitmaps() {
	 
	Resources res = getResources();
	pieceBitmaps.clear();
	
	pieceBitmaps.put(R.drawable.black, BitmapFactory.decodeResource(res, R.drawable.black));
	pieceBitmaps.put(R.drawable.white, BitmapFactory.decodeResource(res, R.drawable.white));
	pieceBitmaps.put(R.drawable.white_checked, BitmapFactory.decodeResource(res, R.drawable.white_checked));
	pieceBitmaps.put(R.drawable.black_checked, BitmapFactory.decodeResource(res, R.drawable.black_checked));
		
	Iterator<Map.Entry<Integer, Bitmap>> it = pieceBitmaps.entrySet().iterator();
	while (it.hasNext()) {
	  Map.Entry<Integer, Bitmap> entry = it.next();
	  pieceBitmaps.put(entry.getKey(), Bitmap.createScaledBitmap(entry.getValue(), piecePx, piecePx, false));	  
	}
	  
  }
  
  
  private Bitmap getFieldBitmap(BoardField field) {
	
	switch (field.getState()) {
	
	  default: 
	  case EMPTY : return null;
	  case WHITE : return field.isChecked() ? pieceBitmaps.get(R.drawable.white_checked) : pieceBitmaps.get(R.drawable.white);
	  case BLACK : return field.isChecked() ? pieceBitmaps.get(R.drawable.black_checked) : pieceBitmaps.get(R.drawable.black);
	
	}
	  
  }
  
  
  public float getMidZoomFactor() {
	return midZoomFactor;
  }
  
  
  
  
}
