package exo.html.parser;

import exo.html.parser.lifecycle.Lifecycle;
import exo.html.parser.lifecycle.LifecycleSupport;
import exo.html.parser.lifecycle.StandardLifecycleListener;
import exo.html.parser.processor.Request;
import exo.html.parser.processor.RequestSupport;
import exo.html.parser.processor.ResponseSupport;
import exo.html.parser.util.ParserException;

public class Parser implements Service, Lifecycle {

	public static final String PRE_PARSER = "pre_parser";
	public static final String PARSING = "parsing";
	public static final String POST_PARSER = "post_parser";
	
	private String feedback = null;
	private LifecycleListener basic;
	private LifecycleSupport support = null;
	private RequestSupport requestSupport = null;
	private ResponseSupport responseSupport = null;

	public Parser(String source) {
		this(source, new StandardLifecycleListener());
		
	}

	public Parser(String source, LifecycleListener listener) {
		requestSupport = new RequestSupport(source);
		support = new LifecycleSupport(new StandardLifecycleListener());
		this.basic = listener;
	}

	public String getFeedback() {
		return feedback;
	}

	public String parse() throws ParserException {

		support.broadcast(PRE_PARSER, requestSupport.getRequest(), responseSupport.getResponse());
		basic.dispatchEvent(new LifecycleEvent(basic, PARSING, requestSupport.getRequest(), responseSupport.getResponse()));
		support.broadcast(POST_PARSER, requestSupport.getRequest(), responseSupport.getResponse());
		return feedback;
	}

	public void addLifecycleListener(LifecycleListener listener) {
		support.addLifecycleListener(listener);
	}

	public void broadcast(String type) {
		try {
		
			support.broadcast(type, requestSupport.getRequest(), responseSupport.getResponse());
		} catch(ParserException pex) {
			pex.printStackTrace();
		}
		
	}

	public void removeLifecycleListener(LifecycleListener listener) {

	}

	public void start() {

	}

	public void stop() {

	}

	public RequestSupport getRequestSupport() {
		return requestSupport;
	}
	
	

}
