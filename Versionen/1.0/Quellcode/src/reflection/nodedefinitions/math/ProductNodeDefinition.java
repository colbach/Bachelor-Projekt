package reflection.nodedefinitions.math;

import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;

public class ProductNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 1;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            default:
                return false;
        }
    }@Override
    public boolean isInletEngaged(int i) {
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
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Produkt";
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
        return "Produkt";
    }

    @Override
    public String getDescription() {
        return "Multipliziert mehrere Werte aufeinander." + TAG_PREAMBLE + " [Math] Mal multiplizieren";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Product";
    }

    @Override
    public String getIconName() {
        return "Product_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Number[] werte = (Number[]) io.in(0, new Number[0]);
        Double prod = 1D;
        for(Number wert : werte) {
            io.terminatedTest();
            prod *= wert.doubleValue();
        }

        io.out(0, prod);
    }

}
