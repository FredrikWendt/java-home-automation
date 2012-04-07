package se.wendt.home.util;

public interface Observable<T> {

	void addListener(T listener);

	void removeListener(T listener);

}
