package langtonsant.entity;

import java.awt.Color;

/**
 * 
 * @author AndrisBorbas
 *
 */
public class Ant {

	/**
	 * The current and last location of the ant
	 */
	private int xpos, ypos, xlastpos, ylastpos;
	/**
	 * Where the ant is facing
	 */
	private Heading heading = new Heading();
	/**
	 * The id of the tile the ant is above
	 */
	private TileID tileID = new TileID();
	/**
	 * The instructionset converted to 90 degree turns
	 */
	private int[] turn;

	/**
	 * The settings of the ant
	 */
	private int scale, spacing, antmargin;

	/**
	 * The default and only constructor
	 * 
	 * @param x
	 * @param y
	 * @param scale
	 * @param spacing
	 * @param antmargin
	 * @param instructionset
	 */
	public Ant(int x, int y, int scale, int spacing, int antmargin, String instructionset) {
		this.xpos = this.xlastpos = x;
		this.ypos = this.ylastpos = y;
		this.scale = scale;
		this.spacing = spacing;
		this.antmargin = antmargin;

		this.heading.set("UP");
		tileID.setID(0);

		turn = convertToTurns(instructionset);

		tileID.setlastID(instructionset.length() - 1);
		tileID.setMax(instructionset.length() - 1);
	}

	/**
	 * Draw the ant and recolor its last location in the memory
	 * 
	 * @param mem
	 * @param width
	 * @param height
	 * @param colors
	 * @return
	 */
	public int[] drawAnt(int[] mem, int width, int height, Color[] colors) {
		for (int y = (ylastpos - scale); y < (ylastpos + scale - spacing); y++) {
			for (int x = (xlastpos - scale); x < (xlastpos + scale - spacing); x++) {
				mem[x + y * width] = colors[tileID.getNextID()].getRGB();
			}
		}
		for (int y = (ypos - scale + antmargin); y < (ypos + scale - spacing - antmargin); y++) {
			for (int x = (xpos - scale + antmargin); x < (xpos + scale - spacing - antmargin); x++) {
				if (x < 0 || x >= width || y < 0 || y >= height) {
					throw new ArrayIndexOutOfBoundsException("Ant Draw OOB");
				}
				mem[x + y * width] = Color.white.getRGB();
			}
		}
		return mem;
	}

	/**
	 * Update the ants position based on the tile under it
	 * 
	 * @param mem
	 * @param width
	 * @param height
	 * @param colors
	 * @return
	 */
	public int[] updateAnt(int[] mem, int width, int height, Color[] colors) {
		int xcheck = (xpos - scale - 1 + antmargin);
		int ycheck = (ypos - scale - 1 + antmargin);

		if (xcheck < 0 || xcheck > width || ycheck < 0 || ycheck > height) {
			throw new ArrayIndexOutOfBoundsException("Ant Update OOB");
		}
		boolean isNew = true;
		for (int i = 0; i < colors.length; i++) {
			if (mem[xcheck + ycheck * width] == colors[i].getRGB()) {
				// System.out.println(colors[i]);
				tileID.setID(i);
				heading.turn(turn[tileID.getID()]);
				isNew = false;
				break;
			}
		}
		if (isNew) {
			tileID.setID(0);
			heading.turn(turn[tileID.getID()]);
		}

		ylastpos = ypos;
		xlastpos = xpos;

		switch (heading.getHeading()) {
		case "UP":
			ypos = ypos - scale * 2;
			break;
		case "DOWN":
			ypos = ypos + scale * 2;
			break;
		case "RIGHT":
			xpos = xpos + scale * 2;
			break;
		case "LEFT":
			xpos = xpos - scale * 2;
			break;
		default:
			break;
		}

		return mem;
	}

	/**
	 * Convert the instructionset into 90 degree turns
	 */
	protected int[] convertToTurns(String instructionset) {
		int[] turn = new int[12];
		for (int i = 0; i < instructionset.length(); i++) {
			switch (instructionset.charAt(i)) {
			case 'R':
			case 'r':
				turn[i] = 90;
				break;
			case 'L':
			case 'l':
				turn[i] = -90;
				break;
			case 'N':
			case 'n':
				turn[i] = 0;
				break;
			case 'U':
			case 'u':
				turn[i] = 180;
				break;
			}
		}
		return turn;
	}
}
