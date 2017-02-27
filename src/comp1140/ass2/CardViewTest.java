package comp1140.ass2;
//written by Felicity Lee u5558958
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import org.junit.Test;

public class CardViewTest extends JFXTest {
	
	Random rng = new Random();


	@Test //testing whether snap to actually snaps too
	 public void testSnapTo() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() { 
	    	 
	    	 for (int i = 0; i < 10; i ++){
	    		 CardView change = new CardView(new Card('4','C'), -1, -1, 0.2, true);
	    		 int x = rng.nextInt(500);
	    		 int y = rng.nextInt(500);
	    		 
	    		 change.snapTo(x,y);
	    		 
	    		 Timeline test1 = new Timeline ( new KeyFrame (Duration.millis(100),
	    				 ea -> {
	    					 assertTrue((change.getLayoutX() ==0)
	    							 && (change.getLayoutX() == 0) );
	    				 }));
	    		 
	    		 test1.setDelay(Duration.millis(200));
	    		 test1.play();
	    		 
	    		 Timeline test2 = new Timeline ( new KeyFrame (Duration.millis(100),
	    				 ea -> {
	    					 assertTrue((change.getLayoutX() != x) &&(change.getLayoutX() !=0)
	    							 && (change.getLayoutY() != y)
	    							 && (change.getLayoutX() != 0) );
	    				 }));
	    		 
	    		 test2.setDelay(Duration.millis(200));
	    		 test2.play();
	    		 
	    		 Timeline test3 = new Timeline ( new KeyFrame (Duration.millis(100),
	    				 ea -> {
	    					 assertTrue((change.getLayoutX() == x)&& (change.getLayoutY() == y));
	    				 }));
	    		 
	    		 test3.setDelay(Duration.millis(400));
	    		 test3.play();
	    		 
	    	}
	    	 endOfJFXTest();
	     }
	     
	   });}
	
	@Test //testing whether enable works
	public void testEnable() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() { 
	    	 
	    	 for (int i = 0; i < 10; i ++){
	    		int x = rng.nextInt(500);
		    	int y = rng.nextInt(500);
	    		CardView change = new CardView(new Card('4','C'), x, y, 0.2, true);
	    		change.enable();
	    		//making a robot to simulate mouse mousements
	    		 try {
					Robot testing = new Robot();
					//seeing if moving the mouse onto the card makes it glow
					testing.mouseMove(0,0);
					assertJFXTrue(change.getGlow() == 0);
					testing.mouseMove(x+1, y+1);					
					Timeline test1 = new Timeline ( new KeyFrame (Duration.millis(100),
		    				 ea -> {
		    			    		assertJFXTrue(change.getGlow() != 0);
		    			    		
		    				 }));
		    		 
		    		 test1.setDelay(Duration.millis(400));
		    		 test1.play();
		    		 //seeing if pressing the card makes it scale and make it move
		    		 testing.mousePress(InputEvent.BUTTON1_MASK);
		    		 Timeline test2 = new Timeline ( new KeyFrame (Duration.millis(100),
		    				 ea -> {
		    					 assertJFXTrue(change.size.getX() != 0.2 && change.size.getY() != 0.2);
		    					 assertJFXTrue(change.getLayoutX() != x && change.getLayoutY() != y);
		    			    		
		    				 }));
		    		 
		    		 test2.setDelay(Duration.millis(500));
		    		 test2.play();
		    		 //seeing if releasing the mouse and moving it off the card reverts the settings
		    		 Timeline test3 = new Timeline ( new KeyFrame (Duration.millis(100),
		    				 ea -> {
		    					 testing.mouseRelease(InputEvent.BUTTON1_MASK);
		    					 testing.mouseMove(0,0);
		    					 assertJFXTrue(change.size.getX() == 0.2 && change.size.getY() == 0.2);
		    					 assertJFXTrue(change.getGlow() ==0);
		    			    		
		    				 }));
		    		 
		    		 test3.setDelay(Duration.millis(1000));
		    		 test3.play();
		    		 
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		 
	    		 
	    	}
	    	 endOfJFXTest();
	     }
	     
	   });}
	
	@Test //testing whether initialize return works
	public void testInitializeReturn() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() { 
	    	 
	    	 for (int i = 0; i < 10; i ++){
	    		int x = rng.nextInt(1000);
		    	int y = rng.nextInt(1000);
	    		CardView change = new CardView(new Card('4','C'), 0, 0, 0.2, true);
	    		
	    		change.snapTo(x,y);
	    		 Timeline test1 = new Timeline ( new KeyFrame (Duration.millis(100),
	    				 ea -> {
	    					 assertTrue(change.getLayoutX() == x && change.getLayoutY() == y);
	    			    	 change.initializeReturn();	
	    				 }));
	    		 
	    		 test1.setDelay(Duration.millis(400));
	    		 test1.play();
	    		 
	    		 Timeline test2 = new Timeline ( new KeyFrame (Duration.millis(100),
	    				 ea -> {
	    					 assertTrue(change.getLayoutX() == 0 && change.getLayoutY() == 0);
	    				 }));
	    		 
	    		 test2.setDelay(Duration.millis(800));
	    		 test2.play();
	    		
	    		 
	    	}
	    	 endOfJFXTest();
	     }
	     
	   });}
}
