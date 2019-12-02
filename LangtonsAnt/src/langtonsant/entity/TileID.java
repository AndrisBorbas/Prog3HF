package langtonsant.entity;

/**
 * Handles updating the tiles inside the ants "brain"
 * @author AndrisBorbas
 *
 */
public class TileID {
	private int currentID, lastID, max;

	public TileID() {
		currentID = 0;
		lastID = 0;
		max = 2;
	}

	public TileID(int id) {
		setID(id);
	}

	public void setMax(int id) {
		this.max = id;
	}

	public int getID() {
		return currentID;
	}

	public int getlastID() {
		return lastID;
	}

	public void setID(int id) {
		this.currentID = id;
		if (id == 0) {
			this.lastID = max;
		} else {
			this.lastID = this.currentID - 1;
		}
	}

	public int getNextID() {
		if (currentID == max) {
			return 0;
		} else {
			return (currentID + 1);
		}
	}

	public void setlastID(int id) {
		this.lastID = id;
	}

	public void swapID() {
		lastID = currentID;
	}
}
