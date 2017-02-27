package comp1140.ass2;



import java.util.ArrayList;
import java.util.Scanner;


/**
 * 
 * @author u5558958 (Felicity) with minor modifications from u5584091 (Max)
 *
 */
public class Human extends Player {
	

	public Human(ArrayList<Card> hand, ArrayList<Card> runSpade,
			ArrayList<Card> runClub, ArrayList<Card> runDiamond,
			ArrayList<Card> runHeart, boolean canPlay, boolean canDraw, int endCount) {
		super(hand, runSpade, runClub, runDiamond, runHeart, canPlay, canDraw, endCount);
	}
	
	public Human(Player pl){
		super(pl.hand, pl.runSpade, pl.runClub, pl.runDiamond, pl.runHeart, pl.canPlay, pl.canDraw, pl.endCount);
	}
	// takes in move from human via typing input and does it depending on whether valid move or not 
	// if not a valid move, then it requests for another move from the person
	// u5558958 Felicity + u5584091 Max
	public void manageMove(Table table, Player opponent) {
		//deckDrawChosen is the deck that the player chooses to draw from
		//cardChosen is the card in the player's hand that the player wishes to play
		//deckPlayChosen is the deck that the player wishes to play into
		Scanner in = new Scanner (System.in);
		System.out.println(table);
		System.out.print("Your ");
		System.out.println(this);
		System.out.print("Opponent's ");
		System.out.println(opponent);
		
		
		
		
		// chosenDraw, chosenPlay and chosenCard are one the moves that the computer reads
		ArrayList<Card> chosenDraw = new ArrayList<Card>();
		Card chosenCard = null;		
		ArrayList<Card> chosenPlay = new ArrayList<Card>();
		while(true){
		while(true){
			System.out.println("Choose deck to draw from");
			System.out.println("Choose from: 'deck', 'discarded clubs', 'discarded diamonds', 'discarded hearts', 'discarded spades'");
			String deckDrawChosen = in.nextLine();
			
		if (deckDrawChosen.equals("deck")){
			chosenDraw = table.deck;
			break;
		} else if (deckDrawChosen.equals("discarded clubs")){
			chosenDraw = table.dpC;
			break;
		} else if (deckDrawChosen.equals("discarded hearts")){
			chosenDraw = table.dpH;
			break;
		}else if (deckDrawChosen.equals("discarded spades")){
			chosenDraw = table.dpS;
			break;
		}else if (deckDrawChosen.equals("discarded diamonds")){
			chosenDraw = table.dpD;
			break;
		} else {
			System.out.println("Did not choose valid deck to draw from");
			}
		}
		
		System.out.println(chosenDraw);
		System.out.println(table);
		System.out.println(this);
		if (checkDraw(chosenDraw ,table)){
			doDraw(chosenDraw, table);
			break;
		} else{
			System.out.println("Not a valid draw");}
		}
		getCard:while(true){
		System.out.println("Choose a card from hand");
		
		for (Card one: hand){
			System.out.print(one);
			System.out.print(", ");}
		System.out.println("");
		
		String cardChosen = in.nextLine();
		
		for (Card one : hand){
			if (one.toString().equals(cardChosen)){
				chosenCard = one;
				break getCard;
				}
			}
		
		if (chosenCard == null)
			{ System.out.println("Did not choose valid card to play");}
		
		}
		while(true){

			while(true){
		System.out.println("Choose deck to play card into");
		System.out.println("Choose: 'runs' or 'discards'");
		String deckPlayChosen = in.nextLine();
		
		
			if (!(deckPlayChosen.equals( "runs")) && !(deckPlayChosen.equals("discards"))){
				System.out.println("Not valid deck to play into");
				
			} else{
				if (deckPlayChosen.equals("runs")){
					if (chosenCard.suit == 'H'){
						chosenPlay = runHeart;
					} else if (chosenCard.suit == 'S'){
						chosenPlay = runSpade;
					} else if (chosenCard.suit == 'D'){
						chosenPlay = runDiamond;
					} else if (chosenCard.suit == 'C'){
						chosenPlay = runClub;
					}
				} else {
					if (chosenCard.suit == 'H'){
						chosenPlay = table.dpH;
					} else if (chosenCard.suit == 'S'){
						chosenPlay = table.dpS;
					} else if (chosenCard.suit == 'D'){
						chosenPlay = table.dpD;
					} else if (chosenCard.suit == 'C'){
						chosenPlay = table.dpC;
					}
				}
				break;
			}
		}
		
		
		
			if (checkPlay (chosenCard, chosenPlay, table)){
				doPlay (chosenCard, chosenPlay);
				break;
			} else 
				System.out.println("Not a valid play");
			
		}
		in.close();
	}
	
	
}
