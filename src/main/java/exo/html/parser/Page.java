package exo.html.parser;

import java.io.IOException;
import java.io.Serializable;

import exo.html.parser.context.Cursor;
import exo.html.parser.processor.Source;
import exo.html.parser.processor.StringSource;
import exo.html.parser.util.ParserException;

@SuppressWarnings("serial")
public class Page implements Serializable {

	protected Source source;

	public static final char EOF = (char) Source.EOF;

	public Page(String text) {
		this.source = new StringSource(text);

	}

	/**
	 * Return a character. Handles end of lines (EOL) specially, retreating the
	 * cursor twice for the '\r\n' case. The cursor position is moved back by one
	 * (or two in the \r\n case).
	 * 
	 * @param cursor
	 *          The position to 'unread' at.
	 * @exception ParserException
	 *              If an IOException on the underlying source occurs.
	 */
	public void ungetCharacter(Cursor cursor) throws ParserException {
		int i;
		char ch;

		cursor.back();
		i = cursor.getPosition();
		try {
			ch = source.getCharacter(i);
			if (('\n' == ch) && (0 != i)) {
				ch = source.getCharacter(i - 1);
				if ('\r' == ch)
					cursor.back();
			}
		} catch (IOException ioe) {
			throw new ParserException("can't read a character at position "	+ cursor.getPosition(), ioe);
		}
	}

	/**
	 * Read the character at the given cursor position. The cursor position can be
	 * only behind or equal to the current source position. Returns end of lines
	 * (EOL) as \n, by converting \r and \r\n to \n, and updates the end-of-line
	 * index accordingly. Advances the cursor position by one (or two in the \r\n
	 * case).
	 * 
	 * @param cursor
	 *          The position to read at.
	 * @return The character at that position, and modifies the cursor to prepare
	 *         for the next read. If the source is exhausted a zero is returned.
	 * @exception ParserException
	 *              If an IOException on the underlying source occurs, or an
	 *              attempt is made to read characters in the future (the cursor
	 *              position is ahead of the underlying stream)
	 */
	public char getCharacter(Cursor cursor) throws ParserException {
		int i;
		int offset;
		char ret;

		i = cursor.getPosition();
		offset = source.offset();
		if (offset == i)
			try {
				i = source.read();
				if (Source.EOF == i)
					ret = EOF;
				else {
					ret = (char) i;
					cursor.next();
				}
			} catch (IOException ioe) {
				throw new ParserException("problem reading a character at position "
						+ cursor.getPosition(), ioe);
			}
		else if (offset > i) {
			// historic read
			try {
				ret = source.getCharacter(i);
			} catch (IOException ioe) {
				throw new ParserException("can't read a character at position " + i,
						ioe);
			}
			cursor.next();
		} else
			throw new ParserException(
					"attempt to read future characters from source " + i + " > "
							+ source.offset());

		// handle \r
		if ('\r' == ret) { // switch to single character EOL
			ret = '\n';

			// check for a \n in the next position
			if (source.offset() == cursor.getPosition())
				try {
					i = source.read();
					if (Source.EOF == i) {
						// do nothing
					} else if ('\n' == (char) i)
						cursor.next();
					else
						try {
							source.unread();
						} catch (IOException ioe) {
							throw new ParserException("can't unread a character at position "
									+ cursor.getPosition(), ioe);
						}
				} catch (IOException ioe) {
					throw new ParserException("problem reading a character at position "
							+ cursor.getPosition(), ioe);
				}
			else
				try {
					if ('\n' == source.getCharacter(cursor.getPosition()))
						cursor.next();
				} catch (IOException ioe) {
					throw new ParserException("can't read a character at position "
							+ cursor.getPosition(), ioe);
				}
		}
		//if ('\n' == ret)
			// update the EOL index in any case
			//mIndex.add(cursor);

		return (ret);
	}

	/**
	 * Put the text identified by the given limits into the given buffer.
	 * 
	 * @param buffer
	 *          The accumulator for the characters.
	 * @param start
	 *          The starting position, zero based.
	 * @param end
	 *          The ending position (exclusive, i.e. the character at the ending
	 *          position is not included), zero based.
	 * @exception IllegalArgumentException
	 *              If an attempt is made to get characters ahead of the current
	 *              source offset (character position).
	 */
	public void getText(StringBuffer buffer, int start, int end)
			throws IllegalArgumentException {
		int length;

		if ((source.offset() < start) || (source.offset() < end))
			throw new IllegalArgumentException(
					"attempt to extract future characters from source" + start + "|"
							+ end + " > " + source.offset());
		if (end < start) {
			length = end;
			end = start;
			start = length;
		}
		length = end - start;
		try {
			source.getCharacters(buffer, start, length);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("can't get the " + (end - start)
					+ "characters at position " + start + " - " + ioe.getMessage());
		}
	}

	/**
	 * Get the text identified by the given limits.
	 * 
	 * @param start
	 *          The starting position, zero based.
	 * @param end
	 *          The ending position (exclusive, i.e. the character at the ending
	 *          position is not included), zero based.
	 * @return The text from <code>start</code> to <code>end</code>.
	 * @see #getText(StringBuffer, int, int)
	 * @exception IllegalArgumentException
	 *              If an attempt is made to get characters ahead of the current
	 *              source offset (character position).
	 */
	public String getText(int start, int end) throws IllegalArgumentException {
		String ret;

		try {
			ret = source.getString(start, end - start);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("can't get the " + (end - start)
					+ "characters at position " + start + " - " + ioe.getMessage());
		}

		return (ret);
	}
	
	

}
