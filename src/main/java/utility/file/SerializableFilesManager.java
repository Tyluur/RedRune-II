package utility.file;

import game.entity.actor.player.Player;

import java.io.*;

public final class SerializableFilesManager {
	
	private static final String PATH = "data/saves/characters/";
	
	private SerializableFilesManager() {
	
	}
	
	public synchronized static boolean containsPlayer(String username) {
		return new File(PATH + username + ".p").exists();
	}
	
	public synchronized static Player loadPlayer(String username) {
		try {
			return (Player) loadSerializedFile(new File(PATH + username + ".p"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object loadSerializedFile(File f) throws IOException, ClassNotFoundException {
		if (!f.exists()) {
			return null;
		}
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		Object object = in.readObject();
		in.close();
		return object;
	}
	
	public synchronized static void savePlayer(Player player) {
		try {
			storeSerializableClass(player, new File(PATH + player.getUsername() + ".p"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void storeSerializableClass(Serializable o, File f) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}
	
}
