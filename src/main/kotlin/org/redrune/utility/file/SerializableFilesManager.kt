package org.redrune.utility.file

import org.redrune.game.entity.actor.player.Player
import java.io.*

object SerializableFilesManager {

    /**
     * The path to where player files are stored
     */
    private const val PATH = "data/saves/characters/"

    @Synchronized
    fun containsPlayer(username: String): Boolean {
        return File(PATH + username + ".p").exists()
    }

    @Synchronized
    fun loadPlayer(username: String): Player? {
        try {
            return loadSerializedFile(File(PATH + username + ".p")) as Player?
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun loadSerializedFile(f: File): Any? {
        if (!f.exists()) {
            return null
        }
        val `in` = ObjectInputStream(FileInputStream(f))
        val `object` = `in`.readObject()
        `in`.close()
        return `object`
    }

    @Synchronized
    fun savePlayer(player: Player) {
        try {
            storeSerializableClass(player, File(PATH + player.username + ".p"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun storeSerializableClass(o: Serializable?, f: File?) {
        val out = ObjectOutputStream(FileOutputStream(f))
        out.writeObject(o)
        out.close()
    }
}