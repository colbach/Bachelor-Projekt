package buildin;

import java.util.HashMap;
import reflection.NodeDefinition;

public class Cache implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 3;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return Object.class;
            case 1:
                return Object.class;
            case 2:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "Init";
            case 1:
                return "Data";
            case 2:
                return "Kennung";
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
                return Object.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            case 0:
                return "Data";
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
        return "Zwischenspeicher";
    }

    @Override
    public String getDescription() {
        return "Speichert Werte zwischen zur weiteren Verwendung ausgehend von einem zweiten Element mit der gleichen Kennung. " + TAG_PREAMBLE + " Gehirn merken Speicher Daten Variable Zurzeitig RAM Attribut ID";
    }

    @Override
    public String getUniqueName() {
        return "buildin.Cache";
    }

    @Override
    public String getIconName() {
        return "Brain-2_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    private static HashMap<String, Object[]> cache = new HashMap<>();

    @Override
    public void run(InOut io) {

        Object[] init = (Object[]) io.in(0);
        Object[] data = (Object[]) io.in(1);
        String key = (String) io.in0(2, "");

        if (data == null || data.length == 0) {
            Object[] cacheData = cache.get(key);
            if (cacheData == null) {
                if (init == null) {
                    data = new Object[0];
                } else {
                    data = init;
                }
            } else {
                data = cacheData;
            }
        }
        cache.put(key, data);

        io.out(0, data);
    }

}
