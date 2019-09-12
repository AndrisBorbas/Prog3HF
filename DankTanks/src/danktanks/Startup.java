package danktanks;

import danktanks.Display;
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;

public class Startup {

	public static void main(String[] args) {
		
		Game game = new Game("Dank Tanks", 400, 400);
		
		game.start();

		/*
		 * JFrame frame = new JFrame("Dank Tanks"); boolean running = true;
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); Map canvas = new Map();
		 * canvas.setSize(400,400); frame.add(canvas); frame.pack();
		 * frame.setVisible(true);
		 */
	}

	/*
	 * public void run() { while (running) {
	 * 
	 * } }
	 */

}
