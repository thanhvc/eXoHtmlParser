package exo.html.parser.lifecycle;

import exo.html.parser.LifecycleEvent;
import exo.html.parser.LifecycleListener;
import exo.html.parser.Parser;
import exo.html.parser.Processor;
import exo.html.parser.processor.Request;
import exo.html.parser.processor.Response;
import exo.html.parser.processor.StringProcessor;
import exo.html.parser.util.ParserException;

public class StandardLifecycleListener implements LifecycleListener {

	public void dispatchEvent(LifecycleEvent event) throws ParserException {
		if (Parser.PARSING == event.getType()) {
			doParser(event.getRequest());
		}
	}
	
	private void doParser(Request request) throws ParserException {
		Processor processor = new StringProcessor();
		
		processor.process(request, new Response());
	}

}
