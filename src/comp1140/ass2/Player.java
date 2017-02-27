package comp1140.ass2;

import java.util.ArrayList;

/**
 * 
 * @author u5552738 (Fangqin) for majority of player functionality
 * 			u5558958 (Felicity) for end-count (detect game end)
 *			u5584091 (Max) - Play to suit function, originally from AI.
 */
public class Player{
	ArrayList<Card> hand;
	ArrayList<Card> runSpade;
	ArrayList<Card> runClub;
	ArrayList<Card> runDiamond;
	ArrayList<Card> runHeart;
	boolean canPlay;
	boolean canDraw;
	int endCount; 
	
	public Player (
			ArrayList<Card> hand, 
			ArrayList<Card> runSpade, 
			ArrayList<Card> runClub,
			ArrayList<Card> runDiamond, 
			ArrayList<Card> runHeart,
			boolean canPlay,
			boolean canDraw,
			int endCount){
		this.hand = hand;
		this.runSpade = runSpade;
		this.runClub = runClub;
		this.runDiamond = runDiamond;
		this.runHeart = runHeart;
		this.canPlay = canPlay;
		this.canDraw = canDraw;
		this.endCount = endCount;
	}
	
	// a function that check if two piles are equal (for referencing purpose) -u5552738
	public boolean eqPiles(ArrayList<Card> pile1, ArrayList<Card> pile2){
		if (pile1.size() != pile2.size()){
			return false;
		}else for (int i = 0; i < pile1.size(); i++){
			if (pile1.get(i).compareTo(pile2.get(i))!=0){
				return false;
			}
		}return true;	
	}
	
	// a function that check if a card exits in a pile -u5552738
	public boolean has(ArrayList<Card> pile, Card c){
		return pile.contains(c);
	}
	
	//outputs the run for a suit 
	// Felicity u5558985
	public ArrayList<Card> findRun (char wanted){
		if (wanted == 'S')
			return runSpade;
		
		if (wanted == 'D')
			return runDiamond;
		
		if (wanted == 'H')
			return runHeart;
		
		if (wanted == 'C')
			return runClub;
		
		
		return null;
	}
	
	//returns a boolean depending on either or not that all the discards piles are empty (true if empty)
	// Felicity u5558958
	public boolean emptyDiscards (Table table) {
		if ((table.dpC.size() == 0) && (table.dpD.size() == 0) && (table.dpS.size() == 0) && (table.dpH.size() == 0))
			return true;
		
		
		return false;
	}
	
	public boolean emptyRuns (){
		if ((runHeart.size() == 0)&& (runSpade.size() == 0)&&(runClub.size() == 0)&&(runDiamond.size() == 0))
			return true;
		
		
		return false;
	}
	
	// a function that checks the draw if valid -u5552738
	public boolean checkDraw(ArrayList<Card> cP, Table table){
		if (cP.size() == 0)
			return false;
		else return canDraw;
	}
	
	//  a function that checks the play is valid -u5552738
	public boolean checkPlay(Card card, ArrayList<Card> cP, Table table){
		if (has(hand,card)){
			if (canPlay && !canDraw){
				if (cP.size() == 0)
					return true;
				else if (cP.get(0).suit == card.suit && 
							(cP==runHeart||cP==runSpade||cP==runClub||cP==runDiamond)){
					
						if (cP.get(0).compareTo(card) <= 0){
							return true;
						}
					}
				else if (cP.size() > 0 && cP.get(0).suit == card.suit &&
						(cP==table.dpH||cP==table.dpS||cP==table.dpC||cP==table.dpD)) 
							return true;}
			}
			
		return false; 
	}
	

	//a function that counts the moves that contribute towards the end of the game
	// u5558958
	public void endCounter(Table table){
		
		
		boolean findingEnd = true; 
		for (Card mine : hand){
			if (findRun(mine.suit).size() != 0){
				if (checkPlay (mine, findRun(mine.suit), table)){
					findingEnd = false;
				}
			}
		}
		
		if (emptyRuns())
			findingEnd = false;
		
		if ((table.deck.size() == 0) && emptyDiscards(table)){
			endCount = 3;
		} else if (table.deck.size() == 0 && findingEnd){
			endCount = endCount + 1;
		} else { endCount = 0;}
		
	}
	
	// a function that does the draw move
	public void doDraw(ArrayList<Card> chosenPile, Table table){
		
			hand.add(chosenPile.get(0));
			chosenPile.remove(0);
			canDraw = false; 
			endCounter (table);
			
		
	}
	// a function that does the play move
	public void doPlay(Card card, ArrayList<Card> chosenPile){
		if(chosenPile.size()==0)
			chosenPile.add(card);
		else
			chosenPile.add(0,card);
		hand.remove(hand.indexOf(card));
		canPlay = false;
	}
	
	@Override
	public String toString(){
		StringBuilder rtn = new StringBuilder();
		rtn.append("Hand: ");
		rtn.append(hand);
		rtn.append("\nRuns: ");
		rtn.append(runClub + " "+ runDiamond + " "+ runHeart+" "+runSpade);
		rtn.append("End Count = " + endCount);
		return rtn.toString();
	}
	/**
	 * Plays a card to table
	 * 
	 * @param card
	 *            - card to be played
	 * @param table
	 *            - current table state
	 * @param play
	 *            - 0 = discard, 1=play to runs
	 * @return if play was successful or not.
	 */
	
	//plays the card to the correct run or discard pile -u5584091
	public boolean playToSuit(Card card, Table table, int play) {
		ArrayList<Card> pile = null;
		if (card.suit == 'C' && play == 1)
			pile = this.runClub;
		else if (card.suit == 'D' && play == 1)
			pile = this.runDiamond;
		else if (card.suit == 'H' && play == 1)
			pile = this.runHeart;
		else if (card.suit == 'S' && play == 1)
			pile = this.runSpade;
		else if (card.suit == 'C' && play == 0)
			pile = table.dpC;
		else if (card.suit == 'D' && play == 0)
			pile = table.dpD;
		else if (card.suit == 'H' && play == 0)
			pile = table.dpH;
		else if (card.suit == 'S' && play == 0)
			pile = table.dpS;
		if (checkPlay(card, pile, table)) {
			doPlay(card, pile);
			return true;
		}
		return false;
	}
}
