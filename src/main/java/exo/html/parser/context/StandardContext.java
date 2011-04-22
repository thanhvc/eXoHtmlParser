package exo.html.parser.context;

import exo.html.parser.Context;
import exo.html.parser.Page;

public final class StandardContext implements Context {
	private Page page;
	private Cursor cursor;

	public StandardContext(String text) {
		this(new Page(text));
	}
	
	public StandardContext(Page page) {
		this.page = page;
		this.cursor = new Cursor(0);
	}

	public Page getPage() {
		return page;
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public int getPosition() {
		return cursor.getPosition();
	}
	
}
