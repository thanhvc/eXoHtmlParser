package exo.html.parser.element;

import exo.html.parser.Element;

public abstract class AbstractElement implements Element {

	protected int startPosition;
	protected int endPosition;
	public AbstractElement(int start, int end) {
		this.startPosition = start;
		this.endPosition = end;
		
	}
	
	public void setStartPosition(int position) {
		this.startPosition = position;
	}
	
	public void setEndPosition(int position) {
		this.endPosition = position;
	}
	
	public int getStartPosition() {
		return this.startPosition;
	}
	
	public int getEndPosition() {
		return this.endPosition;
	}
	

}
