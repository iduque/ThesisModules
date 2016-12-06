/* 
 * Aplicacion.java
 * 
 */

package Main;

import java.io.IOException;

/**
 * @author Ismael Duque García
 * @version 1.0
 */
public class Aplication {


    /**
     * Main method
     */
    public static void main(String[] args) {
        try {

            Controller c = new Controller();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
