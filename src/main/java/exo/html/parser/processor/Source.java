package exo.html.parser.processor;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Source extends Reader implements Serializable {

	public static final int EOF = -1;
	
	/**
	 * Read a single character
	 */
	public abstract int read() throws IOException;
	
	public abstract int read(char[] cbuf, int off, int len) throws IOException;
	
	public abstract int read(char[] cbuf) throws IOException;
	
	 /**
   * Undo the read of a single character.
   * @exception IOException If the source is closed or no characters have
   * been read.
   */
  public abstract void unread () throws IOException;
  
	
	public abstract boolean ready() throws IOException;
	
	public abstract boolean markSupported();
	
	public abstract char getCharacter(int offset) throws IOException;
	/**
   * Append characters already read into a <code>StringBuffer</code>.
   * @param buffer The buffer to append to.
   * @param offset The offset of the first character.
   * @param length The number of characters to retrieve.
   * @exception IOException If the source is closed or the offset or
   * (offset + length) is beyond {@link #offset()}.
   */
  public abstract void getCharacters (StringBuffer buffer, int offset, int length) throws IOException;
  
	
	public abstract String getString(int offset, int length) throws IOException;
	
	public abstract int offset();
	
	public abstract int avaiable();
	
	public abstract String getText();

	
	
}
