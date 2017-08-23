package reflection.nodedefinitions.basic;

import reflection.common.API;
import reflection.common.InOut;
import reflection.common.NodeDefinition;

public class AssertNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 4;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Number.class;
            case 1:
                return Number.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
            case 1:
                return "Referenz-Werte";
            case 2:
                return "Test-Typ";
            case 3:
                return "Meldung";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        switch (index) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return false;
            case 3:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean isInletEngaged(int i) {
        if (i == 0 || i == 1) {
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
                return Number.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Werte";
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
        return "Assert";
    }

    @Override
    public String getDescription() {
        return "Vergleicht Werte und Referenz-Werte auf eine bestimmte Eigenschaft angegeben durch Test-Typ. Zur Auswahl für Test-Typ steht >, <, >=, <=, ==, != zur Verfügung.\nIm Fall dass Werte und Referenz-Werte diese Eigenschaft nicht erfüllt wird eine Ausnahme ausgelöst." + TAG_PREAMBLE + " [Basics] Assert Ausnahme Exception Test Debugging";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Assert";
    }

    @Override
    public String getIconName() {
        return "Assert_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) {

        Object[] werte = io.in(0, new Object[0]);
        Object[] testwerte = io.in(1, new Object[0]);
        Object meldung = io.in(3);
        String testtyp = (String) io.in0(2, "==");
        if (!testtyp.equals(">") && !testtyp.equals("<") && !testtyp.equals(">=") && !testtyp.equals("<=") && !testtyp.equals("!=") && !testtyp.equals("==")) {
            throw new IllegalArgumentException(testtyp + " ist kein erlaubter Test-Typ für Assert");
        }

        io.terminatedTest();

        for (int i = 0; i < werte.length; i++) {
            Number w = (Number) werte[i];
            Number t = (Number) testwerte[i % testwerte.length];
            if (testtyp.equals(">")) {
                if (w.doubleValue() < t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " > " + t + " nicht erfüllt");
                    }
                }
            } else if (testtyp.equals("<")) {
                if (w.doubleValue() < t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " < " + t + " nicht erfüllt");
                    }
                }
            } else if (testtyp.equals(">=")) {
                if (w.doubleValue() <= t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " >= " + t + " nicht erfüllt");
                    }
                }
            } else if (testtyp.equals("<=")) {
                if (w.doubleValue() >= t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " <= " + t + " nicht erfüllt");
                    }
                }
            } else if (testtyp.equals("!=")) {
                if (w.doubleValue() == t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " != " + t + " nicht erfüllt");
                    }
                }
            } else if (testtyp.equals("==")) {
                if (w.doubleValue() != t.doubleValue()) {
                    if (meldung != null) {
                        throw new RuntimeException(meldung.toString());
                    } else {
                        throw new RuntimeException("Assert: " + w + " == " + t + " nicht erfüllt");
                    }
                }
            }
            io.terminatedTest();
        }

        io.out(0, werte);
    }

}
