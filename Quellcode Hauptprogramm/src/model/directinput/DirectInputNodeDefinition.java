package model.directinput;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.settablecontent.DefinitionWithUserSettableContent;
import model.type.Type;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.NoTriggerInlet;

public abstract class DirectInputNodeDefinition extends DefinitionWithUserSettableContent implements NoTriggerInlet {

    public static final String UNIQUE_NAME_PREFIX = "special.directinput";

    public static DirectInputNodeDefinition createDirectInputNodeDefinitionByType(Type contentTypeForDirectInputNodeDefinition) {
        return new DirectInputNodeDefinition() {
            @Override
            public Type getContentType() {
                return contentTypeForDirectInputNodeDefinition;
            }
        };
    }

    public static DirectInputNodeDefinition createDirectInputNodeDefinitionByUniqueName(String uniqueName) {
        if (!uniqueName.startsWith(UNIQUE_NAME_PREFIX)) {
            System.err.println("Achtung: uniqueName ist " + uniqueName + " createDirectInputNodeDefinitionByUniqueName soll nur auf UniqueName von DirectInputNodeDefinition aufgerufen werden.");
        }
        int reconstructableTypeStringStart = uniqueName.indexOf("(") + 1;
        int reconstructableTypeStringEnd = uniqueName.indexOf(")");
        if (reconstructableTypeStringStart >= reconstructableTypeStringEnd || reconstructableTypeStringStart >= uniqueName.length()) {
            System.err.println("uniqueName " + uniqueName + " ist ungueltig. return null.");
            return null;
        }
        String reconstructableTypeString = uniqueName.substring(reconstructableTypeStringStart, reconstructableTypeStringEnd);
        try {
            Type contentTypeForDirectInputNodeDefinition = new Type(reconstructableTypeString);
            return createDirectInputNodeDefinitionByType(contentTypeForDirectInputNodeDefinition);
        } catch (ClassNotFoundException ex) {
            System.err.println("Klasse " + reconstructableTypeString + " zum Bilden von DirectInputNodeDefinition nicht gefunden. return null;");
            return null;
        }
    }

    @Override
    public abstract Type getContentType();

    @Override
    public int getInletCount() {
        return 0;
    }

    @Override
    public boolean isInletEngaged(int inletIndex) {
        return false;
    }

    @Override
    public Class getClassForInlet(int inletIndex) {
        return null;
    }

    @Override
    public String getNameForInlet(int inletIndex) {
        return null;
    }

    @Override
    public boolean isInletForArray(int inletIndex) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 1;
    }

    @Override
    public Class getClassForOutlet(int outletIndex) {
        return getContentType().getTypeClass();
    }

    @Override
    public String getNameForOutlet(int outletIndex) {
        return null;
    }

    @Override
    public boolean isOutletForArray(int outletIndex) {
        return getContentType().isArray();
    }

    @Override
    public String getName() {
        return "Direkte Eingabe";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUniqueName() {
        return UNIQUE_NAME_PREFIX + "(" + getContentType().getReconstructableTypeString() + ")";
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void run(InOut io, API api) {
        Object[] content = getUserSettableContent();
        io.out(0, content);
    }

}
