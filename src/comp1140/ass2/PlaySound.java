package comp1140.ass2;

//entire class written by Fangqin Chen -u5552738

import java.util.Random;

import javafx.scene.media.AudioClip;



public class PlaySound{
    
	//location of the media sources
	private static final String DRAW = PlaySound.class.getResource("/comp1140/ass2/res/draw.mp3").toString();
	private static final String DEAL = PlaySound.class.getResource("/comp1140/ass2/res/deal.mp3").toString();
	private static final String PLAY = PlaySound.class.getResource("/comp1140/ass2/res/play.mp3").toString();
	private static final String AI_SOUND = PlaySound.class.getResource("/comp1140/ass2/res/AI.mp3").toString();
	private static final String AI_EVIL = PlaySound.class.getResource("/comp1140/ass2/res/laugh.mp3").toString();
	private static final String END = PlaySound.class.getResource("/comp1140/ass2/res/scream.mp3").toString();
	private static final String SMASH = PlaySound.class.getResource("/comp1140/ass2/res/smash.mp3").toString();
	private static final String BOO1 = PlaySound.class.getResource("/comp1140/ass2/res/boo1.mp3").toString();
	private static final String BOO2 = PlaySound.class.getResource("/comp1140/ass2/res/boo2.mp3").toString();
	private static final String BOO3 = PlaySound.class.getResource("/comp1140/ass2/res/boo3.mp3").toString();
	private static final String BOO4 = PlaySound.class.getResource("/comp1140/ass2/res/boo4.mp3").toString();
	
	//generate AudioClips -their modifiers are public as they are also used in PlaySoundTest.java
	public static AudioClip draw = new AudioClip(DRAW);
	public static AudioClip deal = new AudioClip(DEAL);
	public static AudioClip play = new AudioClip(PLAY);
	public static AudioClip AI_sound = new AudioClip(AI_SOUND);
	public static AudioClip AI_evil = new AudioClip(AI_EVIL);
	public static AudioClip end = new AudioClip(END);
	public static AudioClip smash = new AudioClip(SMASH);
	public static AudioClip boo1 = new AudioClip(BOO1);
	public static AudioClip boo2 = new AudioClip(BOO2);
	public static AudioClip boo3 = new AudioClip(BOO3);
	public static AudioClip boo4 = new AudioClip(BOO4);
	
	//play the chosen audio clips
	public static void doPlaySound(int i){
		switch (i){
		case(1):
			draw.setCycleCount(1);
			draw.play();
			break;
		case(2):
			deal.setCycleCount(1);
		    deal.play();
		    break;
		case(3): 
			play.setCycleCount(1);
			play.play();
			break;
		case(4):
			AI_sound.setCycleCount(1);
			AI_sound.play();
			break;
		case(5):
			end.setCycleCount(1);
			end.play();
			break;
		default:
			smash.setCycleCount(1);
			smash.play();
		}
	}
	
	//AI will randomly make a laughing sound to distract opponents during the game (20% chance)
	//the method returns an integer for testing purpose only
	public static Integer randomLaugh(){
		Random rnd = new Random();
		int n = rnd.nextInt(5);
		if (n == 1){
			AI_evil.setCycleCount(1);
			AI_evil.play();
		
		}
		return n;
	}
	
	//one of the four different 'boo' sounds is made randomly when the player loses life
	public static void boo(){
		Random rnd = new Random();
		int n = rnd.nextInt(4);
		switch (n){
		case(0):
			boo1.setCycleCount(1);
			boo1.play();
			break;
		case(1):
			boo2.setCycleCount(1);
			boo2.play();
			break;
		case(2):	
			boo3.setCycleCount(1);
			boo3.play();
			break;
		default:
			boo4.setCycleCount(1);
			boo4.play();
		}
	}
	
	
	

}
