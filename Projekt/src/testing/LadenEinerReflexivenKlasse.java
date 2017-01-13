package testing;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import model.resourceloading.InstanceLoader;
import reflection.NodeDefinition;

public class LadenEinerReflexivenKlasse {
    
    
    public static void main(String[] args) throws Exception {
        
        InstanceLoader.loadNodeDefinition(".", "TestNodeSP").run(null);
        
        /*File file = new File(".");
        
        System.out.println(file.getAbsolutePath());
        
        try {
            URL url = file.toURL();
            URL[] urls = new URL[]{url};
            
            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass("TestNodeSP");
            
            Constructor<?> ctor = cls.getConstructor(new Class[0]);
            Object object = ctor.newInstance(new Object[0]);
            
            
            NodeDefinition node = (NodeDefinition) object;
            
            node.run(null);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
