package comp1140.ass2;

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
public interface Renier {
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
	public boolean isValidDraw(char[] before, int selection);
	
	
	/**
	 * Determine whether a play (playing card from hand) is valid
	 * @param before Game state before the play
	 * @param after Game state after the play
	 * @param playerone True if player one was supposed to have played a card
	 * @return true if the play was valid (just one card from the specified
	 * player was moved from their hand into a legal position).
	 */
	public boolean isValidPlay(char[] before, char[] after, boolean playerone);

	/**
	 * Decide which card to draw.
	 * 
	 * @param before Game state before the card is drawn
	 * @param playerone True if player one is to draw the card
	 * @return The index of the first character of the drawn card. An
	 * index within <dp> indicates a particular card to take from the discard
	 * pile; and must be the first card of the selected suit within the <dp>.
	 * The index of the first card within <d> indicates that a (random) card
	 * from the deck is requested.   Any other index is invalid.  
	 */
	public int draw(char[] before, boolean playerone);

	/**
	 * Play a valid card given a starting game state
	 * @param before Game state before the card is played
	 * @param playerone True if player one is to play the card
	 * @return A new game state that reflects the movement of the
	 * played card (onlu)
	 */
	public char[] play(char[] before, boolean playerone);
}
