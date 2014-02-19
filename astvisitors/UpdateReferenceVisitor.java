/**
 * 
 */
package astvisitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldAccess;

/**
 * @author xiang
 * 
 */
public class UpdateReferenceVisitor extends JulietteASTVisitor {
	private String classreference;
	private String fieldname;

	public UpdateReferenceVisitor(String classreference, String fieldname) {
		// TODO Auto-generated constructor stub
		this.classreference = classreference;
		this.fieldname = fieldname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * FieldAccess)
	 */
	@Override
	public boolean visit(FieldAccess node) {
		// TODO Auto-generated method stub
		if (node.getName().getIdentifier().equals(fieldname)) {
			AST ast = node.getAST();
			FieldAccess updatedfieldaccess = ast.newFieldAccess();
			FieldAccess newclassfield = ast.newFieldAccess();
			newclassfield.setName(ast.newSimpleName(classreference));
			newclassfield.setExpression(ast.newThisExpression());
			updatedfieldaccess.setExpression(newclassfield);
			updatedfieldaccess.setName(ast.newSimpleName(fieldname));
			rewriter.replace(node, updatedfieldaccess, null);
		}
		return super.visit(node);
	}

}
