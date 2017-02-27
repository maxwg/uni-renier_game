package comp1140.ass2;

import java.util.ArrayList;
/**
 * Basically an arraylist of areas, however, they are 
 * predefined.
 * 
 * @author Max
 *
 */
public class CardAreas {
	ArrayList<CardArea> areas;
	public CardAreas(){
		areas = new ArrayList<CardArea>();
		areas.add(new CardArea(120, 75, 1000, 550, 'r'));
		areas.add(new CardArea(-200,55 ,100,550,'d'));

	}
	
}
