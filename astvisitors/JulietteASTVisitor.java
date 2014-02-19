/**
 * 
 */
package astvisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * @author xiang
 * 
 */
public class JulietteASTVisitor extends ASTVisitor {
	protected ASTRewrite rewriter;

	/**
	 * @return the rewriter
	 */
	public ASTRewrite getRewriter() {
		return rewriter;
	}

	/**
	 * @param rewriter
	 *            the rewriter to set
	 */
	public void setRewriter(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}

}
