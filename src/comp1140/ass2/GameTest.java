package comp1140.ass2;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

/*
 * Entire class written by Max Wang (u5584091)
 */
public class GameTest extends JFXTest {

	/*
	 * Checks if the deal() method works
	 */
	@Test
	public void testDeal() throws InterruptedException {
		runJFXTest(new Runnable() {
			@Override
			public void run() {
				Random rng = new Random();
				for (int i = 0; i < 20; i++) { // Do this 20 times for
												// reliability.
					int deckDeal = rng.nextInt(10);
					Game game = Game.deal(3, deckDeal, new Random(1234));

					assertTrue(game.table.deck.size() == deckDeal);
					assertTrue(game.table.dpC.size() + game.table.dpS.size()
							+ game.table.dpD.size() + game.table.dpH.size() == (new Deck()).cards
							.size() - deckDeal - 14);
					assertTrue(game.p1.hand.size() == 7);
					assertTrue(game.p2.hand.size() == 7);
					// We don't need to check player's runs - taken care of
					// by checking the deck size!
				}
				endOfJFXTest();
			}

		});
	}

	/**
	 * Checks if scorekeeping function is as expected.
	 */
	@Test
	public void testRun() throws InterruptedException {
		runJFXTest(new Runnable() {
			@Override
			public void run() {
				Game g1 = Game.deal(3,5, new Random(1234));
				ArrayList<Card> dpH = new ArrayList<Card>();
				ArrayList<Card> dpS = new ArrayList<Card>();
				ArrayList<Card> dpC = new ArrayList<Card>();
				ArrayList<Card> dpD = new ArrayList<Card>();
				ArrayList<Card> deck = new ArrayList<Card>();
				Table table = new Table(dpH,dpS,dpC,dpD,deck);
				System.out.println(g1);
				assertTrue(g1.getScore().p1() == 0);
				assertTrue(g1.getScore().p2() == 0);
				g1.p1.doDraw(g1.table.dpD, table);
				g1.p1.doPlay(g1.p1.hand.get(0), g1.p1.runHeart);
				assertTrue(g1.getScore().p1() == -40);
				g1.p1.doDraw(g1.table.dpS, table);
				g1.p1.doPlay(g1.p1.hand.get(0), g1.p1.runHeart);
				assertTrue(g1.getScore().p1() == -20);
				g1.p1.doDraw(g1.table.dpD, table);
				g1.p1.doPlay(g1.p1.hand.get(0), g1.p1.runClub);
				assertTrue(g1.getScore().p1() == -60);
				endOfJFXTest();
			}
		});
	}

	/**
	 * Checks if Old AI is working -- may not necessarily be a good AI as it simply
	 * has to modify canmove and canplay, and that its hand is the right size
	 */
	@Test
	public void testHeuristicAI() throws InterruptedException {
		runJFXTest(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) { // Run 100 tests for reliability
					Game g2 = Game.deal(3,5, new Random());
					g2.p1.canPlay = true;
					g2.p1.canDraw = true;
					g2.p1 = new AI(g2.p1, 25);
					/*
					 * We now test that the AI can make 10 moves. It is highly
					 * unlikely that the game will end in 10 moves, but there is
					 * the slight possibility.
					 */
					for (int j = 0; j < 10; j++) {
						((AI) g2.p1).calculateDraw(g2.table, g2.p1);
						assertTrue(g2.p1.canDraw == false);
						assertTrue(g2.p1.hand.size() == 8);
						((AI) g2.p1).calculatePlay(g2.table, g2.p1);
						assertTrue(g2.p1.canPlay == false);
						assertTrue(g2.p1.hand.size() == 7);
					}
				}
				endOfJFXTest();
			}
		});
	}
	
	@Test
	public void testMonteCarloAI() throws InterruptedException {
		runJFXTest(new Runnable(){
			@Override
			public void run(){
				for (int i = 0; i < 10; i++) { // Run 10 tests for reliability
					Game g2 = Game.deal(3,5, new Random());
					g2.p1.canPlay = true;
					g2.p1.canDraw = true;
					g2.p1 = new AI(g2.p1, 10);
					/*
					 * We now test that the AI makes 10 moves properly, that is,
					 * it doesn't end with extra cards in its hand.
					 */
					for (int j = 0; j < 10; j++) {
						((AI) g2.p1).doPlay(g2, 10);
						assertTrue(g2.p1.canDraw == false);
						assertTrue(g2.p1.hand.size() == 7);
					}
				}
				endOfJFXTest();
			endOfJFXTest();
			}
		});
	}
	
	@Test
	public void testRandomDraw() throws InterruptedException {
		runJFXTest(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) { // Run 100 tests for reliability
					Game g2 = Game.deal(3,5, new Random());
					g2.p1.canPlay = true;
					g2.p1.canDraw = true;
					g2.p1 = new AI(g2.p1, 25);
					for (int j = 0; j < 10; j++) {
						((AI) g2.p1).drawRandom(g2.table);
						assertTrue(g2.p1.hand.size()==8+j);
					}
				}
				endOfJFXTest();
			}
		});
	}
	

}
