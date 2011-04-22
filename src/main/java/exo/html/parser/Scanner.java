package exo.html.parser;

import exo.html.parser.util.ElementList;
import exo.html.parser.util.ParserException;



public interface Scanner {

	/**
   * Scan the tag.
   * The Processor is provided in order to do a lookahead operation.
   * @param tag HTML tag to be scanned for identification.
   * @param processor Provides html page access.
   * @param stack The parse stack. May contain pending tags that enclose
   * this tag. Nodes on the stack should be considered incomplete.
   * @return The resultant tag (may be unchanged).
   * @exception ParserException if an unrecoverable problem occurs.
   */
  public Tag scan (Tag tag, Processor processor, ElementList stack) throws ParserException;
}
