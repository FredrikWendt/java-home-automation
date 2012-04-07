package se.wendt.home.util;

import java.util.HashSet;
import java.util.Set;

public abstract class ObservableRunnableServiceBase<T> extends RunnableService implements Observable<T> {

	// mixins anyone?
	
	protected Set<T> listeners = new HashSet<T>();

	@Override
	public synchronized void addListener(T listener) {
		listeners.add(listener);
	}

	@Override
	public synchronized void removeListener(T listener) {
		listeners.remove(listener);
	}

	protected void fireEvent(EventCallback<T> callback) {
		synchronized (listeners) {
			for (T listener : listeners) {
				callback.process(listener);
			}
		}
	}

	public abstract class EventCallback<T> {
		public abstract void process(T listener);
	}

}
