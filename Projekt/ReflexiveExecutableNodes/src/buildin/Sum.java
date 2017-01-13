package buildin;

import reflection.NodeDefinition;

public class Sum implements NodeDefinition {

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
                return "Summe";
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
        return "Summe";
    }

    @Override
    public String getDescription() {
        return "Addiert mehrere Werte zusammen.";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Sum";
    }

    @Override
    public String getIconName() {
        return "Sum_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io) {

        Object[] werte = io.in(0, new Number[0]);
        Double summe = 0D;
        for(Object wert : werte) {
            summe += ((Number) wert).doubleValue();
        }

        io.out(0, summe);
    }

}
