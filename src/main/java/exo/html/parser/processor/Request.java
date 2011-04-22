package exo.html.parser.processor;

import exo.html.parser.Context;
import exo.html.parser.Page;
import exo.html.parser.context.StandardContext;

public class Request {
	private Context context;
	
	public Request(String text) {
		this(new Page(text));
	}

	public Request(Page page) {
		context = new StandardContext(page);
	}

	public Page getPage() {
		return context.getPage();
	}

	public Context getContext() {
		return context;
	}
	
}
