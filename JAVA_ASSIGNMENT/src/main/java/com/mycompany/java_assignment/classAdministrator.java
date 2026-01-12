
package com.mycompany.java_assignment;

import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class classAdministrator {
    
    public void createUser(String name, String email, String phone, String address, String role) {
        String filename = "User.txt";
        
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(
                "NULL" + ";" +
                "NULL" + ";" +
                "NULL" + ";" +        
                name + ";" +
                email + ";" +
                phone + ";" +
                address + ";" +
                role + "\n"
            );

            JOptionPane.showMessageDialog(null, "Account Successfully Created!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage());
        }
    }
}
