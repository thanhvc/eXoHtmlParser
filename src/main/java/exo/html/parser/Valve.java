package exo.html.parser;

import exo.html.parser.processor.Request;
import exo.html.parser.processor.Response;

public interface Valve {

	public Valve getNext();
	public Valve setNext();
	
	public void invoke(Request request, Response response);
}
