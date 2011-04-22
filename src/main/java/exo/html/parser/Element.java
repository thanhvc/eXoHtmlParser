package exo.html.parser;

import exo.html.parser.util.ElementList;
import exo.html.parser.visitor.ElementVisitor;

public interface Element {

	public void accept(ElementVisitor visitor);
	
	public ElementList getChildren();
	
	public int getStartPosition();
	
	public void setStartPosition(int position);
	
	public int getEndPosition();
	
	public void setEndPosition(int position);
}
