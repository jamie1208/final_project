import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.martio.game.Game;

public class final_game {
	
	private static Random random;
	private static double playerCp = 300; //cp需設double, 每次都取round,呈現時不顯示小數位
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		random = new Random();
		double enemyCp = Math.round(1+random.nextDouble(playerCp));
		System.out.println("enemyCp = "+enemyCp);
		Game game1 = new Game(enemyCp,playerCp);
	}
}
