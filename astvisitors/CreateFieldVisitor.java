/**
 * 
 */
package astvisitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * @author xiang
 * 
 */
public class CreateFieldVisitor extends JulietteASTVisitor {
	private String fieldname;
	private String newclassname;
	private String fieldtype;

	public CreateFieldVisitor(String fieldtype, String fieldname,
			String newclassname) {
		// TODO Auto-generated constructor stub
		this.fieldtype = fieldtype;
		this.fieldname = fieldname;
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
		// TODO Auto-generated method stub
		System.out.println("Test getSimpleName(): "
				+ node.getName().getIdentifier());
		AST ast = node.getAST();
		rewriter = ASTRewrite.create(ast);
		VariableDeclarationFragment newVariableDeclarationFragment = ast
				.newVariableDeclarationFragment();
		newVariableDeclarationFragment.setName(ast.newSimpleName(fieldname
				.toLowerCase()));
		FieldDeclaration newFieldDeclaration = ast
				.newFieldDeclaration(newVariableDeclarationFragment);
		newFieldDeclaration.modifiers().add(
				ast.newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD));
		newFieldDeclaration.setType(ast.newSimpleType(ast
				.newSimpleName(fieldtype)));
		ListRewrite listRewrite = rewriter.getListRewrite(node,
				TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertFirst(newFieldDeclaration, null);
		return false;
	}

}
