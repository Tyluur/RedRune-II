package utility.functions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
public class GsonFunctions {
	
	/**
	 * The gson instance
	 */
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
}
