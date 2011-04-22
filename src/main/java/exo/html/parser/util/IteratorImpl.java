package exo.html.parser.util;

import exo.html.parser.Element;
import exo.html.parser.Page;
import exo.html.parser.Processor;
import exo.html.parser.Scanner;
import exo.html.parser.Tag;
import exo.html.parser.context.Cursor;
import exo.html.parser.processor.Request;

public class IteratorImpl implements ElementIterator {

	private Cursor cursor;
	private Processor processor;

	private Request request;

	public IteratorImpl(Request request, Processor processor) {
		this.cursor = request.getContext().getCursor();
		this.processor = processor;
	}

	/**
	 * Check if more elements are available.
	 * 
	 * @return <code>true</code> if a call to <code>nextNode()</code> will
	 *         succeed.
	 */
	public boolean hasMoreElements() throws ParserException {
		boolean ret;

		cursor.setPosition(request.getContext().getPosition());
		ret = Page.EOF != request.getPage().getCharacter(cursor); // more
																															// characters?

		return (ret);
	}

	/**
	 * Get the next node.
	 * 
	 * @return The next node in the HTML stream, or null if there are no more
	 *         nodes.
	 * @exception ParserException
	 *              If an unrecoverable error occurs.
	 */
	public Element nextElement() throws ParserException {
		Tag tag;
		Scanner scanner;
		ElementList stack;
		Element ret;

		try {
			ret = processor.nextElement();
			if (null != ret) {
				// kick off recursion for the top level node
				if (ret instanceof Tag) {
					tag = (Tag) ret;
					if (!tag.isEndTag()) {
						// now recurse if there is a scanner for this type of tag
						scanner = tag.getThisScanner();
						if (null != scanner) {
							stack = new ElementList();
							ret = scanner.scan(tag, processor, stack);
						}
					}
				}
			}
		} catch (ParserException pe) {
			throw pe; // no need to wrap an existing ParserException
		} catch (Exception e) {
			StringBuffer msgBuffer = new StringBuffer();
			msgBuffer.append("Unexpected Exception occurred while reading ");
			//msgBuffer.append(processor.getPage.getText());
			msgBuffer.append(", in nextElement");
			ParserException ex = new ParserException(msgBuffer.toString(), e);
			//mFeedback.error(msgBuffer.toString(), ex);
			throw ex;
		}

		return (ret);
	}

}
