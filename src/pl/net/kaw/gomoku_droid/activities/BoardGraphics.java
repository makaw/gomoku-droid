package pl.net.kaw.gomoku_droid.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import pl.net.kaw.gomoku_droid.app.IConfig;


/**
 *
 * Graficzna reprezentacja planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardGraphics extends View {
    
  /** Dodatkowy lewy margines planszy */
  private static final int PX_BOARD_MARGIN_H = 15;
  /** Dodatkowy górny margines planszy */
  private static final int PX_BOARD_MARGIN_V = 25;  	
	
  /** Minimalna długość boku pola (w px) */	
  private int minPxField = IConfig.DEFAULT_MIN_PX_FIELD;		
  /** Ilość wierszy i kolumn planszy */
  private int colsAndRows = IConfig.DEFAULT_COLS_AND_ROWS;
  /** Bieżący kontekst */
  private final Context context;
  /** Ustawienia rysowania */
  private Paint paint;
  /** Szerokość i wysokość (w pikselach) pojedynczego pola planszy */   
  private int pxField;
  /** Długość boku planszy */
  private int pxBoardSize;
  /** Wysokość planszy -1 rząd */
  private int pxBoardSizeDec;
  

  public BoardGraphics(Context context) {
      
    super(context);
    this.context = context;
    init();
    
  }

  public BoardGraphics(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.context = context;
      init();
  }

  public BoardGraphics(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.context = context;
      init();
  }
  
  
  /**
   * Inicjalizacja zmiennych
   */
  private void init() {
    	  
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.parseColor("#3A3A3A"));
    paint.setStyle(Style.FILL); 
    paint.setStrokeWidth(1.5f);        

	DisplayMetrics metrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics); 
    
    pxField = (int)Math.round(metrics.widthPixels * 0.75f / colsAndRows);
    if (pxField < minPxField) pxField = minPxField;
    pxBoardSize = pxField * colsAndRows + PX_BOARD_MARGIN_H*2;
    pxBoardSizeDec = PX_BOARD_MARGIN_V + (colsAndRows-1)*pxField;
    
  }
  
  
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  
	 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	 setMeasuredDimension(pxBoardSize, pxBoardSize - PX_BOARD_MARGIN_H + PX_BOARD_MARGIN_V);
	  
  }
  
  

  @SuppressLint("DrawAllocation")
  @Override
  protected void onDraw(Canvas canvas)  {
    
	super.onDraw(canvas);		
		
    // rysowanie planszy (siatka i podpisy)
    for (int i=0;i<colsAndRows;i++) {
        
      canvas.drawLine(
    		  getPix(PX_BOARD_MARGIN_H+i*pxField+12), getPix(PX_BOARD_MARGIN_V),
    		  getPix(PX_BOARD_MARGIN_H+i*pxField+12), getPix(pxBoardSizeDec), paint);
      
      canvas.drawText(Character.toString((char)('A' + i)), 
    		  getPix(PX_BOARD_MARGIN_H+i*pxField+9), getPix(pxBoardSizeDec+21), paint);
      
      canvas.drawText(Character.toString((char)('A' + i)), 
    		  getPix(PX_BOARD_MARGIN_H+i*pxField+9), 10, paint);      
    
      canvas.drawLine(
    		  getPix(PX_BOARD_MARGIN_H+12), getPix(PX_BOARD_MARGIN_V+i*pxField),
    		  getPix(PX_BOARD_MARGIN_H+(colsAndRows-1)*pxField+12),
    		  getPix(PX_BOARD_MARGIN_V+i*pxField), paint);
      
      canvas.drawText(Integer.toString(colsAndRows-i), 
    		  getPix(PX_BOARD_MARGIN_H-(i<6 ? 13:11)), 
    		  getPix(PX_BOARD_MARGIN_V+i*pxField+4), paint);
      
      canvas.drawText(Integer.toString(colsAndRows-i), 
    		  getPix(pxBoardSize - PX_BOARD_MARGIN_H -(i<6 ? 6 : 0)),
    		  getPix(PX_BOARD_MARGIN_V+i*pxField+4), paint);      

    }
   
    //  kropki 
    int tmp = colsAndRows;
    while (tmp > 11) tmp-=4;
    
    if (tmp == 11) {
      for (int i=3; i<=colsAndRows-4; i+=4) for (int j=3; j<=colsAndRows-4; j+=4) {
        canvas.drawArc(
          new RectF(getPix(PX_BOARD_MARGIN_H+pxField*i+9),
        		    getPix(PX_BOARD_MARGIN_V+pxField*j-3),
        		    getPix(PX_BOARD_MARGIN_H+pxField*i+15),
        		    getPix(PX_BOARD_MARGIN_V+pxField*j+3)), 0, 360, true, paint);
      }
    }
   
  }
 
  
  private int getPix(int pixels) {
	return pixels;
  }


  
}
