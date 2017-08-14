package model.resourceloading;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import reflection.NodeDefinition;
import utils.structures.tuples.Pair;

public class InstanceLoader {

    /**
     * Laed NodeDefinition aus Datei. classDirectory gibt Wurzel an und
     * className den Name der Klasse. Beispiel: loadNodeDefinition("./nodes",
     * "reflection.TestNode") Hierbei muss sich TestNode im Verzeichniss
     * ./nodes/reflection/
     *
     * ACHTUNG: Objekte welche ueber verschiedenen Classloader geladen werden
     * koennen keine Instanzen von Klassen verwenden welche nicht bereits durch
     * deren Parent-Classloader geladen sind!
     */
    public static NodeDefinition loadNodeDefinition(String classDirectory, String className)
            throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        
        ClassLoader classloader = createClassLoader(classDirectory);

        return loadNodeDefinition(classloader, className);

    }
    
    public static ClassLoader createClassLoader(String classDirectory) throws MalformedURLException {
        
        File file = new File(classDirectory);
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader classloader = new URLClassLoader(urls);
        
        return classloader;
    }

    /**
     * Laed NodeDefinition aus Datei ueber den angegeben Classloader.
     */
    public static NodeDefinition loadNodeDefinition(ClassLoader classloader, String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class cls = classloader.loadClass(className);
        Constructor<?> ctor = cls.getConstructor(new Class[0]);
        Object object = ctor.newInstance(new Object[0]);
        return (NodeDefinition) object;

    }

}
