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

		Game game = new Game("Langtons's Ant", 900, 900, 4, 0, 2, "RRLLLRLLLRRR");
		
		game.start();
		
		/*
		Thread gameThread = new Thread(game);
		gameThread.setName("Game");
		gameThread.start();
		*/
		/*
		try {
			Thread.sleep(0);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/

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
