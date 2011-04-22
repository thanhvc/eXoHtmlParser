package exo.html.parser.util;

import exo.html.parser.Element;

public interface ElementIterator {

	/**
   * Check if more nodes are available.
   * @return <code>true</code> if a call to <code>nextHTMLNode()</code> will succeed.
   */
  public boolean hasMoreElements() throws ParserException;

  /**
   * Get the next element.
   * @return The next node in the HTML stream, or null if there are no more nodes.
   */
  public Element nextElement() throws ParserException;
}
