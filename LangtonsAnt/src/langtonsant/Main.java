package langtonsant;

import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;

import langtonsant.Display;
import langtonsant.Game;

public class Main {

	public static void main(String[] args) {

		Game game = new Game("Langtons's Ant", 600, 600);

		game.start();

	}

}
