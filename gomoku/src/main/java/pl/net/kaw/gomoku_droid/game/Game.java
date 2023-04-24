/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.game;

import java.util.List;

import android.os.Handler;
import android.util.Log;
import pl.net.kaw.gomoku_droid.app.AppBase;
import pl.net.kaw.gomoku_droid.app.AppEventBus;
import pl.net.kaw.gomoku_droid.app.IConfig;
import pl.net.kaw.gomoku_droid.app.Settings;
import pl.net.kaw.gomoku_droid.events.BoardClickEvent;
import pl.net.kaw.gomoku_droid.events.GameOverEvent;
import pl.net.kaw.gomoku_droid.events.PlaySoundEvent;
import pl.net.kaw.gomoku_droid.events.PlayerMoveEvent;
import pl.net.kaw.gomoku_droid.events.ProgressEvent;


/**
 *
 * Wątek kontrolujący przebieg rozgrywki
 * 
 * @author Maciej Kawecki
 * 
 */
public class Game extends Thread {
  
   private final static String TAG = Game.class.getSimpleName();

   /** Stan gry */
   private volatile boolean running;
   /** Ustawienia */
   private final Settings settings;
   /** Nr ruchu */
   private int moveNo;
   /** Czy teraz ruch gracza */
   private volatile boolean humansTurn;
   /** Ostatni ruch gracza (klik) */   
   private volatile BoardClickEvent lastClick;
   /** Uchwyt do UI */
   private final Handler handler;
   /** Plansza */
   private Board board;
   /** Zatrzymanie po ruchu AI */
   private boolean sleep = false;
   
   
   public Game(Handler handler) {
     settings = AppBase.getInstance().getSettings();
     this.handler = handler;
     running = false;
   }
   
   public void stopRunning() {
	  running = false;
   }
   
   public boolean isRunning() {
	  return running;
   }

   
   /**
    * Rozpoczęcie nowej rozgrywki.
    */
   @Override
   public void run() {
     	   
	 board = new Board(settings);
	 lastClick = null;
	 moveNo = 1;
	 Player[] players = new Player[2]; 
		     	 	 
	 if (settings.isComputerStarts()) {
	   players[0] = new PlayerComputer(BoardFieldState.getFirst(), "Android");
	   players[1] = new PlayerHuman(BoardFieldState.getSecond(), "Player");
	 }
	 else {        	  
	   players[0] = new PlayerHuman(BoardFieldState.getFirst(), "Player");
	   players[1] = new PlayerComputer(BoardFieldState.getSecond(), "Android");
	 }
		         
	 running = true;  
	   
     while(running) { 
    	 
       for (int i=0; i <= 1; i++) {
    	        
         BoardFieldState color = players[i].getPieceColor();	 
         humansTurn = players[i].isHuman();
         BoardField lastMove = null;
       
         if (humansTurn)  { 
    	  
    	   try {  			 
		     synchronized(this) {
		  	   wait();				 
		     }			 		 
		   }		   
		   catch (InterruptedException ex) {
			  stopRunning();
			  break;
		   }    	
    	     
    	   try {
    	     lastMove = new BoardField(lastClick.getA(), lastClick.getB(), color);
    	   }
    	   catch (NullPointerException e) {
    	     lastMove = null;
    	   }
    	   
         }
         else {
           fireProgress();
    	   lastMove = MoveGenerator.getMove(board, color);   
    	   if (sleep) {
      		 sleep = false;
    		 try {  			 
    		   synchronized(this) {
    		     wait();				 
    		   }			 		 
    		 }		   
    		 catch (InterruptedException ex) {
    		   stopRunning();
    		   break;
    		 }    	
    	   }
    	   fireProgress();
         }
              
         if (lastMove == null || !running) return;
       
         final PlayerMoveEvent moveEvent = new PlayerMoveEvent(lastMove, players[i].getName());
  	     board.setFieldState(lastMove.getA(), lastMove.getB(), lastMove.getState());              
         if (IConfig.DEBUG) Log.d(TAG, moveEvent.toString());
  	        
         handler.post(() -> {
             AppEventBus.post(moveEvent);
             AppEventBus.post(new PlaySoundEvent(PlaySoundEvent.SoundType.MOVE));
         });
          
         List<BoardField> winRow = board.getWinningRow(lastMove);
        
         if (winRow != null || board.freeFieldsAmount==0) {
                
            final GameOverEvent gameOverEvent = new GameOverEvent(winRow, players[i], moveNo);
            handler.post(() -> {
              AppEventBus.post(gameOverEvent);
              AppEventBus.post(new PlaySoundEvent(PlaySoundEvent.SoundType.SUCCESS));
            });
         	 	
            stopRunning();         
                 
          }           
                       
          moveNo++;         
    
       }
          
     }
     
   }
   
   
   
   public boolean setLastClick(BoardClickEvent lastClick) {
	  if (!running || !humansTurn || board == null
		|| board.getFieldState(lastClick.getA(), lastClick.getB()) != BoardFieldState.EMPTY) return false;
	  this.lastClick = lastClick;
	  return true;
   }
   
   
   public void sleep() {
	 sleep = true;
   }
   
   
   private void fireProgress() {
	   
	   handler.post(() -> AppEventBus.post(new ProgressEvent()));
	   
	   if (moveNo < 3) {
		 try {
		   synchronized(this) {
		     wait(300);
		   }
		 }
		 catch (InterruptedException e) { 
			 stopRunning();
		 }
	   }
	   
   }
   

}


