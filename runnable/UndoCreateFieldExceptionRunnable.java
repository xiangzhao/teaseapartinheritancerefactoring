/**
 * 
 */
package runnable;

import util.PluginUtil;
import exceptions.UndoCreateFieldException;

import laser.ddg.ProcedureInstanceNode;
import laser.ddg.ProvenanceData;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;
import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class UndoCreateFieldExceptionRunnable extends JulietteRunnable {
	private ProvenanceData pd;

	public UndoCreateFieldExceptionRunnable(AgendaItem item,
			RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		if (item.getDdgbuilder() instanceof DDGBuilder) {
			DDGBuilder d = (DDGBuilder) item.getDdgbuilder();
			pd = d.getProvData();
		}
		ProcedureInstanceNode pinNodeRevertible = pd.drawRevertibleGraph();
		ragent.setRevertiblePoint(pinNodeRevertible);
		PluginUtil.terminate(item, new UndoCreateFieldException());
	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

}
