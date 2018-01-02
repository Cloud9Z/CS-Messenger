package test.client;

import java.util.Observable;
import java.util.Observer;

/**
 * DummyObserver to test the client.
 *
 * @author Yunxiao Zhuang
 * @version 2017-03-20
 */
public class DummyObserver implements Observer {

	private String response = null;

	@Override
	public void update(Observable arg0, Object arg1) {
		response = arg1.toString();
		System.err.println("Update was called. Response is now: " + response);
	}
	
	public String getResponse() {
		return response;
	}

}
