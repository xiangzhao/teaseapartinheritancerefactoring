/**
 * 
 */
package runnable;

import java.util.ArrayList;

import agent.RefactoringAgent;
import artifacts.Dimension;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class IdentifyDimensionsRunnable extends JulietteRunnable {

	public IdentifyDimensionsRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		Dimension d1 = new Dimension();
		d1.setDimensionDescription("Source");
		Dimension d2 = new Dimension();
		d2.setDimensionDescription("Type");
		ArrayList<Dimension> dimensionlist = new ArrayList<Dimension>();
		dimensionlist.add(d1);
		dimensionlist.add(d2);
		setParams("dimensionlist", dimensionlist);
		try {
			System.err.println("Identify Dimension:" + item.getParent());
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// item.setParameter("dimensionlist", dimensionlist);

	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#setInstructions()
	 */
	@Override
	protected void setInstructions(String text) {
		// TODO Auto-generated method stub
		super.setInstructions("Different aspects of the class hierarchy will be identified");
	}

}
