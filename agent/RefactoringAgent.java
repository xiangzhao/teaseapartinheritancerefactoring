/**
 * 
 */
package agent;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;

import ui.RefactoringHelperUIRunnable;

import artifacts.Dimension;
import laser.ddg.ProcedureInstanceNode;
import laser.ddg.ProvenanceData;
import laser.juliette.agent.AbstractAgent;
import laser.juliette.ams.AgendaItem;
import laser.littlejil.smartchecklist.gui.ProcessEventManager;
import laser.littlejil.smartchecklist.gui.ProcessPanel;
import laser.littlejil.smartchecklist.gui.SmartChecklistGUI;

/**
 * @author xiang
 * 
 */
public class RefactoringAgent extends ProcessEventManager {

	public RefactoringAgent(ProcessPanel gui) {
		super(gui);
		// TODO Auto-generated constructor stub
	}

	public RefactoringAgent() {
		super();

	}

	public ProcessPanel processPanel_;

	private Dimension retractDimension;

	private ProcedureInstanceNode revertiblePoint;

	private boolean completing = false;

	private Label before;

	private Label current;

	private Label next;

	private Composite artifactselector;

	private ProvenanceData pd;

	private Label progressbar;

	Tree tree;

	private StyledText oldText;
	private StyledText newText;

	private Composite right;

	private Combo comboDropDown;

	private Composite instructions;

	private Combo dimensioncombo;

	private AgendaItem undoagendaitem;

	public void setUndo(AgendaItem undoagendaitem) {
		this.undoagendaitem = undoagendaitem;
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree
	 *            the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * @return the before
	 */
	public Label getBefore() {
		return before;
	}

	public void setBefore(Label before) {
		this.before = before;
	}

	/**
	 * @return the next
	 */
	public Label getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(Label next) {
		this.next = next;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(Label current) {
		this.current = current;
	}

	/**
	 * @return the current
	 */
	public Label getCurrent() {
		return current;
	}

	/**
	 * @return the completing
	 */
	public boolean isCompleting() {
		return completing;
	}

	/**
	 * @param completing
	 *            the completing to set
	 */
	public synchronized void setCompleting(boolean completing) {
		this.completing = completing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see laser.juliette.agent.AbstractAgent#configureAgent()
	 */
	@Override
	protected void configureAgent() {
		// TODO Auto-generated method stub
		PlatformUI.getWorkbench().getDisplay()
				.syncExec(new RefactoringHelperUIRunnable(this));
		setItemHandlerFactory(new RefactoringAgentHandlerFactory(this));
	}

	/**
	 * @return the retractDimension
	 */
	public Dimension getRetractDimension() {
		return retractDimension;
	}

	/**
	 * @param retractDimension
	 *            the retractDimension to set
	 */
	public void setRetractDimension(Dimension retractDimension) {
		this.retractDimension = retractDimension;
	}

	/**
	 * @return the revertiblePoint
	 */
	public ProcedureInstanceNode getRevertiblePoint() {
		return revertiblePoint;
	}

	/**
	 * @param revertiblePoint
	 *            the revertiblePoint to set
	 */
	public void setRevertiblePoint(ProcedureInstanceNode revertiblePoint) {
		this.revertiblePoint = revertiblePoint;
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

	/**
	 * @return the oldText
	 */
	public StyledText getOldText() {
		return oldText;
	}

	/**
	 * @param oldText
	 *            the oldText to set
	 */
	public void setOldText(StyledText oldText) {
		this.oldText = oldText;
	}

	/**
	 * @return the newText
	 */
	public StyledText getNewText() {
		return newText;
	}

	/**
	 * @param newText
	 *            the newText to set
	 */
	public void setNewText(StyledText newText) {
		this.newText = newText;
	}

	public void setRight(Composite right) {
		// TODO Auto-generated method stub
		this.right = right;

	}

	/**
	 * @return the pd
	 */
	public ProvenanceData getPd() {
		return pd;
	}

	/**
	 * @param pd
	 *            the pd to set
	 */
	public void setPd(ProvenanceData pd) {
		this.pd = pd;
	}

	/**
	 * @return the progressbar
	 */
	public Label getProgressbar() {
		return progressbar;
	}

	/**
	 * @param progressbar
	 *            the progressbar to set
	 */
	public void setProgressbar(Label progressbar) {
		this.progressbar = progressbar;
	}

	/**
	 * @return the comboDropDown
	 */
	public Combo getComboDropDown() {
		return comboDropDown;
	}

	/**
	 * @param comboDropDown
	 *            the comboDropDown to set
	 */
	public void setComboDropDown(Combo comboDropDown) {
		this.comboDropDown = comboDropDown;
	}

	public void setInstructions(Composite instructions) {
		// TODO Auto-generated method stub
		this.instructions = instructions;

	}

	public Composite getInstructions() {
		return instructions;
	}

	public void setDimensionCombo(Combo dimensioncombo) {
		// TODO Auto-generated method stub
		this.dimensioncombo = dimensioncombo;
	}

	public Combo getDimensionCombo() {
		return dimensioncombo;
	}

	public laser.juliette.ams.AgendaItem getUndo() {
		// TODO Auto-generated method stub
		return undoagendaitem;
	}

}
