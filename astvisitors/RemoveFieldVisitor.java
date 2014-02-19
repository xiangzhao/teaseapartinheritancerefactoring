/**
 * 
 */
package astvisitors;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * @author xiang
 * 
 */
public class RemoveFieldVisitor extends JulietteASTVisitor {

	private String fieldname;

	public RemoveFieldVisitor(String fieldname) {
		// TODO Auto-generated constructor stub
		this.fieldname = fieldname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * VariableDeclarationFragment)
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		// TODO Auto-generated method stub
		if (node.getName().getIdentifier().equals(fieldname)) {
		}
		return super.visit(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * FieldDeclaration)
	 */
	@Override
	public boolean visit(FieldDeclaration node) {
		// TODO Auto-generated method stub
		List<VariableDeclarationFragment> varlist = (List<VariableDeclarationFragment>) node
				.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment vf : varlist) {
			if (vf.getName().getIdentifier().equals(fieldname)) {
				AST ast = node.getAST();
				rewriter = ASTRewrite.create(ast);
				rewriter.remove(node, null);
				return false;
			}
		}

		return super.visit(node);
	}
}
