/**
 * 
 */
package agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import laser.juliette.agent.ItemHandler;
import laser.juliette.agent.ItemHandlerFactory;
import laser.juliette.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class RefactoringAgentHandlerFactory implements ItemHandlerFactory {

	private RefactoringAgent ragent;

	public RefactoringAgentHandlerFactory(RefactoringAgent refactoringAgent) {
		// TODO Auto-generated constructor stub
		this.ragent = refactoringAgent;
	}

	@Override
	public ItemHandler createItemHandler(AgendaItem arg0) {
		InputStreamReader isr = new InputStreamReader(System.in);
		final BufferedReader br = new BufferedReader(isr);
		return new RefactoringAgentHandler(
				(laser.juliette.runner.ams.AgendaItem) arg0, ragent, br);
	}

}
