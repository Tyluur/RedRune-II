package com.rs.utility.game.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rs.utility.Misc;

import java.io.File;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class JsonFileManager {
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * Loads the file data
	 *
	 * @param file
	 * 		The file to load data from
	 */
	public static <K> K loadJsonData(File file) {
		if (!file.exists()) {
			return null;
		}
		return GSON.fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<K>() {
		}.getType());
	}
	
}
