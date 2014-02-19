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
import astvisitors.UpdateReferenceVisitor;
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
public class UpdateReferencesRunnable extends JulietteRunnable {

	public UpdateReferencesRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
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
			String newclassname = (String) item
					.getParameterFromDDG("newclassname");
			String fieldname = (String) item.getParameterFromDDG("fieldname");
			for (ICompilationUnit icu : PluginUtil.getAllICompilationUnit(null,
					null, null)) {
				if (!icu.getTypes()[0].getElementName().equals(newclassname)) {
					UpdateReferenceVisitor urv = new UpdateReferenceVisitor(
							newclassname.toLowerCase(), fieldname);
					PluginUtil.performASTRewrite(icu, urv);
					Thread.sleep(300);
				}
				icu.commitWorkingCopy(false, null);
			}
			setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	void adjustAfterUI() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#setInstructions()
	 */
	@Override
	protected void setInstructions(String text) {
		super.setInstructions("Reference to the field will be fixed in the original class hierarchy");
	}

}
