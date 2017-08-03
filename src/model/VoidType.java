package model;

import model.type.Type;

public class VoidType extends Type {

    private static VoidType instance = null;
    
    public static VoidType getInstance() {
        if(instance == null) {
            instance = new VoidType();
        }
        return instance;
    }
    
    private VoidType() {
        super(Void.class, false);
    }
    
}
