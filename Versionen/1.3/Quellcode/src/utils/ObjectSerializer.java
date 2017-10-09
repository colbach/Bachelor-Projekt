package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectSerializer {

    public static byte[] serializeObjectToBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(object);
                out.flush();
                return bos.toByteArray();
            } catch (IOException ex) {
                System.err.println("Kann nicht serialisiert werden.");
            }
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public static Object derializeBytesToObject(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            try {
                in = new ObjectInputStream(bis);
                Object object = in.readObject();
                return object;
            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("Kann nicht deserialisiert werden.");
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public static String serializeObjectToBase64(Object object) {

        byte[] bytes = serializeObjectToBytes(object);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    public static Object deserializeBase64ToObject(String base64) {

        byte[] decoded = Base64.getDecoder().decode(base64);
        return derializeBytesToObject(decoded);
    }
    
    public static String[] serializeObjectsToBase64Array(Object[] objects) {
        
        String[] base64a = new String[objects.length];
        for(int i=0; i<objects.length; i++) {
            base64a[i] = serializeObjectToBase64(objects[i]);
        }
        return base64a;
    }

    public static Object[] deserializeBase64ArrayToObjects(String[] base64a) {

        Object[] objects = new Object[base64a.length];
        for(int i=0; i<base64a.length; i++) {
            objects[i] = deserializeBase64ToObject(base64a[i]);
        }
        return objects;
    }
}
