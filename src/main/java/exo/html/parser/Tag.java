package exo.html.parser;


public interface Tag extends Element {

	/**
   * Returns the value of an attribute.
   * @param name Name of attribute, case insensitive.
   * @return The value associated with the attribute or null if it does
   * not exist, or is a stand-alone.
   * @see #setAttribute
   */
  String getAttribute (String name);

  /**
   * Set attribute with given key, value pair.
   * Figures out a quote character to use if necessary.
   * @param key The name of the attribute.
   * @param value The value of the attribute.
   * @see #getAttribute
   * @see #setAttribute(String,String,char)
   */
  void setAttribute (String key, String value);

  /**
   * Set attribute with given key/value pair, the value is quoted by quote.
   * @param key The name of the attribute.
   * @param value The value of the attribute.
   * @param quote The quote character to be used around value.
   * If zero, it is an unquoted value.
   * @see #getAttribute
   */
  void setAttribute (String key, String value, char quote);

  /**
   * Remove the attribute with the given key, if it exists.
   * @param key The name of the attribute.
   */
  void removeAttribute (String key);

  /**
   * Return the name of this tag.
   * <p>
   * <em>
   * Note: This value is converted to uppercase and does not
   * begin with "/" if it is an end tag. Nor does it end with
   * a slash in the case of an XML type tag.
   * The conversion to uppercase is performed with an ENGLISH locale.
   * </em>
   * @return The tag name.
   * @see #setTagName
   */
  String getTagName ();

  /**
   * Set the name of this tag.
   * This creates or replaces the first attribute of the tag (the
   * zeroth element of the attribute vector).
   * @param name The tag name.
   * @see #getTagName
   */
  void setTagName (String name);
  
  /**
   * Determines if the given tag breaks the flow of text.
   * @return <code>true</code> if following text would start on a new line,
   * <code>false</code> otherwise.
   */
  boolean breaksFlow ();

  /**
   * Predicate to determine if this tag is an end tag (i.e. &lt;/HTML&gt;).
   * @return <code>true</code> if this tag is an end tag.
   */
  boolean isEndTag ();
  
  /**
   * Return the set of names handled by this tag.
   * Since this a a generic tag, it has no ids.
   * @return The names to be matched that create tags of this type.
   */
  String[] getIds ();

  /**
   * Return the set of tag names that cause this tag to finish.
   * These are the normal (non end tags) that if encountered while
   * scanning (a composite tag) will cause the generation of a virtual
   * tag.
   * Since this a a non-composite tag, the default is no enders.
   * @return The names of following tags that stop further scanning.
   */
  String[] getEnders ();

  /**
   * Return the set of end tag names that cause this tag to finish.
   * These are the end tags that if encountered while
   * scanning (a composite tag) will cause the generation of a virtual
   * tag.
   * Since this a a non-composite tag, it has no end tag enders.
   * @return The names of following end tags that stop further scanning.
   */
  String[] getEndTagEnders ();

  /**
   * Get the end tag for this (composite) tag.
   * For a non-composite tag this always returns <code>null</code>.
   * @return The tag that terminates this composite tag, i.e. &lt;/HTML&gt;.
   * @see #setEndTag
   */
  Tag getEndTag ();

  /**
   * Set the end tag for this (composite) tag.
   * For a non-composite tag this is a no-op.
   * @param tag The tag that closes this composite tag, i.e. &lt;/HTML&gt;.
   * @see #getEndTag
   */
  void setEndTag (Tag tag);
  /**
   * Return the scanner associated with this tag.
   * @return The scanner associated with this tag.
   * @see #setThisScanner
   */
  Scanner getThisScanner ();

  /**
   * Set the scanner associated with this tag.
   * @param scanner The scanner for this tag.
   * @see #getThisScanner
   */
  void setThisScanner (Scanner scanner);

  /**
   * Get the line number where this tag starts.
   * @return The (zero based) line number in the page where this tag starts.
   */
  int getStartingLineNumber ();
  /**
   * Get the line number where this tag ends.
   * @return The (zero based) line number in the page where this tag ends.
   */
  int getEndingLineNumber ();
}
