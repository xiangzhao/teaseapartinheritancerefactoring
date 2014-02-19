/**
 * 
 */
package artifacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xiang
 * 
 */
public class PackageFragmentRoot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1736239351581656674L;

	private ArrayList<String> compilationUnitList = new ArrayList<String>();

	private HashMap<String, String> compilationUnitContents = new HashMap<String, String>();

	/**
	 * @return the compilationUnitList
	 */
	public ArrayList<String> getCompilationUnitList() {
		return compilationUnitList;
	}

	/**
	 * @param compilationUnitList
	 *            the compilationUnitList to set
	 */
	public void setCompilationUnitList(ArrayList<String> compilationUnitList) {
		this.compilationUnitList = compilationUnitList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (String compilationUnit : compilationUnitList) {
			result.append(compilationUnit + "\n");
		}

		return result.subSequence(0, result.length() - 1).toString();
	}

	/**
	 * @return the compilationUnitContents
	 */
	public HashMap<String, String> getCompilationUnitContents() {
		return compilationUnitContents;
	}

	/**
	 * @param compilationUnitContents
	 *            the compilationUnitContents to set
	 */
	public void setCompilationUnitContents(
			HashMap<String, String> compilationUnitContents) {
		this.compilationUnitContents = compilationUnitContents;
	}
}
