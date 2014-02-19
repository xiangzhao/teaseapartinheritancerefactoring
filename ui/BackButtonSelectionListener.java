/**
 * 
 */
package ui;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.AgendaItem;
import laser.juliette.ams.AgendaManager;
import laser.juliette.ams.IllegalTransition;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import exceptions.RestartException;

import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class BackButtonSelectionListener extends SelectionAdapter {

	private RefactoringAgent ragent;

	public BackButtonSelectionListener(RefactoringAgent ragent) {
		// TODO Auto-generated constructor stub
		super();
		this.ragent = ragent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		try {
			Iterator<AgendaItem> iterator = AgendaManager.getAgendaFactory()
					.getAgenda("auto", null).items().iterator();
			Stack<AgendaItem> agstack = new Stack<AgendaItem>();
			while (iterator.hasNext()) {
				agstack.push(iterator.next());
			}

			agstack.pop();
			AgendaItem current = agstack.pop();
			AgendaItem result = null;
			while (!agstack.isEmpty()) {
				result = agstack.pop();
				if (result.getStep().getName().equals("Create Field")
						&& result.getState().equals(AgendaItem.COMPLETED))
					break;
			}
			ragent.setUndo(result);
			if (current != null) {
				HashSet<Serializable> ex = new HashSet<Serializable>();
				ex.add(new RestartException());
				current.terminate(ex);
			}
		} catch (AMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalTransition e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		synchronized (ragent) {
			ragent.notifyAll();
		}
		super.widgetSelected(e);
	}

}
