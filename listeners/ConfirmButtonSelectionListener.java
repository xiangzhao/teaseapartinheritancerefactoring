/**
 * 
 */
package listeners;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class ConfirmButtonSelectionListener extends SelectionAdapter {

	private RefactoringAgent ragent;

	public ConfirmButtonSelectionListener(RefactoringAgent ragent) {
		super();
		this.ragent = ragent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		synchronized (ragent) {
			ragent.notifyAll();
		}
		super.widgetSelected(e);
	}

}
