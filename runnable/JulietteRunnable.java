/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import agent.RefactoringAgent;
import artifacts.PackageFragmentRoot;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;
import listeners.ArtifactButtonSelectionListener;
import listeners.ComboSelectionListener;

/**
 * @author xiang
 * 
 */
public abstract class JulietteRunnable implements Runnable {
	protected AgendaItem item;
	protected RefactoringAgent ragent;
	protected HashMap<String, String> oldParams = new HashMap<String, String>();
	protected HashMap<String, String> currentParams = new HashMap<String, String>();
	final Color myColor = new Color(PlatformUI.getWorkbench().getDisplay(), 0,
			0, 255);

	public JulietteRunnable(AgendaItem item, RefactoringAgent ragent) {
		super();
		this.item = item;
		this.ragent = ragent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		saveParams();
		prepareUI();
		performStep();
		syncUI();
	}

	/**
	 * Set parameters
	 * 
	 * @param name
	 * @param value
	 */
	protected void setParams(String name, Serializable value) {
		try {
			item.setParameter(name, value);
			if (name.equals("agent")) {
			} else if (name.equals("packagefragmentroot")) {
				PackageFragmentRoot pfr = (PackageFragmentRoot) value;
				HashMap<String, String> cucontents = pfr
						.getCompilationUnitContents();
				currentParams.putAll(cucontents);
			} else if (value instanceof ArrayList) {
				String content = "";
				for (int j = 0; j < ((ArrayList) value).size(); j++) {
					content += ((ArrayList) value).get(j).toString() + "\n";
				}
				currentParams.put(name, content);
			} else {
				currentParams.put(name, value.toString());
			}
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (UnknownParameter e) {
			e.printStackTrace();
		}

	}

	/**
	 * Prepare UI for the specific step
	 */
	void prepareUI() {
		checkCurrentStep(ragent.getTree().getItems()[0]);
		ragent.getTree().layout();
		ragent.getTree().redraw();
		ragent.getTree().update();
		setInstructions("");
	}

	protected void setInstructions(String text) {
		ragent.getProgressbar().setText("");
		ragent.getProgressbar().setText(text);
		ragent.getInstructions().pack();
	}

	private boolean checkCurrentStep(TreeItem treeItem) {
		try {
			for (TreeItem c : treeItem.getItems()) {
				if (c.getText().equals(item.getStep().getName())) {
					// if (c.getData() == item.getStep()) {
					if (c.getForeground().equals(myColor))
						continue;
					c.setForeground(myColor);
					Stack<TreeItem> treeitems = new Stack<TreeItem>();
					while (c.getParentItem() != null) {
						treeitems.push(c.getParentItem());
						c = c.getParentItem();
					}
					while (!treeitems.isEmpty()) {
						TreeItem currentTreeItem = treeitems.pop();
						currentTreeItem.setExpanded(true);
						boolean complete = true;
						if (currentTreeItem.getText().startsWith("[")) {
							for (TreeItem t : currentTreeItem.getItems()) {
								complete = complete
										|| t.getForeground().equals(myColor);
							}
						} else {
							for (TreeItem t : currentTreeItem.getItems()) {
								complete = complete
										&& t.getForeground().equals(myColor);
							}
						}
						if (complete)
							currentTreeItem.setForeground(myColor);
					}
					return true;
				}
				if (checkCurrentStep(c))
					return true;
			}
			return false;
		} catch (AMSException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void syncUI() {
		// Reset the text
		ragent.getOldText().setText("");
		ragent.getNewText().setText("");
		ComboSelectionListener.getListener().setParameters(oldParams,
				currentParams);
		ragent.getComboDropDown().select(0);
		ragent.getComboDropDown().select(1);

		// Set<String> oset = oldParams.keySet();
		// Set<String> cset = currentParams.keySet();
		// Set<String> removed = new HashSet<String>(oset);
		// removed.removeAll(cset);
		// Set<String> added = new HashSet<String>(cset);
		// added.removeAll(oset);
		// Set<String> changed = new HashSet<String>(oset);
		// changed.retainAll(cset);

		// GridData artifactbuttongriddata = new GridData(SWT.FILL, SWT.FILL,
		// true, true);

		// // removed
		// for (String s : removed) {
		// Button b = new Button(artifactselector, SWT.RADIO);
		// b.setText(s);
		// b.addSelectionListener(new ArtifactButtonSelectionListener(
		// oldParams.get(s), "", ragent));
		// b.setLayoutData(artifactbuttongriddata);
		// }
		//
		// // added
		// for (String s : added) {
		// Button b = new Button(artifactselector, SWT.RADIO);
		// b.setText(s);
		// b.addSelectionListener(new ArtifactButtonSelectionListener("",
		// currentParams.get(s), ragent));
		// b.setLayoutData(artifactbuttongriddata);
		// }
		//
		// // changed
		// for (String s : changed) {
		// if (!currentParams.get(s).equals(oldParams.get(s))) {
		// Button b = new Button(artifactselector, SWT.RADIO);
		// b.setText(s);
		// b.addSelectionListener(new ArtifactButtonSelectionListener(
		// oldParams.get(s), currentParams.get(s), ragent));
		// b.setLayoutData(artifactbuttongriddata);
		// }
		// }
		// ragent.getArtifactselector().layout();
	}

	/**
	 * Save the artifacts
	 */
	protected void saveParams() {
		try {
			for (String s : item.parameterNames()) {
				if (s.equals("agent"))
					continue;
				Serializable val;
				// if
				// (item.getStep().getName().equals("Create Getter and Setter"))
				// {
				val = item.getParameterFromDDG(s);
				// } else {
				// val = item.getParameter(s);
				// }
				if (val instanceof ArrayList) {
					String content = "";
					for (int j = 0; j < ((ArrayList) val).size(); j++) {
						content += ((ArrayList) val).get(j).toString() + "\n";
					}
					val = content;
				}
				if (s.equals("packagefragmentroot")) {
					PackageFragmentRoot pfr = (PackageFragmentRoot) val;
					HashMap<String, String> cucontents = pfr
							.getCompilationUnitContents();
					oldParams.putAll(cucontents);
					continue;
				}
				oldParams.put(s, val.toString());
			}
		} catch (AMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Where says how the step is actually performed
	 */
	protected abstract void performStep();

	void adjustAfterUI() {

	}
}
