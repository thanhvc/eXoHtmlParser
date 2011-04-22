package exo.html.parser.processor;

import exo.html.parser.Element;
import exo.html.parser.Page;
import exo.html.parser.Processor;
import exo.html.parser.util.ElementIterator;
import exo.html.parser.util.IteratorImpl;
import exo.html.parser.util.ParserException;

public final class StringProcessor extends AbstractProcessor implements Processor {

	public void process(Request request, Response response) {

		this.context = request.getContext();
		this.cursor = this.context.getCursor();
		iterator = new IteratorImpl(request, this);

	}
	
	@Override
	public ElementIterator elements() throws ParserException {
		return iterator;
	}

	/**
	 * Get the next node from the source.
	 * 
	 * @param quotesmart
	 *          If <code>true</code>, strings ignore quoted contents.
	 * @return A Remark, Text or Tag, or <code>null</code> if no more lexemes are
	 *         present.
	 * @exception ParserException
	 *              If there is a problem with the underlying page.
	 */
	public Element nextElement(boolean quotesmart) throws ParserException {
		Element ret;
		int start = cursor.getPosition();
		char ch = this.page.getCharacter(cursor);
		switch (ch) {
		case Page.EOF:
			ret = null;
			break;
		case '<':
			ch = page.getCharacter(cursor);
			if (Page.EOF == ch)
				ret = makeString(start, cursor.getPosition());
			else if ('/' == ch || '%' == ch || Character.isLetter(ch)) {
				page.ungetCharacter(cursor);
				ret = parseTag(start);
			} else if ('!' == ch) {
				ch = page.getCharacter(cursor);
				if (Page.EOF == ch)
					ret = makeString(start, cursor.getPosition());
				else {
					if ('>' == ch) // handle <!>
						ret = makeRemark(start, cursor.getPosition());
					else {
						page.ungetCharacter(cursor); // remark/tag need this char
						if ('-' == ch)
							ret = parseRemark(start, quotesmart);
						else {
							page.ungetCharacter(cursor); // tag needs prior one too
							ret = parseTag(start);
						}
					}
				}
			} else
				ret = parseString(start, quotesmart);
			break;
		default:
			page.ungetCharacter(cursor); // string needs to see leading foreslash
			ret = parseString(start, quotesmart);
			break;
		}

		return (ret);
	}

	public Element nextElement() throws ParserException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
