package exo.html.parser.lifecycle;

import exo.html.parser.LifecycleListener;

public interface Lifecycle {
	public void addLifecycleListener(LifecycleListener listener);
	public void removeLifecycleListener(LifecycleListener listener);
}
