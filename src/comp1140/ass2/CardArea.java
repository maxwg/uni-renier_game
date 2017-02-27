package comp1140.ass2;

/**
 * Four attributes whih determines areas of drag-and-drop.
 * Used for human drag and drop plays/discards.
 * 
 * @author Max
 *
 */
public class CardArea {
	double tlx;
	double tly;
	double brx;
	double bry;
	char type;
	/**
	 * @param type - d = discard, r=run
	 */
	
	public CardArea(double tlx, double tly, double brx, double bry, char type){
		this.tlx = tlx;
		this.tly = tly;
		this.brx = brx;
		this.bry = bry;
		this.type = type;
	}
}
