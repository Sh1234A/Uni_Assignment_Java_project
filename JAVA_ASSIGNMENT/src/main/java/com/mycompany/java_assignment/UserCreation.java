
package com.mycompany.java_assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @mayan
 */
public class UserCreation {
    
    private static final String FILENAME = "User.txt";

    public static void createUser(String name, String email, String phone, String address, String role) {
        String userID = generateUserID(role);
        String username = generateUsername(name);
        String password = generatePassword(name, phone);

        try (FileWriter fw = new FileWriter(FILENAME, true)) {
            fw.write(userID + ";" + username + ";" + password + ";" +
                     name + ";" + email + ";" + phone + ";" + address + ";" + role + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        
        // If role is customer, add to CustomerBalance.txt
        if (role.equalsIgnoreCase("customer")) {
            try (FileWriter balanceWriter = new FileWriter("CustomerBalance.txt", true)) {
                String currentDate = java.time.LocalDate.now().toString(); 
                balanceWriter.write(userID + ";0.00;" + currentDate + "\n"); // Initialize balance to 0.00
            } catch (IOException e) {
                System.out.println("Error writing to CustomerBalance.txt: " + e.getMessage());
        }
    }

        JOptionPane.showMessageDialog(null, "User created successfully!\n" +
        "UserID: " + userID + "\nUsername: " + username + "\nPassword: " + password,
        "User Created", JOptionPane.INFORMATION_MESSAGE);

    }

    private static String generateUserID(String role) {
        String prefix;
        switch (role.toLowerCase()) {
            case "customer": prefix = "C"; break;
            case "vendor": prefix = "V"; break;
            case "delivery runner": prefix = "D"; break;
            case "manager": prefix = "M"; break;
            default: throw new IllegalArgumentException("Invalid role: " + role);
        }

        int maxNumber = 0;
        File file = new File(FILENAME);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length > 0 && parts[0].startsWith(prefix)) {
                        int num = Integer.parseInt(parts[0].substring(1));
                        if (num > maxNumber) {
                            maxNumber = num;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }
        return String.format("%s%03d", prefix, maxNumber + 1);
    }

    private static String generateUsername(String name) {
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0].toLowerCase();
        int randomNum = (int) (Math.random() * 90) + 10;
        return firstName + randomNum;
    }

    private static String generatePassword(String name, String phone) {
    String[] nameParts = name.split(" ");
    String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : nameParts[0]; 
    String lastFourDigits = phone.length() >= 4 ? phone.substring(phone.length() - 4) : phone; 

    return lastName.toLowerCase() + lastFourDigits;
    }

}
