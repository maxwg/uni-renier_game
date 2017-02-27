package comp1140.ass2;

import java.util.ArrayList;

public class Table {
	ArrayList<Card> dpH;
	ArrayList<Card> dpS;
	ArrayList<Card> dpC;
	ArrayList<Card> dpD;
	ArrayList<Card> deck;
	
	public Table (ArrayList<Card> dpH, ArrayList<Card> dpS,ArrayList<Card> dpC, ArrayList<Card> dpD, ArrayList<Card> deck){
		this.dpH = dpH;
		this.dpS = dpS;
		this.dpC = dpC;
		this.dpD = dpD;
		this.deck = deck;
	} //u5552738 Fangqin Chen
	@Override
	public String toString(){
		StringBuilder rtn = new StringBuilder();
		rtn.append("Discard: ");
		rtn.append(dpC +" "+dpD+" "+dpH+" "+dpS);
		rtn.append("\nDeck:    ");
		rtn.append(deck);
		return rtn.toString();
	}
 
}
