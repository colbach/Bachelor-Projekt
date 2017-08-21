package projectrunner.debugger.letdatastorage;

import utils.format.ObjectArrayFormat;

public class DebuggerLetDataStorageData {

    private static final boolean ONLY_HOLD_SHORTENED_DATA_DEFAULT = true;
    private static final int SHORTENED_LENGTH = 100;
    
    private boolean onlyHoldShortenedData = ONLY_HOLD_SHORTENED_DATA_DEFAULT;

    private Object[] data;
    private String shortenedData;
    private boolean delivered;

    protected DebuggerLetDataStorageData(Object[] data) {
        if (onlyHoldShortenedData) {
            this.data = null;
        } else {
            this.data = data;
        }
        this.shortenedData = ObjectArrayFormat.formatShortened(data, SHORTENED_LENGTH);
        this.delivered = false;
    }

    public Object[] getData() {
        if (onlyHoldShortenedData) {
            System.err.println("Data nicht gespeichert. Nur dataShortened verfuegbar. return null");
            return null;
        } else {
            return data;
        }
    }

    public void setData(Object[] data) {
        if (!onlyHoldShortenedData) {
            this.data = data;
        }
        this.shortenedData = ObjectArrayFormat.formatShortened(data, SHORTENED_LENGTH);
    }

    public String getShortenedData() {
        return shortenedData;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setDelivered() {
        this.delivered = true;
    }
}
