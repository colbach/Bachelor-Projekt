package commandline;

import java.util.HashMap;

public class Aliases {

    private final HashMap<String, String> aliasMap;

    public Aliases() {
        this.aliasMap = new HashMap<>();
    }
    
    public void put(String[] aliases, String name) {
        for (String alias : aliases) {
            if(aliasMap.containsKey(alias)) {
                System.err.println("Warnung, Alias \"" + alias + "\" bereits vorhanden!");
            }
            aliasMap.put(alias, name);
        }
    }

    public String get(String key) {
        return aliasMap.get(key);
    }

    public HashMap<String, String> getAliasMap() {
        return aliasMap;
    }
    
}
