package org.redrune.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class Misc {
	
	/**
	 * Checks if a character is valid to use.
	 *
	 * @param c
	 * 		The character.
	 * @return {@code True} if so.
	 */
	public static boolean allowed(char c) {
		return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == ' ';
	}
	
	/**
	 * Constructs a logger from the class
	 *
	 * @param clazz
	 * 		The class
	 */
	public static Logger constructLogger(Class clazz) {
		return Logger.getLogger(clazz.getSimpleName());
	}
	
	/**
	 * Collapses a wide array of numbers
	 *
	 * @param numbers
	 * 		The numbers
	 */
	public static int[] arguments(int... numbers) {
		return numbers;
	}
	
	/**
	 * Gets all of the classes in a directory
	 *
	 * @param directory
	 * 		The directory to iterate through
	 * @return The list of classes
	 */
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new ArrayList<>();
		for (File file : new File("./bin/" + directory.replace(".", "/")).listFiles()) {
			if (file.getName().contains("$") || file.getName().contains("dropbox")) {
				continue;
			}
			try {
				Object objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", "")).newInstance());
				classes.add(objectEvent);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}
	
	/**
	 * Gets all of the sub directories of a folder
	 */
	public static List<String> getSubDirectories(Object object) {
		String firstDirectory;
		if (object instanceof Class) {
			firstDirectory = ((Class<?>) object).getPackage().getName();
		} else if (object instanceof String) {
			firstDirectory = ((String) object);
		} else {
			throw new IllegalStateException();
		}
		String directory = "./bin/" + firstDirectory.replace(".", "/");
		File file = new File(directory);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return Arrays.asList(directories != null ? directories : new String[0]);
	}
	
	/**
	 * Converts an IP-Address as string to Integer.
	 *
	 * @return The Integer.
	 */
	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens()) {
			ip[i++] = Integer.parseInt(st.nextToken());
		}
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}
	
	/**
	 * Formats the IP-Address.
	 *
	 * @param unformatted
	 * 		The unformatted IP.
	 * @return The formatted IP.
	 */
	public static final String formatIp(String unformatted) {
		String ipAddress = unformatted;
		ipAddress = ipAddress.replaceAll("/", "").replaceAll(" ", "");
		ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
		return ipAddress;
	}
	
}
