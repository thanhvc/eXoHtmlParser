package exo.html.parser;

import java.util.Vector;

import exo.html.parser.util.ParserException;

public interface ElementFactory {

	/**
	 * Create a new text node.
	 * 
	 * @param page
	 *          The page the node is on.
	 * @param start
	 *          The beginning position of the string.
	 * @param end
	 *          The ending positiong of the string.
	 * @throws ParserException
	 *           If there is a problem encountered when creating the node.
	 * @return A text node comprising the indicated characters from the page.
	 */
	Text createStringNode(Page page, int start, int end) throws ParserException;

	/**
	 * Create a new remark node.
	 * 
	 * @param page
	 *          The page the node is on.
	 * @param start
	 *          The beginning position of the remark.
	 * @param end
	 *          The ending positiong of the remark.
	 * @throws ParserException
	 *           If there is a problem encountered when creating the node.
	 * @return A remark node comprising the indicated characters from the page.
	 */
	Remark createRemarkNode(Page page, int start, int end) throws ParserException;

	/**
	 * Create a new tag node. Note that the attributes vector contains at least
	 * one element, which is the tag name (standalone attribute) at position zero.
	 * This can be used to decide which type of node to create, or gate other
	 * processing that may be appropriate.
	 * 
	 * @param page
	 *          The page the node is on.
	 * @param start
	 *          The beginning position of the tag.
	 * @param end
	 *          The ending positiong of the tag.
	 * @param attributes
	 *          The attributes contained in this tag.
	 * @throws ParserException
	 *           If there is a problem encountered when creating the node.
	 * @return A tag node comprising the indicated characters from the page.
	 */
	Tag createTagNode(Page page, int start, int end, Vector attributes)
			throws ParserException;
}
