package org.redrune.core.system;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class SystemFinalization extends Thread {
	
	
	@Override
	public void run() {
		System.out.println("System shutdown has been invoked.");
	}
}
