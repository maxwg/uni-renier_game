package comp1140.ass2;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/*
 * Entire class written by Max Wang (u5584091). 
 * Based solely on java documentation from oracle.
 */
public class CardView extends StackPane {
	/*
	 * Basis class variables - is the card selectable, the image of the card
	 * as well as the actual card itself.
	 */
	boolean selectable;
	ImageView img;
	Card card;
	//Holder acts as a container for the cards. This allows the shadow to be
	//applied at the same angle regardless of the card's rotation and perspective.
	StackPane holder;
	
	/*
	 * Various variables relating to transforms of the card
	 */
	private double gAmt = 0.0;
	private Glow glow = new Glow(gAmt);
	private PerspectiveTransform perspective = new PerspectiveTransform();
	private DropShadow ds = new DropShadow(8, 3, 3, Color.BLACK);
	Scale size;
	Rotate rotate;
	
	/*
	 * Timeline animations - declared here so they are public and
	 * playable through the main class.
	 */
	Timeline over;
	Timeline out;
	Timeline press;
	Timeline unpress;
	Timeline returnCard;
	Timeline doRotate;
	Timeline unRotate;
	Timeline doSlowRotate;
	double rotateVariable = 90;
	
	/*
	 * Various variables used in JavaFX to calculate certain transforms,
	 * such as the amazing perspective effect on the card.
	 */
	private double pmousex;
	private double pmousey;
	private double[] xDistArr = new double[10];
	private double[] yDistArr = new double[10];
	private int arrNo;
	private double scale;
	private double pScale;
	private double mScale;
	double pPosX;
	double pPosY;
	private int colVal = 255;
	
	/** Creates new CardView
	 * 
	 * @param card - the card that needs to be changed
	 * @param x - the x location of the upper left corner
	 * @param y - the y location of the upper left corner
	 * @param sc - size of card
	 * @param selectable - whether or not the card can be clicked/dragged
	 */
	public CardView(Card card, double x, double y, double sc, boolean selectable) {
		/*
		 * Initialise various variables.
		 */
		super();
		this.img = new ImageView(card);
		holder = new StackPane();
		holder.getChildren().add(this.img);
		pPosX = x;
		pPosY = y;
		this.card = card;
		/*
		 * Set starting transforms, and initialize various variables.
		 */
		this.setLayoutX(x);
		this.setLayoutY(y);
		size = new Scale(sc, sc, 0, 0);
		rotate = new Rotate(0,128,179);
		scale = sc;
		mScale = scale * 1.05;
		pScale = sc;
		this.selectable = selectable;
		holder.setEffect(glow);
		holder.getTransforms().add(rotate);
		this.setEffect(ds);
		
		//Set card style
		holder.setStyle("-fx-border-color:black; \n"
				+ "-fx-background-radius:12;\n" + "-fx-border-radius:12;\n"
				+ "-fx-border-width:2.0;\n" + "-fx-padding:4;"
				+ "-fx-background-color:#fffdfa");
		
		initializeReturn();
		this.getChildren().add(holder);
		this.getTransforms().add(size);
		/*
		 * Slightly improves performance.
		 */
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
		if (selectable) {
			//we make the card draggable.
			this.enable();
		}
		
		
		/*
		 * Initialize various timeline animations
		 */
		doRotate = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			double ramt = Math.min(rotate.getAngle()+12, 90);
			rotate.setAngle(ramt);
		}));
		doRotate.setCycleCount(8);
		unRotate = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			double ramt = Math.max(rotate.getAngle()-12, 0);
			rotate.setAngle(ramt);
		}));
		unRotate.setCycleCount(8);
		
		doSlowRotate = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			rotate.setAngle(rotateVariable);
			rotateVariable = Math.max(rotate.getAngle()-10, 0);
		})
				);
		doSlowRotate.setCycleCount(12);
	}

	/*
	 * Written by Max Wang
	 */
	/**This function sets the perspective of the card, based on the current
	 * and previous velocities of the mouse
	 * 
	 * @param tX - previous velocity x
	 * @param tY - previous velocity y
	 */
	public void setPerspective(double tX, double tY) {
		perspective.setUlx(+tY / 30);
		perspective.setUly(+tX / 20);
		perspective.setUrx(+this.getWidth() - tY / 30);
		perspective.setUry(-tX / 20);
		perspective.setLrx(+this.getWidth() + tY / 30);
		perspective.setLry(+this.getHeight() + tX / 20);
		perspective.setLlx(-tY / 30);
		perspective.setLly(+this.getHeight() - tX / 20);
		arrNo = (arrNo + 1) % 10;
	}
	
	/*
	 * Basic interpolator, created using common sense
	 */
	public static class EaseOutQuad extends Interpolator {
		@Override
		protected double curve(double t) {
			return 1 - Math.pow((1 - t), 4);
		}
	}
	/**Gets glow for testing purposes
	 * 
	 */
	public double getGlow(){
		return gAmt;
	}
	/**
	 * "Activate" a card, makes the cards draggable, and initializes
	 * more timeline animation.
	 */
	public void enable(){
		/*
		 * We now create various timeline animations for certain
		 * states of the card.
		 */
		
		/*
		 * Mouseover
		 */
		over = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			gAmt = Math.min(gAmt + 0.08, 0.4);
			glow.setLevel(gAmt);
			colVal = Math.max(colVal - 24, 170);
			holder.setStyle("-fx-border-color:black; \n"
					+ "-fx-background-radius:12;\n" + "-fx-border-radius:12;\n"
					+ "-fx-border-width:2.0;\n" + "-fx-padding:4;"
					+ "-fx-background-color:#" + Integer.toHexString(colVal)
					+ "fdfa;");
		}));
		over.setCycleCount(5);

		/*
		 * MouseOut
		 */
		out = new Timeline(new KeyFrame(Duration.millis(20), ae -> {
			gAmt = Math.max(gAmt - 0.024, 0.0);
			glow.setLevel(gAmt);
			colVal = Math.min(colVal + 2, 255);
			holder.setStyle("-fx-border-color:black; \n"
					+ "-fx-background-radius:12;\n" + "-fx-border-radius:12;\n"
					+ "-fx-border-width:2.0;\n" + "-fx-padding:4;"
					+ "-fx-background-color:#" + Integer.toHexString(colVal)
					+ "fdfa;");
		}));
		out.setCycleCount(70);

		/*
		 * MousePress
		 */
		press = new Timeline(new KeyFrame(Duration.millis(30), ae -> {
			scale = Math.min(scale + 0.006, mScale);
			size.setX(scale);
			size.setY(scale);
			this.setTranslateX((pScale - scale) * 250);
			this.setTranslateY((pScale - scale) * 250);
			ds.setRadius(8+(scale - pScale) * 400);
			ds.setOffsetX(3 + (scale - pScale) * 200);
			ds.setOffsetY(3 + (scale - pScale) * 200);
		}));
		press.setCycleCount(20);

		
		/*
		 * MouseRelease
		 */
		unpress = new Timeline(new KeyFrame(Duration.millis(40),
				ae -> {
					scale = Math.max(scale - 0.002, pScale);
					size.setX(scale);
					size.setY(scale);
					this.setTranslateX((pScale - scale) * 250);
					this.setTranslateY((pScale - scale) * 250);
					ds.setRadius(8+(scale - pScale) * 400);
					ds.setOffsetX(3 + (scale - pScale) * 200);
					ds.setOffsetY(3 + (scale - pScale) * 200);
					xDistArr[arrNo] = 0;
					yDistArr[arrNo] = 0;
					double tX = 0;
					double tY = 0;
					for (double i : xDistArr)
						tX += i;
					for (double i : yDistArr)
						tY += i;
					setPerspective(tX, tY);
				}));
		unpress.setCycleCount(40);
		
		
		
		this.setOnMouseEntered(event -> {
			out.stop();
			over.play();
			this.getParent().setCursor(Cursor.OPEN_HAND);
		});
		this.setOnMouseExited(event -> {
			over.stop();
			out.play();
			this.getParent().setCursor(Cursor.DEFAULT);
		});
		
		
		this.setOnMouseDragged(event -> {
			double xDist = event.getSceneX() - pmousex;
			double yDist = event.getSceneY() - pmousey;
			this.setLayoutX(this.getLayoutX() + xDist);
			this.setLayoutY(this.getLayoutY() + yDist);
			xDistArr[arrNo] = xDist;
			yDistArr[arrNo] = yDist;
			double tX = 0;
			double tY = 0;
			for (double i : xDistArr)
				tX += i;
			for (double i : yDistArr)
				tY += i;
			pmousex = event.getSceneX();
			pmousey = event.getSceneY();
			this.setPerspective(tX, tY);
			glow.setInput(perspective);
		});
		
		this.setOnMousePressed(event -> {
			pmousex = event.getSceneX();
			pmousey = event.getSceneY();
			unpress.stop();
			press.play();
			for (double i : xDistArr)
				i = 0;
			for (double i : yDistArr)
				i = 0;
			arrNo = 0;
			returnCard.stop();
			this.toFront();
		});
	}
	
	public void disable(){
		/*
		 * We can not remove event handlers for lambda
		 * events well, so let's just set them to empty
		 * events.
		 */
		this.setOnMouseDragged(event->{});
		this.setOnMousePressed(event->{});
		this.setOnMouseReleased(event->{});
		this.setOnMouseEntered(event->{});
		this.setOnMouseExited(event->{});
	}
	
	/*
	 * Animate movement of card to certain x and y coords.
	 */
	public void snapTo(double x, double y){
		Timeline snap = new Timeline();
		snap.setCycleCount(1);
		Interpolator easeOutQuad = new EaseOutQuad();
		final KeyValue xa = new KeyValue(this.layoutXProperty(), x,
				easeOutQuad);
		final KeyValue ya = new KeyValue(this.layoutYProperty(), y,
				easeOutQuad);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), xa, ya);
		snap.getKeyFrames().add(kf);
		snap.play();
	}
	public void initializeReturn(){
		returnCard = new Timeline();
		returnCard.setCycleCount(1);
		Interpolator easeOutQuad = new EaseOutQuad();
		final KeyValue xa = new KeyValue(this.layoutXProperty(), pPosX,
				easeOutQuad);
		final KeyValue ya = new KeyValue(this.layoutYProperty(), pPosY,
				easeOutQuad);
		final KeyFrame kf = new KeyFrame(Duration.millis(400), xa, ya);
		returnCard.getKeyFrames().add(kf);
	}
}
