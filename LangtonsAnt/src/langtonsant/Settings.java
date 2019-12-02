package langtonsant;

import java.io.Serializable;

/**
 * Stores the settings of the ant
 * 
 * @author AndrisBorbas
 *
 */
public class Settings implements Serializable, Comparable<Settings> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1950403861603766221L;
	/**
	 * The generated id
	 */
	private int id;
	/**
	 * The settings to store
	 */
	private int scale, spacing, antmargin;

	public Settings(int scale, int spacing, int antmargin) {
		this.scale = scale;
		this.spacing = spacing;
		this.antmargin = antmargin;
		this.id = (scale * 100) + (spacing * 10) + antmargin;
	}

	public Settings() {

	}

	@Override
	public String toString() {
		return super.toString() + ": [id=" + id + ", scale=" + scale + ", spacing=" + spacing + ", antmargin="
				+ antmargin + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Settings)) {
			return false;
		}
		Settings other = (Settings) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Settings o) {
		if (id == o.id) {
			return 0;
		}
		if (id < o.getID()) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the antmargin
	 */
	public int getAntmargin() {
		return antmargin;
	}

	/**
	 * @param antmargin the antmargin to set
	 */
	public void setAntmargin(int antmargin) {
		this.antmargin = antmargin;
	}

}
