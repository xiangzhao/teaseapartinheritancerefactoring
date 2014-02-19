/**
 * 
 */
package runnable;

import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import util.PluginUtil;

import config.RefactoringConstants;

import agent.RefactoringAgent;
import artifacts.Dimension;
import artifacts.PackageFragmentRoot;

import laser.juliette.ams.AMSException;
import laser.juliette.ams.UnknownParameter;
import laser.juliette.runner.ams.AgendaItem;

/**
 * @author xiang
 * 
 */
public class CreateClassRunnable extends JulietteRunnable {

	public CreateClassRunnable(AgendaItem item, RefactoringAgent ragent) {
		super(item, ragent);
	}

	@Override
	public void performStep() {
		try {
			System.err.println("Create Class:"
					+ item.getParent().getStep().getName());
		} catch (AMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// AST ast = new AST();
		// CompilationUnit unit = ast.newCompilationUnit();
		// PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		// packageDeclaration.setName(ast.newSimpleName("net.xiangzhao.test"));
		// unit.setPackage(packageDeclaration);
		// // ImportDeclaration importDeclaration = ast.newImportDeclaration();
		// // QualifiedName name =
		// // ast.newQualifiedName(
		// // ast.newSimpleName("java"),
		// // ast.newSimpleName("util"));
		// // importDeclaration.setName(name);
		// // importDeclaration.setOnDemand(true);
		// // unit.imports().add(importDeclaration);
		// TypeDeclaration type = ast.newTypeDeclaration();
		// type.setInterface(false);
		// type.modifiers().add(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
		// type.setName(ast.newSimpleName(newclassname));
		// // MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		// // methodDeclaration.setConstructor(false);
		// // methodDeclaration.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
		// // methodDeclaration.setName(ast.newSimpleName("main"));
		// // methodDeclaration.setReturnType(ast
		// // .newPrimitiveType(PrimitiveType.VOID));
		// // SingleVariableDeclaration variableDeclaration = ast
		// // .newSingleVariableDeclaration();
		// // variableDeclaration.setModifiers(Modifier.NONE);
		// // variableDeclaration.setType(ast.newArrayType(ast.newSimpleType(ast
		// // .newSimpleName("String"))));
		// // variableDeclaration.setName(ast.newSimpleName("args"));
		// // methodDeclaration.parameters().add(variableDeclaration);
		// // org.eclipse.jdt.core.dom.Block block = ast.newBlock();
		// // MethodInvocation methodInvocation = ast.newMethodInvocation();
		// // name = ast.newQualifiedName(ast.newSimpleName("System"),
		// // ast.newSimpleName("out"));
		// // methodInvocation.setExpression(name);
		// // methodInvocation.setName(ast.newSimpleName("println"));
		// // InfixExpression infixExpression = ast.newInfixExpression();
		// // infixExpression.setOperator(InfixExpression.Operator.PLUS);
		// // StringLiteral literal = ast.newStringLiteral();
		// // literal.setLiteralValue("Hello");
		// // infixExpression.setLeftOperand(literal);
		// // literal = ast.newStringLiteral();
		// // literal.setLiteralValue(" world");
		// // infixExpression.setRightOperand(literal);
		// // methodInvocation.arguments().add(infixExpression);
		// // ExpressionStatement expressionStatement = ast
		// // .newExpressionStatement(methodInvocation);
		// // block.statements().add(expressionStatement);
		// // methodDeclaration.setBody(block);
		// // type.bodyDeclarations().add(methodDeclaration);
		// unit.types().add(type);

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(RefactoringConstants.PROJECTNAME);
		IFolder sourceFolder = project
				.getFolder(RefactoringConstants.SOURCEFOLDER);
		try {
			String newclassname = ((Dimension) item.getParameter("dimension"))
					.getDimensionDescription().trim();
			if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
				IJavaProject javaProject = JavaCore.create(project);
				IPackageFragment pack = javaProject.getPackageFragmentRoot(
						sourceFolder).createPackageFragment(
						RefactoringConstants.PACKAGENAME, false, null);
				ICompilationUnit nicu = pack.createCompilationUnit(newclassname
						+ ".java", "", true, null);
				nicu.createPackageDeclaration(pack.getElementName(), null);
				nicu.createType("public class " + newclassname + "{\n\n}\n",
						null, true, null);
				// ArrayList<String> iculist = new ArrayList<String>();

				// for (ICompilationUnit icu : pack.getCompilationUnits()) {
				// iculist.add(icu.getElementName());
				// }
				// PackageFragmentRoot pfr = new PackageFragmentRoot();

				// pfr.setCompilationUnitList(iculist);
				setParams("packagefragmentroot", PluginUtil.getCurrentPFR());
				setParams("newclassname", newclassname);
				// item.setParameter("packagefragmentroot", pfr);
				// item.setParameter("newclassname", newclassname);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (AMSException e) {
			e.printStackTrace();
		} catch (UnknownParameter e) {
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
		super.setInstructions("A new root class will be created");
	}

}
