package reflection.nodedefinitions.composed;

import reflection.NodeDefinition;
import reflection.composednodedefinition.BidirectionalLink;
import reflection.composednodedefinition.ComposedNodeDefinition;
import reflection.composednodedefinition.InletLink;
import reflection.composednodedefinition.OutletLink;
import reflection.nodedefinitions.files.WriteRawDataNodeDefinition;
import reflection.nodedefinitions.images.convolve.ConvolveImageNodeDefinition;
import reflection.nodedefinitions.images.convolve.CreateGaussKernelNodeDefinition;
import reflection.nodedefinitions.text.TextToDataNodeDefinition;

public class BlurImageComposedNodeDefinition extends ComposedNodeDefinition implements NodeDefinition {

    public BlurImageComposedNodeDefinition() {
        super("Bild bluren");

        CreateGaussKernelNodeDefinition createGaussKernelNodeDefinition = new CreateGaussKernelNodeDefinition();
        ConvolveImageNodeDefinition convolveImageNodeDefinition = new ConvolveImageNodeDefinition();

        addNodeDefinitoon(createGaussKernelNodeDefinition);
        addNodeDefinitoon(convolveImageNodeDefinition);

        addExternalInlet(new InletLink(createGaussKernelNodeDefinition, 0));
        addExternalInlet(new InletLink(convolveImageNodeDefinition, 0));

        addInternalConnections(new BidirectionalLink(createGaussKernelNodeDefinition, 0, convolveImageNodeDefinition, 1));
        
        addExternalOutlet(new OutletLink(convolveImageNodeDefinition, 0));
    }

}
