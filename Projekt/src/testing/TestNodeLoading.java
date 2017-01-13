/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import model.resourceloading.BuildInNodeDefinitionsLibrary;
import reflection.NodeDefinition;

/**
 *
 * @author christiancolbach
 */
public class TestNodeLoading {
    
    public static void main(String[] args) {
        BuildInNodeDefinitionsLibrary nn = BuildInNodeDefinitionsLibrary.getInstance();
        
        System.out.println(nn.size());
        NodeDefinition nn0 = nn.get(0);
        
        
        System.out.println(nn0.getName());
    }
}
