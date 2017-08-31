package model.settablecontent;

import java.awt.Color;
import model.type.Type;
import reflection.common.NodeDefinition;
import utils.format.ObjectArrayFormat;

public abstract class DefinitionWithUserSettableContent implements NodeDefinition {

    private Object[] content = null;
    private Type contentType = null;

    public Object[] getUserSettableContent() {
        return content;
    }

    public String getShortenedUserSettableContent() {
        return ObjectArrayFormat.formatShortened(content, 20);
    }

    public void setUserSettableContent(Object[] content) {
        this.content = content;
    }

    public Type getContentType() {
        return contentType;
    }

}
