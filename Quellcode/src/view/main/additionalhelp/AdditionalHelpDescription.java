package view.main.additionalhelp;

public class AdditionalHelpDescription {

    private final String[] keys;
    private final String[] labels;

    public AdditionalHelpDescription(String[] keys, String[] labels) {
        if (keys.length != labels.length) {
            throw new IllegalArgumentException("Anzahl der keys muss gleich Anzahl der labels sein.");
        }
        this.keys = keys;
        this.labels = labels;
    }

    public AdditionalHelpDescription() {
        this.keys = new String[0];
        this.labels = new String[0];
    }
    
    public AdditionalHelpDescription(String key, String description) {
        this.keys = new String[]{key};
        this.labels = new String[]{description};
    }

    public int getCount() {
        return this.keys.length;
    }

    public String getKey(int i) {
        return this.keys[this.keys.length - 1 - i];
    }

    public String getLabel(int i) {
        return this.labels[this.labels.length - 1 - i];
    }
    
    public boolean isEmpty() {
        return keys.length == 0;
    }

}
