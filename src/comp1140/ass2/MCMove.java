package comp1140.ass2;

import java.util.ArrayList;
/**
 * A monte carlo move.
 * @author Max
 *
 */
public class MCMove {
	ArrayList<Card> drawPile;
	char dpSuit;
	int cardPlayed;
	int play; //0 = discard, 1=play.
	int wins;
	Card drawnCard;
	
	public MCMove(ArrayList<Card> drawPile, int cardPlayed, int play, char dpSuit){
		wins =0;
		this.drawPile = drawPile;
		this.cardPlayed = cardPlayed;
		this.play = play;
		this.dpSuit=dpSuit;
	}
}
