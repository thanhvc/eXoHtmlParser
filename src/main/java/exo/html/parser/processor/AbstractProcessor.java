package exo.html.parser.processor;

import java.util.Vector;

import exo.html.parser.Context;
import exo.html.parser.Element;
import exo.html.parser.ElementFactory;
import exo.html.parser.Page;
import exo.html.parser.Processor;
import exo.html.parser.context.Cursor;
import exo.html.parser.util.ElementIterator;
import exo.html.parser.util.ParserException;

public abstract class AbstractProcessor implements Processor {

	/**
   * Process remarks strictly flag.
   * If <code>true</code>, remarks are not terminated by ---$gt;
   * or --!$gt;, i.e. more than two dashes. If <code>false</code>,
   * a more lax (and closer to typical browser handling) remark parsing
   * is used.
   * Default <code>true</code>.
   */
  public static boolean STRICT_REMARKS = true;
  
	protected ElementFactory factory;
	protected Cursor cursor;
	protected Page page;
	protected Context context;
	protected ElementIterator iterator;

	/**
	 * Parse a string element. Scan characters until "&lt;/", "&lt;%", "&lt;!" or
	 * &lt; followed by a letter is encountered, or the input stream is exhausted,
	 * in which case <code>null</code> is returned.
	 * 
	 * @param start
	 *          The position at which to start scanning.
	 * @param quotesmart
	 *          If <code>true</code>, strings ignore quoted contents.
	 * @return The parsed node.
	 * @exception ParserException
	 *              If a problem occurs reading from the source.
	 */
	protected Element parseString(int start, boolean quotesmart)
			throws ParserException {
		boolean done = false;
		char ch;
		char quote = 0;

		while (!done) {
			ch = page.getCharacter(cursor);
			if (Page.EOF == ch)
				done = true;
			else if (0x1b == ch) {// escape
				ch = page.getCharacter(cursor);
				if (Page.EOF == ch) {
					done = true;
				} else if ('$' == ch) {
					ch = page.getCharacter(cursor);
					if (Page.EOF == ch) {
						done = true;
					} else if ('B' == ch) {
						// scanJIS(cursor);
					} else {
						page.ungetCharacter(cursor);
						page.ungetCharacter(cursor);
					}
				} else
					page.ungetCharacter(cursor);
			} else if (quotesmart && (0 == quote) && (('\'' == ch) || ('"' == ch)))
				quote = ch; // enter quoted state
			else if (quotesmart && (0 != quote) && ('\\' == ch)) {
				ch = page.getCharacter(cursor); // try to consume escape
				if ((Page.EOF != ch) && ('\\' != ch) // escaped backslash
						&& (ch != quote)) // escaped quote character
					// ( reflects ["] or ['] whichever opened the quotation)
					page.ungetCharacter(cursor); // not consume char if char not an escape
			} else if (quotesmart && (ch == quote))
				quote = 0; // exit quoted state
			else if (quotesmart && (0 == quote) && (ch == '/')) {
				// handle multiline and double slash comments (with a quote)
				// in script like:
				// I can't handle single quotations.
				ch = page.getCharacter(cursor);
				if (Page.EOF == ch)
					done = true;
				else if ('/' == ch) {
					do
						ch = page.getCharacter(cursor);
					while ((Page.EOF != ch) && ('\n' != ch));
				} else if ('*' == ch) {
					do {
						do
							ch = page.getCharacter(cursor);
						while ((Page.EOF != ch) && ('*' != ch));
						ch = page.getCharacter(cursor);
						if (ch == '*')
							page.ungetCharacter(cursor);
					} while ((Page.EOF != ch) && ('/' != ch));
				} else
					page.ungetCharacter(cursor);
			} else if ((0 == quote) && ('<' == ch)) {
				ch = page.getCharacter(cursor);
				if (Page.EOF == ch)
					done = true;
				// the order of these tests might be optimized for speed:
				else if ('/' == ch || Character.isLetter(ch) || '!' == ch || '%' == ch
						|| '?' == ch) {
					done = true;
					page.ungetCharacter(cursor);
					page.ungetCharacter(cursor);
				} else {
					// it's not a tag, so keep going, but check for quotes
					page.ungetCharacter(cursor);
				}
			}
		}

		return (makeString(start, cursor.getPosition()));
	}

	/**
	 * Create a string node based on the current cursor and the one provided.
	 * 
	 * @param start
	 *          The starting point of the node.
	 * @param end
	 *          The ending point of the node.
	 * @exception ParserException
	 *              If the nodefactory creation of the text node fails.
	 * @return The new Text node.
	 */
	protected Element makeString(int start, int end) throws ParserException {
		int length;
		Element ret;

		length = end - start;
		if (0 != length)
			// got some characters
			ret = getElementFactory().createStringNode(page, start, end);
		else
			ret = null;

		return (ret);
	}

	/**
	 * Create a remark node based on the current cursor and the one provided.
	 * 
	 * @param start
	 *          The starting point of the node.
	 * @param end
	 *          The ending point of the node.
	 * @exception ParserException
	 *              If the nodefactory creation of the remark node fails.
	 * @return The new Remark node.
	 */
	protected Element makeRemark(int start, int end) throws ParserException {
		Element ret;

		int length = end - start;
		if (0 != length) { // return tag based on second character, '/', '%', Letter
												// (ch), '!'
			if (2 > length)
				// this is an error
				return (makeString(start, end));
			ret = getElementFactory().createRemarkNode(this.page, start, end);
		} else
			ret = null;

		return (ret);
	}

	/**
	 * This method uses a state machine with the following states:
	 * <ol>
	 * <li>state 0 - outside of any attribute</li>
	 * <li>state 1 - within attributre name</li>
	 * <li>state 2 - equals hit</li>
	 * <li>state 3 - within naked attribute value.</li>
	 * <li>state 4 - within single quoted attribute value</li>
	 * <li>state 5 - within double quoted attribute value</li>
	 * <li>state 6 - whitespaces after attribute name could lead to state 2 (=)or
	 * state 0</li>
	 * </ol>
	 * <p>
	 * 
	 * @param context
	 * @param start
	 * @return
	 * @throws ParserException
	 */
	protected Element parseTag(int start) throws ParserException {
		boolean done = false;
		char ch;
		int state = 0;
		int[] bookmarks = new int[8];
		Vector attributes = new Vector();
		
		bookmarks[0] = cursor.getPosition();
		while (!done) {
			bookmarks[state + 1] = cursor.getPosition();
			ch = page.getCharacter(cursor);
			switch (state) {
			case 0: // outside of any attribute
				if ((Page.EOF == ch) || ('>' == ch) || ('<' == ch)) {
					if ('<' == ch) {
						// don't consume the opening angle
						page.ungetCharacter(cursor);
						bookmarks[state + 1] = cursor.getPosition();
					}
					whitespace(context, attributes, bookmarks);
					done = true;
				} else if (!Character.isWhitespace(ch)) {
					whitespace(context, attributes, bookmarks);
					state = 1;
				}
				break;
			case 1: // within attribute name
				if ((Page.EOF == ch) || ('>' == ch) || ('<' == ch)) {
					if ('<' == ch) {
						// don't consume the opening angle
						page.ungetCharacter(cursor);
						bookmarks[state + 1] = cursor.getPosition();
					}
					standalone(context, attributes, bookmarks);
					done = true;
				} else if (Character.isWhitespace(ch)) {
					bookmarks[6] = bookmarks[2]; // setting the bookmark[0] is done in
																				// state 6 if applicable
					state = 6;
				} else if ('=' == ch)
					state = 2;
				break;
			case 2: // equals hit
				if ((Page.EOF == ch) || ('>' == ch)) {
					empty(context, attributes, bookmarks);
					done = true;
				} else if ('\'' == ch) {
					state = 4;
					bookmarks[4] = bookmarks[3];
				} else if ('"' == ch) {
					state = 5;
					bookmarks[5] = bookmarks[3];
				} else if (Character.isWhitespace(ch)) {
					// collect white spaces after "=" into the assignment string;
					// do nothing
				} else
					state = 3;
				break;
			case 3: // within naked attribute value
				if ((Page.EOF == ch) || ('>' == ch)) {
					naked(context, attributes, bookmarks);
					done = true;
				} else if (Character.isWhitespace(ch)) {
					naked(context, attributes, bookmarks);
					bookmarks[0] = bookmarks[4];
					state = 0;
				}
				break;
			case 4: // within single quoted attribute value
				if (Page.EOF == ch) {
					single_quote(context, attributes, bookmarks);
					done = true; // complain?
				} else if ('\'' == ch) {
					single_quote(context, attributes, bookmarks);
					bookmarks[0] = bookmarks[5] + 1;
					state = 0;
				}
				break;
			case 5: // within double quoted attribute value
				if (Page.EOF == ch) {
					double_quote(context, attributes, bookmarks);
					done = true; // complain?
				} else if ('"' == ch) {
					double_quote(context, attributes, bookmarks);
					bookmarks[0] = bookmarks[6] + 1;
					state = 0;
				}
				break;
			case 6: // undecided for state 0 or 2
							// we have read white spaces after an attributte name
				if (Page.EOF == ch) {
					// same as last else clause
					standalone(context, attributes, bookmarks);
					bookmarks[0] = bookmarks[6];
					page.ungetCharacter(cursor);
					state = 0;
				} else if (Character.isWhitespace(ch)) {
					// proceed
				} else if ('=' == ch) // the white spaces belonged to the equal.
				{
					bookmarks[2] = bookmarks[6];
					bookmarks[3] = bookmarks[7];
					state = 2;
				} else {
					// white spaces were not ended by equal
					// meaning the attribute was a stand alone attribute
					// now: create the stand alone attribute and rewind
					// the cursor to the end of the white spaces
					// and restart scanning as whitespace attribute.
					standalone(context, attributes, bookmarks);
					bookmarks[0] = bookmarks[6];
					page.ungetCharacter(cursor);
					state = 0;
				}
				break;
			default:
				throw new IllegalStateException("Nothing for " + state);
			}
		}

		return (makeTag(start, cursor.getPosition(), attributes));
	}

	/*
	 * 
	 * @param start The position at which to start scanning.
	 * 
	 * @param quotesmart If <code>true</code>, strings ignore quoted contents.
	 * 
	 * @return The parsed node.
	 * 
	 * @exception ParserException If a problem occurs reading from the source.
	 */
	protected Element parseRemark(int start, boolean quotesmart)
			throws ParserException {
		boolean done;
		char ch;
		int state;

		done = false;
		state = 0;
		while (!done) {
			ch = page.getCharacter(cursor);
			if (Page.EOF == ch)
				done = true;
			else
				switch (state) {
				case 0: // prior to the first open delimiter
					if ('>' == ch)
						done = true;
					if ('-' == ch)
						state = 1;
					else
						return (parseString(start, quotesmart));
					break;
				case 1: // prior to the second open delimiter
					if ('-' == ch) {
						// handle <!--> because netscape does
						ch = page.getCharacter(cursor);
						if (Page.EOF == ch)
							done = true;
						else if ('>' == ch)
							done = true;
						else {
							page.ungetCharacter(cursor);
							state = 2;
						}
					} else
						return (parseString(start, quotesmart));
					break;
				case 2: // prior to the first closing delimiter
					if ('-' == ch)
						state = 3;
					else if (Page.EOF == ch)
						return (parseString(start, quotesmart)); // no terminator
					break;
				case 3: // prior to the second closing delimiter
					if ('-' == ch)
						state = 4;
					else
						state = 2;
					break;
				case 4: // prior to the terminating >
					if ('>' == ch)
						done = true;
					else if (Character.isWhitespace(ch)) {
						// stay in state 4
					} else if (!STRICT_REMARKS && (('-' == ch) || ('!' == ch))) {
						// stay in state 4
					} else
						// bug #1345049 HTMLParser should not terminate a comment with --->
						// should maybe issue a warning mentioning STRICT_REMARKS
						state = 2;
					break;
				default:
					throw new IllegalStateException("how the fuck did we get in state "
							+ state);
				}
		}

		return (makeRemark(start, cursor.getPosition()));
	}

	/**
	 * Create a tag element based on the current cursor and the one provided.
	 * 
	 * @param start
	 *          The starting point of the node.
	 * @param end
	 *          The ending point of the node.
	 * @param attributes
	 *          The attributes parsed from the tag.
	 * @exception ParserException
	 *              If the elementfactory creation of the tag node fails.
	 * @return The new Tag element.
	 */
	protected Element makeTag(int start, int end,
			Vector attributes) throws ParserException {
		int length;
		Element ret;

		length = end - start;
		if (0 != length) { // return tag based on second character, '/', '%', Letter
												// (ch), '!'
			if (2 > length)
				// this is an error
				return (makeString(start, end));
			ret = getElementFactory().createTagNode(this.page, start, end, attributes);
		} else
			ret = null;

		return ret;
	}

	/**
	 * Generate an empty attribute -- color=.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void empty(Context context, Vector attributes, int[] bookmarks) {
		attributes.addElement(new PageAttribute(context, bookmarks[1],
				bookmarks[2], bookmarks[2] + 1, -1, (char) 0));
	}

	/**
	 * Generate a standalone attribute -- font.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void standalone(Context context, Vector attributes, int[] bookmarks) {
		attributes.addElement(new PageAttribute(context, bookmarks[1],
				bookmarks[2], -1, -1, (char) 0));
	}

	/**
	 * Generate an unquoted attribute -- size=1.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void naked(Context context, Vector attributes, int[] bookmarks) {
		attributes.addElement(new PageAttribute(context, bookmarks[1],
				bookmarks[2], bookmarks[3], bookmarks[4], (char) 0));
	}

	/**
	 * Generate a whitespace 'attribute',
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void whitespace(Context context, Vector attributes, int[] bookmarks) {
		if (bookmarks[1] > bookmarks[0])
			attributes.addElement(new PageAttribute(context, -1, -1, bookmarks[0],
					bookmarks[1], (char) 0));
	}

	/**
	 * Generate an single quoted attribute -- width='100%'.
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void single_quote(Context context, Vector attributes, int[] bookmarks) {
		attributes.addElement(new PageAttribute(context, bookmarks[1],
				bookmarks[2], bookmarks[4] + 1, bookmarks[5], '\''));
	}

	/**
	 * Generate an double quoted attribute -- CONTENT="Test Development".
	 * 
	 * @param attributes
	 *          The list so far.
	 * @param bookmarks
	 *          The array of positions.
	 */
	private void double_quote(Context context, Vector attributes, int[] bookmarks) {
		attributes.addElement(new PageAttribute(context, bookmarks[1],
				bookmarks[2], bookmarks[5] + 1, bookmarks[6], '"'));
	}

	/**
	 * Get the current node factory.
	 * 
	 * @return The processor's element factory.
	 */
	public ElementFactory getElementFactory() {
		return (factory);
	}
	
	public abstract ElementIterator elements() throws ParserException;

}
