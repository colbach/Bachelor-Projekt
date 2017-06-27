/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _testing;

import java.io.File;

/**
 *
 * @author christiancolbach
 */
public class FolderTest {
    
    
    
    public static void main(String[] args) {
        
        
        
        
        File f = new File("./Hallo/Welt/");
        f.mkdirs();
                System.out.println(f.getAbsoluteFile().getAbsoluteFile().getAbsolutePath());
                
                
                
                }

    public FolderTest() {
    }
}
