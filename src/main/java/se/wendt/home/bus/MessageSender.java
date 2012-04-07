package se.wendt.home.bus;

public interface MessageSender {

	Message createMessage();
	
	void send(Message message);
	
}
