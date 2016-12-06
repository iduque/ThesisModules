/* 
 * Aplicacion.java
 * 
 */

package Main;

import Controller.Controller;
import java.io.IOException;
import java.sql.*;

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

        } catch (InstantiationException ex) {
            System.out.println("Instantiation Exception");
            System.exit(0);
        } catch (IllegalAccessException ex) {
            System.out.println("Illegal Access Exception");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            System.out.println("Class Not Found Exception");
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("I/O File Exception");
            System.exit(0);
        } catch (SQLException ex) {
            System.out.println("SQL Connection Exception");
            System.exit(0);
        }
    }

}
