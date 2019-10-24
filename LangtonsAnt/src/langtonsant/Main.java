package langtonsant;

import java.io.IOException;

import langtonsant.game.Game;

public class Main {

	/**
	 * Entry-point of the application.
	 * 
	 * @param args command-line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Game game = new Game("Langtons's Ant", 1000, 1000, 1, 0, 1, "RRLLLRLLLRRR");

		game.start();

	}

}
