package exo.html.parser.processor;

import exo.html.parser.Context;

public class RequestSupport {

	private Request request;
	public RequestSupport(String text) {
		request = new Request(text);
	}
	
	public Request getRequest() {
		return request;
	}
	
	public Context getContext() {
		return request.getContext();
	}
}
