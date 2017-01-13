package model.directinput;

import model.settablecontent.DefinitionWithUserSettableContent;
import model.type.Type;
import reflection.NodeDefinition;

public abstract class DirectInputNodeDefinition extends DefinitionWithUserSettableContent {

    public abstract Type getContentType();

    @Override
    public int getInletCount() {
        return 0;
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
        return "special.directinput." + getContentType().toString();
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
    public void run(InOut io) {
        Object[] content = getUserSettableContent();
        io.out(0, content);
    }

}
