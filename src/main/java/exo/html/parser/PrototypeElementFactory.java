package exo.html.parser;

import java.io.Serializable;
import java.util.Vector;

import exo.html.parser.util.ParserException;

@SuppressWarnings("serial")
public class PrototypeElementFactory implements Serializable, ElementFactory {

	protected Text element;
	protected Remark remark;
	protected Tag tag;
	
	/**
	 * 
	 */
	public PrototypeElementFactory() {
		this(false);
	}
	
	
	public PrototypeElementFactory(boolean empty) {
		

	}





	public Text createStringNode(Page page, int start, int end)
			throws ParserException {

		return null;
	}

	public Remark createRemarkNode(Page page, int start, int end)
			throws ParserException {
		return null;
	}

	public Tag createTagNode(Page page, int start, int end, Vector attributes)
			throws ParserException {
		
		return null;
	}

}
