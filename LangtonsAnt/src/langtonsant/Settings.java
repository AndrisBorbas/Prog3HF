package langtonsant;

import java.io.Serializable;

public class Settings implements Serializable, Comparable<Settings> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161023995311445340L;
	
	private int id;
	private int scale, spacing, antmargin;

	public Settings(int scale, int spacing, int antmargin) {
		this.scale = scale;
		this.spacing = spacing;
		this.antmargin = antmargin;
		this.id = (scale * 100) + (spacing * 10) + antmargin;
	}

	public Settings(){
		
	}

	public int getID() {
		return id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public int getAntmargin() {
		return antmargin;
	}

	public void setAntmargin(int antmargin) {
		this.antmargin = antmargin;
	}

}
