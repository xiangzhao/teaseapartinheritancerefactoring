/**
 * 
 */
package runnable;

import agent.RefactoringAgent;
import exceptions.DimensionException;
import util.PluginUtil;
import laser.ddg.ProcedureInstanceNode;
import laser.ddg.ProvenanceData;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class DimensionExceptionRunnable extends JulietteRunnable {

	ProvenanceData pd;

	public DimensionExceptionRunnable(AgendaItem item, RefactoringAgent ragent) {
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
		PluginUtil.terminate(item, new DimensionException());
	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

}
