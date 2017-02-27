package comp1140.ass2;


/**
 * Basically an int tuple, to keep track of scores i na game.
 * @author u5584091
 *
 */
public class Score {
	int p1;
	int p2;
	public Score(){
		p1 = 0;
		p2 = 0;
	}
	public int p1(){
		return p1;
	}
	public int p2(){
		return p2;
	}
	@Override
	public String toString(){
		return "P1: "+p1+",  P2:"+p2;
	}
}
