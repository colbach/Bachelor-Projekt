package model.resourceloading;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import reflection.NodeDefinition;

public class InstanceLoader {
    
    /**
     * Laed Klasse aus Datei.
     * classDirectory gibt Wurzel an und className den Name der Klasse.
     * Beispiel: loadNodeDefinition("./nodes", "reflection.TestNode")
     * Hierbei muss sich TestNode im Verzeichniss ./nodes/reflection/
     */
    public static NodeDefinition loadNodeDefinition(String classDirectory, String className)
            throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
                   InstantiationException, IllegalAccessException, IllegalArgumentException,
                   InvocationTargetException {
        
        File file = new File(classDirectory);
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader cl = new URLClassLoader(urls);
        Class cls = cl.loadClass(className);
        Constructor<?> ctor = cls.getConstructor(new Class[0]);
        Object object = ctor.newInstance(new Object[0]);
        return (NodeDefinition) object;
        
    }
    
}
