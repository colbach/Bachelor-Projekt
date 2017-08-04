package reflection.nodedefinitions.composed;

import reflection.NodeDefinition;
import reflection.composednodedefinition.BidirectionalLink;
import reflection.composednodedefinition.ComposedNodeDefinition;
import reflection.composednodedefinition.DefaultDataForInlet;
import reflection.composednodedefinition.InletLink;
import reflection.composednodedefinition.OutletLink;
import reflection.nodedefinitions.files.ChooseFileNodeDefinition;
import reflection.nodedefinitions.files.WriteRawDataNodeDefinition;
import reflection.nodedefinitions.text.TextToDataNodeDefinition;

public class WriteTextToFileViaDialogComposedNodeDefinition extends ComposedNodeDefinition implements NodeDefinition {

    public WriteTextToFileViaDialogComposedNodeDefinition() {
        super("Text via Dialog speichern");

        ChooseFileNodeDefinition chooseFileNodeDefinition = new ChooseFileNodeDefinition();
        TextToDataNodeDefinition textToDataNodeDefinition = new TextToDataNodeDefinition();
        WriteRawDataNodeDefinition writeRawDataNodeDefinition = new WriteRawDataNodeDefinition();

        addNodeDefinitoon(chooseFileNodeDefinition);
        addNodeDefinitoon(textToDataNodeDefinition);
        addNodeDefinitoon(writeRawDataNodeDefinition);
        

        addDefaultDataForInlet(new DefaultDataForInlet(new Boolean[] {true}, chooseFileNodeDefinition, 2));
        addDefaultDataForInlet(new DefaultDataForInlet(new String[] {"Speicherort für Textdatei wählen"}, chooseFileNodeDefinition, 3));
        
        addExternalInlet(new InletLink(chooseFileNodeDefinition, 0));
        addExternalInlet(new InletLink(writeRawDataNodeDefinition, 2));
        addExternalInlet(new InletLink(textToDataNodeDefinition, 0));
        addExternalInlet(new InletLink(textToDataNodeDefinition, 1));
        
        addInternalConnections(new BidirectionalLink(chooseFileNodeDefinition, 0, writeRawDataNodeDefinition, 0));
        addInternalConnections(new BidirectionalLink(textToDataNodeDefinition, 0, writeRawDataNodeDefinition, 1));
        
    }

}
