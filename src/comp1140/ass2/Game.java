package comp1140.ass2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/*Please merge with game in the future*/
public class Game implements Renier{
	
	Table table;
	Player p1;
	Player p2;
	boolean endGame=false;
	boolean playerOne;
	
	
	
	public Game(Table table, Player p1, Player p2){
		this.table = table;
		this.p1 = p1;
		this.p2 = p2;
		playerOne=true;
	}
	
	public Game(Table table, Player p1, Player p2, boolean turn){
		this.table = table;
		this.p1 = p1;
		this.p2 = p2;
		playerOne=turn;
	}
	
	@SuppressWarnings("unchecked")
	public Game clone(){
		ArrayList<Card> dC = (ArrayList<Card>) table.dpC.clone();
		ArrayList<Card> dD = (ArrayList<Card>) table.dpD.clone();
		ArrayList<Card> dH = (ArrayList<Card>) table.dpH.clone();
		ArrayList<Card> dS = (ArrayList<Card>) table.dpS.clone();
		ArrayList<Card> dDk = (ArrayList<Card>) table.deck.clone();
		Table tb = new Table(dH, dS, dC, dD, dDk);
		ArrayList<Card> p1p = (ArrayList<Card>) p1.hand.clone();
		ArrayList<Card> p1C = (ArrayList<Card>) p1.runClub.clone();
		ArrayList<Card> p1D = (ArrayList<Card>) p1.runDiamond.clone();
		ArrayList<Card> p1H = (ArrayList<Card>) p1.runHeart.clone();
		ArrayList<Card> p1S = (ArrayList<Card>) p1.runSpade.clone();
		ArrayList<Card> p2p = (ArrayList<Card>) p2.hand.clone();
		ArrayList<Card> p2C = (ArrayList<Card>) p2.runClub.clone();
		ArrayList<Card> p2D = (ArrayList<Card>) p2.runDiamond.clone();
		ArrayList<Card> p2H = (ArrayList<Card>) p2.runHeart.clone();
		ArrayList<Card> p2S = (ArrayList<Card>) p2.runSpade.clone();
		Player np1 = new Player(p1p, p1S, p1C, p1D, p1H, p1.canPlay, p1.canDraw, p1.endCount);
		Player np2 = new Player(p2p, p2S, p2C, p2D, p2H, p2.canPlay, p2.canDraw, p2.endCount);
		return new Game(tb, np1, np2, playerOne);
	}
	/**
	 * Creates a new games with cards dealt.
	 * @param deckNo - This is the number of cards we want to put into the deck.
	 * @return Game with dealt cards
	 */
	/*
	 * Deal written by Max Wang (u5584091)
	 */
	public static Game deal(int royals, int deckNo, Random rng){
		Deck deck = new Deck(royals);
	//	Deck deck = new Deck();
		Collections.shuffle(deck.cards, rng);
		
		/*
		 * We initialize the player's hands\
		 */
		ArrayList<Card> p1hand = new ArrayList<Card>();
		ArrayList<Card> p2hand = new ArrayList<Card>();
		for(int i=0; i<7; i++){
			p1hand.add(deck.cards.get(i));
		}
		for(int i=7; i<14; i++){
			p2hand.add(deck.cards.get(i));
		}
		
		/*
		 * We now initialize the deck based on the number of cards given
		 */
		ArrayList<Card> gamedeck = new ArrayList<Card>();
		for(int i=14; i<14+deckNo; i++){
			gamedeck.add(deck.cards.get(i));
		}
		/*
		 * And now, for the four discard piles.
		 */
		ArrayList<Card> clubs = new ArrayList<Card>();
		ArrayList<Card> diamonds = new ArrayList<Card>();
		ArrayList<Card> hearts = new ArrayList<Card>();
		ArrayList<Card> spades = new ArrayList<Card>();
		
		for(int i = 14+deckNo; i<deck.cards.size(); i++){
			if(deck.cards.get(i).suit() == 'C')
				clubs.add(deck.cards.get(i));
			if(deck.cards.get(i).suit() == 'D')
				diamonds.add(deck.cards.get(i));
			if(deck.cards.get(i).suit() == 'H')
				hearts.add(deck.cards.get(i));
			if(deck.cards.get(i).suit() == 'S')
				spades.add(deck.cards.get(i));
		}
		
		return new Game(new Table(hearts, spades, clubs, diamonds, gamedeck),
						 new Player(p1hand, new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), true, true, 0),
						 new Player(p2hand, new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), false, false, 0));
	};
	/*
	 * toString() written by Max Wang(u5584091)
	 */
	public String toString(){
		StringBuilder rtn = new StringBuilder();
		rtn.append("P1 Hand: ");
		rtn.append(p1.hand);
		rtn.append("\nP1 Runs: ");
		rtn.append(p1.runClub + " "+ p1.runDiamond + " "+ p1.runHeart+" "+p1.runSpade);
		rtn.append("\nP2 Hand: ");
		rtn.append(p2.hand);
		rtn.append("\nP2 Runs: ");
		rtn.append(p2.runClub + " "+ p2.runDiamond + " "+ p2.runHeart+" "+p2.runSpade+"\n");
		rtn.append(table.toString());
		rtn.append("\n"+"EndCounts = " + p1.endCount + " , " + p2.endCount);
		rtn.append("\n" + "Player one: cP" + p1.canPlay + ", cD" + p1.canDraw);
		rtn.append("\n" + "Player two: cP" + p2.canPlay + ", cD" + p2.canDraw);
		return rtn.toString();
	}
	
	/*
	 * Returns score of game.
	 * Note: Needs to manage JQKs in the future.
	 * Function written by Max Wang (u5584091)
	 */
	public Score getScore(){
		Score score = new Score();
		score.p1+=getRunScore(p1.runClub);
		score.p1+=getRunScore(p1.runDiamond);
		score.p1+=getRunScore(p1.runHeart);
		score.p1+=getRunScore(p1.runSpade);
		score.p2+=getRunScore(p2.runClub);
		score.p2+=getRunScore(p2.runDiamond);
		score.p2+=getRunScore(p2.runHeart);
		score.p2+=getRunScore(p2.runSpade);	
		return score;
	}
	public static int getRunScore(ArrayList<Card> run){
		int score = 0;
		if(run.size()>0){
			score-=20;
			for(Card c:run){
				score+=c.value();
			}
			//If face card (i.e, 0 value), double returns
			for(Card c:run){
				if(c.value()==0)
					score*=2;
			}
		}
		return score;
	}
	
	
	/**
	 * Deprecated functions. These were implemented as part of the first deliverable,
	 * and are no longer in use due to the massive restructuring of the program.
	 * Initially, we though of having a function that converted the game back into the
	 * the state of an array of cards. However, this was deemed unnecessary as it
	 * would bring little to the game, and also significantly increase the computational
	 * complexity of doing plays and draws - which is detrimental to the ability of AI.
	 * 
	 * They remain due to the requirement of implementing renier.
	 */
	
	
	/*
	 * Notation:
	 * 
	 * Each card is represented by two characters, the card's rank followed by its
	 * suit:
	 *    rank: The letters 'A', '2', .. '9', 'T', 'J', 'Q' 'K' represent the ranks
	 *          ace, two, .. nine, ten, jack queen and king respectively.
	 *    suit: The letters 'S', 'C', 'D', 'H' represent spades, clubs, diamonds and
	 *          hearts respectively.
	 * So for example, the seven of hearts is represented as "7H" and the ace of
	 * spades is represented as "AS".
	 * 
	 * The game state is represented as an array of characters 85 characters,
	 * increasing to 93, 101, or 109 if the one, two, or three face cards are
	 * added to the game.   The array is composed of six sets of cards,
	 * each set separated by a space character, thus:
	 * 
	 * <p1h>' '<p1r>' '<p2h>' '<p2r>' '<dp>' '<d>
	 * 
	 * where:
	 *   <p1h> is the set of cards in player one's hand
	 *   <p1r> is the set of cards in player one's runs
	 *   <p2h> is the set of cards in player two's hand
	 *   <p2r> is the set of cards in player two's runs
	 *   <dp> is an ordered list of cards in the discard pile
	 *   <d> is the set of cards in the deck
	 * 
	 */

	@Override
	/**
	 * Determine whether a draw (a choice of card to pick up) is valid
	 * @param before Game state before the card is drawn
	 * @param selection Index of first character of selected card.  An
	 * index within <dp> indicates a particular card to take from the discard
	 * pile; and must be the first card of the selected suit within the <dp>.
	 * The index of the first card within <d> indicates that a (random) card
	 * from the deck is requested.  Any other index is invalid.  
	 * @return true if the draw was valid
	 */
	public boolean isValidDraw(char[] before, int selection) {
		// TODO Auto-generated method stub
		int[] indices = getIndices(before);
		
		if(selection < before.length
		&& selection >= indices[3]
		&& before[selection] != ' '
		&& before[selection] != 'H'
		&& before[selection] != 'S'
		&& before[selection] != 'C'
		&& before[selection] != 'D'
		){
			if(selection >= indices[3]
			&& selection < indices[4]){
				//Selection is inside discard piles
				char selectedSuit = before[selection+1];
				for(int i = (selection-1); i >= indices[3]; i-=2){
					if(before[i]==selectedSuit)
						return false;
				}
					return true;
			}
			else if(selection==indices[4]){
				//Selection is inside deck
				return true;
			}
		}
		return false;
	}
	
	/* Returns the start indices of the different sets cards
	 * separated by a space character.
	 * return[0] = <p1r>
	 * return[1] = <p2h>
	 * return[2] = <p2r>
	 * return[3] = <dp>
	 * return[4] = <d>
	 */
	private static int[] getIndices(char[] state){
		int[] indices = new int[5];
		int currIndex = 0;
		for(int i = 0; i< state.length; i++){
			if(state[i]==' '){
				indices[currIndex] = i+1;
				currIndex++;
			}
		}
		if(currIndex!=5){
			indices[4]=state.length-1;
		}
		return indices;
	}
	
	
	/* Checks if the game state is a valid one.
	 * This function assumes that no cards are duplicate,	
	 * and that every card is a valid card.
	 * (this can be assumed as we are generating the
	 * char array based on the cards later on.) The function
	 * checks that there are 7 cards in the player's hands,
	 * and that the runs as well as discard piles are grouped
	 * in suits.
	 */
	
	@Override
	public boolean isValidPlay(char[] before, char[] after, boolean playerone) {
		
		/*steps
		 * 1. check playerone and see if only one card was moved
		 * 2. check playertwo and see if only one card was moved
		 * 3. next find out which card was moved
		 * 4. next find out where that card is now either in runs or discard pile? 
		 */
		
		if (after == before)
			return false;
		
		if (playerone && (getIndices(before)[0] != getIndices(after)[0] + 2))
			return false;
		
		if (playerone && (getIndices(before)[2]-getIndices(before)[1]!=getIndices(after)[2]-getIndices(after)[1]))
			return false;
		
		if ( playerone != true && (getIndices(before)[2] - getIndices(before)[1]) != ((getIndices(after)[2] - getIndices(after)[1]) + 2)) 
			return false;
		if (!playerone && (getIndices(before)[0] != getIndices(after)[0]))
			return false;
		System.out.println(getIndices(before)[1]-getIndices(before)[0]);
		System.out.println(getIndices(after)[1]-getIndices(after)[0]);
		if(playerone && ((getIndices(before)[1]-getIndices(before)[0])!=(getIndices(after)[1]-getIndices(after)[0]))){
			if(playerone && (getIndices(before)[1]-getIndices(before)[0])!=(getIndices(after)[1]-getIndices(after)[0]-2)){
				return false;	
			}
		}
		
			
		if(!playerone && (getIndices(before)[3]-getIndices(before)[2])!=(getIndices(after)[3]-getIndices(after)[2])){
			if(!playerone && (getIndices(before)[3]-getIndices(before)[2])!=(getIndices(after)[3]-getIndices(after)[2]-2)){
				return false;
			}
		}
				
		if(playerone && ((getIndices(before)[3]-getIndices(before)[2])!=(getIndices(after)[3]-getIndices(after)[2]))){
			return false;
		}
		if(!playerone && ((getIndices(before)[1]-getIndices(before)[0])!=(getIndices(after)[1]-getIndices(after)[0])))
			return false;
		
		return true;
	}

	@Override
	public int draw(char[] before, boolean playerone) {
		int[] indices = getIndices(before);
		int firstDeck = indices[4];
		int firstDiscard = indices[3];
		if (firstDeck < 84){ //check if the deck is empty
			 return firstDeck;
		}else if ((firstDeck - firstDiscard) > 2){ //checks if the discard pile is empty
			return firstDiscard;
		}else
		    return 0; 
	}
	/**
	 * Play a valid card given a starting game state
	 * @param before Game state before the card is played
	 * @param playerone True if player one is to play the card
	 * @return A new game state that reflects the movement of the
	 * played card (onlu)
	 */
	@Override
	public char[] play(char[] before, boolean playerone) {
		// TODO Auto-generated method stub
		for(char i: before)System.out.print(i);
		System.out.println();
		char[] retboard = new char[before.length];
		int[] indices = getIndices(before);
		if(playerone){
			//We basically discard the first card of player 1.
			//This technically is a valid play.
			//Albeit useless.
			char suit = before[1];
			int indexofsuit = 0;
			for(int i = indices[3]+1; i<indices[4]; i+=2){
				if(before[i]==suit){
					indexofsuit=i-1;
					break;
				}
			}
			if(indexofsuit==0){
				System.arraycopy(before, 2, retboard, 0, indices[3]-2);
				System.arraycopy(before, 0, retboard, indices[3]-2, 2);
				System.arraycopy(before, indices[3], retboard, indices[3], before.length-indices[3]);
				for(char i: retboard) System.out.print(i);
			}
			else{
				System.arraycopy(before, 2, retboard, 0, indexofsuit-2);
				System.arraycopy(before, 0, retboard, indexofsuit-2, 2);
				System.arraycopy(before, indexofsuit, retboard, indexofsuit, before.length-indexofsuit);
				for(char i: retboard) System.out.print(i);
			}
		}
		else{
			
			char suit = before[1+indices[1]];
			int indexofsuit = 0;
			for(int i = indices[3]+1; i<indices[4]; i+=2){
				if(before[i]==suit){
					indexofsuit=i-1;
					break;
				}
			}
			if(indexofsuit==0){
				System.arraycopy(before, 0, retboard, 0, indices[1]);
				System.arraycopy(before, indices[1]+2, retboard, indices[1], indices[3]-indices[1]-2);
				System.arraycopy(before, indices[1], retboard, indices[3]-2, 2);
				System.arraycopy(before, indices[3], retboard, indices[3], before.length-indices[3]);
				for(char i: retboard) System.out.print(i);
			}
			else{
				System.arraycopy(before, 0, retboard, 0, indices[1]);
				System.arraycopy(before, indices[1]+2, retboard, indices[1], indexofsuit-indices[1]-2);
				System.arraycopy(before, indices[1], retboard, indexofsuit-2, 2);
				System.arraycopy(before, indexofsuit, retboard, indexofsuit, before.length-indexofsuit);
				for(char i: retboard) System.out.print(i);
			}
		}
		return retboard;
	}
	
	
}
