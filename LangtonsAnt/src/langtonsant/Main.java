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
	 * Whether to get the Systems UI.
	 */
	public static boolean isDarkMode = false;

	/**
	 * Entry-point of the application.
	 * 
	 * @param args command-line arguments
	 * @throws IOException if the application has no permission to save files.
	 */
	public static void main(String[] args) throws IOException {

		Game game = new Game("Langtons's Ant", 900, 900, 1, 0, 1, "RL", false);

		game.start();

		return;

	}

}
