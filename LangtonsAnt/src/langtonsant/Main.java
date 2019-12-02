package langtonsant;

import java.io.IOException;

import langtonsant.game.Game;

/**
 * 
 * @author AndrisBorbas
 *
 */
public class Main {

	/**
	 * Entry-point of the application.
	 * 
	 * @param args command-line arguments
	 * @throws IOException if the application has no permission to save files.
	 */
	public static void main(String[] args) throws IOException {

		Game game = new Game("Langtons's Ant", 900, 900, 3, 1, 1, "RLLR");

		game.start();

		return;

	}

}
