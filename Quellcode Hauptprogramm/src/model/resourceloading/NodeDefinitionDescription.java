package model.resourceloading;

import logging.AdditionalErr;
import logging.AdditionalLogger;
import reflection.NodeDefinition;
import reflection.additionalnodedefinitioninterfaces.Experimental;

public class NodeDefinitionDescription {

    public static String getDescription(NodeDefinition definition) {
        return definition.getDescription();
    }

    public static String getDescriptionWithoutTags(NodeDefinition definition) {
        String description = definition.getDescription();
        if (description == null) {
            AdditionalLogger.err.println("Definition (" + definition.getUniqueName() + ") hat als Description null zurueck gegeben! getDescriptionWithoutTags(...) returns \"\"");
            description = "";
        } else {
            int index = description.indexOf(NodeDefinition.TAG_PREAMBLE);
            if (index != -1) {
                description = description.substring(0, index);
            }
        }

        if (definition instanceof Experimental) {
            description += "\nAchtung: Es handelt sich hierbei um ein experimentelles Element.";
            Experimental experimentalDefinition = (Experimental) definition;
            String experimentalNote = experimentalDefinition.getExperimentalNote();
            if (experimentalNote != null) {
                description += " Für dieses experimentelle Element gibt es folgende Anmerkung:\n";
                description += experimentalNote;
            }
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
            Class classForInlet = definition.getClassForInlet(i);
            if (classForInlet != null) {
                sb.append(definition.getClassForInlet(i).toString());
            } else {
                sb.append("?");
                System.err.println("Definition " + definition.getName() + " gibt bei getClassForInlet(" + i + ") null zurueck.");
            }
        }

        // Outlets...
        for (int i = 0; i < definition.getOutletCount(); i++) {
            sb.append(" ");
            sb.append(definition.getNameForOutlet(i));
            sb.append(" ");
            Class classForOutlet = definition.getClassForOutlet(i);
            if (classForOutlet != null) {
                sb.append(classForOutlet.toString());
            } else {
                System.err.println("Definition " + definition.getName() + " gibt bei getClassForInlet(" + i + ") null zurueck.");
            }
        }

        // UniqueName...
        sb.append(definition.getUniqueName());

        return sb.toString();

    }
}
