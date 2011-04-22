package exo.html.parser.processor;

import exo.html.parser.Attribute;
import exo.html.parser.Context;
import exo.html.parser.Page;

@SuppressWarnings("serial")
public class PageAttribute extends Attribute {

	protected Context context;
	protected int nameStart;
	protected int nameEnd;

	protected int valueStart;
	protected int valueEnd;

	public PageAttribute(Context context, int nameStartArg, int nameEndArg,
			int valueStartArg, int valueEndArg, char quote) {

		this.context = context;
		this.nameStart = nameStartArg;
		this.nameEnd = nameEndArg;
		this.valueStart = valueStartArg;
		this.valueEnd = valueEndArg;

		setName(null);
		setAssignment(null);
		setValue(null);
		setQuote(quote);
	}

	private void init() {
		context = null;
		this.nameStart = -1;
		this.nameEnd = -1;
		this.valueStart = -1;
		this.valueEnd = -1;

	}

	/**
	 * Create an attribute with the name, assignment string, value and quote
	 * given. If the quote value is zero, assigns the value using
	 * {@link #setRawValue} which sets the quote character to a proper value if
	 * necessary.
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
	public PageAttribute(String name, String assignment, String value, char quote) {
		super(name, assignment, value, quote);
		init();
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
	public PageAttribute(String name, String value, char quote) {
		super(name, value, quote);
		init();
	}

	/**
	 * Create a whitespace attribute with the value given.
	 * 
	 * @param value
	 *          The value of this attribute.
	 * @exception IllegalArgumentException
	 *              if the value contains other than whitespace. To set a real
	 *              value use {@link #PageAttribute(String,String)}.
	 */
	public PageAttribute(String value) throws IllegalArgumentException {
		super(value);
		init();
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
	public PageAttribute(String name, String value) {
		super(name, value);
		init();
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
	public PageAttribute(String name, String assignment, String value) {
		super(name, assignment, value);
		init();
	}

	/**
	 * Create an empty attribute. This will provide "" from the {@link #toString}
	 * and {@link #toString(StringBuffer)} methods.
	 */
	public PageAttribute() {
		super();
		init();
	}

	/**
	 * Get the name of this attribute. The part before the equals sign, or the
	 * contents of the stand-alone attribute.
	 * 
	 * @return The name, or <code>null</code> if it's just a whitepace
	 *         'attribute'.
	 */
	public String getName() {
		String ret;

		ret = super.getName();
		if (null == ret) {
			if ((null != context.getPage()) && (0 <= nameStart)) {
				ret = context.getPage().getText(nameStart, nameEnd);
				setName(ret); // cache the value
			}
		}

		return (ret);
	}

	/**
	 * Get the name of this attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the name in.
	 * @see #getName()
	 */
	public void getName(StringBuffer buffer) {
		String name;

		name = super.getName();
		if (null == name) {
			if ((null != context.getPage()) && (0 <= nameStart))
				context.getPage().getText(buffer, nameStart, nameEnd);
		} else
			buffer.append(name);
	}

	/**
	 * Get the assignment string of this attribute. This is usually just an equals
	 * sign, but in poorly formed attributes it can include whitespace on either
	 * or both sides of an equals sign.
	 * 
	 * @return The assignment string.
	 */
	public String getAssignment() {
		String ret;

		ret = super.getAssignment();
		if (null == ret) {
			if ((null != context.getPage()) && (0 <= nameEnd) && (0 <= valueStart)) {
				ret = context.getPage().getText(nameEnd, valueStart);
				// remove a possible quote included in the assignment
				// since valueStart points at the real start of the value
				if (ret.endsWith("\"") || ret.endsWith("'"))
					ret = ret.substring(0, ret.length() - 1);
				setAssignment(ret); // cache the value
			}
		}

		return (ret);
	}

	/**
	 * Get the assignment string of this attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the assignment string in.
	 * @see #getAssignment()
	 */
	public void getAssignment(StringBuffer buffer) {
		int length;
		char ch;
		String assignment;

		assignment = super.getAssignment();
		if (null == assignment) {
			if ((null != context.getPage()) && (0 <= nameEnd) && (0 <= valueStart)) {
				context.getPage().getText(buffer, nameEnd, valueStart);
				// remove a possible quote included in the assignment
				// since valueStart points at the real start of the value
				length = buffer.length() - 1;
				ch = buffer.charAt(length);
				if (('\'' == ch) || ('"' == ch))
					buffer.setLength(length);
			}
		} else
			buffer.append(assignment);
	}

	/**
	 * Get the value of the attribute. The part after the equals sign, or the text
	 * if it's just a whitepace 'attribute'. <em>NOTE:</em> This does not include
	 * any quotes that may have enclosed the value when it was read. To get the
	 * un-stripped value use {@link #getRawValue}.
	 * 
	 * @return The value, or <code>null</code> if it's a stand-alone or empty
	 *         attribute, or the text if it's just a whitepace 'attribute'.
	 */
	public String getValue() {
		String ret;

		ret = super.getValue();
		if (null == ret) {
			if ((null != context.getPage()) && (0 <= valueEnd)) {
				ret = context.getPage().getText(valueStart, valueEnd);
				setValue(ret); // cache the value
			}
		}

		return (ret);
	}

	/**
	 * Get the value of the attribute.
	 * 
	 * @param buffer
	 *          The buffer to place the value in.
	 * @see #getValue()
	 */
	public void getValue(StringBuffer buffer) {
		String value;

		value = super.getValue();
		if (null == value) {
			if ((null != context.getPage()) && (0 <= valueEnd))
				context.getPage().getText(buffer, nameStart, nameEnd);
		} else
			buffer.append(value);
	}

	/**
	 * Get the raw value of the attribute. The part after the equals sign, or the
	 * text if it's just a whitepace 'attribute'. This includes the quotes around
	 * the value if any.
	 * 
	 * @return The value, or <code>null</code> if it's a stand-alone attribute, or
	 *         the text if it's just a whitepace 'attribute'.
	 */
	public String getRawValue() {
		char quote;
		StringBuffer buffer;
		String ret;

		ret = getValue();
		if (null != ret && (0 != (quote = getQuote()))) {
			buffer = new StringBuffer(ret.length() + 2);
			buffer.append(quote);
			buffer.append(ret);
			buffer.append(quote);
			ret = buffer.toString();
		}

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
	 */
	public void getRawValue(StringBuffer buffer) {
		char quote;

		if (null == value) {
			if (0 <= valueEnd) {
				if (0 != (quote = getQuote()))
					buffer.append(quote);
				if (valueStart != valueEnd)
					context.getPage().getText(buffer, valueStart, valueEnd);
				if (0 != quote)
					buffer.append(quote);
			}
		} else {
			if (0 != (quote = getQuote()))
				buffer.append(quote);
			buffer.append(value);
			if (0 != quote)
				buffer.append(quote);
		}
	}

	/**
	 * Get the page this attribute is anchored to, if any.
	 * 
	 * @return The page used to construct this attribute, or null if this is just
	 *         a regular attribute.
	 */
	public Page getPage() {
		return (context.getPage());
	}

	/**
	 * Get the starting position of the attribute name.
	 * 
	 * @return The offset into the page at which the name begins.
	 */
	public int getNameStartPosition() {
		return (nameStart);
	}

	/**
	 * Set the starting position of the attribute name.
	 * 
	 * @param start
	 *          The new offset into the page at which the name begins.
	 */
	public void setNameStartPosition(int start) {
		nameStart = start;
		setName(null); // uncache value
	}

	/**
	 * Get the ending position of the attribute name.
	 * 
	 * @return The offset into the page at which the name ends.
	 */
	public int getNameEndPosition() {
		return (nameEnd);
	}

	/**
	 * Set the ending position of the attribute name.
	 * 
	 * @param end
	 *          The new offset into the page at which the name ends.
	 */
	public void setNameEndPosition(int end) {
		nameEnd = end;
		setName(null); // uncache value
		setAssignment(null); // uncache value
	}

	/**
	 * Get the starting position of the attribute value.
	 * 
	 * @return The offset into the page at which the value begins.
	 */
	public int getValueStartPosition() {
		return (valueStart);
	}

	/**
	 * Set the starting position of the attribute value.
	 * 
	 * @param start
	 *          The new offset into the page at which the value begins.
	 */
	public void setValueStartPosition(int start) {
		valueStart = start;
		setAssignment(null); // uncache value
		setValue(null); // uncache value
	}

	/**
	 * Get the ending position of the attribute value.
	 * 
	 * @return The offset into the page at which the value ends.
	 */
	public int getValueEndPosition() {
		return (valueEnd);
	}

	/**
	 * Set the ending position of the attribute value.
	 * 
	 * @param end
	 *          The new offset into the page at which the value ends.
	 */
	public void setValueEndPosition(int end) {
		valueEnd = end;
		setValue(null); // uncache value
	}

	/**
	 * Predicate to determine if this attribute is whitespace.
	 * 
	 * @return <code>true</code> if this attribute is whitespace,
	 *         <code>false</code> if it is a real attribute.
	 */
	public boolean isWhitespace() {
		return (((null == super.getName()) && (null == context.getPage())) || ((null != context
				.getPage()) && (0 > nameStart)));
	}

	/**
	 * Predicate to determine if this attribute has no equals sign (or value).
	 * 
	 * @return <code>true</code> if this attribute is a standalone attribute.
	 *         <code>false</code> if has an equals sign.
	 */
	public boolean isStandAlone() {
		return (!isWhitespace() // not whitespace
				&& (null == super.getAssignment()) // and no explicit assignment
																						// provided
				&& !isValued() // and has no value
		&& ((null == context.getPage()) // and either its not coming from a page
		// or it is coming from a page and it doesn't have an assignment part
		|| ((null != context.getPage()) && (0 <= nameEnd) && (0 > valueStart))));
	}

	/**
	 * Predicate to determine if this attribute has an equals sign but no value.
	 * 
	 * @return <code>true</code> if this attribute is an empty attribute.
	 *         <code>false</code> if has an equals sign and a value.
	 */
	public boolean isEmpty() {
		return (!isWhitespace() // not whitespace
				&& !isStandAlone() // and not standalone
				&& (null == super.getValue()) // and no explicit value provided
		&& ((null == context.getPage()) // and either its not coming from a page
		// or it is coming from a page and has no value
		|| ((null != context.getPage()) && (0 > valueEnd))));
	}

	/**
	 * Predicate to determine if this attribute has a value.
	 * 
	 * @return <code>true</code> if this attribute has a value. <code>false</code>
	 *         if it is empty or standalone.
	 */
	public boolean isValued() {
		return ((null != super.getValue()) // an explicit value provided
		// or it is coming from a page and has a non-empty value
		|| ((null != context.getPage()) && ((0 <= valueStart) && (0 <= valueEnd)) && (valueStart != valueEnd)));
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
		name = super.getName();
		if (null != name)
			ret += name.length();
		else if ((null != context.getPage()) && (0 <= nameStart) && (0 <= nameEnd))
			ret += nameEnd - nameStart;
		assignment = super.getAssignment();
		if (null != assignment)
			ret += assignment.length();
		else if ((null != context.getPage()) && (0 <= nameEnd) && (0 <= valueStart))
			ret += valueStart - nameEnd;
		value = super.getValue();
		if (null != value)
			ret += value.length();
		else if ((null != context.getPage()) && (0 <= valueStart)
				&& (0 <= valueEnd))
			ret += valueEnd - valueStart;
		quote = getQuote();
		if (0 != quote)
			ret += 2;

		return (ret);
	}

}
