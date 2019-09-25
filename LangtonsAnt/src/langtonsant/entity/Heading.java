package langtonsant.entity;

public class Heading {

	public int heading;

	public Heading() {
		heading = 90;
	}

	public Heading(String h) {
		convert(h);
	}

	public void convert(String h) {
		if (h.compareToIgnoreCase("UP") == 0)
			heading = 0;
		if (h.compareToIgnoreCase("RIGHT") == 0)
			heading = 90;
		if (h.compareToIgnoreCase("DOWN") == 0)
			heading = 180;
		if (h.compareToIgnoreCase("LEFT") == 0)
			heading = 270;
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
