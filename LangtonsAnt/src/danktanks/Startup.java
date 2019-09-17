package danktanks;

import danktanks.Display;
import danktanks.Game;
import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;

public class Startup {

	public static void main(String[] args) {

		Game game = new Game("Dank Tanks", 400, 400);

		game.start();

	}

}
