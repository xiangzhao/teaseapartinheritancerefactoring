/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import config.RefactoringConstants;

import agent.RefactoringAgent;
import artifacts.PackageFragmentRoot;

import laser.ddg.ProcedureInstanceNode;
import laser.ddg.ProvenanceData;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class UndoRunnable extends JulietteRunnable {

	ProvenanceData pd;

	public UndoRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		try {
			if (item.getDdgbuilder() instanceof DDGBuilder) {
				DDGBuilder d = (DDGBuilder) item.getDdgbuilder();
				pd = d.getProvData();
			}
			ProcedureInstanceNode pinNodeRevertible = ragent
					.getRevertiblePoint();
			Map<String, Serializable> revertTo = pd.revertTo(pinNodeRevertible);
			ResourcesPlugin.getWorkspace().getRoot()
					.getProject(RefactoringConstants.PROJECTNAME)
					.getFolder(RefactoringConstants.SOURCEFOLDER)
					.refreshLocal(IResource.DEPTH_INFINITE, null);
			Thread.sleep(500);
			// PackageFragmentRoot pfr = (PackageFragmentRoot) revertTo
			// .get("packagefragmentroot");
			// ArrayList<String> culist = pfr.getCompilationUnitList();
			// HashMap<String, String> cucontents = pfr
			// .getCompilationUnitContents();
			// IProject project = ResourcesPlugin.getWorkspace().getRoot()
			// .getProject(RefactoringConstants.PROJECTNAME);
			// IFolder sourceFolder =
			// project.getFolder(RefactoringConstants.SOURCEFOLDER);
			// IJavaProject javaProject = JavaCore.create(project);
			// IPackageFragment pack = javaProject.getPackageFragmentRoot(
			// sourceFolder).createPackageFragment("net.xiangzhao.test",
			// false, null);
			// for (ICompilationUnit icu : pack.getCompilationUnits()) {
			// if (!culist.contains(icu.getElementName())) {
			// System.out.println("deleting " + icu.getElementName());
			// icu.delete(true, null);
			// continue;
			// }
			// String oldcontent=cucontents.get(icu.getElementName());
			// if(oldcontent!=null && !oldcontent.equals(""))
			// icu.getBuffer().setContents(
			// oldcontent);
			// icu.commitWorkingCopy(false, null);
			// }
			for (Iterator<String> it = revertTo.keySet().iterator(); it
					.hasNext();) {
				String paramname = it.next();
				// System.out.println("&&&&&&&&&&" + paramname);
				if (paramname.equals(""))
					continue;
				if (!item.parameterNames().contains(paramname))
					continue;
				item.setParameter(paramname, revertTo.get(paramname));
			}
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

}
