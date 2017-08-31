package reflection.nodedefinitions.composed;

import reflection.NodeDefinition;
import reflection.composednodedefinition.BidirectionalLink;
import reflection.composednodedefinition.ComposedNodeDefinition;
import reflection.composednodedefinition.InletLink;
import reflection.composednodedefinition.OutletLink;
import reflection.nodedefinitions.files.WriteRawDataNodeDefinition;
import reflection.nodedefinitions.text.TextToDataNodeDefinition;

public class WriteTextToFileComposedNodeDefinition extends ComposedNodeDefinition implements NodeDefinition {

    public WriteTextToFileComposedNodeDefinition() {
        super("Text in Datei schreiben");

        TextToDataNodeDefinition textToDataNodeDefinition = new TextToDataNodeDefinition();
        WriteRawDataNodeDefinition writeRawDataNodeDefinition = new WriteRawDataNodeDefinition();

        addNodeDefinitoon(textToDataNodeDefinition);
        addNodeDefinitoon(writeRawDataNodeDefinition);

        addExternalInlet(new InletLink(writeRawDataNodeDefinition, 0));
        addExternalInlet(new InletLink(writeRawDataNodeDefinition, 2));
        addExternalInlet(new InletLink(textToDataNodeDefinition, 0));
        addExternalInlet(new InletLink(textToDataNodeDefinition, 1));

        addInternalConnections(new BidirectionalLink(textToDataNodeDefinition, 0, writeRawDataNodeDefinition, 1));
    }

}
