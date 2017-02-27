package comp1140.ass2;

import java.util.ArrayList;
import java.util.Random;

import comp1140.ass2.CardView.EaseOutQuad;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * High Stakes Renier. COMP1140 Assignment 2 Max Wang (u5584091) Felicity Lee
 * (u5558958) Fangqin Chen (u5552738)
 * 
 * Main roles: Game Design: Felicity Lee - Created background, designed board,
 * designed start screen, fonts. Max Wang - Rearranged start screen, created end
 * game screen, designed cards
 * 
 * Animator: Max Wang - implemented everything besides deal.
 * 			 Felicity Lee - implemented the deal animation.
 * 
 * Sound Artist: Fangqin Chen - obtained/recorded all sound effects used in the
 * game, and used JFX to play these sounds appropriately.
 * 
 * Artificial Intelligence: Max Wang - Implemented the base Monte-Carlo method
 * used in the current AI, as well As the previous heuristics based AI. Fangqin
 * Chen - Improved Monte-Carlo based AI with heuristic rules.
 * 
 * Player Interaction: Felicity Lee - Managed the original Human text-based play
 * class Max Wang - Implemented drag-and-drop methods for JFX humans.
 * 
 * Game Tester: Fangqin Chen - Discovered most of the bugs of the game
 */
public class Main extends Application {

	/* creating constants for the location of media */
	private final Image bg = new Image("comp1140/ass2/res/bg.png");
	private final String FONT_SICK = Main.class.getResource(
			"/comp1140/ass2/res/fonts/KalenderblattGrotesk.ttf").toString();
	private final String FONT_SCORE = Main.class.getResource(
			"/comp1140/ass2/res/ftg.ttf").toString();
	private final String FONT_KILL = Main.class.getResource(
			"/comp1140/ass2/res/fonts/murderer.otf").toString();
	private final Image arrowleft = new Image("comp1140/ass2/res/leftarrow.png");
	private final Image arrowright = new Image("comp1140/ass2/res/arrow.png");
	private final Image playbutton = new Image(
			"comp1140/ass2/res/playbutton.png");

	/* layout constants for the cards on the background */
	double r1x = 164;
	double r2x = 592;
	double r1Cy = 449;
	double r1Dy = 449;
	double r1Hy = 449;
	double r1Sy = 449;
	double r2Cy = 187;
	double r2Dy = 187;
	double r2Hy = 187;
	double r2Sy = 187;
	double dx = 32;
	double dy = 104;


	/* text for the scores of both players on the screen */
	Text p1score = new Text(12, 758, "0");
	Text p2score = new Text(12, 27, "0");
	Text p1lives = new Text(0, 724, " ♥♥♥");
	Text p2lives = new Text(0, 54, " ♥♥♥");

	Rectangle cover;
	CardView deckHider;

	/*
	 * Various vaariables to keep track of curent game state.
	 */
	double smashamt = 0;
	boolean isSmash = false;
	PerspectiveTransform boardPersp = new PerspectiveTransform();
	BoxBlur boardBlur = new BoxBlur();
	ColorAdjust boardDarken = new ColorAdjust();
	Timeline smashHead;
	double p1Damage = 0;
	double p2Damage = 0;
	double bAmt = 0;
	double gameintensity = 0;
	ArrayList<CardView> p1hand;
	ArrayList<CardView> p2hand;
	ArrayList<CardView> dpiles;

	/*
	 * default numbers for the intensity, difficulty of the game, and the types
	 * of both players
	 */
	String p1click = "Human";
	String p2click = "Human";
	int deckcards = 0;
	int royalscards = 0;
	double intensVal;
	double p1AiDiff = 0;
	double p2AiDiff = 0;

	Group root;
	Scene scene;
	Timeline blurEffect;
	Timeline gameTime;
	Timeline ws;
	Timeline scoreShow;
	
	int p1cardNo = 0;
	int p2cardNo = 0;
	int displacedH ;
	int displacedS ;
	int displacedC ;
	int displacedD ;
	double scoreShowing = 0;
	

	public static void main(String[] args) {
		launch(args);

	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set the scene
		primaryStage.setTitle("Renier");
		root = new Group();
		scene = new Scene(root, 1024, 768);
		primaryStage.setScene(scene);

		// Set background

		ImageView background = new ImageView(bg);
		root.getChildren().add(background);
		scene.setFill(Color.color(0.18039215686, 0.43137254902, 0.16470588235));

		makeOpening();
		primaryStage.show();
	}

	/**
	 * Makes the starting screen where user can set difficulty and player
	 * options. Written mainly by Felicity, with some minor modifications by
	 * Max.
	 */
	public void makeOpening() {

		// making all colours and fonts

		Font f = Font.loadFont(FONT_SICK, 40);
		Color c = Color.DARKGREY;

		// making title

		Image title = new Image("comp1140/ass2/res/Reniertitle.png");
		ImageView renierTitle = new ImageView(title);
		renierTitle.setLayoutX(-50);
		renierTitle.setLayoutY(100);
		root.getChildren().add(renierTitle);
		
		//making royal buttons

		Text royals = new Text(505, 470, "Royals ");
		royals.setFont(f);
		royals.setFill(c);
		root.getChildren().add(royals);
		Text royalsnum = new Text(710, 470, "0");
		royalsnum.setFont(f);
		royalsnum.setFill(c);
		royalsnum.setWrappingWidth(30);
		royalsnum.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().add(royalsnum);
		ImageView royalsLeft = new ImageView(arrowleft);
		royalsLeft.setLayoutX(666);
		royalsLeft.setLayoutY(437);
		root.getChildren().add(royalsLeft);
		ImageView royalsRight = new ImageView(arrowright);
		royalsRight.setLayoutX(755);
		royalsRight.setLayoutY(437);
		root.getChildren().add(royalsRight);
		
		//making deck cards buttons
		Text deck = new Text(410, 525, "Deck cards ");
		deck.setFont(f);
		deck.setFill(c);
		root.getChildren().add(deck);
		Text decknum = new Text(697, 525, "0");
		decknum.setFont(f);
		decknum.setFill(c);
		decknum.setTextAlignment(TextAlignment.CENTER);
		decknum.setWrappingWidth(56);
		root.getChildren().add(decknum);
		ImageView deckLeft = new ImageView(arrowleft);
		deckLeft.setLayoutX(666);
		deckLeft.setLayoutY(493);
		root.getChildren().add(deckLeft);
		ImageView deckRight = new ImageView(arrowright);
		deckRight.setLayoutX(755);
		deckRight.setLayoutY(493);
		root.getChildren().add(deckRight);

		// making play button
		ImageView play = new ImageView(playbutton);
		play.setLayoutX(800);
		play.setLayoutY(320);
		root.getChildren().add(play);

		Rectangle playclick = new Rectangle(815, 330, 165, 80);
		playclick.setOpacity(0);
		root.getChildren().add(playclick);

		// making p1 text and arrows
		Text p1 = new Text(460, 688, "Human");
		p1.setFont(f);
		p1.setFill(c);
		root.getChildren().add(p1);

		ImageView p1Left = new ImageView(arrowleft);
		p1Left.setLayoutX(420);
		p1Left.setLayoutY(657);
		root.getChildren().add(p1Left);

		ImageView p1Right = new ImageView(arrowright);
		p1Right.setLayoutX(620);
		p1Right.setLayoutY(657);
		root.getChildren().add(p1Right);

		// making p2 text and arrows
		Text p2 = new Text(460, 100, "Human");
		p2.setFont(f);
		p2.setFill(c);
		root.getChildren().add(p2);

		ImageView p2Left = new ImageView(arrowleft);
		p2Left.setLayoutX(420);
		p2Left.setLayoutY(65);
		root.getChildren().add(p2Left);

		ImageView p2Right = new ImageView(arrowright);
		p2Right.setLayoutX(620);
		p2Right.setLayoutY(65);
		root.getChildren().add(p2Right);

		// making difficulty sliders

		Slider p1Diff = new Slider(0, 4, 2);
		Slider p2Diff = new Slider(0, 4, 2);
		p1Diff.setBlockIncrement(0.001);
		p1Diff.setLayoutX(460);
		p1Diff.setLayoutY(695);
		p2Diff.setBlockIncrement(0.001);
		p2Diff.setLayoutX(460);
		p2Diff.setLayoutY(110);

		// making intensity sliders
		Slider intensity = new Slider(0, 10, 1.5);
		intensity.setBlockIncrement(0.1);
		intensity.setLayoutX(670);
		intensity.setLayoutY(310);
		intensity.setRotate(270.0);
		root.getChildren().add(intensity);
		Text intenseLabel = new Text(704, 328, "Intensity");
		intenseLabel.setRotate(270);
		Font fsmall = Font.loadFont(FONT_SICK, 24);
		intenseLabel.setFont(fsmall);
		intenseLabel.setFill(c);
		root.getChildren().add(intenseLabel);

		// making arrows clickable
		p1Left.setOnMouseClicked(event -> {
			setPlayer1(p1, p1Diff);
		});

		p1Right.setOnMouseClicked(event -> {
			setPlayer1(p1, p1Diff);
		});
		p2Left.setOnMouseClicked(event -> {
			setPlayer2(p2, p2Diff);
		});

		p2Right.setOnMouseClicked(event -> {
			setPlayer2(p2, p2Diff);
		});

		royalsLeft.setOnMouseClicked(event -> {
			if ((Integer.parseInt(royalsnum.getText())) == 0) {
				royalsnum.setText("3");
				royalscards = 3;
			} else {
				royalsnum.setText(""
						+ (Integer.parseInt(royalsnum.getText()) - 1));
				royalscards = royalscards - 1;
			}
		});
		royalsRight.setOnMouseClicked(event -> {
			if ((Integer.parseInt(royalsnum.getText())) == 3) {
				royalsnum.setText("0");
				royalscards = 0;
			} else {
				royalsnum.setText((Integer.parseInt(royalsnum.getText()) + 1)
						+ "");

				royalscards = royalscards + 1;
			}
		});

		deckLeft.setOnMouseClicked(event -> {
			if ((Integer.parseInt(decknum.getText())) == 0) {
				decknum.setText("26");
				deckcards = 26;
			}

			else {
				decknum.setText("" + (Integer.parseInt(decknum.getText()) - 1));
				deckcards = deckcards - 1;
			}
		});
		deckRight
				.setOnMouseClicked(event -> {
					if ((Integer.parseInt(decknum.getText())) == 26) {
						decknum.setText("0");
						deckcards = 0;
					} else {
						decknum.setText((Integer.parseInt(decknum.getText()) + 1)
								+ "");
						deckcards = deckcards + 1;
					}
				});
		
		//making clicking play allow for the start of the game along with some other animations
		playclick.setOnMouseClicked(event -> {
			playclick.setMouseTransparent(true);
			bAmt = 0;
			root.setEffect(boardBlur);
			Timeline fadeIn = new Timeline(new KeyFrame(Duration.millis(40),
					ae -> {
						bAmt += 2;
						boardBlur.setHeight(bAmt);
						boardBlur.setWidth(bAmt);
						royals.setOpacity(1 - bAmt * 0.02);
						decknum.setOpacity(1 - bAmt * 0.02);
						p1.setOpacity(1 - bAmt * 0.02);
						p2.setOpacity(1 - bAmt * 0.02);
						deckRight.setOpacity(1 - bAmt * 0.02);
						deckLeft.setOpacity(1 - bAmt * 0.02);
						p1Left.setOpacity(1 - bAmt * 0.02);
						p2Left.setOpacity(1 - bAmt * 0.02);
						p1Right.setOpacity(1 - bAmt * 0.02);
						p2Right.setOpacity(1 - bAmt * 0.02);
						royalsLeft.setOpacity(1 - bAmt * 0.02);
						royalsRight.setOpacity(1 - bAmt * 0.02);
						play.setOpacity(1 - bAmt * 0.02);
						deck.setOpacity(1 - bAmt * 0.02);
						renierTitle.setOpacity(1 - bAmt * 0.02);
						intensity.setOpacity(1 - bAmt * 0.02);
					}));
			fadeIn.setCycleCount(25);
			fadeIn.play();
			new Timeline(new KeyFrame(Duration.millis(1200), ae -> {
				p1AiDiff = p1Diff.getValue();
				p2AiDiff = p2Diff.getValue();
				intensVal = intensity.getValue();

				root.getChildren().remove(royals);
				root.getChildren().remove(decknum);
				root.getChildren().remove(royalsnum);
				root.getChildren().remove(p1);
				root.getChildren().remove(p2);
				root.getChildren().remove(deckRight);
				root.getChildren().remove(deckLeft);
				root.getChildren().remove(p1Left);
				root.getChildren().remove(p2Left);
				root.getChildren().remove(p1Right);
				root.getChildren().remove(p2Right);
				root.getChildren().remove(royalsLeft);
				root.getChildren().remove(royalsRight);
				root.getChildren().remove(play);
				root.getChildren().remove(deck);
				root.getChildren().remove(renierTitle);
				root.getChildren().remove(intensity);
				root.getChildren().remove(intenseLabel);
				if (p1click == "AI")
					root.getChildren().remove(p1Diff);
				if (p2click == "AI")
					root.getChildren().remove(p2Diff);
				startGame();
			})).play();
		});

	}
	public void setPlayer1(Text pl, Slider p1Slide) {
		if (pl.getText() == "Human") {
			pl.setText("AI");
			pl.setLayoutX(50);
			p1click = "AI";
			root.getChildren().add(p1Slide);
		} else {
			pl.setText("Human");
			pl.setLayoutX(0);
			p1click = "Human";
			root.getChildren().remove(p1Slide);
		}
	}

	public void setPlayer2(Text pl, Slider p2Slide) {
		if (pl.getText() == "Human") {
			pl.setText("AI");
			pl.setLayoutX(50);
			p2click = "AI";
			root.getChildren().add(p2Slide);
		} else {
			pl.setText("Human");
			pl.setLayoutX(0);
			p2click = "Human";
			root.getChildren().remove(p2Slide);
		}
	}
	

	/*
	 * Function mainly written by Max, with some editing form Felicity.
	 */
	private void startGame() {

		Font scFont = Font.loadFont(FONT_SCORE, 32);
		// Set score boards and lives.
		p1score.setFont(scFont);
		p2score.setFont(scFont);
		p1score.setTextAlignment(TextAlignment.CENTER);
		p2score.setTextAlignment(TextAlignment.CENTER);
		p1lives.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
		p2lives.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
		p1lives.setFill(Color.CRIMSON);
		p2lives.setFill(Color.CRIMSON);

		Game game = Game.deal(royalscards, deckcards, new Random());
		/* Create a game with human and AI */

		if (p1click == "Human") {
			game.p1 = new Human(game.p1);
		} else
			game.p1 = new AI(game.p1, (int) Math.pow(10, p1AiDiff));

		if (p2click == "Human") {
			game.p2 = new Human(game.p2);
		} else
			game.p2 = new AI(game.p2, (int) Math.pow(10, p2AiDiff));

		game.p1.canPlay = true;
		game.p1.canDraw = true;

		p1hand = new ArrayList<CardView>();
		p2hand = new ArrayList<CardView>();
		dpiles = new ArrayList<CardView>();

		// * Show own hand.
		root.getChildren().add(p1score);
		root.getChildren().add(p2score);
		root.getChildren().add(p1lives);
		root.getChildren().add(p2lives);
		
		//variables

		double startX = dx;
		double startY = dy+222;
		int timing = 100;
		final double SIZE = 0.347380;
		
		Timeline noBlur = new Timeline(new KeyFrame(Duration.millis(50),
				ea -> {
					bAmt = bAmt - 2;
					boardBlur.setHeight(bAmt);
					boardBlur.setWidth(bAmt);
				} ));
		
		noBlur.setCycleCount(25);
		noBlur.play();

		Card cBack = new Card('B', 'A');
		deckHider = new CardView(cBack, startX, startY, SIZE, false);		
		deckHider.rotate.setAngle(90);
		deckHider.setMouseTransparent(true);
		root.getChildren().add(deckHider);
	
		Timeline p1handanime = new Timeline(new KeyFrame(Duration.millis(timing),
				ea -> {CardView card = new CardView(game.p1.hand.get(p1cardNo), startX,
						startY, SIZE, false);
				card.rotate.setAngle(90);
				p1hand.add(0, card);
				root.getChildren().add(card);
				card.pPosX=911 - p1cardNo * 107;
				card.pPosY=609;
				card.initializeReturn();
				card.returnCard.play();
				card.doSlowRotate.play();
				p1cardNo ++;
				PlaySound.doPlaySound(2);
				deckHider.toFront();
				}));
		
		p1handanime.setCycleCount(7);
		p1handanime.play();
		
		
		Timeline p2handanime = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					CardView card = new CardView(game.p2.hand.get(p2cardNo), startX,
							startY, SIZE, false);
					card.rotate.setAngle(90);
					p2hand.add(0, card);

					root.getChildren().add(card);
					deckHider.toFront();
					card.pPosX=911 - p2cardNo * 107;
					card.pPosY=24;
					card.initializeReturn();
					card.returnCard.play();
					card.doSlowRotate.play();
					p2cardNo ++;
					PlaySound.doPlaySound(2);
					deckHider.toFront();
				}));
		p2handanime.setCycleCount(7);
		p2handanime.setDelay(Duration.millis(7*timing));
		p2handanime.play();
		
		

		// * Initialise the discard piles.
		displacedC = game.table.dpC.size()-1;
		displacedD = game.table.dpD.size()-1;
		displacedH = game.table.dpH.size()-1;
		displacedS = game.table.dpS.size()-1;
		Timeline dclubanime = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					CardView cd = new CardView(game.table.dpC.get(displacedC), startX, startY, SIZE,
							false);
					cd.rotate.setAngle(90);
					dpiles.add(cd);
					root.getChildren().add(cd);
					cd.pPosX=dx;
					cd.pPosY=dy;
					cd.initializeReturn();
					cd.returnCard.play();
					displacedC --;
					PlaySound.doPlaySound(2);
					deckHider.toFront();
				}));
		dclubanime.setCycleCount(game.table.dpC.size());
		dclubanime.setDelay(Duration.millis(14*timing));
		dclubanime.play();
		
		Timeline ddiaanime = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					CardView cd = new CardView(game.table.dpD.get(displacedD), startX, startY, SIZE,
							false);
					cd.rotate.setAngle(90);
					dpiles.add(cd);
					root.getChildren().add(cd);
					deckHider.toFront();
					cd.pPosX=dx;
					cd.pPosY=dy+111;
					cd.initializeReturn();
					cd.returnCard.play();
					displacedD --;
					PlaySound.doPlaySound(2);
					deckHider.toFront();
					
				}));
		ddiaanime.setCycleCount(game.table.dpD.size());
		ddiaanime.setDelay(Duration.millis(timing*(14+game.table.dpC.size())));
		ddiaanime.play();
		
		
		Timeline dheartsanime = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					CardView cd = new CardView(game.table.dpH.get(displacedH), startX, startY, SIZE,
							false);
					cd.rotate.setAngle(90);
					dpiles.add(cd);
					root.getChildren().add(cd);
					deckHider.toFront();
					cd.pPosX=dx;
					cd.pPosY=dy+333;
					cd.initializeReturn();
					cd.returnCard.play();
					displacedH --;
					PlaySound.doPlaySound(2);
					deckHider.toFront();
					
				}));
		dheartsanime.setCycleCount(game.table.dpH.size());
		dheartsanime.setDelay(Duration.millis(timing*(14+game.table.dpC.size()+game.table.dpD.size())));
		dheartsanime.play();
		
		Timeline dspadeanime = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					CardView cd = new CardView(game.table.dpS.get(displacedS), startX, startY, SIZE,
							false);
					cd.rotate.setAngle(90);
					dpiles.add(cd);
					root.getChildren().add(cd);
					deckHider.toFront();
					cd.pPosX=dx;
					cd.pPosY=dy+444;
					cd.initializeReturn();
					cd.returnCard.play();
					displacedS --;
					PlaySound.doPlaySound(2);
					deckHider.toFront();
					
				}));
		dspadeanime.setCycleCount(game.table.dpS.size());
		dspadeanime.setDelay(Duration.millis(timing*(14+game.table.dpC.size()+game.table.dpD.size()+game.table.dpH.size())));
		dspadeanime.play();
		
		if (game.table.deck.size() != 0){
			
							for (int i = game.table.deck.size() - 1; i >= 0; i--) {
								CardView cd = new CardView(game.table.deck.get(i), dx, dy + 222,
										SIZE, false);
								cd.rotate.setAngle(90);
								dpiles.add(cd);
								root.getChildren().add(cd);
						}

		}else {
			deckHider.setOpacity(0);
			
		}
		
		Timeline initiateYou = new Timeline (new KeyFrame (Duration.millis(timing),
				ea -> {
					
					blurEffect.play();
					initiate(game);
					new Timeline(new KeyFrame(Duration.millis(200), ev ->{
						root.setEffect(boardDarken);
					})).play();
					
				}));

		initiateYou.setDelay(Duration.millis(timing*(14+game.table.dpC.size()+game.table.dpD.size()+game.table.dpH.size()+game.table.dpS.size()+7)));
		initiateYou.play();

		
		smashHead = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			if (isSmash) {
				if (smashamt < 100)
					smashamt += 14;
				else
					isSmash = false;
			} else {
				smashamt = Math.max(smashamt - 3, 0);
				if (smashamt == 0)
					boardBlur.setInput(null);
			}
			setSmash();
		}));
		smashHead.setCycleCount(100);

		blurEffect = new Timeline(new KeyFrame(Duration.millis(120), ae -> {
			setBlur(game);
		}));
		blurEffect.setCycleCount(Timeline.INDEFINITE);


		gameTime = new Timeline(new KeyFrame(Duration.millis(100), ae -> {
			gameintensity += intensVal;
			if (gameintensity > 255) {
				if (game.playerOne)
					p1LoseLife(game);
				else
					p2LoseLife(game);
			} else
				cover.setFill(Color.rgb((int) gameintensity, 0, 0));
		}));
		gameTime.setCycleCount(Timeline.INDEFINITE);


		// Add timer for human players.
		cover = new Rectangle(0, 0, 1024, 768);
		cover.setFill(Color.rgb(0, 0, 0));
		cover.setBlendMode(BlendMode.ADD);
		cover.setMouseTransparent(true);
		root.getChildren().add(cover);


		boardBlur.setIterations(1);
		boardDarken.setInput(boardBlur);
		
	}

	/*
	 * Function mainly written by max, with some modifications from Felicity,
	 * including the addition of a restart method.
	 */
	void endGame(Game game) {

		for (CardView cd : p1hand)
			cd.disable();
		for (CardView cd : p2hand)
			cd.disable();
		for (CardView cd : dpiles)
			cd.disable();
		gameTime.stop();
		gameintensity = 0;
		p1Damage = 0;
		p2Damage = 0;
		cover.toFront();
		new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
			PlaySound.doPlaySound(5); // playing screaming sound to end game
				Image endScreenImg;
				Image bloodSplatterImg = new Image(
						"comp1140/ass2/res/blood1.png");
				if (p1lives.getText().length() == 0)
					endScreenImg = new Image("comp1140/ass2/res/bloodp1.png");
				else if (p2lives.getText().length() == 0)
					endScreenImg = new Image("comp1140/ass2/res/bloodp2.png");
				else if (game.getScore().p1 > game.getScore().p2)
					endScreenImg = new Image("comp1140/ass2/res/bloodp2.png");
				else if (game.getScore().p1 < game.getScore().p2)
					endScreenImg = new Image("comp1140/ass2/res/bloodp1.png");
				else
					endScreenImg = new Image("comp1140/ass2/res/bloodd.png");
				ImageView bloodSplatter = new ImageView(bloodSplatterImg);
				ImageView endScreen = new ImageView(endScreenImg);
				bloodSplatter.setLayoutY(-1024);
				endScreen.setLayoutY(-1024);
				root.getChildren().add(bloodSplatter);
				root.getChildren().add(endScreen);
				Timeline bloodSpill = new Timeline();
				bloodSpill.setCycleCount(1);
				Interpolator easeOutQuad = new EaseOutQuad();
				final KeyValue ya = new KeyValue(
						bloodSplatter.layoutYProperty(), 300, easeOutQuad);
				final KeyFrame kf = new KeyFrame(Duration.millis(1400), ya);
				bloodSpill.getKeyFrames().add(kf);
				Timeline screenSpill = new Timeline();

				screenSpill.setCycleCount(1);
				final KeyValue ta = new KeyValue(endScreen.layoutYProperty(),
						0, easeOutQuad);
				final KeyFrame sf = new KeyFrame(Duration.millis(8000), ta);
				screenSpill.getKeyFrames().add(sf);

				ws = new Timeline(new KeyFrame(Duration.millis(80),
						ea -> {
							gameintensity++;
							cover.setFill(Color.rgb((int) gameintensity,
									(int) gameintensity, (int) gameintensity));
						}));
				ws.setCycleCount(255);
				ws.play();
				bloodSpill.play();
				screenSpill.play();
				
				Font kill = Font.loadFont(FONT_KILL, 60);
				
				p1score.setFill(Color.WHITE);
				p2score.setFill(Color.WHITE);
				p2score.setY(34);
				p1score.setY(730);
				p1score.setFont(kill);
				p2score.setFont(kill);
				p1score.toFront();
				p2score.toFront();
				p1score.setOpacity(0);
				p2score.setOpacity(0);
				
				
				scoreShow = new Timeline (new KeyFrame (Duration.millis(200),
						ea -> {
							p1score.setOpacity(scoreShowing);
							p2score.setOpacity(scoreShowing);
							scoreShowing = scoreShowing + 0.01; 
						}));
				scoreShow.setDelay(Duration.millis(1500));
				scoreShow.setCycleCount(60);
				scoreShow.play();
				
				//adding the restart button that allows player to restart game

				Rectangle restart = new Rectangle(100, 500, 250, 170);
				restart.setOpacity(0);
				root.getChildren().add(restart);

				restart.setOnMouseEntered(event -> {
					root.setCursor(Cursor.OPEN_HAND);
				});
				restart.setOnMouseExited(event -> {
					root.setCursor(Cursor.DEFAULT);
				});

				restart.setOnMouseClicked(event -> {
					for (int i = 0; i < root.getChildren().size(); i++) {
						root.getChildren().remove(0);
					}
					root.getChildren().remove(endScreen);
					root.getChildren().remove(bloodSplatter);

					restart();
				});

			})).play();

	}

	/**
	 * Restarts an ended game
	 */
	/* Function written mainly by Felicity and Max*/
	public void  restart(){

		// Set background
		blurEffect.stop();
		scoreShow.stop();
		ws.stop();
		ImageView background = new ImageView(bg);
		root.getChildren().add(background);
		r1Cy = 449;
		r1Dy = 449;
		r1Hy = 449;
		r1Sy = 449;
		r2Cy = 187;
		r2Dy = 187;
		r2Hy = 187;
		r2Sy = 187;
		root.setEffect(null);
		p1score = new Text(12, 758, "0");
		p2score = new Text(12, 27, "0");
		p1lives = new Text(0, 724, " ♥♥♥");
		p2lives = new Text(0, 54, " ♥♥♥");
		smashamt = 0;
		isSmash = false;
		boardPersp = new PerspectiveTransform();
		boardBlur = new BoxBlur();
		boardDarken = new ColorAdjust();
		p1Damage = 0;
		p2Damage = 0;
		bAmt = 0;
		gameintensity = 0;
		p1hand = null;
		p2hand = null;
		dpiles = null;
		p1click = "Human";
		p2click = "Human";
		deckcards = 0;
		royalscards = 0;
		p1AiDiff = 0;
		p2AiDiff = 0;
		gameTime = null;
		blurEffect = null;
		p1cardNo = 0;
		p2cardNo = 0;
		displacedH = 0;
		displacedS = 0;
		displacedC = 0;
		displacedD = 0;
		makeOpening();
		smashHead = null;
		blurEffect = null;
		gameTime=null;
		cover=null;
		scoreShowing=0;
	}

	/**
	 * The following functions are used to animate the board - upon lives lost.
	 * Increases immersivity. Improves Gameplay. Written by Max.
	 */
	
	/**Animation for smashing head into board*/
	public void setSmash() {
		boardPersp.setUlx(0 - 9 * smashamt);
		boardPersp.setUly(-8 - 9 * smashamt);
		boardPersp.setUrx(1024 + 9 * smashamt);
		boardPersp.setUry(-8 - 9 * smashamt);
		boardPersp.setLrx(1024 + 17 * smashamt);
		boardPersp.setLry(768 + 17 * smashamt);
		boardPersp.setLlx(0 - 17 * smashamt);
		boardPersp.setLly(768 + 17 * smashamt);
		boardDarken.setBrightness(-smashamt * 0.01);
	}
	/** Animation for blurring of screen*/
	public void setBlur(Game game) {
		if (game.playerOne) {
			if (bAmt > p1Damage)
				bAmt--;
			else if (p1Damage != 0)
				bAmt = Math.max(0, bAmt + (Math.random() - 0.58) / 5);
		} else {
			if (bAmt > p2Damage)
				bAmt--;
			else if (p2Damage != 0)
				bAmt = bAmt += Math.max(0, bAmt + (Math.random() - 0.58) / 5);
		}
		boardBlur.setHeight(bAmt);
		boardBlur.setWidth(bAmt);
		boardDarken.setBrightness(bAmt / 30);
	}
	/**Adds effects with player 1s loss of life
	 * 
	 * @param game - The current game
	 */
	public void p1LoseLife(Game game) {
		isSmash = true;
		boardBlur.setInput(boardPersp);
		smashHead.stop();
		smashHead.play();
		p1Damage += 5;
		bAmt = p1Damage;
		gameintensity = 0;
		p1lives.setText(p1lives.getText()
				.subSequence(0, p1lives.getText().length() - 1).toString());
		if (p1lives.getText().length() == 0)
			initiate(game);
		PlaySound.doPlaySound(6); // play the smash head sound to indicate life
									// loss
		PlaySound.boo(); // play the 'boo' sound from 'audience'
	}
	/**Adds effects with player 1s lose of life
	 * 
	 * @param game
	 */
	public void p2LoseLife(Game game) {
		isSmash = true;
		boardBlur.setInput(boardPersp);
		smashHead.stop();
		smashHead.play();
		p2Damage += 5;
		bAmt = p2Damage;
		gameintensity = 0;
		p2lives.setText(p2lives.getText()
				.subSequence(0, p2lives.getText().length() - 1).toString());
		if (p2lives.getText().length() == 0)
			initiate(game);
		PlaySound.doPlaySound(6); // play smash head sound
		PlaySound.boo(); // play 'boo' sound from 'audience'
	}

	/*
	 *  Written by Max Wang (u5584091)
	 */
	/**Function enables card to be dragged
	 * 
	 * @param card- the card that is being made draggable
	 * @param game - the current game
	 */
	public void makeDraggable(CardView card, Game game) {
		CardAreas cardAreas = new CardAreas();
		card.setOnMouseReleased(event -> {
			PlaySound.doPlaySound(3); // play the card releasing sound for Human
										// Players
			card.press.stop();
			card.unpress.play();
			boolean validplay = false;
			for (CardArea ca : cardAreas.areas) {
				if (card.getLayoutX() < ca.brx && card.getLayoutX() > ca.tlx
						&& card.getLayoutY() > ca.tly
						&& card.getLayoutY() < ca.bry) {
					validplay = true;
					card.out.play();
					if (ca.type == 'r') {
						if (game.playerOne) {
							if (game.p1.playToSuit(card.card, game.table, 1)) {
								if (card.card.suit == 'C') {
									card.snapTo(r1x + 212, r1Cy);
									r1Cy -= 20;
								} else if (card.card.suit == 'D') {
									card.snapTo(r1x + 319, r1Dy);
									r1Dy -= 20;
								} else if (card.card.suit == 'H') {
									card.snapTo(r1x + 105, r1Hy);
									r1Hy -= 20;
								} else if (card.card.suit == 'S') {
									card.snapTo(r1x, r1Sy);
									r1Sy -= 20;
								}
								for (int i = 0; i < p1hand.size(); i++) {
									if (p1hand.get(i) != card) {

										p1hand.get(i).pPosX += 107;
										p1hand.get(i).initializeReturn();
										p1hand.get(i).returnCard.play();
									} else {
										p1hand.remove(card);
										break;
									}
								}

								card.disable();

								/*
								 * We now initiate for player 2.
								 */
								game.playerOne = false;
								initiate(game);

							} else {
								card.returnCard.play();
								p1LoseLife(game);
							}
						} else {
							if (game.p2.playToSuit(card.card, game.table, 1)) {
								if (card.card.suit == 'C') {
									card.snapTo(r2x + 105, r2Cy);
									r2Cy += 20;
								} else if (card.card.suit == 'D') {
									card.snapTo(r2x, r2Dy);
									r2Dy += 20;
								} else if (card.card.suit == 'H') {
									card.snapTo(r2x + 211, r2Hy);
									r2Hy += 20;
								} else if (card.card.suit == 'S') {
									card.snapTo(r2x + 319, r2Sy);
									r2Sy += 20;
								}

								for (int i = 0; i < p2hand.size(); i++) {
									if (p2hand.get(i) != card) {
										p2hand.get(i).pPosX += 107;
										p2hand.get(i).initializeReturn();
										p2hand.get(i).returnCard.play();
									} else {
										p2hand.remove(card);
										break;
									}
								}
								card.disable();
								game.playerOne = true;
								initiate(game);
							} else {
								card.returnCard.play();
								p2LoseLife(game);
							}
						}
					} else if (ca.type == 'd') {
						if (game.playerOne) {
							if (game.p1.playToSuit(card.card, game.table, 0)
									&& !game.p1.canDraw) {
								if (card.card.suit == 'C') {
									card.snapTo(dx, dy);
								} else if (card.card.suit == 'D') {
									card.snapTo(dx, dy + 111);
								} else if (card.card.suit == 'H') {
									card.snapTo(dx, dy + 333);
								} else if (card.card.suit == 'S') {
									card.snapTo(dx, dy + 444);
								}
								for (int i = 0; i < p1hand.size(); i++) {
									if (p1hand.get(i) != card) {
										p1hand.get(i).pPosX += 107;
										p1hand.get(i).initializeReturn();
										p1hand.get(i).returnCard.play();
									} else {
										p1hand.remove(card);
										dpiles.add(card);

										break;
									}
								}

								makeCollectable(card, game);
								card.doRotate.play();

								/*
								 * We now initiate for player 2.
								 */
								game.playerOne = false;
								initiate(game);

							} else {
								card.returnCard.play();
								isSmash = true;
								boardBlur.setInput(boardPersp);
								smashHead.stop();
								smashHead.play();
								p1Damage += 5;
								bAmt = p1Damage;
								p1lives.setText(p1lives
										.getText()
										.subSequence(0,
												p1lives.getText().length() - 1)
										.toString());
								if (p1lives.getText().length() == 0)
									initiate(game);
							}
						} else {
							if (game.p2.playToSuit(card.card, game.table, 0)
									&& !game.p2.canDraw) {
								if (card.card.suit == 'C') {
									card.snapTo(dx, dy);
								} else if (card.card.suit == 'D') {
									card.snapTo(dx, dy + 111);
								} else if (card.card.suit == 'H') {
									card.snapTo(dx, dy + 333);
								} else if (card.card.suit == 'S') {
									card.snapTo(dx, dy + 444);
								}
								for (int i = 0; i < p2hand.size(); i++) {
									if (p2hand.get(i) != card) {
										p2hand.get(i).pPosX += 107;
										p2hand.get(i).initializeReturn();
										p2hand.get(i).returnCard.play();
									} else {
										p2hand.remove(card);
										dpiles.add(card);
										break;
									}
								}
								makeCollectable(card, game);

								card.doRotate.play();
								game.playerOne = true;
								initiate(game);
							} else {
								card.returnCard.play();
								isSmash = true;
								boardBlur.setInput(boardPersp);
								smashHead.stop();
								smashHead.play();
								p2Damage += 5;
								bAmt = p2Damage;
								p2lives.setText(p1lives
										.getText()
										.subSequence(0,
												p1lives.getText().length() - 1)
										.toString());
								if (p2lives.getText().length() == 0)
									initiate(game);
							}
						}
					}
				}
			}
			if (!validplay)
				card.returnCard.play();
		});

	}

	/*Written by Max(u5584091)
	 */
	/**Function makes card acquirable (i.e, in the discard/deck)
	 * 
	 * @param card - the card in question
	 * @param game - the current game
	 */
	public void makeCollectable(CardView card, Game game) {
		card.setOnMouseReleased(event -> {
			PlaySound.doPlaySound(1); // play the drawing card sound for Human
										// players
			
			card.out.play();
			card.unpress.play();
			if (game.playerOne) {
				if (game.p1.canDraw) {
					if (game.table.deck.contains(card.card))
						game.p1.doDraw(game.table.deck, game.table);
					else if (card.card.suit == 'C')
						game.p1.doDraw(game.table.dpC, game.table);
					else if (card.card.suit == 'D')
						game.p1.doDraw(game.table.dpD, game.table);
					else if (card.card.suit == 'H')
						game.p1.doDraw(game.table.dpH, game.table);
					else if (card.card.suit == 'S')
						game.p1.doDraw(game.table.dpS, game.table);
					card.unRotate.play();
					card.pPosX = 162;
					card.pPosY = 609;
					card.initializeReturn();
					p1hand.add(0, card);
					dpiles.remove(card);
					game.p1.canDraw = false;
					makeDraggable(card, game);
				} else {
					isSmash = true;
					smashHead.play();
					p1LoseLife(game);
				}

			} else {
				if (game.p2.canDraw) {
					if (game.table.deck.contains(card.card))
						game.p2.doDraw(game.table.deck, game.table);
					else if (card.card.suit == 'C')
						game.p2.doDraw(game.table.dpC, game.table);
					else if (card.card.suit == 'D')
						game.p2.doDraw(game.table.dpD, game.table);
					else if (card.card.suit == 'H')
						game.p2.doDraw(game.table.dpH, game.table);
					else if (card.card.suit == 'S')
						game.p2.doDraw(game.table.dpS, game.table);
					card.unRotate.play();
					card.pPosX = 162;
					card.pPosY = 24;
					card.initializeReturn();
					game.p2.canDraw = false;
					p2hand.add(0, card);
					dpiles.remove(card);
					makeDraggable(card, game);
				} else {
					isSmash = true;
					smashHead.play();
					p2LoseLife(game);
				}
			}
			card.returnCard.play();
			if (game.table.deck.size() == 0)
				deckHider.setOpacity(0);
		});
	}



	/*
	 * Function mainly written by Max (u5584091)
	 */
	/**Switch between AI and Human players.
	 * 
	 * @param game - the current game
	 */
	public void initiate(Game game) {
		if (game.table.deck.size() == 0)
			deckHider.setOpacity(0);
		Score sc = game.getScore();
		p1score.setText(sc.p1 + "");
		p2score.setText(sc.p2 + "");
		gameintensity = 0;
		gameTime.stop();
		cover.setFill(Color.BLACK);

		if (!(game.p1.endCount >= 3 || game.p2.endCount == 3
				|| p1lives.getText().length() == 0 || p2lives.getText()
				.length() == 0)) {
			for (CardView cd : p1hand)
				cd.disable();
			for (CardView cd : p2hand)
				cd.disable();
			for (CardView cd : dpiles)
				cd.disable();
			if (game.playerOne) {
				bAmt = p1Damage - 2;
				game.p1.canDraw = true;
				game.p1.canPlay = true;
				if (game.p1 instanceof Human) {
					gameTime.play();
					for (CardView c : p1hand) {
						c.enable();
						makeDraggable(c, game);
					}
					for (CardView c : dpiles) {
						c.enable();
						makeCollectable(c, game);
					}
				} else if (game.p1 instanceof AI) {
					/**
					 * We delay the AI playing as to let the human's card
					 * animation finish. (Concurrency was too hard to implement,
					 * as I couldn't2 work out how to make a thread return a
					 * result without a massive change to the AI.)
					 * Alternatively, if anyone wants to implement concurrent
					 * threads, feel free to do so.
					 */
					new Timeline(new KeyFrame(Duration.millis(800), ae -> {
						MCMove move = ((AI) game.p1).doPlay(game,
								((AI) game.p1).difficulty);
						CardView assoc = null;
						for (CardView d : dpiles) {
							if (d.card == move.drawnCard) {
								d.toFront();
								d.unRotate.play();
								d.pPosX = 162;
								d.pPosY = 609;
								d.initializeReturn();
								d.returnCard.play();
								p1hand.add(0, d);
								assoc = d;
							}
						}
						dpiles.remove(assoc);

						Card cardPlayed = p1hand.get(7 - move.cardPlayed).card;

							assoc = null;
							for (int i = 0; i < p1hand.size(); i++) {
								if (p1hand.get(i).card != cardPlayed) {
									p1hand.get(i).pPosX += 107;
									p1hand.get(i).initializeReturn();
									p1hand.get(i).returnCard.play();
								} else if (p1hand.get(i).card == cardPlayed) {
									p1hand.get(i).toFront();
									if (cardPlayed.suit == 'C'
											&& move.play == 1) {
										p1hand.get(i).snapTo(r1x + 212, r1Cy);
										p1hand.get(i).disable();
										r1Cy -= 24;
									} else if (cardPlayed.suit == 'D'
											&& move.play == 1) {
										p1hand.get(i).snapTo(r1x + 319, r1Dy);
										p1hand.get(i).disable();
										r1Dy -= 24;
									} else if (cardPlayed.suit == 'H'
											&& move.play == 1) {
										p1hand.get(i).snapTo(r1x + 105, r1Hy);
										p1hand.get(i).disable();
										r1Hy -= 24;
									} else if (cardPlayed.suit == 'S'
											&& move.play == 1) {
										p1hand.get(i).snapTo(r1x, r1Sy);
										p1hand.get(i).disable();
										r1Sy -= 24;
									} else if (cardPlayed.suit == 'C'
											&& move.play == 0) {
										p1hand.get(i).snapTo(dx, dy);
										p1hand.get(i).doRotate.play();
										dpiles.add(p1hand.get(i));
									} else if (cardPlayed.suit == 'D'
											&& move.play == 0) {
										p1hand.get(i).snapTo(dx, dy + 111);
										p1hand.get(i).doRotate.play();
										dpiles.add(p1hand.get(i));
									} else if (cardPlayed.suit == 'H'
											&& move.play == 0) {
										p1hand.get(i).snapTo(dx, dy + 333);
										p1hand.get(i).doRotate.play();
										dpiles.add(p1hand.get(i));
									} else if (cardPlayed.suit == 'S'
											&& move.play == 0) {
										p1hand.get(i).snapTo(dx, dy + 444);
										p1hand.get(i).doRotate.play();
										dpiles.add(p1hand.get(i));
									}
									assoc = p1hand.get(i);
									break;
								}
							}
							if (assoc != null)
								p1hand.remove(assoc);

							game.playerOne = false;
							initiate(game);

						})).play();

				}
			} else {
				bAmt = p2Damage - 2;
				game.p2.canDraw = true;
				game.p2.canPlay = true;
				if (game.p2 instanceof Human) {
					gameTime.play();
					for (CardView c : p2hand) {
						c.enable();
						makeDraggable(c, game);
					}
					for (CardView c : dpiles) {
						c.enable();
						makeCollectable(c, game);
					}
				} else if (game.p2 instanceof AI) {
					/**
					 * We delay the AI playing as to let the human's card
					 * animation finish. (Concurrency was too hard to implement,
					 * as I couldn't2 work out how to make a thread return a
					 * result without a massive change to the AI.)
					 * Alternatively, if anyone wants to implement concurrent
					 * threads, feel free to do so.
					 */
					new Timeline(new KeyFrame(Duration.millis(800), ae -> {
						MCMove move = ((AI) game.p2).doPlay(game,
								((AI) game.p2).difficulty);
						CardView assoc = null;
						for (CardView d : dpiles) {
							if (d.card == move.drawnCard) {
								d.toFront();
								d.unRotate.play();
								d.pPosX = 162;
								d.pPosY = 24;
								d.initializeReturn();
								d.returnCard.play();
								p2hand.add(0, d);
								assoc = d;
							}
						}
						dpiles.remove(assoc);

						Card cardPlayed = p2hand.get(7 - move.cardPlayed).card;

						// &*^%@#$
							assoc = null;
							for (int i = 0; i < p2hand.size(); i++) {
								if (p2hand.get(i).card != cardPlayed) {
									p2hand.get(i).pPosX += 107;
									p2hand.get(i).initializeReturn();
									p2hand.get(i).returnCard.play();
								} else if (p2hand.get(i).card == cardPlayed) {
									p2hand.get(i).toFront();
									if (cardPlayed.suit == 'C'
											&& move.play == 1) {
										p2hand.get(i).snapTo(r2x + 105, r2Cy);
										p2hand.get(i).disable();
										r2Cy += 24;
									} else if (cardPlayed.suit == 'D'
											&& move.play == 1) {
										p2hand.get(i).snapTo(r2x, r2Dy);
										p2hand.get(i).disable();
										r2Dy += 24;
									} else if (cardPlayed.suit == 'H'
											&& move.play == 1) {
										p2hand.get(i).snapTo(r2x + 211, r2Hy);
										p2hand.get(i).disable();
										r2Hy += 24;
									} else if (cardPlayed.suit == 'S'
											&& move.play == 1) {
										p2hand.get(i).snapTo(r2x + 319, r2Sy);
										p2hand.get(i).disable();
										r2Sy += 24;
									} else if (cardPlayed.suit == 'C'
											&& move.play == 0) {
										p2hand.get(i).snapTo(dx, dy);
										p2hand.get(i).doRotate.play();
										dpiles.add(p2hand.get(i));
									} else if (cardPlayed.suit == 'D'
											&& move.play == 0) {
										p2hand.get(i).snapTo(dx, dy + 111);
										p2hand.get(i).doRotate.play();
										dpiles.add(p2hand.get(i));
									} else if (cardPlayed.suit == 'H'
											&& move.play == 0) {
										p2hand.get(i).snapTo(dx, dy + 333);
										p2hand.get(i).doRotate.play();
										dpiles.add(p2hand.get(i));
									} else if (cardPlayed.suit == 'S'
											&& move.play == 0) {
										p2hand.get(i).snapTo(dx, dy + 444);
										p2hand.get(i).doRotate.play();
										dpiles.add(p2hand.get(i));
									}
									assoc = p2hand.get(i);
									break;
								}
							}
							if (assoc != null)
								p2hand.remove(assoc);

							game.playerOne = true;
							initiate(game);

						})).play();

				}

			}
		} else {
			endGame(game);
		};
	}

}
