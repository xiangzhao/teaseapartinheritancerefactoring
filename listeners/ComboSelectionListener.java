/**
 * 
 */
package listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class ComboSelectionListener implements Listener {

	private static ComboSelectionListener listener;

	private Combo combo;

	private Composite artifactselector;

	private HashMap<String, String> oldParams;
	private HashMap<String, String> currentParams;
	private Set<String> added;
	private Set<String> removed;
	private Set<String> changed;

	private RefactoringAgent ragent;

	public void setParameters(HashMap<String, String> oldParams,
			HashMap<String, String> currentParams) {
		this.oldParams = oldParams;
		this.currentParams = currentParams;
		Set<String> oset = oldParams.keySet();
		Set<String> cset = currentParams.keySet();
		removed = new HashSet<String>(oset);
		removed.removeAll(cset);
		added = new HashSet<String>(cset);
		added.removeAll(oset);
		changed = new HashSet<String>(oset);
		changed.retainAll(cset);
	}

	public static ComboSelectionListener getListener() {
		if (listener == null) {
			listener = new ComboSelectionListener();
		}
		return listener;
	}

	private ComboSelectionListener() {
	}

	/**
	 * @return the combo
	 */
	public Combo getCombo() {
		return combo;
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	/**
	 * @return the artifactselector
	 */
	public Composite getArtifactselector() {
		return artifactselector;
	}

	/**
	 * @param artifactselector
	 *            the artifactselector to set
	 */
	public void setArtifactselector(Composite artifactselector) {
		this.artifactselector = artifactselector;
	}

	public void setRagent(RefactoringAgent ragent) {
		this.ragent = ragent;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		for (Control c : artifactselector.getChildren()) {
			c.dispose();
		}
		GridData artifactbuttongriddata = new GridData(SWT.FILL, SWT.FILL,
				true, true);
		if (combo != null) {
			if (combo.getText().equalsIgnoreCase("input")) {
				for (String s : removed) {
					Button b = new Button(artifactselector, SWT.RADIO);
					b.setText(s);
					b.addSelectionListener(new ArtifactButtonSelectionListener(
							oldParams.get(s), "", ragent));
					b.setLayoutData(artifactbuttongriddata);
				}
			} else if (combo.getText().equalsIgnoreCase("output")) {
				// added
				for (String s : added) {
					Button b = new Button(artifactselector, SWT.RADIO);
					b.setText(s);
					b.addSelectionListener(new ArtifactButtonSelectionListener(
							"", currentParams.get(s), ragent));
					b.setLayoutData(artifactbuttongriddata);
				}

				// changed
				for (String s : changed) {
					if (!currentParams.get(s).equals(oldParams.get(s))) {
						Button b = new Button(artifactselector, SWT.RADIO);
						b.setText(s);
						b.addSelectionListener(new ArtifactButtonSelectionListener(
								oldParams.get(s), currentParams.get(s), ragent));
						b.setLayoutData(artifactbuttongriddata);
					}
				}

			} else {

			}
			artifactselector.layout();
		}

	}

}
