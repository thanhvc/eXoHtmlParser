package exo.html.parser.visitor;

import exo.html.parser.Remark;
import exo.html.parser.Tag;
import exo.html.parser.Text;

public abstract class ElementVisitor {

	private boolean recurseChildren;
	private boolean recurseSelf;
	
	public ElementVisitor() {
		this(true);
	}

	public ElementVisitor(boolean recurseChildren) {
		this.recurseChildren = recurseChildren;
	}
	/**
	 * Override this method if you wish to do special
	 * processing prior to the start of parsing.
	 */
	public void beginParsing() {
		
	}
	
	/**
	 * Called for each <code>Tag</code> visited
	 *  @param tag The tag being visited.
	 */
	public void visitTag(Tag tag) {
		
	}
	
	/**
	 * Called for each <code>Tag</code> visited that is an end tag.
	 * @param tag The en tag being visited.
	 */
	public void visitEndTag() {
		
	}
	
	/**
	 * Called for each <code>StringNode</code> visited.
	 * @param string The string node being visited.
	 */
	public void visitStringNode(Text string) {
		
	}
	
	/**
	 * Called for each <code>RemarkElement</code> visited.
	 * @param remark The remark node being visited.
	 */
	public void visitRemarkElement(Remark remark) {
		
	}
	/**
	 * Override this method if you wish to do special
	 * processing upon completion of parsing.
	 */
	public void finishedParsing() {
		
	}
	
	public boolean shouldRescureChildren() {
		return recurseChildren;
	}
	
	public boolean shouldRescureSelf() {
		return recurseSelf;
	}
	
	
	
}
