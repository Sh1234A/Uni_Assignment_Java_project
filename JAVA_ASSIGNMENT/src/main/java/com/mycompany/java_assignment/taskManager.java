package com.mycompany.java_assignment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * TaskManager class for handling task-related operations
 */
public class taskManager {

    private Object jTable1;
    
    private String findAddress(String userID) {
    try (BufferedReader userReader = new BufferedReader(new FileReader("User.txt"))) {
        String line;
        while ((line = userReader.readLine()) != null) {
            String[] userData = line.split(";");
            if (userData.length >= 8 && userData[0].equals(userID)) {
                return userData[6]; // Address is at index 6
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading User.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return "Unknown"; // Return "Unknown" if address not found
}


    // Load tasks from Task.txt and populate JTable
    public void loadTasks(JTable jTable5) {
    DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
    model.setRowCount(0); // Clear existing rows

    List<String[]> newTasks = new ArrayList<>();

    try (BufferedReader orderReader = new BufferedReader(new FileReader("Order.txt"))) {
        String line;

        // Read orders and create tasks for status
        while ((line = orderReader.readLine()) != null) {
            String[] orderData = line.split(";");
            if (orderData.length >= 10 && "Accepted".equals(orderData[9])) {
                String orderID = orderData[0];
                String vendorID = orderData[1];
                String customerID = orderData[2];

                // Find pickup and delivery addresses from User.txt
                String pickupAddress = findAddress(vendorID);
                String deliveryAddress = findAddress(customerID);

                String[] task = {orderID, UserSession.getName(), pickupAddress, deliveryAddress};
                newTasks.add(task);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading Order.txt: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Write new tasks to Task.txt
    try (BufferedWriter taskWriter = new BufferedWriter(new FileWriter("Task.txt", true))) {
        for (String[] task : newTasks) {
            taskWriter.write(String.join(";", task));
            taskWriter.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing to Task.txt: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Load tasks into JTable
    try (BufferedReader taskReader = new BufferedReader(new FileReader("Task.txt"))) {
        String line;
        while ((line = taskReader.readLine()) != null) {
            String[] taskData = line.split(";");
            if (taskData.length >= 4) {
                model.addRow(new Object[]{taskData[0], taskData[1], taskData[2], taskData[3]});
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage(), "Error", 
                JOptionPane.ERROR_MESSAGE);
    }
}



    // Accept a task: Assign a runner ID and update Task.txt
    public void acceptTask(JTable jTable5) { 
        int selectedRow = jTable5.getSelectedRow();
        if (selectedRow != -1) {
            String orderID = jTable5.getValueAt(selectedRow, 0).toString();
            
            String runnerID = UserSession.getUserID();

            updateTaskFile(orderID, runnerID);  // Update Task.txt with the runner ID
            loadTasks(jTable5); // Refresh table
            JOptionPane.showMessageDialog(null, "Task " + orderID + " accepted!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task first!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
        
        // Add accepted task to jTable4
        public void addToAcceptedTable(JTable jTable4, JTable jTable5, int selectedRow, 
                String orderID, String runnerID, String pickupAddress, String deliveryAddress) {
    // Add the task to jTable4 (Accepted Tasks)
    DefaultTableModel acceptedModel = (DefaultTableModel) jTable4.getModel();
    acceptedModel.addRow(new Object[]{orderID, runnerID, pickupAddress, deliveryAddress});

    // Remove the task from jTable5 (Available Tasks)
    DefaultTableModel availableModel = (DefaultTableModel) jTable5.getModel();
    availableModel.removeRow(selectedRow);
    }


    // Decline a selected task (removes from JTable only, not from Task.txt)
    public void declineTask(JTable jTable5) {
        int selectedRow = jTable5.getSelectedRow();
        if (selectedRow != -1) {
            String orderID = jTable5.getValueAt(selectedRow, 0).toString();

            // Remove from JTable only
            DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
            model.removeRow(selectedRow);

            JOptionPane.showMessageDialog(null, "Task " + orderID + " declined! (Removed from your view only)");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task first!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Update Task.txt: Assign runnerID to the task
    private void updateTaskFile(String orderID, String runnerID) {
        File taskFile = new File("Task.txt");
        File tempFile = new File("TempTask.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(taskFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 1 && data[0].equals(orderID)) {
                    // Assign runnerID
                    data[1] = runnerID;
                    line = String.join(";", data);
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating Task.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Replace Task.txt with updated file
        taskFile.delete();
        tempFile.renameTo(taskFile);
    }
    
    // Mark task as completed
    public void markTaskAsCompleted(JTable jTable4) {
        int selectedRow = jTable4.getSelectedRow();
        if (selectedRow != -1) {
            String orderID = jTable4.getValueAt(selectedRow, 0).toString();
            updateOrderFile(orderID, "Complete");
            ((DefaultTableModel) jTable4.getModel()).removeRow(selectedRow);
            JOptionPane.showMessageDialog(null, "Task " + orderID + " marked as completed!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task first!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Mark task as canceled
    public void markTaskAsCanceled(JTable jTable4) {
        int selectedRow = jTable4.getSelectedRow();
        if (selectedRow != -1) {
            String orderID = jTable4.getValueAt(selectedRow, 0).toString();
            updateOrderFile(orderID, "Cancelled");
            ((DefaultTableModel) jTable4.getModel()).removeRow(selectedRow);
            JOptionPane.showMessageDialog(null, "Task " + orderID + " marked as canceled!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task first!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Update Order.txt with new status
    private void updateOrderFile(String orderID, String status) {
        File orderFile = new File("Order.txt");
        File tempFile = new File("TempOrder.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(orderFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 10 && data[0].equals(orderID)) {
                    // Update status
                    data[9] = status;
                    line = String.join(";", data);
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating Order.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Replace Order.txt with updated file
        orderFile.delete();
        tempFile.renameTo(orderFile);
    }
    
    // Update runner revenue when a task is completed
    public void updateRunnerRevenue() {
        File revenueFile = new File("RunnerRevenue.txt");
        File tempFile = new File("TempRunnerRevenue.txt");

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        String today = dayFormat.format(new Date());
        String currentMonth = monthFormat.format(new Date());
        String currentYear = yearFormat.format(new Date());

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(revenueFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");

                if (data.length >= 4 && data[0].equals(UserSession.getUserID())) {
                    double dailyRevenue = Double.parseDouble(data[1].split(":")[1]);
                    double monthlyRevenue = Double.parseDouble(data[2].split(":")[1]);
                    double yearlyRevenue = Double.parseDouble(data[3].split(":")[1]);

                    // Reset revenue if the date, month, or year has changed
                    if (!data[1].startsWith(today)) {
                        dailyRevenue = 0;
                    }
                    if (!data[2].startsWith(currentMonth)) {
                        monthlyRevenue = 0;
                    }
                    if (!data[3].startsWith(currentYear)) {
                        yearlyRevenue = 0;
                    }

                    // Add $5.00 revenue for the completed task
                    dailyRevenue += 5.00;
                    monthlyRevenue += 5.00;
                    yearlyRevenue += 5.00;

                    // Write updated revenue
                    writer.write(UserSession.getUserID() + ";" + today + ":" + dailyRevenue + ";" +
                            currentMonth + ":" + monthlyRevenue + ";" + currentYear + ":" + yearlyRevenue);
                    writer.newLine();
                    found = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            // If runner ID not found, create a new entry
            if (!found) {
                writer.write(UserSession.getUserID() + ";" + today + ":5.00;" +
                        currentMonth + ":5.00;" + currentYear + ":5.00");
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating RunnerRevenue.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Replace old file with updated file
        revenueFile.delete();
        tempFile.renameTo(revenueFile);
    }
    
//    public void loadRunnerRevenue() {
//    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//    model.setRowCount(0); // Clear existing rows
//
//    try (BufferedReader reader = new BufferedReader(new FileReader("RunnerRevenue.txt"))) {
//        String line;
//        while ((line = reader.readLine()) != null) {
//            String[] data = line.split(";");
//
//            if (data.length >= 4 && data[0].equals(UserSession.getUserID())) {
//                double dailyRevenue = Double.parseDouble(data[1]);
//                double monthlyRevenue = Double.parseDouble(data[2]);
//                double yearlyRevenue = Double.parseDouble(data[3]);
//
//                // Add runner's revenue to jTable1
//                model.addRow(new Object[]{UserSession.getUserID(), dailyRevenue, monthlyRevenue, yearlyRevenue});
//            }
//        }
//    } catch (IOException e) {
//        JOptionPane.showMessageDialog(null, "Error loading revenue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//    }
//    }
//    
//    private static final String FILE_NAME = "Order.txt";
//    private JTable jTable2;
//
//    public taskManager(JTable jTable2) {
//        this.jTable2 = jTable2;
//    }
//
//    public void loadOrders() {
//        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
//        model.setRowCount(0); // Clear table before loading
//
//        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] data = line.split(";");
//
//                if (data.length >= 10) {  // Ensure proper format
//                    model.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9]});
//                }
//            }
//        } catch (FileNotFoundException e) {
//            JOptionPane.showMessageDialog(null, "Order.txt not found!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
}
