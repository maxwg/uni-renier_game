package comp1140.ass2;


import org.junit.Test;

//class written by Fangqin Chen -u5552738

public class CardAreasTest extends JFXTest{

	private boolean sameCardArea(CardArea one, CardArea two){
		return one.tlx == two.tlx && one.tly==two.tly && one.brx == two.brx && one.bry == two.bry && one.type == two.type;
	}
	
	/**
	 * this function checks CardAreas has the desired CardArea
	 * @throws InterruptedException
	 */
	@Test
	 public void testCompareValue() throws InterruptedException {
	   runJFXTest(new Runnable() {
	     @Override
	     public void run() {
	    	CardAreas area = new CardAreas();
	    	CardArea one = new CardArea(120, 75, 1000, 550, 'r');
	    	CardArea two = new CardArea(-200,55 ,100,550,'d');
	    	assertJFXTrue(area.areas.size()==2);
	    	assertJFXTrue(sameCardArea(area.areas.get(0),one));
	    	assertJFXTrue(sameCardArea(area.areas.get(1),two));

	    	
	     
	 		endOfJFXTest();
	     }
	     
	   });}

}
