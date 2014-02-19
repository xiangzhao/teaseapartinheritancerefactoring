/**
 * 
 */
package runnable;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import config.RefactoringConstants;

import util.PluginUtil;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;
import agent.RefactoringAgent;
import artifacts.PackageFragmentRoot;
import astvisitors.MakeLinkVisitor;

/**
 * @author xiang
 * 
 */
public class MakeLinkRunnable extends JulietteRunnable {

	public MakeLinkRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		// TODO Auto-generated method stub
		try {
			System.err.println(item.getParent().getStep().getName());
			ICompilationUnit targetICompilationUnit = PluginUtil
					.getICompilationUnit(null, null, null, "NewsFeedItem", true);
			String newclassname = (String) item.getParameter("newclassname");
			PluginUtil.performASTRewrite(targetICompilationUnit,
					new MakeLinkVisitor(newclassname));
			// targetICompilationUnit.commitWorkingCopy(false, null);
			// CompilationUnit targetCompilationUnit = PluginUtil
			// .getCurrentCompilationUnit();
			// TypeDeclaration td = (TypeDeclaration) targetCompilationUnit
			// .types().get(0);
			// MakeLinkVisitor mlVisitor = new MakeLinkVisitor(newclassname);
			// td.accept(mlVisitor);
			// ASTRewrite rewriter = mlVisitor.getRewriter();
			// Document document = new
			// Document(targetICompilationUnit.getSource());
			// TextEdit edits = rewriter.rewriteAST(document,
			// targetICompilationUnit.getJavaProject().getOptions(true));
			// edits.apply(document);
			// targetICompilationUnit.getBuffer().setContents(document.get());

			targetICompilationUnit.commitWorkingCopy(false, null);

			// IProject project = ResourcesPlugin.getWorkspace().getRoot()
			// .getProject(RefactoringConstants.PROJECTNAME);
			// IFolder sourceFolder = project
			// .getFolder(RefactoringConstants.SOURCEFOLDER);
			// if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			// IJavaProject javaProject = JavaCore.create(project);
			// IPackageFragment pack = javaProject.getPackageFragmentRoot(
			// sourceFolder).createPackageFragment(
			// RefactoringConstants.PACKAGENAME, false, null);
			// ArrayList<String> iculist = new ArrayList<String>();
			// HashMap<String, String> icucontents = new HashMap<String,
			// String>();
			//
			// for (ICompilationUnit icu : pack.getCompilationUnits()) {
			// iculist.add(icu.getElementName());
			// icucontents.put(icu.getElementName(), icu.getSource());
			//
			// }
			// PackageFragmentRoot pfr = new PackageFragmentRoot();
			//
			// pfr.setCompilationUnitList(iculist);
			// pfr.setCompilationUnitContents(icucontents);
			setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
			// item.setParameter("packagefragmentroot", pfr);
			// }
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (UnknownParameter e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
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
		// TODO Auto-generated method stub
		super.setInstructions("A link will be created between the two class hierarchies");
	}

}
