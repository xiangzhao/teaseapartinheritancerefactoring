/**
 * 
 */
package artifacts;

import java.io.Serializable;

/**
 * @author xiang
 * 
 */
public class Dimension implements Serializable {

	private String dimensionDescription;

	public Dimension(String dimensionDescription) {
		super();
		this.dimensionDescription = dimensionDescription;
	}

	public Dimension() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -808407891968874219L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return dimensionDescription;
	}

	/**
	 * @return the dimensionDescription
	 */
	public String getDimensionDescription() {
		return dimensionDescription;
	}

	/**
	 * @param dimensionDescription
	 *            the dimensionDescription to set
	 */
	public void setDimensionDescription(String dimensionDescription) {
		this.dimensionDescription = dimensionDescription;
	}

}
