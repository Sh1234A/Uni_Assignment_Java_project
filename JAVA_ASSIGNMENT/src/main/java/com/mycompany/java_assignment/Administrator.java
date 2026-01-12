package com.mycompany.java_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextField;


/**
 *
 * @mayan
 */
public class Administrator {
    private JTable tblUsers;
    
    public Administrator() {
        this.tblUsers = tblUsers;
    }
    
    
    // Method to create new users
    public void createNewUser(String name, String email, String phone, String address, String role) {
        UserCreation.createUser(name, email, phone, address, role);
    }
    
    // Method to match userID from table
    public boolean isUserIDMatching(JTable tblUsers, String userID) {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow != -1) { 
            String selectedUserID = tblUsers.getValueAt(selectedRow, 0).toString();
            return selectedUserID.equals(userID);
    }
    
    return false; 
    }

    // Method to load users according role, into a table
    public void loadUsersFromFile(String selectedRole, JTable tblUsers) {
        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0); 

        try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 8) {
                    String userID = data[0].trim();
                    String name = data[3].trim();
                    String role = data[7].trim();

                    if (role.equalsIgnoreCase(selectedRole)) {
                        model.addRow(new Object[]{userID, name});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void PopulateTextFields(JTable tblUsers, JTextField txtID, JTextField txtName, JTextField txtUsername,
        JTextField txtEmail, JTextField txtPhone, JTextField txtAddress, JTextField txtPassword) {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow != -1) { 
            String selectedUserID = tblUsers.getValueAt(selectedRow, 0).toString(); 

            try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");

                    if (data.length >= 8 && isUserIDMatching(tblUsers, data[0].trim())) {
                        txtID.setText(data[0].trim());       
                        txtName.setText(data[3].trim());
                        txtUsername.setText(data[1].trim());
                        txtEmail.setText(data[4].trim());    
                        txtPhone.setText(data[5].trim());    
                        txtAddress.setText(data[6].trim());  
                        txtPassword.setText(data[2].trim()); 

                        break; 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    public void DeleteSelectedUser(JTable tblUsers) {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow != -1) { 
            String selectedUserID = tblUsers.getValueAt(selectedRow, 0).toString();

            File inputFile = new File("User.txt");
            File tempFile = new File("User_temp.txt");

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");

                    // Write all lines except the selected user
                    if (data.length >= 8 && !data[0].trim().equals(selectedUserID)) {
                        bw.write(line + System.lineSeparator());
                        
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Replace the original file with the updated one
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                // Remove row from JTable
                DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
                model.removeRow(selectedRow);
            } else {
                System.out.println("Error replacing the file");
            }
            JOptionPane.showMessageDialog(null, "User Deleted Successfully!");
        }

        }
    
    public void UpdateSelectedUser(JTable tblUsers, JTextField txtID, JTextField txtName, JTextField txtUsername,
                               JTextField txtEmail, JTextField txtPhone, JTextField txtAddress, JTextField txtPassword){
        
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow != -1) { 
            String selectedUserID = tblUsers.getValueAt(selectedRow, 0).toString(); 

            File inputFile = new File("User.txt");
            File tempFile = new File("User_temp.txt");

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");

                    if (data.length >= 8 && data[0].trim().equals(selectedUserID)) {
                        // Replace with updated values
                        line = selectedUserID + ";" + 
                               txtUsername.getText().trim() + ";" + 
                               txtPassword.getText().trim() + ";" + 
                               txtName.getText().trim() + ";" + 
                               txtEmail.getText().trim() + ";" + 
                               txtPhone.getText().trim() + ";" + 
                               txtAddress.getText().trim() + ";" + 
                               data[7].trim();  // Keep role unchanged
                    }

                    bw.write(line + System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (inputFile.delete() && tempFile.renameTo(inputFile)) {

                DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
                model.setValueAt(txtName.getText().trim(), selectedRow, 1);  // Update name column in JTable

                JOptionPane.showMessageDialog(null, "User Updated Successfully!");
            } else {
                System.out.println("Error replacing the file");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to update.");
        }

    }
    
    public void loadRequests(JTable tblRequests) {
        DefaultTableModel model = (DefaultTableModel) tblRequests.getModel();
        model.setRowCount(0); // Clear existing rows before loading new data

        try (BufferedReader br = new BufferedReader(new FileReader("TopUpRequest.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                if (data.length >= 6) {
                    String topupID = data[0].trim();
                    String customerID = data[1].trim();
                    String amount = data[2].trim();
                    String date = data[4].trim();
                    String status = data[5].trim(); 

                    // Only show requests that are still "Sent"
                    if (status.equalsIgnoreCase("Sent")) {
                        model.addRow(new Object[]{topupID, customerID, amount, date});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading top-up requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // Method to top up customer balance
    public void processTopUp(JTable tblRequests) {
    int selectedRow = tblRequests.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select a request to process.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String topupID = tblRequests.getValueAt(selectedRow, 0).toString();
    String customerID = tblRequests.getValueAt(selectedRow, 1).toString();
    double topUpAmount = Double.parseDouble(tblRequests.getValueAt(selectedRow, 2).toString());

    File balanceFile = new File("CustomerBalance.txt");
    File tempBalanceFile = new File("CustomerBalance_temp.txt");
    File requestFile = new File("TopUpRequest.txt");
    File tempRequestFile = new File("TopUpRequest_temp.txt");
    File receiptFile = new File("TopUpReceipt.txt");

    double oldBalance = 0;
    double newBalance = 0;
    String modifiedDate = java.time.LocalDate.now().toString();
    boolean updatedBalance = false;
    boolean updatedRequest = false;

    // Update balance in CustomerBalance.txt
    try (BufferedReader br = new BufferedReader(new FileReader(balanceFile));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempBalanceFile))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 3 && data[0].trim().equals(customerID)) {
                oldBalance = Double.parseDouble(data[1].trim());
                newBalance = oldBalance + topUpAmount;
                bw.write(customerID + ";" + newBalance + ";" + modifiedDate + System.lineSeparator());
                updatedBalance = true;
                System.out.println("Balance Updated: " + customerID + " | Old: " + oldBalance + " | New: " + newBalance);
            } else {
                bw.write(line + System.lineSeparator());
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating customer balance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!updatedBalance) {
        JOptionPane.showMessageDialog(null, "Customer not found in balance file.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Ensure original file is deleted before renaming
    if (!balanceFile.delete()) {
        JOptionPane.showMessageDialog(null, "Error deleting balance file. Make sure it's not open.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!tempBalanceFile.renameTo(balanceFile)) {
        JOptionPane.showMessageDialog(null, "Error replacing balance file.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    //Write to TopUpReceipt.txt
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(receiptFile, true))) {
        bw.write(topupID + ";" + customerID + ";" + topUpAmount + ";" + oldBalance + ";" + newBalance + ";" + modifiedDate + System.lineSeparator());
        System.out.println("Receipt Created for " + topupID);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing to top-up receipt file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Update `TopUpRequest.txt`
    try (BufferedReader br = new BufferedReader(new FileReader(requestFile));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempRequestFile))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 6 && data[0].trim().equals(topupID) && data[5].trim().equalsIgnoreCase("Sent")) {
                data[5] = "Complete"; // ✅ Change status
                updatedRequest = true;
                System.out.println("Top-up request updated: " + topupID);
            }
            bw.write(String.join(";", data) + System.lineSeparator());
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating top-up request status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!updatedRequest) {
        JOptionPane.showMessageDialog(null, "Request not found or already completed.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!requestFile.delete()) {
        JOptionPane.showMessageDialog(null, "Error deleting request file. Make sure it's not open.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!tempRequestFile.renameTo(requestFile)) {
        JOptionPane.showMessageDialog(null, "Error replacing top-up request file.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JOptionPane.showMessageDialog(null, "Top-up processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

    // Refresh the table to remove completed requests
    loadRequests(tblRequests);
}


}

    

    





