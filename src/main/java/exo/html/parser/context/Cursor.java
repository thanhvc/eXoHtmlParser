package exo.html.parser.context;

import java.io.Serializable;

import exo.html.parser.Context;

@SuppressWarnings("serial")
public class Cursor implements Serializable {

	protected int position;
	
	public Cursor(int offset) {
		this.position = offset;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void next() {
		position ++;
	}
	
	public void back() {
		position--;
		if (0>position)
			position = 0;
	}

	public void setPosition(int position) {
		this.position = position;
		
	}
	
	
	
}
