package comp1140.ass2;

import java.util.ArrayList;

/**
 * A deck of cards.
 * @author Max & Felicity.
 *
 */
public class Deck {
	ArrayList<Card> cards = new ArrayList<Card>();
	public Deck(){
		char[] suits = {'C', 'D', 'H', 'S'};
		char[] vals = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
		for (char s:suits){
			for(char c: vals)
				cards.add(new Card(c,s));
		}
	}
	public Deck(int royals){
		
		char[] suits = {'C', 'D', 'H', 'S'};
		char[] vals = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
		
		int deckcards = 10+ royals;

			for (char s: suits){
				for (int y = 0; y <deckcards; y++ ){
						cards.add (new Card (vals[y],s));
					}
				}
					
			
		}
		
		
	
}
