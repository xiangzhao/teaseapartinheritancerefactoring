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
public class ArtifactButtonSelectionListener extends SelectionAdapter {
	String oldvalue;
	String currentvalue;
	RefactoringAgent ragent;

	private static ArtifactButtonSelectionListener listener;

	public static ArtifactButtonSelectionListener getArtifactButtonSelectionListener() {
		if (listener == null)
			listener = new ArtifactButtonSelectionListener();
		return listener;
	}

	public ArtifactButtonSelectionListener(String oldvalue,
			String currentvalue, RefactoringAgent ragent) {
		super();
		this.oldvalue = oldvalue;
		this.currentvalue = currentvalue;
		this.ragent = ragent;
	}

	public ArtifactButtonSelectionListener() {
		super();
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
		ragent.getOldText().setEditable(true);
		ragent.getNewText().setEditable(true);
		ragent.getOldText().setText(oldvalue);
		ragent.getNewText().setText(currentvalue);
		if (oldvalue.equals(""))
			ragent.getOldText().setEditable(false);
		if (currentvalue.equals(""))
			ragent.getNewText().setEditable(false);
		super.widgetSelected(e);
	}

}
