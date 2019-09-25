package langtonsant;

import langtonsant.Game;

public class Main {

	public static void main(String[] args) {

		Game game = new Game("Langtons's Ant", 900, 900, 3, 1, 1, "RL");

		game.start();

	}

}
