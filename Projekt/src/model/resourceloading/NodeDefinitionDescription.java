package model.resourceloading;

import reflection.NodeDefinition;

public class NodeDefinitionDescription {

    public static String getDescription(NodeDefinition definition) {
        return definition.getDescription();
    }

    public static String getDescriptionWithoutTags(NodeDefinition definition) {
        String description = definition.getDescription();
        int index = description.indexOf(NodeDefinition.TAG_PREAMBLE);
        if (index != -1) {
            return description.substring(0, index);
        }
        return description;
    }

    public static String getTotalStringWithAllInformation(NodeDefinition definition) {

        // Name...
        StringBuilder sb = new StringBuilder(definition.getName());
        sb.append(" ");
        
        // Beschreibung...
        sb.append(definition.getDescription());
        
        // Inlets...
        for (int i = 0; i < definition.getInletCount(); i++) {
            sb.append(" ");
            sb.append(definition.getNameForInlet(i));
            sb.append(" ");
            sb.append(definition.getClassForInlet(i).toString());
        }
        
        // Outlets...
        for (int i = 0; i < definition.getOutletCount(); i++) {
            sb.append(" ");
            sb.append(definition.getNameForOutlet(i));
            sb.append(" ");
            sb.append(definition.getClassForOutlet(i).toString());
        }
        
        // UniqueName...
        sb.append(definition.getUniqueName());
        
        return sb.toString();

    }
}
