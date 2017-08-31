package reflection.nodedefinitions.basic;

import java.util.HashMap;
import reflection.API;
import reflection.InOut;
import reflection.NodeDefinition;
import reflection.customdatatypes.SmartIdentifier;

public class CacheNodeDefinition implements NodeDefinition {

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
                return SmartIdentifier.class;
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
                return "Identifier";
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
    public boolean isInletEngaged(int i) {
        if (i == 2) {
            return true;
        }
        return false;
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
        return "Speichert Werte zwischen zur weiteren Verwendung ausgehend von einem zweiten Element mit der gleichen Kennung. " + TAG_PREAMBLE + " [Basics] brain Gehirn merken Speicher Daten Variable Zurzeitig RAM Attribut ID";
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

    private static HashMap<SmartIdentifier, Object[]> cache = new HashMap<>();
    private static Object[] undefinedCache;

    @Override
    public void run(InOut io, API api) {

        Object[] init = (Object[]) io.in(0);
        Object[] data = (Object[]) io.in(1);
        SmartIdentifier key = (SmartIdentifier) io.in0(2, null);

        if (data == null || data.length == 0) {
            Object[] cacheData;
            if (key == null) {
                cacheData = undefinedCache;
            } else {
                cacheData = cache.get(key);
            }
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
        if (key == null) {
            undefinedCache = data;
        } else {
            cache.put(key, data);
        }

        io.out(0, data);
    }

}
