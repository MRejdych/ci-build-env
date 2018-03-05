package app;

import java.io.*;
import java.util.List;

public class Utils {

    public static List<String> bytesArrayToStringList(byte[] bytea) {

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytea))){
            Object o = ois.readObject();

            if (o instanceof List) {
                return ( List<String>) o;
            }
        } catch (EOFException ignored) {
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
