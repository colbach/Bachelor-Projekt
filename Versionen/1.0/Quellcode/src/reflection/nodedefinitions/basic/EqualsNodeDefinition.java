package reflection.nodedefinitions.basic;

import java.util.Objects;
import reflection.*;

public class EqualsNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 2;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "A";
            case 1:
                return "B";
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
                return Boolean.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "A ist gleich B";
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
        return "Ist gleich";
    }

    @Override
    public String getDescription() {
        return "Gibt an Ausgang weiter ob A und B gleich ist." + TAG_PREAMBLE + " [Basics] === equals";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Equals";
    }

    @Override
    public String getIconName() {
        return "Equals_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object a = (Object) io.in0(0, null);
        Object b = (Object) io.in0(1, null);

        Boolean aistgleichb = Objects.equals(a, b);

        io.out(0, aistgleichb);
    }

}
