package exo.html.parser;

import exo.html.parser.context.Cursor;


public interface Context {

	public Page getPage();
	public Cursor getCursor();

	public int getPosition();
}

