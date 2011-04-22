package exo.html.parser;

public interface Remark extends Element {
	/**
	 * Returns the text contents of the comment tag.
	 * 
	 * @return The contents of the text inside the comment delimiters.
	 * @see #setText
	 */
	String getText();

	/**
	 * Sets the string contents of the node. If the text has the remark delimiters
	 * (&lt;!-- --&gt;), these are stripped off.
	 * 
	 * @param text
	 *          The new text for the node.
	 * @see #getText
	 */
	void setText(String text);

}
