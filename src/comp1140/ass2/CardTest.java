package comp1140.ass2;

import static org.junit.Assert.assertTrue;


import org.junit.Test;

//Written by Felicity Lee

public class CardTest extends JFXTest{
	
	// see if the compare value for card is working
	@Test
	 public void testCompareValue() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() {
	    	Card one = new  Card('2','C');
	    	Card two = new  Card('5', 'C');
	    	Card three = new  Card('6','H');
	    	Card four = new  Card('9','D');
	    	Card five = new  Card('2','C');
	 	 	
	    	assertTrue(two.compareTo(one) > 0);
	    	assertTrue(one.compareTo(five) == 0);
	    	assertTrue(three.compareTo(four) < 0);
	    	assertTrue(two.compareTo(three) < 0 );
	     
	 		endOfJFXTest();
	     }
	     
	   });}
	//see if the same suit function is working
	
	@Test
	 public void testSameSuit() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() {
	    	Card one = new  Card('2','C');
	    	Card two = new  Card('5', 'C');
	    	Card three = new  Card('6','H');
	 	 	
	    	assertTrue(one.sameSuit(two));
	    	assertTrue(! (one.sameSuit(three)));
	     
	 		endOfJFXTest();
	     }
	     
	   });}
	
}
