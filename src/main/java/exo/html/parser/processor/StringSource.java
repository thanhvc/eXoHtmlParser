package exo.html.parser.processor;

import java.io.IOException;

public class StringSource extends Source {

	protected String text;

	/**
	 * The current offset into the string.
	 */
	protected int offset;

	/**
	 * The bookmark
	 */
	protected int mark;

	public StringSource(String text) {
		this.text = text;
		this.offset = 0;
		this.mark = -1;
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read(char[] cbuf) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Undo the read of a single character.
	 * 
	 * @exception IOException
	 *              If no characters have been read or the source is closed.
	 */
	public void unread() throws IOException {
		if (null == text)
			throw new IOException("source is closed");
		else if (offset <= 0)
			throw new IOException("can't unread no characters");
		else
			offset--;
	}

	@Override
	public boolean ready() throws IOException {
		return (offset < text.length());
	}

	@Override
	public boolean markSupported() {

		return true;
	}

	@Override
	public char getCharacter(int offset) throws IOException {
		char ret;

		if (null == text)
			throw new IOException("source is closed");
		else if (offset >= offset)
			throw new IOException("read beyond current offset");
		else
			ret = text.charAt(offset);

		return (ret);
	}

	/**
	 * Append characters already read into a <code>StringBuffer</code>. Asking for
	 * characters ahead of {@link #offset()} will throw an exception.
	 * 
	 * @param buffer
	 *          The buffer to append to.
	 * @param offset
	 *          The offset of the first character.
	 * @param length
	 *          The number of characters to retrieve.
	 * @exception IOException
	 *              If the source is closed or an attempt is made to read beyond
	 *              {@link #offset()}.
	 */
	public void getCharacters(StringBuffer buffer, int offset, int length)
			throws IOException {
		if (null == text)
			throw new IOException("source is closed");
		else {
			if (offset + length > offset)
				throw new IOException("read beyond end of string");
			else
				buffer.append(text.substring(offset, offset + length));
		}
	}

	@Override
	public String getString(int offset, int length) throws IOException {
		String ret;

		if (null == text)
			throw new IOException("source is closed");
		else {
			if (offset + length > offset)
				throw new IOException("read beyond end of string");
			else
				ret = text.substring(offset, offset + length);
		}

		return (ret);
	}

	/**
	 * Get the position (in characters).
	 * 
	 * @return The number of characters that have already been read, or
	 *         {@link #EOF EOF} if the source is closed.
	 */
	@Override
	public int offset() {
		int ret;

		if (null == text)
			ret = EOF;
		else
			ret = offset;

		return (ret);
	}
	
	public String getText() {
		return text;
	}

	@Override
	public int avaiable() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {

	}

}
