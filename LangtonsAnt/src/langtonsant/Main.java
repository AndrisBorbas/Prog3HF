package langtonsant;

import langtonsant.game.Game;

public class Main {

	public static void main(String[] args) {

		Game game = new Game("Langtons's Ant", 1000, 1000, 3, 1, 1, "LLRR");

		game.start();

	}

}
