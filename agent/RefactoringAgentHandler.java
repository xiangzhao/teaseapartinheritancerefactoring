/**
 * 
 */
package agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ui.PlatformUI;

import runnable.CreateClassRunnable;
import runnable.CreateFieldRunnable;
import runnable.DecideDimensionRunnable;
import runnable.DimensionExceptionRunnable;
import runnable.EncapsulateFieldRunnable;
import runnable.IdentifyDimensionsRunnable;
import runnable.LeafStepRunnable;
import runnable.LoadSourceFilesRunnable;
import runnable.MakeLinkRunnable;
import runnable.RemoveFieldRunnable;
import runnable.UndoCreateFieldExceptionRunnable;
import runnable.UndoRunnable;
import runnable.UpdateReferencesRunnable;

import artifacts.Dimension;

import laser.juliette.runner.ams.AgendaItem;

import laser.juliette.agent.ItemHandlerAdapter;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.IllegalTransition;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.littlejil.smartchecklist.gui.ProcessEventHandler;

/**
 * @author xiang
 * 
 */
public class RefactoringAgentHandler extends ProcessEventHandler {

	// private final BufferedReader br;
	private RefactoringAgent ragent;
	protected AgendaItem item;

	public RefactoringAgentHandler(AgendaItem item, RefactoringAgent ragent,
			BufferedReader br) {
		super(ragent.processPanel_, item);
		this.ragent = ragent;
		// this.br = br;
		this.item = item;
		if (this.ragent.getPd() == null)
			this.ragent
					.setPd(((DDGBuilder) item.getDdgbuilder()).getProvData());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see laser.juliette.agent.ItemHandlerAdapter#posted()
	 */
	@Override
	public void posted() {
		try {
			if (item.getStep().getName()
					.equals("Decide Dimension to be Extracted")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new DecideDimensionRunnable(item, ragent));
				synchronized (ragent) {
					ragent.wait();
				}
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {
						String[] items = ragent.getDimensionCombo().getItems();
						for (String s : items) {
							if (!s.equals(ragent.getDimensionCombo().getText())) {
								ragent.setRetractDimension(new Dimension(s));
								break;
							}
						}
						ragent.getDimensionCombo().dispose();
						try {
							item.start();
						} catch (AMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalTransition e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
				// System.out
				// .println("Please choose one from the following dimensions:");
				// @SuppressWarnings("unchecked")
				// ArrayList<Dimension> dimensionlist = (ArrayList<Dimension>)
				// item
				// .getParameter("dimensionlist");
				// for (int j = 0; j < dimensionlist.size(); j++) {
				// System.out.println(j + ": " + dimensionlist.get(j));
				// }
				// String op = br.readLine();
				// for (int k = 0; k < dimensionlist.size(); k++) {
				// if (k != Integer.valueOf(op))
				// ragent.setRetractDimension(dimensionlist.get(k));
				// }
			} else if (ragent.getRetractDimension() != null
					&& item.getStep().getName().equals("Extract Dimension")
					&& ragent
							.getRetractDimension()
							.getDimensionDescription()
							.equals(((Dimension) item.getParameter("dimension"))
									.getDimensionDescription())) {
				System.out.println(((Dimension) item.getParameter("dimension"))
						.getDimensionDescription() + " retracted");
			} else {
				item.start();
			}
			super.posted();
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (IllegalTransition e) {
			e.printStackTrace();
		} catch (UnknownParameter e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see laser.juliette.agent.ItemHandlerAdapter#started()
	 */
	@Override
	public void started() {
		try {
			// System.err.println(item.getStep().getName() + " executing");
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					try {
						ragent.getCurrent().setText(item.getStep().getName());
						ragent.getCurrent().getParent().layout();
					} catch (AMSException e) {
						e.printStackTrace();
					}
				}
			});
			if (item.getStep().getName().equals("Identify Dimensions")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new IdentifyDimensionsRunnable(item, ragent));
			} else if (item.getStep().getName().equals("Load Source Files")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new LoadSourceFilesRunnable(item, ragent));
			} else if (item.getStep().getName().equals("Create a Class")) {
				// System.out.println("Press Enter to create a new class called :"
				// + ((Dimension) item.getParameter("dimension"))
				// .getDimensionDescription());
				// br.readLine();
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new CreateClassRunnable(item, ragent));
			} else if (item.getStep().getName().equals("Undo")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new UndoRunnable(item, ragent));
			} else if (item.getStep().getName()
					.equals("Make a Link between Two Classes")) {
				System.out
						.println("Press Enter to create a link between two classes, or undo to revert");
				// if (br.readLine().equals("undo")) {
				// PlatformUI
				// .getWorkbench()
				// .getDisplay()
				// .syncExec(
				// new DimensionExceptionRunnable(item, ragent));
				// } else {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new MakeLinkRunnable(item, ragent));
				// }
			} else if (item.getStep().getName().equals("Encapsualte Field")) {
				System.out
						.println("Please select the field you want to extract");
				// if (br.readLine().equals("undo")) {
				// PlatformUI
				// .getWorkbench()
				// .getDisplay()
				// .syncExec(
				// new DimensionExceptionRunnable(item, ragent));
				// } else {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new EncapsulateFieldRunnable(item, ragent));

				// }
			} else if (item.getStep().getName().equals("Create Field")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new CreateFieldRunnable(item, ragent));
			} else if (item.getStep().getName()
					.equals("Remove Field in Source")) {
				System.out.println("Press Enter to Remove the Field");
				// if (br.readLine().equals("undo")) {
				// PlatformUI.getWorkbench().getDisplay().syncExec(new
				// DimensionExceptionRunnable(item, ragent));
				// } else {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new RemoveFieldRunnable(item, ragent));
				// }
			} else if (item.getStep().getName().equals("Update References")) {
				String fieldname = (String) item.getParameter("fieldname");
				System.out
						.println("Press Enter to update all the references to "
								+ fieldname);
				// if (br.readLine().equals("undo")) {
				// } else {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new UpdateReferencesRunnable(item, ragent));
				// }
				System.out.println("Enter undo to revert the changes");
				// if (br.readLine().equals("undo")) {
				// PlatformUI
				// .getWorkbench()
				// .getDisplay()
				// .syncExec(
				// new UndoCreateFieldExceptionRunnable(item,
				// ragent));
				// }
			} else if (item.getStep().getName().equals("Undo Changes")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new UndoRunnable(item, ragent));
			} else if (item.getStep().getName().equals("skip")) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new SkipRunnable(item, ragent));
			} else if (item.getStep().isLeaf()) {
				PlatformUI.getWorkbench().getDisplay()
						.syncExec(new LeafStepRunnable(item, ragent));
			}
			if (!item.getStep().getName().equals("skip")) {
				synchronized (ragent) {
					ragent.wait();
				}
			}
			if (item.getStep().isLeaf()
					&& !item.getState().equals(AgendaItem.TERMINATED)) {
				item.complete();
			}
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (IllegalTransition e) {
			e.printStackTrace();
		} catch (UnknownParameter e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see laser.juliette.agent.ItemHandlerAdapter#completed()
	 */
	@Override
	public void completed() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					ragent.getBefore().setText(item.getStep().getName());
					ragent.getBefore().getParent().layout();
				} catch (AMSException e) {
					e.printStackTrace();
				}
			}
		});
		super.completed();
	}
}
