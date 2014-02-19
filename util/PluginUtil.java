/**
 * 
 */
package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.AgendaItem;
import laser.juliette.ams.IllegalTransition;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import artifacts.PackageFragmentRoot;
import astvisitors.CreateFieldVisitor;
import astvisitors.JulietteASTVisitor;

import config.RefactoringConstants;

/**
 * @author zhao
 * 
 */
public class PluginUtil {
	public static String getActiveEditorContent() {
		ITextEditor activeEditor = (ITextEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput input = activeEditor.getEditorInput();
		IDocumentProvider provider = activeEditor.getDocumentProvider();
		IDocument document = provider.getDocument(input);
		System.out.println(document.get());
		return document.get();
	}

	public static ITextEditor getActiveEditor() {
		ITextEditor activeEditor = (ITextEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		return activeEditor;
	}

	public static ICompilationUnit getCurrentICompilationUnit() {
		ITextEditor activeEditor = (ITextEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ICompilationUnit firstElement = JavaCore
				.createCompilationUnitFrom((IFile) activeEditor
						.getEditorInput().getAdapter(IFile.class));
		return firstElement;
	}

	public static CompilationUnit getCurrentCompilationUnit() {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(getCurrentICompilationUnit());
		CompilationUnit astRoot = (CompilationUnit) parser
				.createAST(new NullProgressMonitor());
		return astRoot;
	}

	public static void terminate(AgendaItem item, Serializable e) {
		// TODO Auto-generated method stub
		Set<Serializable> exceptions = new HashSet<Serializable>();
		exceptions.add(e);
		try {
			item.terminate(exceptions);
		} catch (AMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalTransition e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static IFile getActiveEditorIFile() {
		ITextEditor activeEditor = getActiveEditor();
		IFile file = (IFile) activeEditor.getEditorInput().getAdapter(
				IFile.class);
		if (file != null) {
			return file;
		} else
			return null;

	}

	public static ICompilationUnit[] getAllICompilationUnit(String projectname,
			String sourcefoldername, String packagename) {
		if (projectname == null) {
			projectname = RefactoringConstants.PROJECTNAME;
		}
		if (sourcefoldername == null) {
			sourcefoldername = RefactoringConstants.SOURCEFOLDER;
		}
		if (packagename == null) {
			packagename = RefactoringConstants.PACKAGENAME;
		}
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectname);
		IJavaProject javaProject = JavaCore.create(project);
		IFolder sourceFolder = project.getFolder(sourcefoldername);
		try {
			IPackageFragment pack = javaProject.getPackageFragmentRoot(
					sourceFolder).createPackageFragment(packagename, false,
					null);
			return pack.getCompilationUnits();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Get ICompilationUnit according to the typename
	 * 
	 * @param typename
	 * @return
	 */
	public static ICompilationUnit getICompilationUnit(String projectname,
			String sourcefoldername, String packagename, String typename,
			boolean openineditor) {
		// if (projectname == null) {
		// projectname = RefactoringConstants.PROJECTNAME;
		// }
		// if (sourcefoldername == null) {
		// sourcefoldername = RefactoringConstants.SOURCEFOLDER;
		// }
		// if (packagename == null) {
		// packagename = RefactoringConstants.PACKAGENAME;
		// }
		if (typename == null) {
			return null;
		}
		// IProject project = ResourcesPlugin.getWorkspace().getRoot()
		// .getProject(projectname);
		// IJavaProject javaProject = JavaCore.create(project);
		// IFolder sourceFolder = project.getFolder(sourcefoldername);
		try {

			// IPackageFragment pack = javaProject.getPackageFragmentRoot(
			// sourceFolder).createPackageFragment(packagename, false,
			// null);
			for (ICompilationUnit icu : getAllICompilationUnit(projectname,
					sourcefoldername, packagename)) {
				System.out.println(icu.getTypes()[0].getElementName()
						+ " ELEMEMENENENT");
				if (icu.getTypes()[0].getElementName().equals(typename)) {
					System.out.println("found" + typename);
					// if (openineditor) {
					// JavaUI.openInEditor(icu);
					// }
					return icu;

				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static CompilationUnit getCompilationUnit(String projectname,
			String sourcefoldername, String packagename, String typename,
			boolean openineditor) {
		ICompilationUnit icu = getICompilationUnit(projectname,
				sourcefoldername, packagename, typename, openineditor);
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(icu);
		CompilationUnit astRoot = (CompilationUnit) parser
				.createAST(new NullProgressMonitor());
		return astRoot;
	}

	public static void performASTRewrite(ICompilationUnit iCompilationUnit,
			JulietteASTVisitor jVisitor) throws JavaModelException,
			MalformedTreeException, BadLocationException, PartInitException {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(iCompilationUnit);
		CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(new NullProgressMonitor());
		TypeDeclaration td = (TypeDeclaration) compilationUnit.types().get(0);
		jVisitor.setRewriter(ASTRewrite.create(td.getAST()));
		td.accept(jVisitor);
		ASTRewrite rewriter = jVisitor.getRewriter();
		if (rewriter != null) {
			JavaUI.openInEditor(iCompilationUnit);
			Document document = new Document(iCompilationUnit.getSource());
			TextEdit edits = rewriter.rewriteAST(document, iCompilationUnit
					.getJavaProject().getOptions(true));
			edits.apply(document);
			iCompilationUnit.getBuffer().setContents(document.get());
		}

	}

	public static void openJavaEditor(ICompilationUnit icu) {
		try {
			JavaUI.openInEditor(icu);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static PackageFragmentRoot getCurrentPFR() {
		PackageFragmentRoot pfr = new PackageFragmentRoot();
		try {
			ArrayList<String> iculist = new ArrayList<String>();
			HashMap<String, String> icucontents = new HashMap<String, String>();
			for (ICompilationUnit icu : getAllICompilationUnit(null, null, null)) {
				iculist.add(icu.getElementName());
				icucontents.put(icu.getElementName(), icu.getSource());
			}
			pfr.setCompilationUnitList(iculist);
			pfr.setCompilationUnitContents(icucontents);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return pfr;
	}
}
