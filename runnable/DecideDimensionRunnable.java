/**
 * 
 */
package runnable;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;
import agent.RefactoringAgent;
import artifacts.Dimension;

/**
 * @author xiang
 * 
 */
public class DecideDimensionRunnable extends JulietteRunnable {

	public DecideDimensionRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#performStep()
	 */
	@Override
	protected void performStep() {
		// TODO Auto-generated method stub
		try {
			ArrayList<Dimension> dimensionlist = (ArrayList<Dimension>) item
					.getParameter("dimensionlist");
			Combo c = new Combo(ragent.getInstructions(), SWT.READ_ONLY
					| SWT.DROP_DOWN);
			ragent.setDimensionCombo(c);
			for (int j = 0; j < dimensionlist.size(); j++) {
				c.add(dimensionlist.get(0).toString());
			}
			ragent.getInstructions().layout();
			ragent.getInstructions().pack();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#setInstructions()
	 */
	@Override
	protected void setInstructions(String text) {
		super.setInstructions("Choose a dimension to extract: ");
	}

}
