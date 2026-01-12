/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class Manager {
    
        public static void loadRunner(JTable tblRunner) {
        DefaultTableModel model = (DefaultTableModel) tblRunner.getModel();
        model.setRowCount(0); // Clear previous data

        try (BufferedReader br = new BufferedReader(new FileReader("RunnerRevenue.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length == 4) { // Ensure correct format
                    model.addRow(new Object[]{values[0], values[1], values[2], values[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadVendor(JTable tblVendor) {
        DefaultTableModel model = (DefaultTableModel) tblVendor.getModel();
        model.setRowCount(0); // Clear previous data

        try (BufferedReader br = new BufferedReader(new FileReader("VendorRevenue.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length == 4) { // Ensure correct format
                    model.addRow(new Object[]{values[0], values[1], values[2], values[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadComplaintData(JTable tblComplaints) {
    DefaultTableModel model = (DefaultTableModel) tblComplaints.getModel();
    model.setRowCount(0); // Clear existing data

    try (BufferedReader br = new BufferedReader(new FileReader("Complaint.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");
            if (values.length == 4) { // Ensure correct format
                model.addRow(new Object[]{values[0], values[1], values[2], values[3]});
            }
        }
    } catch (IOException e) {   
        e.printStackTrace();
    }
}


    public void populateVendorComboBox(JComboBox<String> comboBox) {
        String filePath = "User.txt"; // Ensure the file is in the correct directory
        
        comboBox.removeAllItems(); // Clear existing items

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 7 && parts[7].equalsIgnoreCase("Vendor")) { // Ensure role exists
                    comboBox.addItem(parts[3]); // Assuming 'name' is at index 3
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
        }
    }
    
    public static void loadVendorMenuFromComboBox(JComboBox<String> cmbVendors, JTable tblVendors) {
    String selectedVendor = (String) cmbVendors.getSelectedItem();
    if (selectedVendor == null) {
        JOptionPane.showMessageDialog(null, "No vendor selected", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String vendorID = getVendorID(selectedVendor);
    if (vendorID == null) {
        JOptionPane.showMessageDialog(null, "Vendor ID not found for " + selectedVendor, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    loadMenuForVendor(tblVendors, vendorID);
}

private static String getVendorID(String vendorName) {
    try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 8 && parts[7].equalsIgnoreCase("Vendor")) {
                if (parts[3].trim().equalsIgnoreCase(vendorName.trim())) {
                    return parts[0]; // Vendor ID
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error reading User.txt: " + e.getMessage());
    }
    return null;
}

private static void loadMenuForVendor(JTable tblVendors, String vendorID) {
    DefaultTableModel model = (DefaultTableModel) tblVendors.getModel();
    model.setRowCount(0); // Clear previous data
    
    try (BufferedReader br = new BufferedReader(new FileReader("Menu.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");
            if (values.length >= 6 && values[0].trim().equalsIgnoreCase(vendorID)) {
                model.addRow(new Object[]{values[1], values[2], values[3], values[4], values[5]});
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error reading Menu.txt: " + e.getMessage());
    }
}


    
    public static void addSolution(JTable tblComplaint, JTextField txtSolution) {
        int selectedRow = tblComplaint.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a complaint from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get selected complaint details
        DefaultTableModel model = (DefaultTableModel) tblComplaint.getModel();
        String complaintID = model.getValueAt(selectedRow, 0).toString();
        String customerID = model.getValueAt(selectedRow, 1).toString();
        String complaineeID = model.getValueAt(selectedRow, 2).toString();
        String complaintText = model.getValueAt(selectedRow, 3).toString();
        String solutionText = txtSolution.getText().trim();

        if (solutionText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a solution before saving.", "Empty Solution", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Append to Complaint.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Complaint.txt", true))) {
            bw.write(complaintID + ";" + customerID + ";" + complaineeID + ";" + complaintText + ";" + solutionText);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Solution added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }

    }   
    
    public void removeSelectedMenuItem(JTable tblVendors,JComboBox<String> cmbVendors) {
    int selectedRow = tblVendors.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select an item to remove.");
        return;
    }

    String selectedItemID = (String) tblVendors.getValueAt(selectedRow, 0); // Get ItemID
    String selectedVendor = (String) cmbVendors.getSelectedItem(); // Get Vendor Name

    // Get VendorID from User.txt
    String vendorID = null;
    try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length >= 8 && parts[3].equalsIgnoreCase(selectedVendor) && parts[7].equalsIgnoreCase("Vendor")) {
                vendorID = parts[0]; // VendorID is at index 0
                break;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading User.txt: " + e.getMessage());
        return;
    }

    if (vendorID == null) {
        JOptionPane.showMessageDialog(null, "Vendor ID not found.");
        return;
    }

    // Read Menu.txt and remove the matching entry
    File menuFile = new File("Menu.txt");
    File tempFile = new File("Menu_temp.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(menuFile));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");
            if (values.length >= 6 && values[0].equals(vendorID) && values[1].equals(selectedItemID)) {
                continue; // Skip the matching entry (i.e., remove it)
            }
            bw.write(line);
            bw.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error processing Menu.txt: " + e.getMessage());
        return;
    }

    // Replace original Menu.txt with updated file
    if (!menuFile.delete() || !tempFile.renameTo(menuFile)) {
        JOptionPane.showMessageDialog(null, "Error updating Menu.txt.");
        return;
    }

    JOptionPane.showMessageDialog(null, "Item removed successfully.");
}
}





