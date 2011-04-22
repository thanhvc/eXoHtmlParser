package exo.html.parser;

import exo.html.parser.processor.Request;
import exo.html.parser.processor.Response;
import exo.html.parser.util.ParserException;

public interface Processor {
	
	public void process(Request request, Response response) throws ParserException;
	public Element nextElement () throws ParserException;
	
}
