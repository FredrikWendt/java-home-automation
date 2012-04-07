package se.wendt.home.bus;

public interface MessageQuery {

	Message createQuery();
	
	Message send(Message query);
}
