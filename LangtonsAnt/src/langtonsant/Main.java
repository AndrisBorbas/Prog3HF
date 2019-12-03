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
	public static boolean isDarkMode = true;

	/**
	 * Entry-point of the application.
	 * 
	 * @param args command-line arguments
	 * @throws IOException if the application has no permission to save files.
	 */
	public static void main(String[] args) throws IOException {

		int res = 900;

		if (args.length != 0) {
			int temp = Integer.parseInt(args[0]);
			if (temp >= 400 && temp <= 2100) {
				res = temp;
			}
		}

		Game game = new Game("Langtons's Ant", res, res, 1, 0, 1, "RL", false);

		game.start();

		return;

	}

}
