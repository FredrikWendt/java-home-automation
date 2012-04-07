package se.wendt.home.bus;

public interface MessageResponse {

	void handle(Message request, Message response);
	
}
