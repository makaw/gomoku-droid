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
  /** Dodatkowy lewy margines planszy */
  private int pxBoardMargin;
  /** Długość boku planszy */
  private int pxBoardSize;
  

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
    pxBoardMargin = (int)Math.round(pxField *0.4f);	
    pxBoardSize = pxField * colsAndRows + pxBoardMargin;
    
  }
  
  
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  
	 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	 setMeasuredDimension(pxBoardSize, pxBoardSize);
	  
  }
  
  

  @SuppressLint("DrawAllocation")
  @Override
  protected void onDraw(Canvas canvas)  {
    
	super.onDraw(canvas);		
		
    // rysowanie planszy (siatka i podpisy)
    for (int i=0;i<colsAndRows;i++) {
        
      canvas.drawLine(
    		  getPix(pxBoardMargin+i*pxField+12), getPix(pxBoardMargin),
    		  getPix(pxBoardMargin+i*pxField+12), 
    		  getPix(pxBoardMargin+(colsAndRows-1)*pxField), paint);
      
      canvas.drawText(Character.toString((char)('A' + i)), 
    		  getPix(pxBoardMargin+i*pxField+9), 
              getPix(pxBoardMargin+(colsAndRows-1)*pxField+21), paint);
    
      canvas.drawLine(
    		  getPix(pxBoardMargin+12), getPix(pxBoardMargin+i*pxField),
    		  getPix(pxBoardMargin+(colsAndRows-1)*pxField+12),
    		  getPix(pxBoardMargin+i*pxField), paint);
      
      canvas.drawText(Integer.toString(colsAndRows-i), 
    		  getPix(pxBoardMargin-(i<6 ? 13:11)), 
              getPix(pxBoardMargin+i*pxField+4), paint);

    }
   
    //  kropki 
    int tmp = colsAndRows;
    while (tmp > 11) tmp-=4;
    
    if (tmp == 11) {
      for (int i=3; i<=colsAndRows-4; i+=4) for (int j=3; j<=colsAndRows-4; j+=4) {
        canvas.drawArc(
          new RectF(getPix(pxBoardMargin+pxField*i+9),
        		    getPix(pxBoardMargin+pxField*j-3),
        		    getPix(pxBoardMargin+pxField*i+15),
        		    getPix(pxBoardMargin+pxField*j+3)), 0, 360, true, paint);
      }
    }
   
  }
 
  
  private int getPix(int pixels) {
	return pixels;
  }


  
}
