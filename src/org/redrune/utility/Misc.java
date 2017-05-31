package org.redrune.utility;

import com.google.common.base.Preconditions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.Consumer;
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
		final File[] files = new File(String.format("./bin/%s", directory.replace(".", "/"))).listFiles();
		if (files == null) {
			return classes;
		}
		for (File file : files) {
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
	
	/**
	 * Gets the direction the player is running
	 *
	 * @param dx
	 * 		The x direction
	 * @param dy
	 * 		The y direction
	 */
	public static int getRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2) {
			return 0;
		}
		if (dx == -1 && dy == -2) {
			return 1;
		}
		if (dx == 0 && dy == -2) {
			return 2;
		}
		if (dx == 1 && dy == -2) {
			return 3;
		}
		if (dx == 2 && dy == -2) {
			return 4;
		}
		if (dx == -2 && dy == -1) {
			return 5;
		}
		if (dx == 2 && dy == -1) {
			return 6;
		}
		if (dx == -2 && dy == 0) {
			return 7;
		}
		if (dx == 2 && dy == 0) {
			return 8;
		}
		if (dx == -2 && dy == 1) {
			return 9;
		}
		if (dx == 2 && dy == 1) {
			return 10;
		}
		if (dx == -2 && dy == 2) {
			return 11;
		}
		if (dx == -1 && dy == 2) {
			return 12;
		}
		if (dx == 0 && dy == 2) {
			return 13;
		}
		if (dx == 1 && dy == 2) {
			return 14;
		}
		if (dx == 2 && dy == 2) {
			return 15;
		}
		return -1;
	}
	
	/**
	 * Gets the direction the player is wlking
	 *
	 * @param dx
	 * 		The x direction
	 * @param dy
	 * 		The y direction
	 */
	public static int getWalkDirection(int dx, int dy) {
		if (dx < 0 && dy < 0) {
			return 0;
		}
		if (dx == 0 && dy < 0) {
			return 1;
		}
		if (dx > 0 && dy < 0) {
			return 2;
		}
		if (dx < 0 && dy == 0) {
			return 3;
		}
		if (dx > 0 && dy == 0) {
			return 4;
		}
		if (dx < 0 && dy > 0) {
			return 5;
		}
		if (dx == 0 && dy > 0) {
			return 6;
		}
		if (dx > 0 && dy > 0) {
			return 7;
		}
		return -1;
	}
	
	/**
	 * Format a player's name for display.
	 *
	 * @param name
	 * 		The name to be formatted.
	 * @return The formatted string.
	 */
	public static String formatPlayerNameForDisplay(String name) {
		final StringBuilder builder = new StringBuilder();
		name = name.replaceAll("_", " ").toLowerCase();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				builder.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				builder.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return builder.toString();
	}
	
	/**
	 * Optimizes the text for a chat message
	 *
	 * @param text
	 * 		The text
	 */
	public static String optimizeText(String text) {
		StringBuilder sb = new StringBuilder();
		char buf[] = text.toCharArray();
		boolean wasSpace = false;
		boolean firstChar = false;
		boolean lastEndMark = false;
		for (char c : buf) {
			if (!firstChar) {
				if (c != ' ') {
					firstChar = true;
					wasSpace = c == ':' || c == ';';
					sb.append(Character.toUpperCase(c));
				}
				continue;
			}
			if (!wasSpace && Character.isUpperCase(c)) {
				c = Character.toLowerCase(c);
			}
			if (lastEndMark) {
				c = Character.toUpperCase(c);
			}
			sb.append(c);
			wasSpace = c == ' ' || c == ':' || c == ';';
			lastEndMark = c == '.' || c == '!' || c == '?';
		}
		return sb.toString();
	}
	
	/**
	 * Polls every element within the specified {@link Queue} and performs the specified {@link Consumer} event for
	 * each element.
	 *
	 * @param queue
	 * 		The {@link Queue} to poll elements from. Must not be {@code null}.
	 * @param consumer
	 * 		The {@link Consumer} to execute for each polled element. Must not be {@code null}.
	 */
	public static <T> void pollAll(Queue<T> queue, Consumer<T> consumer) {
		Preconditions.checkNotNull(queue, "Queue may not be null");
		Preconditions.checkNotNull(consumer, "Consumer may not be null");
		
		T element;
		while ((element = queue.poll()) != null) {
			consumer.accept(element);
		}
	}
	
	/**
	 * Getting the text in the file as a formatted {@code String} {@code Object}
	 *
	 * @param location
	 * 		The location of the file
	 */
	public static String getText(String location) {
		File file = new File(location);
		if (!file.exists()) {
			throw new IllegalStateException("File doesn't exist:\t" + file.getAbsolutePath());
		}
		StringBuilder text = new StringBuilder();
		for (String fileText : getFileText(location)) {
			text.append(fileText).append("\n");
		}
		return text.toString();
	}
	
	/**
	 * Gets the text from a file.
	 *
	 * @param file
	 * 		The location of the file.
	 * @return A list of the text in the file. Different lines are separated by different list indexes.
	 */
	public static List<String> getFileText(String file) {
		List<String> text = new ArrayList<>();
		File realFile = new File(file);
		if (!realFile.exists()) {
			return text;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals("") || line.equals(" ")) {
					continue;
				}
				text.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public static int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0) {
				return 5;
			} else if (yOffset > 0) {
				return 0;
			} else {
				return 3;
			}
		} else if (xOffset > 0) {
			if (yOffset < 0) {
				return 7;
			} else if (yOffset > 0) {
				return 2;
			} else {
				return 4;
			}
		} else {
			if (yOffset < 0) {
				return 6;
			} else if (yOffset > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
