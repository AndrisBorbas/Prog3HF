package langtonsant;

import langtonsant.Game;

public class Main {

	public static void main(String[] args) {

		Game game = new Game("Langtons's Ant", 1000, 1000, 4, 1, 1, "LRRRL");

		game.start();

	}

}
