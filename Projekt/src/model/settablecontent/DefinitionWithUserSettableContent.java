package model.settablecontent;

import java.awt.Color;
import model.type.Type;
import reflection.NodeDefinition;

public abstract class DefinitionWithUserSettableContent implements NodeDefinition {

    private Object[] content = null;
    private Type contentType = null;

    public Object[] getUserSettableContent() {
        return content;
    }

    public String getShortenedUserSettableContent() {
        String contentString = "";
        if(content != null) {
            boolean first = true;
            for(Object c : content) {
                if(!first)
                    contentString += ", ";
                String toString;
                if(c instanceof Color) {
                    Color color = (Color) c;
                    toString = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                } else {
                    toString = c.toString();
                }
                contentString += toString;
                first = false;
            }
        }
        if(contentString.length() > 20) {
            return contentString.substring(0, 18) + "...";
        } else {
            return contentString;
        }
    }

    public void setUserSettableContent(Object[] content) {
        this.content = content;
    }

    public Type getContentType() {
        return contentType;
    }

}
