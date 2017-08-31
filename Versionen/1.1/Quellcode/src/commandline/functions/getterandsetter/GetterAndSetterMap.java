package commandline.functions.getterandsetter;

import commandline.Aliases;
import commandline.CommandLine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class GetterAndSetterMap {

    private final HashMap<String, Getter> getters;
    private final HashMap<String, Setter> setters;
    private final TreeMap<String, String> descriptions;
    private final CommandLine commandLine;

    protected GetterAndSetterMap(CommandLine commandLine) {
        this.getters = new HashMap<>();
        this.setters = new HashMap<>();
        this.descriptions = new TreeMap<>(); // TreeMap wegen Sortierung
        this.commandLine = commandLine;
    }

    public void put(String key, String description, Getter getter) {
        getters.put(key.toLowerCase(), getter);
        descriptions.put(key.toLowerCase(), description);
    }

    public void put(String key, String description, Setter setter) {
        setters.put(key.toLowerCase(), setter);
        descriptions.put(key.toLowerCase(), description);
    }

    public void put(String key, String description, GetterSetter getterSetter) {
        put(key, description, (Getter) getterSetter, (Setter) getterSetter);
    }

    public void put(String key, String description, Getter getter, Setter setter) {
        setters.put(key.toLowerCase(), setter);
        getters.put(key.toLowerCase(), getter);
        descriptions.put(key.toLowerCase(), description);
    }

    public void put(String key, String description, Setter setter, Getter getter) {
        put(key, description, getter, setter);
    }

    public void put(String key, String aliases[], String description, Getter getter) {
        put(key, description, getter);
        commandLine.getAliases().put(aliases, key);
    }

    public void put(String key, String aliases[], String description, Setter setter) {
        put(key, description, setter);
        commandLine.getAliases().put(aliases, key);
    }

    public void put(String key, String aliases[], String description, GetterSetter getterSetter) {
        put(key, aliases, description, (Getter) getterSetter, (Setter) getterSetter);
    }

    public void put(String key, String aliases[], String description, Getter getter, Setter setter) {
        put(key, description, getter, setter);
        commandLine.getAliases().put(aliases, key);
    }

    public void put(String key, String aliases[], String description, Setter setter, Getter getter) {
        put(key, description, setter, getter);
        commandLine.getAliases().put(aliases, key);
    }

    public void put(String key, String aliases, String description, Getter getter) {
        put(key, aliases.split(" "), description, getter);
    }

    public void put(String key, String aliases, String description, Setter setter) {
        put(key, aliases.split(" "), description, setter);
    }

    public void put(String key, String aliases, String description, GetterSetter getterSetter) {
        put(key, aliases, description, (Getter) getterSetter, (Setter) getterSetter);
    }

    public void put(String key, String aliases, String description, Getter getter, Setter setter) {
        put(key, aliases.split(" "), description, getter, setter);
    }

    public void put(String key, String aliases, String description, Setter setter, Getter getter) {
        put(key, aliases.split(" "), description, setter, getter);
    }

    public Set<String> keySet() {
        return descriptions.keySet();
    }

    public Getter getGetter(String key) {
        return getters.get(key.toLowerCase());
    }

    public Setter getSetter(String key) {
        return setters.get(key.toLowerCase());
    }

    public String getDescription(String key) {
        return descriptions.get(key);
    }

    public String getInfo(String key) {
        if (descriptions.containsKey(key)) {
            Setter setter = getSetter(key);
            Getter getter = getGetter(key);
            if (getter != null && setter != null) {
                return key + " (Lesen und " + (setter.isPersistent() ? "persistentes " : "") + "Schreiben):\n      -> " + getDescription(key);
            } else if (getter != null) {
                return key + " (Nur Lesen):\n      -> " + getDescription(key);
            } else if (setter != null) {
                return key + " (Nur " + (setter.isPersistent() ? "persistentes " : "") + "Schreiben):\n      -> " + getDescription(key);
            } else {
                System.err.println("Eintrag ohne Getter und Setter gefunden");
                return null;
            }
        } else {
            return null;
        }
    }

    public String[] getGetInfos() {
        ArrayList<String> infos = new ArrayList<>();
        for (String key : keySet()) {
            if (getGetter(key) != null) {
                infos.add(getInfo(key));
            }
        }
        return infos.toArray(new String[0]);
    }

    public String[] getSetInfos() {
        ArrayList<String> infos = new ArrayList<>();
        for (String key : keySet()) {
            if (getSetter(key) != null) {
                infos.add(getInfo(key));
            }
        }
        return infos.toArray(new String[0]);
    }

    public String[] getInfos() {
        String[] infos = new String[keySet().size()];
        int i = 0;
        for (String key : keySet()) {
            infos[i++] = getInfo(key);
        }
        return infos;
    }

    public String getInfosSeparatedByLineBreaks() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String info : getInfos()) {
            if (!first) {
                sb.append("\n");
            }
            sb.append(info);
            first = false;
        }
        return sb.toString();
    }

    public String getSetInfosSeparatedByLineBreaks() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String info : getSetInfos()) {
            if (!first) {
                sb.append("\n");
            }
            sb.append(info);
            first = false;
        }
        return sb.toString();
    }

    public String getGetInfosSeparatedByLineBreaks() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String info : getGetInfos()) {
            if (!first) {
                sb.append("\n");
            }
            sb.append(info);
            first = false;
        }
        return sb.toString();
    }
}
