/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.PartInitException;

import agent.RefactoringAgent;
import astvisitors.RemoveFieldVisitor;

import laser.ddg.ProvenanceData;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;
import util.PluginUtil;

/**
 * @author xiang
 * 
 */
public class RemoveFieldRunnable extends JulietteRunnable {

	public RemoveFieldRunnable(AgendaItem item, RefactoringAgent ragent) {
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
		try {
			String fieldname = (String) item.getParameterFromDDG("fieldname");
			ICompilationUnit icu = PluginUtil.getICompilationUnit(null, null,
					null, "NewsFeedItem", true);
			PluginUtil
					.performASTRewrite(icu, new RemoveFieldVisitor(fieldname));
			icu.commitWorkingCopy(false, null);
			setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
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
		super.setInstructions("The field will be removed from the original class hierarcy");
	}

}
