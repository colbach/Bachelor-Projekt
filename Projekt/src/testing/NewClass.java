/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.util.ArrayList;
import model.Inlet;
import utils.structures.Tuple;

/**
 *
 * @author christiancolbach
 */
public class NewClass {
    public static void main(String[] args) {
        
        Number n = new Integer(6);
        String s = "Hallo";
        
        Comparable c = (Comparable) n;
        
        Class a = Number.class;
        Class b = Comparable.class;
        Class d = String.class;
        
        
        //ClassUtils.isAssignable(Class, Class, true);
        
        //clazz.isAssignableFrom(obj.getClass()) == clazz.isInstance(obj);
        
       /* System.out.println("===" + b.isInstance(a));
        
        
        System.out.println(a.isAssignableFrom(b));
        System.out.println(b.isAssignableFrom(a));
        System.out.println(b.isAssignableFrom(d));
        System.out.println(d.isAssignableFrom(b));
        System.out.println(n instanceof Comparable);
        */
        /*Comparable c = new Number() {
            @Override
            public int intValue() {
                return 1;
            }

            @Override
            public long longValue() {
                return 1;
            }

            @Override
            public float floatValue() {
                return 1f;
            }

            @Override
            public double doubleValue() {
                return 1d;
            }
        };*/
        
        
    }
}
