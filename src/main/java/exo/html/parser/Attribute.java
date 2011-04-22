package exo.html.parser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Attribute implements Serializable {

	/**
	 * The name of this attribute. The part before the equals sign, or the
	 * stand-alone attribute. This will be <code>null</code> if the attribute is
	 * whitespace.
	 */
	protected String name;

	/**
	 * The assignment string of the attribute. The equals sign. This will be
	 * <code>null</code> if the attribute is a stand-alone attribute.
	 */
	protected String assignment;

	/**
	 * The value of the attribute. The part after the equals sign. This will be
	 * <code>null</code> if the attribute is an empty or stand-alone attribute.
	 */
	protected String value;

	/**
	 * The quote, if any, surrounding the value of the attribute, if any. This
	 * will be zero if there are no quotes around the value.
	 */
	protected char quote;

	/**
	 * Create an attribute with the name, assignment, value and quote given. If
	 * the quote value is zero, assigns the value using {@link #setRawValue} which
	 * sets the quote character to a proper value if necessary.
	 * 
	 * @param name
	 *          The name of this attribute.
	 * @param assignment
	 *          The assignment string of this attribute.
	 * @param value
	 *          The value of this attribute.
	 * @param quote
	 *          The quote around the value of this attribute.
	 */
	public Attribute(String name, String assignment, String value, char quote) {
		setName(name);
		setAssignment(assignment);
		if (0 == quote)
			setRawValue(value);
		else {
			setValue(value);
			setQuote(quote);
		}
	}

	/**
	 * Create an attribute with the name, value and quote given. Uses an equals
	 * sign as the assignment string if the value is not <code>null</code>, and
	 * calls {@link #setRawValue} to get the correct quoting if <code>quote</code>
	 * is zero.
	 * 
	 * @param name
	 *          The name of this attribute.
	 * @param value
	 *          The value of this attribute.
	 * @param quote
	 *          The quote around the value of this attribute.
	 */
	public Attribute(String name, String value, char quote) {
		this(name, (null == value ? "" : "="), value, quote);
	}

	/**
	 * Create a whitespace attribute with the value given.
	 * 
	 * @param value
	 *          The value of this attribute.
	 * @exception IllegalArgumentException
	 *              if the value contains other than whitespace. To set a real
	 *              value use {@link #Attribute(String,String)}.
	 */
	public Attribute(String value) throws IllegalArgumentException {
		if (0 != value.trim().length())
			throw new IllegalArgumentException("non whitespace value");
		else {
			setName(null);
			setAssignment(null);
			setValue(value);
			setQuote((char) 0);
		}
	}

	/**
	 * Create an attribute with the name and value given. Uses an equals sign as
	 * the assignment string if the value is not <code>null</code>, and calls
	 * {@link #setRawValue} to get the correct quoting.
	 * 
	 * @param name
	 *          The name of this attribute.
	 * @param value
	 *          The value of this attribute.
	 */
	public Attribute(String name, String value) {
		this(name, (null == value ? "" : "="), value, (char) 0);
	}

	/**
	 * Create an attribute with the name, assignment string and value given. Calls
	 * {@link #setRawValue} to get the correct quoting.
	 * 
	 * @param name
	 *          The name of this attribute.
	 * @param assignment
	 *          The assignment string of this attribute.
	 * @param value
	 *          The value of this attribute.
	 */
	public Attribute(String name, String assignment, String value) {
		this(name, assignment, value, (char) 0);
	}

	/**
	 * Create an empty attribute. This will provide "" from the {@link #toString}
	 * and {@link #toString(StringBuffer)} methods.
	 */
	public Attribute() {
		this(null, null, null, (char) 0);
	}

	/**
	 * Get the name of this attribute. The part before the equals sign, or the
	 * contents of the stand-alone attribute.
	 * 
	 * @return The name, or <code>null</code> if it's just a whitepace
	 *         'attribute'.
	 * @see #setName
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the name of this attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the name in.
	 * @see #getName()
	 * @see #setName
	 */
	public void getName(StringBuffer buffer) {
		if (null != name)
			buffer.append(name);
	}

	/**
	 * Set the name of this attribute. Set the part before the equals sign, or the
	 * contents of the stand-alone attribute. <em>WARNING:</em> Setting this to
	 * <code>null</code> can result in malformed HTML if the assignment string is
	 * not <code>null</code>.
	 * 
	 * @param name
	 *          The new name.
	 * @see #getName
	 * @see #getName(StringBuffer)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the assignment string of this attribute. This is usually just an equals
	 * sign, but in poorly formed attributes it can include whitespace on either
	 * or both sides of an equals sign.
	 * 
	 * @return The assignment string.
	 * @see #setAssignment
	 */
	public String getAssignment() {
		return assignment;
	}

	/**
	 * Get the assignment string of this attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the assignment string in.
	 * @see #getAssignment()
	 * @see #setAssignment
	 */
	public void getAssignment(StringBuffer buffer) {
		if (null != assignment)
			buffer.append(assignment);
	}

	/**
	 * Set the assignment string of this attribute. <em>WARNING:</em> Setting this
	 * property to other than an equals sign or <code>null</code> will result in
	 * malformed HTML. In the case of a <code>null</code>, the {@link #setValue
	 * value} should also be set to <code>null</code>.
	 * 
	 * @param assignment
	 *          The new assignment string.
	 * @see #getAssignment
	 * @see #getAssignment(StringBuffer)
	 */
	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}

	/**
	 * Get the value of the attribute. The part after the equals sign, or the text
	 * if it's just a whitepace 'attribute'. <em>NOTE:</em> This does not include
	 * any quotes that may have enclosed the value when it was read. To get the
	 * un-stripped value use {@link #getRawValue}.
	 * 
	 * @return The value, or <code>null</code> if it's a stand-alone or empty
	 *         attribute, or the text if it's just a whitepace 'attribute'.
	 * @see #setValue
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Get the value of the attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the value in.
	 * @see #getValue()
	 * @see #setValue
	 */
	public void getValue(StringBuffer buffer) {
		if (null != value)
			buffer.append(value);
	}

	/**
	 * Set the value of the attribute. The part after the equals sign, or the text
	 * if it's a whitepace 'attribute'. <em>WARNING:</em> Setting this property to
	 * a value that needs to be quoted without also setting the quote character
	 * will result in malformed HTML.
	 * 
	 * @param value
	 *          The new value.
	 * @see #getValue
	 * @see #getValue(StringBuffer)
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Get the quote, if any, surrounding the value of the attribute, if any.
	 * 
	 * @return Either ' or " if the attribute value was quoted, or zero if there
	 *         are no quotes around it.
	 * @see #setQuote
	 */
	public char getQuote() {
		return (quote);
	}

	/**
	 * Get the quote, if any, surrounding the value of the attribute, if any.
	 * 
	 * @param buffer
	 *          The buffer to place the quote in.
	 * @see #getQuote()
	 * @see #setQuote
	 */
	public void getQuote(StringBuffer buffer) {
		if (0 != quote)
			buffer.append(quote);
	}

	/**
	 * Set the quote surrounding the value of the attribute. <em>WARNING:</em>
	 * Setting this property to zero will result in malformed HTML if the
	 * {@link #getValue value} needs to be quoted (i.e. contains whitespace).
	 * 
	 * @param quote
	 *          The new quote value.
	 * @see #getQuote
	 * @see #getQuote(StringBuffer)
	 */
	public void setQuote(char quote) {
		quote = quote;
	}

	/**
	 * Get the raw value of the attribute. The part after the equals sign, or the
	 * text if it's just a whitepace 'attribute'. This includes the quotes around
	 * the value if any.
	 * 
	 * @return The value, or <code>null</code> if it's a stand-alone attribute, or
	 *         the text if it's just a whitepace 'attribute'.
	 * @see #setRawValue
	 */
	public String getRawValue() {
		char quote;
		StringBuffer buffer;
		String ret;

		if (isValued()) {
			quote = getQuote();
			if (0 != quote) {
				buffer = new StringBuffer(); // todo: what is the value length?
				buffer.append(quote);
				getValue(buffer);
				buffer.append(quote);
				ret = buffer.toString();
			} else
				ret = getValue();
		} else
			ret = null;

		return (ret);
	}

	/**
	 * Get the raw value of the attribute. The part after the equals sign, or the
	 * text if it's just a whitepace 'attribute'. This includes the quotes around
	 * the value if any.
	 * 
	 * @param buffer
	 *          The string buffer to append the attribute value to.
	 * @see #getRawValue()
	 * @see #setRawValue
	 */
	public void getRawValue(StringBuffer buffer) {
		getQuote(buffer);
		getValue(buffer);
		getQuote(buffer);
	}

	/**
	 * Set the value of the attribute and the quote character. If the value is
	 * pure whitespace, assign it 'as is' and reset the quote character. If not,
	 * check for leading and trailing double or single quotes, and if found use
	 * this as the quote character and the inner contents of <code>value</code> as
	 * the real value. Otherwise, examine the string to determine if quotes are
	 * needed and an appropriate quote character if so. This may involve changing
	 * double quotes within the string to character references.
	 * 
	 * @param value
	 *          The new value.
	 * @see #getRawValue
	 * @see #getRawValue(StringBuffer)
	 */
	public void setRawValue(String value) {
		char ch;
		boolean needed;
		boolean singleq;
		boolean doubleq;
		String ref;
		StringBuffer buffer;
		char quote;

		quote = 0;
		if ((null != value) && (0 != value.trim().length())) {
			if (value.startsWith("'") && value.endsWith("'") && (2 <= value.length())) {
				quote = '\'';
				value = value.substring(1, value.length() - 1);
			} else if (value.startsWith("\"") && value.endsWith("\"")
					&& (2 <= value.length())) {
				quote = '"';
				value = value.substring(1, value.length() - 1);
			} else {
				// first determine if there's whitespace in the value
				// and while we're at it find a suitable quote character
				needed = false;
				singleq = true;
				doubleq = true;
				for (int i = 0; i < value.length(); i++) {
					ch = value.charAt(i);
					if ('\'' == ch) {
						singleq = false;
						needed = true;
					} else if ('"' == ch) {
						doubleq = false;
						needed = true;
					} else if (!('-' == ch) && !('.' == ch) && !('_' == ch)
							&& !(':' == ch) && !Character.isLetterOrDigit(ch)) {
						needed = true;
					}
				}

				// now apply quoting
				if (needed) {
					if (doubleq)
						quote = '"';
					else if (singleq)
						quote = '\'';
					else {
						// uh-oh, we need to convert some quotes into character
						// references, so convert all double quotes into &#34;
						quote = '"';
						ref = "&quot;"; // Translate.encode (quote);
						// JDK 1.4: value = value.replaceAll ("\"", ref);
						buffer = new StringBuffer(value.length() * (ref.length() - 1));
						for (int i = 0; i < value.length(); i++) {
							ch = value.charAt(i);
							if (quote == ch)
								buffer.append(ref);
							else
								buffer.append(ch);
						}
						value = buffer.toString();
					}
				}
			}
		}
		setValue(value);
		setQuote(quote);
	}

	/**
	 * Predicate to determine if this attribute is whitespace.
	 * 
	 * @return <code>true</code> if this attribute is whitespace,
	 *         <code>false</code> if it is a real attribute.
	 */
	public boolean isWhitespace() {
		return (null == getName());
	}

	/**
	 * Predicate to determine if this attribute has no equals sign (or value).
	 * 
	 * @return <code>true</code> if this attribute is a standalone attribute.
	 *         <code>false</code> if has an equals sign.
	 */
	public boolean isStandAlone() {
		return ((null != getName()) && (null == getAssignment()));
	}

	/**
	 * Predicate to determine if this attribute has an equals sign but no value.
	 * 
	 * @return <code>true</code> if this attribute is an empty attribute.
	 *         <code>false</code> if has an equals sign and a value.
	 */
	public boolean isEmpty() {
		return ((null != getAssignment()) && (null == getValue()));
	}

	/**
	 * Predicate to determine if this attribute has a value.
	 * 
	 * @return <code>true</code> if this attribute has a value. <code>false</code>
	 *         if it is empty or standalone.
	 */
	public boolean isValued() {
		return (null != getValue());
	}

	/**
	 * Get the length of the string value of this attribute.
	 * 
	 * @return The number of characters required to express this attribute.
	 */
	public int getLength() {
		String name;
		String assignment;
		String value;
		char quote;
		int ret;

		ret = 0;
		name = getName();
		if (null != name)
			ret += name.length();
		assignment = getAssignment();
		if (null != assignment)
			ret += assignment.length();
		value = getValue();
		if (null != value)
			ret += value.length();
		quote = getQuote();
		if (0 != quote)
			ret += 2;

		return (ret);
	}

	/**
	 * Get a text representation of this attribute. Suitable for insertion into a
	 * tag, the output is one of the forms: <code>
	 * <pre>
	 * value
	 * name
	 * name=
	 * name=value
	 * name='value'
	 * name="value"
	 * </pre>
	 * </code>
	 * 
	 * @return A string that can be used within a tag.
	 */
	public String toString() {
		int length;
		StringBuffer ret;

		// get the size to avoid extra StringBuffer allocations
		length = getLength();
		ret = new StringBuffer(length);
		toString(ret);

		return (ret.toString());
	}

	/**
	 * Get a text representation of this attribute.
	 * 
	 * @param buffer
	 *          The accumulator for placing the text into.
	 * @see #toString()
	 */
	public void toString(StringBuffer buffer) {
		getName(buffer);
		getAssignment(buffer);
	}
}
