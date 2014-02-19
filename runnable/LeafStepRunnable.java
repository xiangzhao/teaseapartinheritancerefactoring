/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.Map;

import laser.ddg.ProvenanceData;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;
import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class LeafStepRunnable extends JulietteRunnable {

	public LeafStepRunnable(AgendaItem item, RefactoringAgent ragent) {
		// TODO Auto-generated constructor stub
		super(item, ragent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#performStep()
	 */
	@Override
	protected void performStep() {
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

	}

}
