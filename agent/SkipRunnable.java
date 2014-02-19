/**
 * 
 */
package agent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;

import exceptions.SkipException;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.IllegalTransition;
import laser.juliette.runner.ams.AgendaItem;
import runnable.JulietteRunnable;

/**
 * @author xiang
 * 
 */
public class SkipRunnable extends JulietteRunnable {

	public SkipRunnable(AgendaItem item, RefactoringAgent ragent) {
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
			if (ragent.getUndo() != null
					&& !ragent.getUndo().getStep().getName()
							.equals(item.getParent().getStep().getName())) {
				item.terminate(new HashSet<Serializable>(Collections
						.singleton(new SkipException())));
			}
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalTransition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
