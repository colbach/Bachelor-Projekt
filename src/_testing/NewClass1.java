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
public class NewClass1 {

    public static void main(String[] args) {

        int n = 8;

        for (int y = n; y > 0; y--) {
            for (int x = 0; x < n; x++) {

                if (x == y/2 || x == n-y/2-1) {
                    System.out.print("A");
                } else {
                    System.out.print(" ");
                }

            }
            System.out.println("");
        }

    }

}