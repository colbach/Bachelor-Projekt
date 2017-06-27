/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _testing;

/**
 *
 * @author christiancolbach
 */
public class classfromstringtest {
    
    
    public static void main(String[] args) throws Exception {
        
        
        
        
        
        
        
        
        Class clazz = Class.forName("java.util.Date");
        
        System.out.println(clazz.getName());
        
        clazz = Class.forName("java.lang.Integer");
        
        System.out.println(clazz.getName());
        
        
        
    }
    
    
}
