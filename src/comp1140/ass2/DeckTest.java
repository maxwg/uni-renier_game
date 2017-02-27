package comp1140.ass2;

import java.util.Random;

import org.junit.Test;

//entire class written by Fangqin Chen -u5552738

public class DeckTest extends JFXTest {

	@Test //test the deck has 52 cards in it
	 public void testDeck1() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() { 
	    	Deck deck = new Deck();
	 		assertJFXTrue(deck.cards.size() == 52);
	 		endOfJFXTest();
	     }
	     
	   });}
	
	@Test //test the deck has correct number of cards for chosen number of royals
	 public void testDeck2() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() { 
	    	Random rnd = new Random();
	    	int i = rnd.nextInt(4);
	    	Deck deck = new Deck(i);
	 		assertJFXTrue(deck.cards.size() == (40+i*4));
	 		endOfJFXTest();
	     }
	     
	   });}
	
	@Test //test there are no duplicate cards in a deck
	public void testDeck3() throws InterruptedException {
		runJFXTest(new Runnable(){
			@Override
			public void run(){
				Random rnd = new Random();
				int i = rnd.nextInt(4);
				Deck deck = new Deck(i);
				boolean noDuplicate = true;
				for (int j = 0; j < deck.cards.size(); j++){
					for (int k = j+1; k < deck.cards.size(); k++){
						if (deck.cards.get(j) == deck.cards.get(k))
							noDuplicate = false;
					}
				}
				assertJFXTrue(noDuplicate);
				endOfJFXTest();
			}
		});}

}
