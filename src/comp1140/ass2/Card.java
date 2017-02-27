package comp1140.ass2;

import javafx.scene.image.Image;

/*
 * Entire class written by Max Wang (u5584091)
 */
public class Card extends Image implements Comparable<Card> {
	char value;
	char suit;

	public Card(char value, char suit, String url) {
		super(url);
		this.value = value;
		this.suit = suit;
	}

	public Card(char value, char suit) {
		super("comp1140/ass2/res/" + value + suit + ".png");
		this.value = value;
		this.suit = suit;
	}

	public char suit() {
		return suit;
	}

	public int value() {
		if (value != 'K' && value != 'T' && value != 'A' && value != 'J'
				&& value != 'Q')
			return Integer.parseInt(new String(value + ""));
		if (value == 'T')
			return 10;
		if (value == 'A')
			return 1;
		else
			return 0;
	}

	public boolean sameSuit(Card other) {
		return (this.suit == other.suit);
	}

	/*
	 * J, Q, and K are considered value of 0 when comparing,
	 * to simplify them being on the bottom.
	 */
	@Override
	public int compareTo(Card other) {
		return this.value() - other.value();
	}

	@Override
	public String toString() {
		return this.value + "" + this.suit;
	}
}
