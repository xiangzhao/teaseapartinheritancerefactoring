/**
 * 
 */
package astvisitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * @author xiang
 * 
 */
public class MakeLinkVisitor extends JulietteASTVisitor {
	private String newclassname;

	public MakeLinkVisitor(String newclassname) {
		this.newclassname = newclassname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * TypeDeclaration)
	 */

	@Override
	public boolean visit(TypeDeclaration node) {
		AST ast = node.getAST();
		rewriter = ASTRewrite.create(ast);
		VariableDeclarationFragment newVariableDeclarationFragment = ast
				.newVariableDeclarationFragment();
		newVariableDeclarationFragment.setName(ast.newSimpleName(newclassname
				.toLowerCase()));
		FieldDeclaration newFieldDeclaration = ast
				.newFieldDeclaration(newVariableDeclarationFragment);
		newFieldDeclaration.modifiers().add(
				ast.newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD));
		newFieldDeclaration.setType(ast.newSimpleType(ast
				.newSimpleName(newclassname)));
		// VariableDeclarationStatement newVariableDeclarationStatement = ast
		// .newVariableDeclarationStatement(newVariableDeclarationFragment);
		// newVariableDeclarationStatement.setType(ast.newSimpleType(ast
		// .newSimpleName(sourcename)));
		ListRewrite listRewrite = rewriter.getListRewrite(node,
				TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertFirst(newFieldDeclaration, null);
		return false;
	}
}
