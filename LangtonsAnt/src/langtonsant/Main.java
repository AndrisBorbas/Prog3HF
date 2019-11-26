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

		Game game = new Game("Langtons's Ant", 900, 900, 4, 0, 4, "RRLLLRLLLRRR");

		game.start();
		
		try {
			System.out.println("1");
			game.thread.join();
			System.out.println("2");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("wa");
		
		return;

	}

}
