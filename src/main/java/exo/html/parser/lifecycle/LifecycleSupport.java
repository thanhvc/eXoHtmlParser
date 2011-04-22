package exo.html.parser.lifecycle;

import exo.html.parser.LifecycleEvent;
import exo.html.parser.LifecycleListener;
import exo.html.parser.util.ParserException;

public final class LifecycleSupport {
	
	private LifecycleListener lifecycle;
	private LifecycleListener listeners[] = new LifecycleListener[0];
	private final Object listenersLock = new Object();

	
	public LifecycleSupport(LifecycleListener lifecycle) {
		this.lifecycle = lifecycle;
	}

	public void addLifecycleListener(LifecycleListener listener) {
		synchronized(listenersLock) {
			LifecycleListener results[] = new LifecycleListener[listeners.length];
			for(int i=0; i<listeners.length; i++)
				results[i] = listeners[i];
			results[listeners.length] = listener;
			listeners = results;
			
		}
	}
	
	public void broadcast(String type, Object request, Object response) throws ParserException {
		LifecycleEvent event = new LifecycleEvent(lifecycle, type, request, response);
		LifecycleListener interested[] = listeners;
		
		for (int i=0; i<interested.length; i++)
			interested[i].dispatchEvent(event);
	}
	
	public LifecycleListener[] findLifecycleListener() {
		return listeners;
	}
}
