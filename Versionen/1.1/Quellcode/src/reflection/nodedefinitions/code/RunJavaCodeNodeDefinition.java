package reflection.nodedefinitions.code;

import java.lang.reflect.Method;
import javax.tools.ToolProvider;
import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.additionalnodedefinitioninterfaces.SourceCodeTemplate;
import reflection.customdatatypes.SourceCode;

public class RunJavaCodeNodeDefinition implements NodeDefinition, SourceCodeTemplate, CompatibilityTestable {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return SourceCode.class;
            case 1:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Javacode";
            case 1:
                return "Methodennamen";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        return false;
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Rückgabe";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Sourcecode ausführen";
    }

    @Override
    public String getDescription() {
        return "Erwartet als Eingabe den Sourcecode einer Javaklasse mit einer (nicht statichen) Funktion ohne Parameter namens doSomething()." + TAG_PREAMBLE + " [Basics] Run Java Sourcecode Quellcode";
    }

    @Override
    public String getUniqueName() {
        return "buildin.RunJavaCode";
    }

    @Override
    public String getIconName() {
        return "Run-Java-Code_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        SourceCode sourceCode = (SourceCode) io.in0(0, null);
        String methodename = (String) io.in0(1, "doSomething");

        Object instance = api.createInstanceFromSourceCode(sourceCode);
        Class<?> clazz = instance.getClass();

        io.terminatedTest();

        Method method = clazz.getDeclaredMethod(methodename, null);
        Object returned = method.invoke(instance, null);

        if (returned instanceof Object[]) {
            io.out(0, (Object[]) returned);
        } else {
            io.out(0, new Object[]{returned});
        }

    }

    @Override
    public String getTemplate() {
        return "public class NewClass {\n"
                + "   public void doSomething(){\n"
                + "      // ...\n"
                + "   }\n"
                + "}";
    }

    @Override
    public String testForCompatibility() {
        if(ToolProvider.getSystemJavaCompiler() == null) {
            return "Java Development Kit nicht gefunden.";
        } else {
            return null;
        }
    }

}
