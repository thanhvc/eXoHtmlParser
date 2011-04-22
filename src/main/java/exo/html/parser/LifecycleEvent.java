package exo.html.parser;

import exo.html.parser.processor.Request;
import exo.html.parser.processor.Response;



public class LifecycleEvent {

	private String type;
	private Object request;
	private Object response;
	LifecycleListener lifecycle;
	public LifecycleEvent(LifecycleListener lifecycle, String type, Object request, Object response) {
		this.lifecycle = lifecycle;
		this.type = type;
		this.request = request;
	}
	
	public String getType() {
		return type;
	}
	public Request getRequest() {
		return request instanceof Request? (Request) request : null;
		
	}
	
	public Response getResponse() {
		return response instanceof Response? (Response) response : null;
		
	}
	
	public LifecycleListener getLifecycle() {
		return lifecycle;
	}
	
	

	
}
