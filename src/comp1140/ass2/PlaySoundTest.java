package comp1140.ass2;

//entire class written by Fangqin Chen -u5552738

import static org.junit.Assert.*;

import java.util.Random;

import javafx.scene.media.AudioClip;

import org.junit.Test;

public class PlaySoundTest extends JFXTest{
	
	private AudioClip correspondingAudioClip(int n){
		if (n == 1){
			return PlaySound.draw;}
		if (n == 2){
			return PlaySound.deal;}
		if (n == 3){
			return PlaySound.play;}
		if (n == 4){
			return PlaySound.AI_sound;}
		if (n == 5){
			return PlaySound.end;}
		else 
			return PlaySound.smash;
		
	}
	
	private AudioClip correspondingBoo(int n){
		if (n == 0){
			return PlaySound.boo1;}
		if (n == 1){
			return PlaySound.boo2;}
		if (n == 2){
			return PlaySound.boo3;}
		else 
			return PlaySound.boo4;
	}
	
	/*
	 * The following tests are based on the cycle counts of the AudioClips after the methods are called
	 * In the test, I first set the cycle count to an incorrect value
	 * then called the functions and checked if the getCycleCount return expected values
	 */
	
	@Test
	public void testDoPlaySound() throws InterruptedException {
		   runJFXTest(new Runnable() {
		     @Override
		     public void run() { 
		    	Random rand = new Random();
		    	int n = rand.nextInt(6)+1;
		    	correspondingAudioClip(n).setCycleCount(3);
		    	PlaySound.doPlaySound(n);
		 		assertTrue(correspondingAudioClip(n).getCycleCount()==1);
		 		endOfJFXTest();
		     }
		     
		   });}
	
	/*
	 * For this test, since boo() calls one of the four BOO AudioClips, 
	 * I checked the total cycle counts instead of the one called randomly
	 */
	@Test
	public void testBoo() throws InterruptedException {
		   runJFXTest(new Runnable() {
		     @Override
		     public void run() { 
		    	for (int n = 0; n < 4; n++){
		    		correspondingBoo(n).setCycleCount(3);
		    	}
		    	PlaySound.boo();
		    	int totalCounts = 0;
		    	for (int n = 0; n < 4; n++){
		    		totalCounts += correspondingBoo(n).getCycleCount();
		    	}
		 		assertTrue(totalCounts==10);
		 		endOfJFXTest();
		     }
		     
		   });}
	
	@Test
	public void testRandomLaugh() throws InterruptedException {
		   runJFXTest(new Runnable() {
		     @Override
		     public void run() {
		    	PlaySound.AI_evil.setCycleCount(3); 
		    	if (PlaySound.randomLaugh() == 1){
		    		assertTrue(PlaySound.AI_evil.getCycleCount() == 1);}
		    	else assertTrue(PlaySound.AI_evil.getCycleCount() == 3);
		 		endOfJFXTest();
		     }
		     
		   });}

}
