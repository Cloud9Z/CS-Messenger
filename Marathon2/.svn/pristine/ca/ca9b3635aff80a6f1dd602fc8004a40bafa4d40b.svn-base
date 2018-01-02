package gui;
import java.util.Observable;
import java.util.Observer;

/**
 * Interface for the GUI used for documenting possible status values.
 *
 * @author All
 * @version 2017-03-20
 */
public interface GUI extends Observer {
	
	/**
	 * 
	 * Precondition: 'arg' is of type Integer and has one of the following values:
	 * 
	 * -----------|----------------------------------------------------
	 *   Value	  |	 Description
	 * -----------|----------------------------------------------------
	 * 	Account:1 	Account was successfully created 
	 * 	Account:-1 	User name already taken 
	 * 	Login:1 	Login was successful
	 * 	Login:-1 	User name does not exist or password incorrect 
	 * 	Login:-2 	User already logged in
	 *  Online:1	A new version of the online list has arrived
	 *  Chat:1 		New message arrived
	 *  Chat:2 		A group chat needs to be closed 
	 *  Offline:1	A user has gone offline; chats need to be closed 
	 *  History:1 	The history list has arrived
	 *  History:2 	A requested history has arrived
	 * 
	 */
	@Override
	public void update(Observable o, Object arg);
	
}
