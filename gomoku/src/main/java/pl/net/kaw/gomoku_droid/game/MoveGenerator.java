/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;

import android.util.Log;
import fr.pixelprose.minimax4j.Difficulty;
import fr.pixelprose.minimax4j.IA;
import pl.net.kaw.gomoku_droid.app.IConfig;

/**
*
* Generator ruchów komputera -  AI, biblioteka Minimax4j
* 
* @author Maciej Kawecki
* 
* 
*/
public class MoveGenerator extends IA<BoardField> {

   /** Wybrany algorytm */
   private final static Algorithm algo = Algorithm.NEGASCOUT;
   /** Maksymalna głębokość rekurencji */
   protected final static int MAX_DEPTH = 2;
   /** Maksymalny możliwy wynik (wygrana) */
   protected final static int MAX_SCORE = Integer.MAX_VALUE - 1;   
   /** Współczynnik obrony */
   private final static double DEFENCE = 0.8;
	
   /** Kolor bieżącego gracza */ 
   private BoardFieldState currentPlayer;   
   /** Referencja do logicznej warstwy planszy */
   private final Board board;
	   
   /** True jeżeli koniec gry (wygrana lub remis) */
   private boolean gameOver = false;
   
   private final static String TAG = MoveGenerator.class.getSimpleName();
   
   
   /**
    * Konstruktor obiektu reprezentującego algorytm AI
    * @param board Referencja do logicznej warstwy planszy
    * @param currentPlayer Kolor kamieni gracza-komputera
    */
   private MoveGenerator(Board board, BoardFieldState currentPlayer) {
	       
	  super(algo); 
      this.currentPlayer = currentPlayer;  
      this.board = board;
	      
   }	
    
   
   /**
    * Pobranie sugerowanego ruchu dla komputera
    * @param board Ref. do logiki planszy
    * @param computerColor Kolor kamieni komputera
    * @return Sugerowany ruch
    */
   public static BoardField getMove(Board board, BoardFieldState computerColor) {
	   
	 // na początku losowy ruch w pobliżu środka
	 if (board.getFreeFieldsAmount() >= board.getFieldsAmount()-1) {
	   int a = board.getColsAndRows() / 2;
	   int rand1, rand2;
	   do {
	     rand1 = new Random().nextInt(a) - a/2;
	     rand2 = new Random().nextInt(a) - a/2;
	   } while (board.getFieldState(a + rand1, a + rand2) != BoardFieldState.EMPTY);
	   return new BoardField(a + rand1, a + rand2, computerColor);
	 }
   	   
	 StopWatch watch;
	
	 if (IConfig.DEBUG) { 
		 watch = new StopWatch();
		 watch.start();
	 }
	 
	 BoardField move = new MoveGenerator(board, computerColor).getBestMove();
	 if (move == null) return null;
	 move.setState(computerColor);
	 
	 if (IConfig.DEBUG)  {
        watch.stop();
        Log.d(TAG, "Move generated @ " + String.format("%.3f", watch.getNanoTime()/1000000000.0));
	 }
	 
	 return move;
	 
   }      
	
	
   @Override
   public Difficulty getDifficulty()
   {
	 return () -> MAX_DEPTH;
   }
	

   @Override
   public boolean isOver() {
	 return gameOver;
   }

   
   @Override
   public void makeMove(BoardField move) {
			
	 board.setFieldState(move.getA(), move.getB(), currentPlayer);
	 next();
	  
   }

   
   @Override
   public void unmakeMove(BoardField move) {

	 board.setFieldState(move.getA(), move.getB(), BoardFieldState.EMPTY);	
	 previous();
	  
   }

   
   @Override
   public List<BoardField> getPossibleMoves() {
		
	 return board.getEmptyFields();
	  
   }
	

   @Override
   public double evaluate() {
	   
	 int score1 = board.getScore(currentPlayer);
	 next();
	 int score2 = board.getScore(currentPlayer);
	 previous();
	 
	 boolean win1 = score1 == MAX_SCORE;
	 gameOver = win1 || score2 == MAX_SCORE || board.getFreeFieldsAmount() == 0;
	 
	 return win1 ? score1 : score1 - score2* DEFENCE;	 
	 
   }
	

   @Override
   public double maxEvaluateValue() {
 	 return MAX_SCORE + 1;
   }

   @Override
   public void next() {
	 currentPlayer = currentPlayer.getOpposite();
   }

   @Override
   public void previous() {
	 next();
   }

}
