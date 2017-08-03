package reflection.nodedefinitions.code;

import javax.tools.ToolProvider;
import reflection.*;
import reflection.additionalnodedefinitioninterfaces.CompatibilityTestable;
import reflection.customdatatypes.SourceCode;

public class TextToJavaCodeNodeDefinition implements NodeDefinition, CompatibilityTestable {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Quellcode als Text";
            case 1:
                return "Sofort kompilieren";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        if (index == 0) {
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
                return SourceCode.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Quellcode";
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            case 0:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Text zu Code";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.TextToJavaCode";
    }

    @Override
    public String getIconName() {
        return "Text-Java-Code_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        String text = (String) io.in0(0, null);
        Boolean compile = (Boolean) io.in0(1, Boolean.TRUE);

        if (text != null) {
            SourceCode sourcecode = new SourceCode(text);
            if(compile) {
                api.compileSourceCode(sourcecode);
            }
            io.out(0, sourcecode);
        }
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
