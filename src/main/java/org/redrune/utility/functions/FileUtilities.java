package org.redrune.utility.functions;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author 'Mystic Flow
 */
public class FileUtilities {

    public static final int BUFFER = 1024;

    public static boolean exists(String name) {
        File file = new File(name);
        return file.exists();
    }

    public static ByteBuffer fileBuffer(String name) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            return null;
        }
        FileInputStream in = new FileInputStream(name);

        byte[] data = new byte[BUFFER];
        int read;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(in.available() + 1);
            while ((read = in.read(data, 0, BUFFER)) != -1) {
                buffer.put(data, 0, read);
            }
            buffer.flip();
            return buffer;
        } finally {
            if (in != null) {
                in.close();
            }
            in = null;
        }
    }

    public static void writeBufferToFile(String name, ByteBuffer buffer) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(name);
        out.write(buffer.array(), 0, buffer.remaining());
        out.flush();
        out.close();
    }

    public static LinkedList<String> readFile(String directory) throws IOException {
        LinkedList<String> fileLines = new LinkedList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(directory));
            String string;
            while ((string = reader.readLine()) != null) {
                fileLines.add(string);
            }
        } finally {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        }
        return fileLines;
    }

}