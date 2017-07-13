package master.utility.system;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class SystemManager {
	
	/**
	 * Gets the amount of processors on the computer
	 */
	public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
	
	/**
	 * Sets default system data.
	 */
	public static void setDefaults() {
		System.setOut(new OutLogger(System.out));
	}
}
