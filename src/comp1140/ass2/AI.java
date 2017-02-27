package comp1140.ass2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Basic Monte Carlo, and original heuristics based AI written by Max.
 * Additional Monte-Carlo heuristics written by Fanqin.
 * 
 * @author u5584091 (Max), u5552738 (Fangqin)
 *
 */
public class AI extends Player {

	int difficulty;

	char[] suits = { 'C', 'D', 'H', 'S' };
	Random rng = new Random();

	public AI(ArrayList<Card> hand, ArrayList<Card> runSpade,
			ArrayList<Card> runClub, ArrayList<Card> runDiamond,
			ArrayList<Card> runHeart, boolean canPlay, boolean canDraw,
			int endCount) {
		super(hand, runSpade, runClub, runDiamond, runHeart, canPlay, canDraw,
				endCount);
	}

	public AI(Player pl, int difficulty) {
		super(pl.hand, pl.runSpade, pl.runClub, pl.runDiamond, pl.runHeart,
				pl.canPlay, pl.canDraw, pl.endCount);
		this.difficulty = difficulty;
	}

	/**
	 * Draws a random card from the possible draws. Used for monte carlo.
	 * Written by Max (u5584091)
	 * 
	 * @param table
	 *            - The game's table.
	 */
	void drawRandom(Table table) {
		boolean played = false;
		if (table.deck.size() + table.dpC.size() + table.dpD.size()
				+ table.dpH.size() + table.dpS.size() == 0) {
			endCount = 4;
			return;
		}
		while (!played) {
			int drawDeck = rng.nextInt(5);
			if (drawDeck == 0) {
				if (table.deck.size() > 0) {
					doDraw(table.deck, table);
					played = true;
				}
			} else if (drawDeck == 1) {
				if (table.dpC.size() > 0) {
					doDraw(table.dpC, table);
					played = true;
				}
			} else if (drawDeck == 2) {
				if (table.dpD.size() > 0) {
					doDraw(table.dpD, table);
					played = true;
				}
			} else if (drawDeck == 3) {
				if (table.dpH.size() > 0) {
					doDraw(table.dpH, table);
					played = true;
				}
			} else if (drawDeck == 4) {
				if (table.dpS.size() > 0) {
					doDraw(table.dpS, table);
					played = true;
				}
			}
		}
	}

	/**
	 * This function plays a random card for the AI.
	 * 
	 * @param table
	 *            = table to be played on
	 */
	private void playRandom(Table table) {
		if (hand.size() == 7) {
			endCount = 4;
			return;
		}
		boolean played = false;
		int card = rng.nextInt(hand.size());
		int play = rng.nextInt(2);// 0 = discard, 1 = play.
		while (!played) { 
			play = rng.nextInt(2);
			card=(card+1)%(hand.size()-1);
			if (!(hand.get(card).value() > 8
					&& totalRun(runPile(hand.get(card))) == 0 && play == 1)) {
				// do not play a card greater than 8 if the runPile is empty
				// -u5552738
				if (!(runPile(hand.get(card)).size() > 0
						&& hand.get(card).value() > runPile(hand.get(card))
								.get(0).value() && play == 0)) {
					// do not discard a card if it is greater than the first
					// card in the run of its suit -u5552738
					// if(!(runPile(hand.get(card)).size() == 0 &&
					// otherRuns(hand.get(card)) && play == 1)){
					// do not start a run if existing runs have playable cards
					// -u5552738
					if (playToSuit(this.hand.get(card), table, play)) {
						played = true;
					}
				}// }
			}
		}
	}

	/**
	 * This uses Monte Carlo to calculate the play to be done. Written by
	 * Max(u5584091)
	 * 
	 * @param game
	 *            - current game.
	 */

	// find which runPile the chosen card belongs to -u5552738
	private ArrayList<Card> runPile(Card c) {
		if (c.suit() == 'H') {
			return runHeart;
		} else if (c.suit() == 'D') {
			return runDiamond;
		} else if (c.suit() == 'S') {
			return runSpade;
		} else
			return runClub;

	}

	/**
	 *  find the total score of the chosen run -u5552738
	 * @param cards - runs to fnd the core of
	 * @return
	 */
	private int totalRun(ArrayList<Card> cards) {
		int sum = 0;
		int multiplyer = 1;
		if (cards.size() == 0) {
			return 0;
		} else {
			for (int i = 0; i < cards.size(); i++)
				if (cards.get(i).value() == 0)
					multiplyer *= 2;
				else
					sum += cards.get(i).value();
			return sum * multiplyer;
		}
	}

	// checks how many royals and find out the multiplyer -u5552738
	private int doubler(ArrayList<Card> cards) {
		int multiplyer = 1;
		for (int i = 0; i < cards.size(); i++)
			if (cards.get(i).value() == 0)
				multiplyer *= 2;
		return multiplyer;
	}

	// check if there exist another card of the same suit in a pile -u5552738
	private boolean suitInPile(ArrayList<Card> cards, Card card) {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).suit() == card.suit())
				return true;
		}
		return false;
	}

	// find the corresponding discard pile of the card -u5552738
	private ArrayList<Card> disPile(Card c, Table table) {
		if (c.suit() == 'H') {
			return table.dpH;
		} else if (c.suit() == 'D') {
			return table.dpD;
		} else if (c.suit() == 'S') {
			return table.dpS;
		} else
			return table.dpC;

	}

	// gives the letter of the suit (referencing purpose) -u5552738
	private char matchingSuit(ArrayList<Card> cards, Game game) {
		if (cards == game.table.deck)
			return 'M';
		else if (cards == game.table.dpH)
			return 'H';
		else if (cards == game.table.dpS)
			return 'S';
		else if (cards == game.table.dpC)
			return 'C';
		else
			return 'D';
	}

	// generate chosen pile to draw from -u5552738
	private ArrayList<Card> csPile(int i, Game game) {
		if (i == 0) {
			return game.table.dpH;
		} else if (i == 1) {
			return game.table.dpD;
		} else if (i == 2) {
			return game.table.dpS;
		} else if (i == 3) {
			return game.table.dpC;
		} else
			return game.table.deck;
	}

	// generate chosen discard pile to draw from when the top card of the
	// discard pile is 1 bigger than the top of the run pile - u5552738
	private ArrayList<Card> csDp(Game game) {
		if (game.table.dpH.size() != 0 && runHeart.size() != 0
				&& game.table.dpH.get(0).value() == runHeart.get(0).value() + 1)
			return game.table.dpH;
		else if (game.table.dpS.size() != 0 && runSpade.size() != 0
				&& game.table.dpS.get(0).value() == runSpade.get(0).value() + 1)
			return game.table.dpS;
		else if (game.table.dpC.size() != 0 && runClub.size() != 0
				&& game.table.dpC.get(0).value() == runClub.get(0).value() + 1)
			return game.table.dpC;
		else
			return game.table.dpD;
	}

	// choose the smallest possible card to play from hand onto a run -u5552738
	private Card smallestToRun(Card card) {
		int value = 100;
		Card result = null;
		ArrayList<Card> run = runPile(card);
		for (Card c : hand) {
			if (run.size() == 0) {
				if (c.value() < value && c.suit == card.suit) {
					value = c.value();
					result = c;
				}
			} else if (c.value() < value && c.suit == card.suit
					&& c.value() > run.get(0).value()) {
				value = c.value();
				result = c;
			}
		}
		return result;
	}

	// check if the run OTHER cards in hand can be played to a run -u5552738
	private boolean playableOther(Card c) {
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i) != c
					&& runPile(hand.get(i)).size() > 0
					&& hand.get(i).value() >= runPile(hand.get(i)).get(0)
							.value())
				return true;
		}
		return false;
	}
	
	
	/**
	 * Uses monte carlo to determine the best possible play in the game.
	 * Written by Max Wang (u5584091), with Heuristics fron Fangqin
	 * Chen (u5552738).
	 * @param game -- the current game state for AI to make a decision
	 * @param iterations -- How many monte-carlo iterations to do for a single game.
	 * @return -- the move to be done by the AI.
	 */
	public MCMove doPlay(Game game, int iterations) {
		ArrayList<MCMove> moves = new ArrayList<MCMove>();
		ArrayList<Game> MCGames = new ArrayList<Game>();
		int[] plays = { 0, 1 };
		// We start by enumerating every possible play.
		for (int i = 0; i < this.hand.size(); i++) {
			if ((game.table.dpH.size() != 0 && runHeart.size() != 0 && game.table.dpH
					.get(0).value() == runHeart.get(0).value() + 1)
					|| (game.table.dpS.size() != 0 && runSpade.size() != 0 && game.table.dpS
							.get(0).value() == runSpade.get(0).value() + 1)
					|| (game.table.dpC.size() != 0 && runClub.size() != 0 && game.table.dpC
							.get(0).value() == runClub.get(0).value() + 1)
					|| (game.table.dpD.size() != 0 && runDiamond.size() != 0 && game.table.dpD
							.get(0).value() == runDiamond.get(0).value() + 1)) {
				moves.add(new MCMove(csDp(game), i, 1, matchingSuit(csDp(game),
						game)));
				// if the first card in discard pile of a suit is greater than
				// that of the run of the same suit
				// then AI will draw from that deck and play it to the run
				// -u5552738
			} else
				for (int j = 0; j < 5; j++) {
					if (runPile(hand.get(i)).size() > 0
							&& hand.get(i).value() == runPile(hand.get(i)).get(
									0).value() + 1) {
						moves.add(new MCMove(csPile(j, game), i, 1,
								matchingSuit(csPile(j, game), game)));
						// always play the card that is 1 above the current run
						// -u5552738
					} else
						for (int p : plays) {
							if (!(hand.get(i).value() > 6
									&& totalRun(runPile(hand.get(i))) == 0 && p == 1)) {
								// do not start a run with a card of value
								// greater than 7 -u5552738
								if (!(runPile(hand.get(i)).size() > 0
										&& hand.get(i).value() > runPile(
												hand.get(i)).get(0).value() && p == 0)) {
									// do not discard a card if it is greater
									// than the first card in the run of its
									// suit -u5552738
									if (!(hand.get(i).value() == 10
											&& (suitInPile(hand, hand.get(i)) || disPile(
													hand.get(i), game.table)
													.size() > 0)
											&& totalRun(runPile(hand.get(i))) < 20 - 10 * doubler(runPile(hand
													.get(i))) && p == 1)) {
										// do not put a 10 in a run when the
										// total sum is less than 20 and there
										// are other accessible cards of the
										// same suit -u5552738
										if (!(hand.get(i) != smallestToRun(hand
												.get(i)) && p == 1)) {
											// do not play a card if it is not
											// the smallest possible to play on
											// a runPile
											if (!(runPile(hand.get(i)).size() == 0
													&& playableOther(hand
															.get(i)) && p == 1)) {
												// do not start a run if
												// existing runs have playable
												// cards -u5552738
												moves.add(new MCMove(
														csPile(j, game),
														i,
														p,
														matchingSuit(
																csPile(j, game),
																game)));
											}
										}
									}
								}
							}
						}
				}
		}
		// We now create new games, where the play is done, if valid.
		for (MCMove move : moves) {
			Game newGame = game.clone();
			newGame.p1 = new AI(newGame.p1, 0);
			newGame.p2 = new AI(newGame.p2, 0);

			if (newGame.playerOne) {
				if (move.dpSuit == 'M') {
					if (newGame.p1.checkDraw(newGame.table.deck, newGame.table)) {
						move.drawnCard = newGame.table.deck.get(0);
						newGame.p1.doDraw(newGame.table.deck, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'C') {
					if (newGame.p1.checkDraw(newGame.table.dpC, newGame.table)) {
						move.drawnCard = newGame.table.dpC.get(0);
						newGame.p1.doDraw(newGame.table.dpC, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'D') {
					if (newGame.p1.checkDraw(newGame.table.dpD, newGame.table)) {
						move.drawnCard = newGame.table.dpD.get(0);
						newGame.p1.doDraw(newGame.table.dpD, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'H') {
					if (newGame.p1.checkDraw(newGame.table.dpH, newGame.table)) {
						move.drawnCard = newGame.table.dpH.get(0);
						newGame.p1.doDraw(newGame.table.dpH, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'S') {
					if (newGame.p1.checkDraw(newGame.table.dpS, newGame.table)) {
						move.drawnCard = newGame.table.dpS.get(0);
						newGame.p1.doDraw(newGame.table.dpS, newGame.table);
					} else
						move.wins = -777777;
				}

				if (!((AI) newGame.p1).playToSuit(
						newGame.p1.hand.get(move.cardPlayed), newGame.table,
						move.play))
					move.wins = -777777;
			} else {
				if (move.dpSuit == 'M') {
					if (newGame.p2.checkDraw(newGame.table.deck, newGame.table)) {
						move.drawnCard = newGame.table.deck.get(0);
						newGame.p2.doDraw(newGame.table.deck, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'C') {
					if (newGame.p2.checkDraw(newGame.table.dpC, newGame.table)) {
						move.drawnCard = newGame.table.dpC.get(0);
						newGame.p2.doDraw(newGame.table.dpC, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'D') {
					if (newGame.p2.checkDraw(newGame.table.dpD, newGame.table)) {
						move.drawnCard = newGame.table.dpD.get(0);
						newGame.p2.doDraw(newGame.table.dpD, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'H') {
					if (newGame.p2.checkDraw(newGame.table.dpH, newGame.table)) {
						move.drawnCard = newGame.table.dpH.get(0);
						newGame.p2.doDraw(newGame.table.dpH, newGame.table);
					} else
						move.wins = -777777;
				} else if (move.dpSuit == 'S') {
					if (newGame.p2.checkDraw(newGame.table.dpS, newGame.table)) {
						move.drawnCard = newGame.table.dpS.get(0);
						newGame.p2.doDraw(newGame.table.dpS, newGame.table);
					} else
						move.wins = -777777;
				}
				if (!((AI) newGame.p2).playToSuit(
						newGame.p2.hand.get(move.cardPlayed), newGame.table,
						move.play))
					move.wins = -777777;
			}
			MCGames.add(newGame);
		}
		// We now simulate random games, and see if they win or lose. If they
		// win,
		// We add the score to the MCMove class object.
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).wins != -777777)// We want to exclude invalid moves
												// to
												// reduce complexity!
			{
				for (int j = 0; j < iterations; j++) {
					Game mcGame = MCGames.get(i).clone();

					mcGame.p1 = new AI(mcGame.p1, 0);
					mcGame.p2 = new AI(mcGame.p2, 0);
					while (mcGame.p1.endCount < 3 && mcGame.p2.endCount < 3) {
						if (mcGame.playerOne) {
							mcGame.p1.canDraw = true;
							mcGame.p1.canPlay = true;
							((AI) mcGame.p1).drawRandom(mcGame.table);
							((AI) mcGame.p1).playRandom(mcGame.table);
						} else {
							mcGame.p2.canDraw = true;
							mcGame.p2.canPlay = true;
							((AI) mcGame.p2).drawRandom(mcGame.table);
							((AI) mcGame.p2).playRandom(mcGame.table);
						}
						mcGame.playerOne = !mcGame.playerOne;
					}

					/*
					 * As of this revision, maximising own points is more
					 * important than minimising opponent score. This is because
					 * the opponent plays randomly, which makes it difficult to
					 * ensure that they dont end up with four piles filled with
					 * rubbish cards.
					 */
					final int weightMod = 2;
					if (MCGames.get(i).playerOne)
						moves.get(i).wins += weightMod * mcGame.getScore().p1()
								- mcGame.getScore().p2();
					else
						moves.get(i).wins += weightMod * mcGame.getScore().p2()
								- mcGame.getScore().p1();
					;

				}
			}
		}
		// We now obtain the most winning play, and do it.
		MCMove bestMove = moves.get(0);
		for (MCMove move : moves) {
			if (move.wins > bestMove.wins)
				bestMove = move;
		}

		if (game.playerOne) {
			if (bestMove.wins != -777777) {
				doDraw(bestMove.drawPile, game.table);
				playToSuit(game.p1.hand.get(bestMove.cardPlayed), game.table,
						bestMove.play);
			} else {
				// Should not happen. Only occurs if game should be ended.
			}
		} else {
			if (bestMove.wins != -777777) {
				doDraw(bestMove.drawPile, game.table);
				playToSuit(game.p2.hand.get(bestMove.cardPlayed), game.table,
						bestMove.play);
			} else {
				// Should not happen. Only occurs if game should be ended.
			}
		}
		PlaySound.doPlaySound(4); // play the sound track of drawing and playing
									// card
		PlaySound.randomLaugh(); // play the random laughing sound 20% chance
		return bestMove;

	}

	/**
	 * Deprecated heuristics focused AI. This has been replaced with the
	 * Monte-Carlo implementation of AI. The logic behind the heuristics is to
	 * start a run with lowest card of the suite with highest total value, and
	 * keep playing to the run with the lowest card possible. Although this gets
	 * more score than our monte-carlo implementation, it has no intuition, and
	 * will not naturally force end a game.
	 */

	/*
	 * determines best play. very poor.
	 */
	public void calculatePlay(Table table, Player opponent) {
		/*
		 * Array describing the values of the cards of the suits within the AI's
		 * hand. [0] == Clubs [1] == Diamonds [2] == Hearts [3] == Spades
		 */
		int handVal[] = new int[4];

		for (int i = 0; i < 4; i++)
			handVal[i] = totalValue(hand, suits[i]);

		boolean hasWorth = false;
		/*
		 * Does the AI have a sequence in hand such that if played, it will get
		 * at least a single point? i.e, value>20
		 */
		for (int v : handVal)
			if (v > 0)
				hasWorth = true;

		if (hasWorth) {
			int max = 0;
			int pile = 0;
			for (int i = 0; i < 4; i++) {
				if (handVal[i] > max) {
					max = handVal[i];
					pile = i;
				}
			}
			if (pile == 0) {
				if (checkPlay(getLowest(hand, 'C'), runClub, table)) {
					doPlay(getLowest(hand, 'C'), runClub);
				} else {
					doPlay(getLowest(hand, 'C'), table.dpC);
				}
			} else if (pile == 1) {
				if (checkPlay(getLowest(hand, 'D'), runDiamond, table)) {
					doPlay(getLowest(hand, 'D'), runDiamond);
				} else {
					doPlay(getLowest(hand, 'D'), table.dpD);
				}
			} else if (pile == 2) {
				if (checkPlay(getLowest(hand, 'H'), runHeart, table)) {
					doPlay(getLowest(hand, 'H'), runHeart);
				} else {
					doPlay(getLowest(hand, 'H'), table.dpH);
				}
			} else if (pile == 3) {
				if (checkPlay(getLowest(hand, 'S'), runSpade, table)) {
					doPlay(getLowest(hand, 'S'), runSpade);
				} else {
					doPlay(getLowest(hand, 'S'), table.dpS);
				}
			}
		} else {
			int min = 1000;
			int pile = 0;
			for (int i = 0; i < 4; i++) {
				if (handVal[i] < min && handVal[i] != 0) {
					min = handVal[i];
					pile = i;
				}
			}
			if (pile == 0)
				doPlay(getLowest(hand, 'C'), table.dpC);
			else if (pile == 1)
				doPlay(getLowest(hand, 'D'), table.dpD);
			else if (pile == 2)
				doPlay(getLowest(hand, 'H'), table.dpH);
			else if (pile == 3)
				doPlay(getLowest(hand, 'S'), table.dpS);
		}

	}

	/*
	 * Makes a decentish draw.
	 */
	public void calculateDraw(Table table, Player opponent) {
		/*
		 * Array describing the values of the cards of the suits within the AI's
		 * hand. [0] == Clubs [1] == Diamonds [2] == Hearts [3] == Spades
		 */
		int handVal[] = new int[4];

		for (int i = 0; i < 4; i++)
			handVal[i] = totalValue(hand, suits[i]);
		int max = 0;
		int pile = 0;
		for (int i = 0; i < 4; i++) {
			if (handVal[i] > max) {
				max = handVal[i];
				pile = i;
			}
		}
		if (pile == 0 && table.dpC.size() > 0)
			doDraw(table.dpC, table);
		else if (pile == 1 && table.dpD.size() > 0)
			doDraw(table.dpD, table);
		else if (pile == 2 && table.dpH.size() > 0)
			doDraw(table.dpH, table);
		else if (pile == 3 && table.dpS.size() > 0)
			doDraw(table.dpS, table);
		else {
			if (table.deck.size() > 0)
				doDraw(table.deck, table);
			else if (table.dpC.size() > 0)
				doDraw(table.dpC, table);
			else if (table.dpS.size() > 0)
				doDraw(table.dpS, table);
			else if (table.dpH.size() > 0)
				doDraw(table.dpH, table);
			else if (table.dpD.size() > 0)
				doDraw(table.dpD, table);
		}
	}

	/*
	 * This function is the heuristic function - it determines how good of a
	 * situation the AI is currently in. Positive -> Leaning towards AI Negative
	 * -> Leaning towards Opponent
	 */
	public int calculateLeaning(Table table, Player opponent) {
		Score score = new Game(table, this, opponent).getScore();
		return score.p1 - score.p2;
	}

	/*
	 * This function calculates the total value of cards of a certain suit
	 * within a chosen 'pile' of cards.
	 */
	private int totalValue(ArrayList<Card> cards, char suit) {
		int val = 0;
		for (Card c : cards) {
			if (c.suit() == suit)
				val += c.value();
		}
		return val;
	}

	/*
	 * Returns lowest value card matching the specified suit.
	 */
	private Card getLowest(ArrayList<Card> cards, char suit) {
		int val = 100;
		Card rtn = null;
		for (Card c : cards) {
			if (c.value() < val && c.suit == suit) {
				val = c.value();
				rtn = c;
			}
		}
		return rtn;
	}
}
