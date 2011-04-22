package exo.html.parser;

import exo.html.parser.util.ParserException;


public interface LifecycleListener {

	public void dispatchEvent(LifecycleEvent event) throws ParserException;
}
