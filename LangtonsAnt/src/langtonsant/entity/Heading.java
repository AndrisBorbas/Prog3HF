package langtonsant.entity;

/**
 * Stores where the ant is facing
 * 
 * @author AndrisBorbas
 *
 */
public class Heading {

	public int heading;

	public Heading() {
		heading = 90;
	}

	public Heading(String h) {
		convert(h);
	}

	public void convert(String h) {
		if (h.compareToIgnoreCase("UP") == 0) {
			heading = 0;
			return;
		}
		if (h.compareToIgnoreCase("RIGHT") == 0) {
			heading = 90;
			return;
		}
		if (h.compareToIgnoreCase("DOWN") == 0) {
			heading = 180;
			return;
		}
		if (h.compareToIgnoreCase("LEFT") == 0) {
			heading = 270;
			return;
		}
		throw new RuntimeException(toString() + "Direction corrupted");
	}

	public void set(String h) {
		convert(h);
	}

	public void turn(int turn) {
		heading += turn;
		if (heading > 270)
			heading -= 360;
		if (heading < 0)
			heading += 360;
	}

	public String getHeading() {
		switch (heading) {
		case 0:
			return "UP";
		case 90:
			return "RIGHT";
		case 180:
			return "DOWN";
		case 270:
			return "LEFT";
		default:
			throw new RuntimeException(toString() + "Direction corrupted");
		}
	}
}
