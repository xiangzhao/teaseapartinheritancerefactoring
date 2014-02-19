/**
 * 
 */
package runnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import util.PluginUtil;

import config.RefactoringConstants;

import agent.RefactoringAgent;
import artifacts.PackageFragmentRoot;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;
import laser.lj.ResolutionException;
import laser.lj.Step;

/**
 * @author xiang
 * 
 */
public class LoadSourceFilesRunnable extends JulietteRunnable {

	HashMap<String, TreeItem> st = new HashMap<String, TreeItem>();

	public LoadSourceFilesRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		// ArrayList<String> iculist = new ArrayList<String>();
		// HashMap<String, String> icucontents = new HashMap<String,
		// String>();
		// PackageFragmentRoot pfr = new PackageFragmentRoot();
		// IProject project = ResourcesPlugin.getWorkspace().getRoot()
		// .getProject(RefactoringConstants.PROJECTNAME);
		// IFolder sourceFolder = project
		// .getFolder(RefactoringConstants.SOURCEFOLDER);
		// IJavaProject javaProject = JavaCore.create(project);
		// IPackageFragment pack = javaProject.getPackageFragmentRoot(
		// sourceFolder).createPackageFragment(
		// RefactoringConstants.PACKAGENAME, false, null);
		// for (ICompilationUnit icu :
		// PluginUtil.getAllICompilationUnit(null,
		// null, null)) {
		// iculist.add(icu.getElementName());
		// icucontents.put(icu.getElementName(), icu.getSource());
		// }
		// pfr.setCompilationUnitList(iculist);
		// pfr.setCompilationUnitContents(icucontents);
		setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
		// item.setParameter("packagefragmentroot",
		// PluginUtil.getCurrentPFR());

	}

	@Override
	void prepareUI() {
		try {
			Step s = item.getStep();
			s = s.getParent();
			// root
			TreeItem ti = new TreeItem(ragent.getTree(), 0);
			// ti.setText(s.getName());
			// ti.setData(s);
			constructProcessTree(s, ti, 2);
			super.prepareUI();
		} catch (AMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

	void constructProcessTree(Step step, TreeItem treeItem, int level) {
		try {
			if (step.isTry()) {
				treeItem.setText("[try] " + step.getName());
			} else if (step.isChoice()) {
				treeItem.setText("[choice] " + step.getName());
			} else if (step.isHandler()) {
				treeItem.setText("[handler] " + step.getName());
			} else {
				treeItem.setText(step.getName());
			}
			treeItem.setData(step);
			List<Step> substeps = new ArrayList<Step>();
			substeps.addAll(step.getSubsteps());
			if (level > 0)
				substeps.addAll(step.getHandlers());
			for (Step substep : substeps) {
				TreeItem tt = new TreeItem(treeItem, 0);
				constructProcessTree(substep, tt, level - 1);
			}
		} catch (ResolutionException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#setInstructions()
	 */
	@Override
	protected void setInstructions(String text) {
		// TODO Auto-generated method stub
		super.setInstructions("The system has loaded the source files");
	}

}
