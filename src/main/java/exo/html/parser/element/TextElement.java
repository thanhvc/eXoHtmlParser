package exo.html.parser.element;

import exo.html.parser.Text;
import exo.html.parser.util.ElementList;
import exo.html.parser.visitor.ElementVisitor;

public class TextElement extends AbstractElement implements Text {

	private String text;
	
	public TextElement(String text) {
		super(0, text.length());
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void accept(ElementVisitor visitor) {
		
	}

	public ElementList getChildren() {
		return null;
	}
	
}
