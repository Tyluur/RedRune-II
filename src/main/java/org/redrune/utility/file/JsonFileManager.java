package org.redrune.utility.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
	
	/**
	 * Saves the data to the file
	 *
	 * @param data
	 * 		The list to save
	 * @param location
	 * 		The location to save to
	 */
	@SuppressWarnings("hiding")
	public static <T> boolean save(T data, String location) {
		try (Writer writer = new FileWriter(location)) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC);
			Gson gson = builder.create();
			gson.toJson(data, writer);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
