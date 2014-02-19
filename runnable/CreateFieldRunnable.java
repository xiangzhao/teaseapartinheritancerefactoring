/**
 * 
 */
package runnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;

import config.RefactoringConstants;

import util.PluginUtil;
import agent.RefactoringAgent;
import artifacts.PackageFragmentRoot;
import astvisitors.CreateFieldVisitor;
import astvisitors.MakeLinkVisitor;
import laser.ddg.ProvenanceData;
import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.ddgbuilder.DDGBuilder;
import laser.juliette.runner.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class CreateFieldRunnable extends JulietteRunnable {

	public CreateFieldRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		try {
			String fieldname = (String) item.getParameterFromDDG("fieldname");
			String fieldtype = (String) item.getParameterFromDDG("fieldtype");
			String newclassname = (String) item
					.getParameterFromDDG("newclassname");
			ICompilationUnit icu = PluginUtil.getICompilationUnit(null, null,
					null, newclassname, true);
			// IEditorPart javaEditor = JavaUI.openInEditor(icu);
			// JavaUI.revealInEditor(javaEditor, (IJavaElement)member);
			PluginUtil.performASTRewrite(icu, new CreateFieldVisitor(fieldtype,
					fieldname, newclassname));
			icu.commitWorkingCopy(false, null);
			setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
			// CompilationUnit cu = PluginUtil.getCompilationUnit(null, null,
			// null, newclassname);
			// TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
			// CreateFieldVisitor cfVisitor = new
			// CreateFieldVisitor(newclassname);
			// td.accept(cfVisitor);
			// ASTRewrite rewriter = cfVisitor.getRewriter();
			// Document document = new Document(icu.getSource());
			// TextEdit edits = rewriter.rewriteAST(document,
			// icu.getJavaProject()
			// .getOptions(true));
			// edits.apply(document);
			// icu.getBuffer().setContents(document.get());
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	void adjustAfterUI() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see runnable.JulietteRunnable#setInstructions()
	 */
	@Override
	protected void setInstructions(String text) {
		super.setInstructions("A new field will be created in the target class");
	}

	@Override
	protected void saveParams() {
		try {
			if (ragent.getUndo() != null) {
				DDGBuilder ddgBuilder = (laser.juliette.ddgbuilder.DDGBuilder) (ragent
						.getUndo().getDdgbuilder());
				ProvenanceData p = ddgBuilder.getProvData();
				Map<String, Serializable> revertTo = p
						.revertTo(DDGBuilder.getAgendaItemMapper()
								.getPINS(ragent.getUndo()).next());
				ResourcesPlugin.getWorkspace().getRoot()
						.getProject(RefactoringConstants.PROJECTNAME)
						.getFolder(RefactoringConstants.SOURCEFOLDER)
						.refreshLocal(IResource.DEPTH_INFINITE, null);
				Thread.sleep(500);
				Iterator<String> iterator = revertTo.keySet().iterator();
				while (iterator.hasNext()) {
					String param = iterator.next();
					System.err.println(param + ": " + revertTo.get(param));
				}
				ragent.setUndo(null);
			}
			for (String s : item.parameterNames()) {
				if (s.equals("agent"))
					continue;
				Serializable val = item.getParameterFromDDG(s);
				if (val instanceof ArrayList) {
					String content = "";
					for (int j = 0; j < ((ArrayList) val).size(); j++) {
						content += ((ArrayList) val).get(j).toString() + "\n";
					}
					val = content;
				}
				if (s.equals("packagefragmentroot")) {
					PackageFragmentRoot pfr = (PackageFragmentRoot) val;
					HashMap<String, String> cucontents = pfr
							.getCompilationUnitContents();
					oldParams.putAll(cucontents);
					continue;
				}
				oldParams.put(s, val.toString());
			}
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
