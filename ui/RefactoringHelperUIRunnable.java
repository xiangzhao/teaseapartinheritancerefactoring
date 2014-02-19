/**
 * 
 */
package ui;

import java.util.Iterator;

import laser.ddg.AbstractProcedureInstanceNode;
import laser.ddg.ProcedureInstanceNode;
import laser.ddg.ProvenanceData;
import laser.littlejil.smartchecklist.gui.SmartChecklistGUI;
import laser.lj.InterfaceDeclaration;
import laser.lj.InterfaceDeclaration.DeclarationKind;
import laser.lj.InterfaceDeclarationSet;
import laser.lj.Step;
import listeners.ComboSelectionListener;
import listeners.ConfirmButtonSelectionListener;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import config.RefactoringConstants;

import agent.RefactoringAgent;

/**
 * @author xiang
 * 
 */
public class RefactoringHelperUIRunnable implements Runnable {

	private RefactoringAgent ragent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		SmartChecklistGUI gui = new SmartChecklistGUI(PlatformUI.getWorkbench()
				.getDisplay(), ragent);
		ragent.processPanel_ = gui.getProcessPanel_();
		gui.open();

		Shell shell = new Shell(PlatformUI.getWorkbench().getDisplay());
		shell.setText("TAIR Refactoring Helper");
		shell.setSize(1000, 600);
		shell.setLocation(300, 300);

		shell.setLayout(new GridLayout(3, false));

		final CTabFolder folder = new CTabFolder(shell, SWT.TOP);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
		folder.setLayoutData(data);
		CTabItem cTabItem1 = new CTabItem(folder, SWT.NONE);
		cTabItem1.setText("Refactoring Workspace");
		CTabItem cTabItem2 = new CTabItem(folder, SWT.NONE);
		cTabItem2.setText("Process View");
		final CTabItem cTabItem3 = new CTabItem(folder, SWT.NONE);
		cTabItem3.setText("Context View");

		// refactoringworkspace
		Composite refactoringworkspace = new Composite(folder, SWT.BORDER);
		GridLayout refactoringworkspacerowlayout = new GridLayout(1, false);
		refactoringworkspace.setLayout(refactoringworkspacerowlayout);
		cTabItem1.setControl(refactoringworkspace);

		// stepindicator
		Composite stepindicator = new Composite(refactoringworkspace,
				SWT.BORDER);
		GridData stepindicatorgriddata = new GridData(SWT.FILL, SWT.FILL, true,
				true);
		stepindicator.setLayoutData(stepindicatorgriddata);
		GridLayout stepindicatorgridlayout = new GridLayout(3, false);
		stepindicatorgridlayout.marginLeft = 10;
		stepindicatorgridlayout.marginRight = 10;

		// stepindicatorrowlayout.justify = true;
		// stepindicatorrowlayout.fill = true;
		// stepindicatorrowlayout.center = true;
		// stepindicatorrowlayout.spacing = 40;
		stepindicator.setLayout(stepindicatorgridlayout);

		ragent.setBefore(new Label(stepindicator, SWT.NONE));
		ragent.getBefore().setText("before step");
		ragent.getBefore().setAlignment(SWT.CENTER);
		final Color beforeColor = new Color(PlatformUI.getWorkbench()
				.getDisplay(), 192, 192, 192);
		ragent.getBefore().setForeground(beforeColor);
		GridData steplabelgriddata = new GridData(SWT.CENTER, SWT.CENTER, true,
				true);
		steplabelgriddata.minimumWidth = 300;
		ragent.getBefore().setLayoutData(steplabelgriddata);

		ragent.setCurrent(new Label(stepindicator, SWT.NONE));
		ragent.getCurrent().setAlignment(SWT.CENTER);
		ragent.getCurrent().setText("current step");
		final Color currentColor = new Color(PlatformUI.getWorkbench()
				.getDisplay(), 0, 0, 255);
		ragent.getCurrent().setForeground(currentColor);
		ragent.getCurrent().setLayoutData(steplabelgriddata);

		ragent.setNext(new Label(stepindicator, SWT.NONE));
		ragent.getNext().setAlignment(SWT.CENTER);
		ragent.getNext().setText("next step");
		ragent.getNext().setLayoutData(steplabelgriddata);

		// stepindicator.pack();

		// artifactselector

		final Composite artifactspace = new Composite(refactoringworkspace,
				SWT.NONE);
		GridData artifactspacegriddata = new GridData(SWT.FILL, SWT.FILL, true,
				true);
		artifactspace.setLayoutData(artifactspacegriddata);
		artifactspace.setLayout(new GridLayout(5, true));

		final Combo comboDropDown = new Combo(artifactspace, SWT.DROP_DOWN
				| SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		comboDropDown.setItems(new String[] { " ", "Input", "Output" });
		comboDropDown.addListener(SWT.Selection,
				ComboSelectionListener.getListener());
		comboDropDown.addListener(SWT.Modify,
				ComboSelectionListener.getListener());
		ComboSelectionListener.getListener().setCombo(comboDropDown);
		ComboSelectionListener.getListener().setRagent(ragent);
		ragent.setComboDropDown(comboDropDown);
		// comboDropDown.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// System.out.println(comboDropDown.getText());
		// super.widgetSelected(e);
		// }
		// });

		final Composite artifactselector = new Composite(artifactspace,
				SWT.NONE);
		ComboSelectionListener.getListener().setArtifactselector(
				artifactselector);
		ragent.setArtifactselector(artifactselector);
		GridData artifactselectorgriddata = new GridData(SWT.FILL, SWT.FILL,
				true, true, 4, 1);
		artifactselector.setLayoutData(artifactselectorgriddata);
		GridLayout artifactselectorgridlayout = new GridLayout(3, true);
		artifactselector.setLayout(artifactselectorgridlayout);

		GridData artifactselectorbuttongriddata = new GridData(SWT.FILL,
				SWT.FILL, true, true);
		Button artifact1 = new Button(ragent.getArtifactselector(), SWT.RADIO);
		artifact1.setText("artifact1");
		artifact1.setSelection(true);
		artifact1.setLayoutData(artifactselectorbuttongriddata);
		Button artifact2 = new Button(ragent.getArtifactselector(), SWT.RADIO);
		artifact2.setText("artifact2");
		artifact2.setLayoutData(artifactselectorbuttongriddata);
		Button artifact3 = new Button(ragent.getArtifactselector(), SWT.RADIO);
		artifact3.setText("artifact3");
		artifact3.setLayoutData(artifactselectorbuttongriddata);

		// artifactcomparator
		Composite artifactcomparator = new Composite(refactoringworkspace,
				SWT.NONE);
		GridData artifactcomparatorgriddata = new GridData(SWT.FILL, SWT.FILL,
				true, true);
		artifactcomparator.setLayoutData(artifactcomparatorgriddata);
		GridLayout artifactcomparatorrowlayout = new GridLayout(2, false);
		artifactcomparator.setLayout(artifactcomparatorrowlayout);

		Label beforelabel = new Label(artifactcomparator, SWT.NONE);
		Label afterlabel = new Label(artifactcomparator, SWT.NONE);
		GridData beforeafterlabel = new GridData(SWT.CENTER, SWT.CENTER, true,
				true);
		beforeafterlabel.minimumWidth = 300;

		beforelabel.setLayoutData(beforeafterlabel);
		afterlabel.setLayoutData(beforeafterlabel);
		beforelabel.setText("Before");
		afterlabel.setText("After");
		beforelabel.setAlignment(SWT.CENTER);
		afterlabel.setAlignment(SWT.CENTER);

		ragent.setOldText(new StyledText(artifactcomparator, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL));
		ragent.getOldText().setText("artifact before value");
		ragent.getOldText().addLineStyleListener(new JavaLineStyler());
		ragent.getOldText().setEditable(false);
		ragent.setNewText(new StyledText(artifactcomparator, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL));
		ragent.getNewText().setText("artifact after value");
		ragent.getNewText().addLineStyleListener(new JavaLineStyler());

		GridData textspec = new GridData();
		textspec.horizontalAlignment = GridData.FILL;
		textspec.grabExcessHorizontalSpace = true;
		textspec.verticalAlignment = GridData.FILL;
		textspec.grabExcessVerticalSpace = true;
		textspec.widthHint = 270;
		textspec.heightHint = 200;
		ragent.getOldText().setLayoutData(textspec);
		ragent.getNewText().setLayoutData(textspec);

		Composite processview = new Composite(folder, SWT.BORDER);
		processview.setLayout(new RowLayout(SWT.VERTICAL));
		cTabItem2.setControl(processview);

		Composite contextview = new Composite(folder, SWT.BORDER);
		contextview.setLayout(new RowLayout(SWT.VERTICAL));
		cTabItem3.setControl(contextview);

		Composite instructions = new Composite(shell, SWT.NONE);
		GridData instructionsData = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		instructions.setLayoutData(instructionsData);
		instructions.setLayout(new FillLayout());
		ragent.setInstructions(instructions);

		Label progressbar = new Label(instructions, SWT.NONE);
		ragent.setProgressbar(progressbar);
		progressbar.setText("progress bar");
		// GridData progressbarData = new GridData(SWT.CENTER, SWT.CENTER, true,
		// true, 1, 1);
		// progressbar.setLayoutData(progressbarData);

		Button back = new Button(shell, SWT.PUSH);
		back.setText("Back");
		GridData backData = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1,
				1);
		back.setLayoutData(backData);
		back.addSelectionListener(new BackButtonSelectionListener(ragent));

		Button confirm = new Button(shell, SWT.PUSH);
		confirm.setText("Confirm");
		GridData confirmData = new GridData(SWT.LEFT, SWT.CENTER, true, true,
				1, 1);
		confirmData.horizontalIndent = 10;
		confirm.setLayoutData(confirmData);
		confirm.addSelectionListener(new ConfirmButtonSelectionListener(ragent));

		final SashForm sashForm = new SashForm(folder, SWT.HORIZONTAL);

		final Composite left = new Composite(sashForm, SWT.NONE);
		left.setLayout(new FillLayout());

		final Composite right = new Composite(sashForm, SWT.NONE);
		ragent.setRight(right);
		final GridLayout gridLayout = new GridLayout(2, false);
		right.setLayout(gridLayout);

		final Label label = new Label(right, SWT.RIGHT);
		final GridData gridData_2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(gridData_2);
		label.setText("Agent");

		final Text agentname = new Text(right, SWT.BORDER);
		final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_HORIZONTAL);
		agentname.setLayoutData(gridData);

		final Label label_1 = new Label(right, SWT.RIGHT);
		label_1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		label_1.setText("Input Artifacts");

		final Text inputParams = new Text(right, SWT.BORDER);
		final GridData gridData_1_1 = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		inputParams.setLayoutData(gridData_1_1);

		final Label label_1_1 = new Label(right, SWT.RIGHT);
		label_1_1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		label_1_1.setText("Output Artifacts");

		final Text outputParams = new Text(right, SWT.BORDER);
		final GridData gridData_1_2 = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		outputParams.setLayoutData(gridData_1_2);

		final Label label_1_2 = new Label(right, SWT.RIGHT);
		label_1_2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		label_1_2.setText("Exceptions");

		final Text exceptionParams = new Text(right, SWT.BORDER);
		final GridData gridData_1 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		exceptionParams.setLayoutData(gridData_1);

		final Label instance = new Label(right, SWT.RIGHT);
		instance.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		instance.setText("Instances:");

		final Composite instancesview = new Composite(right, SWT.NONE);
		instancesview
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		instancesview.setLayout(new GridLayout(1, false));

		Composite contextworkspace = new Composite(folder, SWT.BORDER);
		contextworkspace.setLayout(new GridLayout(1, true));
		final Browser contextbrowser = new Browser(contextworkspace, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 1;
		gd.verticalSpan = 1;
		contextbrowser.setLayoutData(gd);
		contextbrowser.setUrl("http://127.0.0.1:1234/?p=.git;a=shortlog");
		cTabItem3.setControl(contextworkspace);

		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item == cTabItem3) {
					contextbrowser.refresh();
				}
			}
		});

		ragent.setTree(new Tree(left, SWT.BORDER));
		ragent.getTree().addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TreeItem[] selection = ragent.getTree().getSelection();
				Step s = (Step) selection[0].getData();
				String stepname = s.getName();

				if (s.getAgentDeclaration() != null)
					agentname.setText(s.getAgentDeclaration().getName());

				String inputparameters = "";
				Iterator<String> ip = s.getInputParameters().getNames()
						.iterator();
				while (ip.hasNext()) {
					inputparameters += ip.next() + ",";
				}
				inputParams.setText(inputparameters);

				String outputparameters = "";
				Iterator<String> op = s.getOutputParameters().getNames()
						.iterator();
				while (op.hasNext()) {
					outputparameters += op.next() + ",";
				}
				outputParams.setText(outputparameters);

				InterfaceDeclarationSet exdeclarations = s
						.getDeclarations(DeclarationKind.EXCEPTION);
				String exceptionparameters = "";
				Iterator<InterfaceDeclaration> ep = exdeclarations.iterator();
				while (ep.hasNext()) {
					exceptionparameters += ep.next().getTemplate()
							.getObjectType()
							+ ",";
				}
				exceptionParams.setText(exceptionparameters);

				for (Control c : instancesview.getChildren()) {
					c.dispose();
				}
				Iterator<ProcedureInstanceNode> pinIter = ragent.getPd()
						.pinIter();
				while (pinIter.hasNext()) {
					final AbstractProcedureInstanceNode pin = (AbstractProcedureInstanceNode) pinIter
							.next();
					if (pin.getName().equals(stepname)) {
						if (pin.getGitSHA() == null
								|| pin.getGitSHA().equals("")) {
							Label l = new Label(instancesview, SWT.NONE);
							l.setText(pin.getName() + ": "
									+ pin.getCreatedTime());
							l.setLayoutData(new GridData(
									GridData.HORIZONTAL_ALIGN_FILL));
						} else {
							Link l = new Link(instancesview, SWT.NONE);
							l.setText("<a>" + pin.getName() + ": "
									+ pin.getCreatedTime() + "</a>");
							l.setToolTipText("Examine in Context View");
							l.setLayoutData(new GridData(
									GridData.HORIZONTAL_ALIGN_FILL));
							l.addSelectionListener(new SelectionAdapter() {
								/*
								 * (non-Javadoc)
								 * 
								 * @see org.eclipse.swt.events.SelectionAdapter#
								 * widgetSelected
								 * (org.eclipse.swt.events.SelectionEvent)
								 */
								@Override
								public void widgetSelected(SelectionEvent e) {
									contextbrowser
											.setUrl("http://127.0.0.1:1234/?p=.git;a=commit;h="
													+ pin.getGitSHA());
									folder.setSelection(cTabItem3);
									super.widgetSelected(e);
								}
							});
							Menu menu = new Menu(l);
							MenuItem item = new MenuItem(menu, SWT.PUSH);
							item.setText(pin.predecessorIter().next()
									.predecessorIter().next().getName());
							item = new MenuItem(menu, SWT.PUSH);
							item.setText(pin.predecessorIter().next().getName());
							item = new MenuItem(menu, SWT.PUSH);
							item.setText(pin.getName());
							l.setMenu(menu);
							item.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent e) {
									// TODO Auto-generated method stub
									ragent.getPd().revertTo(pin);
									try {
										ResourcesPlugin
												.getWorkspace()
												.getRoot()
												.getProject(
														RefactoringConstants.PROJECTNAME)
												.getFolder(
														RefactoringConstants.SOURCEFOLDER)
												.refreshLocal(
														IResource.DEPTH_INFINITE,
														null);
										Thread.sleep(500);
									} catch (CoreException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}

								@Override
								public void widgetDefaultSelected(
										SelectionEvent e) {
									// TODO Auto-generated method stub

								}

							});
							item = new MenuItem(menu, SWT.PUSH);
							item.setText(pin.successorIter().next().getName());
							item = new MenuItem(menu, SWT.PUSH);
							item.setText(pin.successorIter().next()
									.successorIter().next().getName());
						}
					}
				}
				instancesview.layout();
				instancesview.pack();
				right.layout();
			}
		});
		sashForm.setWeights(new int[] { 1, 2 });
		cTabItem2.setControl(sashForm);

		// shell.pack();
		shell.open();
	}

	public RefactoringHelperUIRunnable(RefactoringAgent ragent) {
		super();
		this.ragent = ragent;
	}

}
