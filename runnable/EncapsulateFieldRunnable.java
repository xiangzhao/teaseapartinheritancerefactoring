/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.ITextEditor;

import agent.RefactoringAgent;

import laser.ddg.ProvenanceData;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;
import util.PluginUtil;

/**
 * @author xiang
 * 
 */
public class EncapsulateFieldRunnable extends JulietteRunnable {

	public EncapsulateFieldRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		// TODO Auto-generated method stub
		if (ragent.getUndo() != null) {
			DDGBuilder ddgBuilder = (laser.juliette.ddgbuilder.DDGBuilder) (ragent
					.getUndo().getDdgbuilder());
			ProvenanceData p = ddgBuilder.getProvData();
			Map<String, Serializable> revertTo = p.revertTo(DDGBuilder
					.getAgendaItemMapper().getPINS(ragent.getUndo()).next());
			String param = revertTo.keySet().iterator().next();
			System.out.println(param + ": " + revertTo.get(param));
			ragent.setUndo(null);
		}
		String fieldname = "author";
		ITextEditor activeEditor = PluginUtil.getActiveEditor();
		ITextSelection selection = (ITextSelection) activeEditor
				.getSelectionProvider().getSelection();
		// item.setParameter("fieldname", fieldname);
		setParams("fieldname", fieldname);
		setParams("fieldtype", "String");
		setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
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
		super.setInstructions("Select the field to be move to the new class");
	}

}
