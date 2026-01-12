package com.mycompany.java_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Customer {
    UserSession user;
    private static JPanel main_panel;
    public Customer() {
        
    }

    // Method to set main panel from Customer_Homepage
    public void setMainPanel(JPanel panel) {
        main_panel = panel;
    }

    // Method to switch panels
    public static void switchPanel(JPanel panel) {
        System.out.println(panel);
        if (main_panel != null) {
            main_panel.removeAll();
            main_panel.setLayout(new java.awt.BorderLayout());
            main_panel.add(panel);
            panel.setVisible(true);
            main_panel.revalidate();
            main_panel.repaint();
        } else {
            System.out.println("Main panel is not set!");
        }
    }
  //========================================================================================================================================   
    private static String getCurrentBalance(String customerID) {
        File file = new File("CustomerBalance.txt");

        if (!file.exists()) {
            return "0"; // Return 0 if the file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 2 && data[0].trim().equals(customerID)) {
                    return data[1].trim(); // Return the current balance
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading balance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return "0"; // Default to 0 if no record is found
    }
   //========================================================================================================================================  
    private static String generateTopUpID() {
        String lastID = "T000"; 
        try (BufferedReader br = new BufferedReader(new FileReader("TopUpRequest.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length > 0) {
                    lastID = data[0].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // Extract numeric part and increment
        int num = Integer.parseInt(lastID.substring(1)) + 1;
        return String.format("T%03d", num); 
    }

   //========================================================================================================================================  
    public static void createTopUpRequest(JTextField txtRequestAmount) {
        String customerID = UserSession.getUserID(); 
        String amount = txtRequestAmount.getText().trim(); 
        String currentBalance = getCurrentBalance(customerID); 
        String date = LocalDate.now().toString(); 
        String status = "Sent";
        String topupID = generateTopUpID(); 

        if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("TopUpRequest.txt", true))) {
            bw.write(topupID + ";" + customerID + ";" + amount + ";" + currentBalance + ";" + date + ";" + status);
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Top-up request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private static String selectedRestaurant = "Restaurant 1"; // Default value
    private static String selectedCategory = "All Category";   // Default value
 //========================================================================================================================================
        // **New method to load data into combo box from a file**
    public static void loadComboBoxData(JComboBox<String> comboBox, String filename) {
        comboBox.removeAllItems(); // Clear existing items
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String role = line.split(";")[7];
                if("Vendor".equals(role)) {
                 String name = line.split(";")[3];
                comboBox.addItem(name); // Add each line as an item   
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading " + filename, "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 //========================================================================================================================================
    public static void loadComboBoxData2(JComboBox<String> comboBox, String filename) {
        comboBox.removeAllItems(); // Clear existing items
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                 String category = line.split(";")[4];
                comboBox.addItem(category); // Add each line as an item   
                
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading " + filename, "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Method to set selected values
    public static void setSelections(String restaurant, String category) {
        selectedRestaurant = restaurant;
        selectedCategory = category;
    }

    // Method to retrieve selected restaurant
    public static String getSelectedRestaurant() {
        return selectedRestaurant;
    }

    // Method to retrieve selected category
    public static String getSelectedCategory() {
        return selectedCategory;
    }
 //========================================================================================================================================
    public static void displayMenuFromFile(String category, String vendorId,  DefaultTableModel tableModel) {
    try {
        tableModel.setRowCount(0);
        FileReader fr  = new  FileReader("Menu.txt");
        BufferedReader reader = new BufferedReader(fr);
        String line;
        while ((line = reader.readLine()) != null) {
             String[] parts = line.split(";");
                String venid = parts[0].trim();
                String nameItems = parts[2].trim();
                double price = Double.parseDouble(parts[3].trim());
                String itemCategory = parts[4].trim();
                String description = parts[5].trim(); //where is decription in the file menu.txt
                if (category.equals(itemCategory)) {
                    
                    if(venid.equals(vendorId)) {
                        System.out.println("done");
                         tableModel.addRow(new Object[]{nameItems, description, price});   
                    }
            } 
        }
        reader.close();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading menu file: " + e.getMessage());
    }
}
  //======================================================================================================================================
     public static String getVendorIDByName(String RestName) {
      String username = null;
        try {
        FileReader fr = new FileReader("User.txt");
        BufferedReader br =  new BufferedReader(fr);
         String read;
                 while((read=br.readLine()) !=null) {
                     String idfile = read.split(";")[0].trim();
                     String nameUser = read.split(";")[3].trim();
                     if(RestName.equals(nameUser)) {
                         username = idfile;
                         break;
                     }              
                 }
                 } catch(IOException e) {
                   JOptionPane.showMessageDialog(null, e.getMessage()); 
              }
             return username;
    }
     //======================================================================
     public static void trackOrder() {
    File file = new File("Order.txt"); // File containing order details

    if (!file.exists() && file.length() == 0) {
        JOptionPane.showMessageDialog(null, "No orders found!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean hasOngoingOrder = false; 

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";"); 
            System.out.println(data[1].trim().equals(UserSession.getUserID()));
            if (data[1].trim().equals(UserSession.getUserID())) { 
                String status = data[5].trim(); 
                System.out.println(status);
                if (status.equalsIgnoreCase("In Progress")) {
                    Order_in_progress panel = new Order_in_progress();
                    switchPanel(panel);
                    hasOngoingOrder = true;
                    break; 
                } else if (status.equalsIgnoreCase("Completed")) {
                    Order_completed panel = new Order_completed();
                    switchPanel(panel);
                    hasOngoingOrder = true;
                    break;
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading Order.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    // If no order is in progress or completed, show a message
    if (!hasOngoingOrder) {
        JOptionPane.showMessageDialog(null, "You have no ongoing orders.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}public static String getFirstOrderIdByCustomerId(String customerId) {
    String file = "Order.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1 && parts[1].equals(customerId) && "In Progress".equals(parts[5])) {
                    return parts[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
public static String getOrderStatusByCustomerId(String customerId) {
    String file = "Order.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1 && parts[1].equals(customerId) && "In Progress".equals(parts[5])) {
                    return parts[5];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 public static void displayOrderHistory(String customerId, DefaultTableModel tableModel) {
        
       tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("Order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1 && parts[1].equals(customerId) && !"Completed".equals(parts[5])) {
                    String[] partss = {parts[0], parts[2], parts[5], parts[4]};
                    tableModel.addRow(partss);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

     //==========================================================================
     public static void addOrder(String menuItem, int quantity) {
    String customerID = UserSession.getUserID(); // Get logged-in user ID
    String orderID = generateOrderID(); // Generate a unique order ID
    String status = "In Progress"; // Default order status
    String date = LocalDate.now().toString(); // Get today's date

    // Check if quantity is valid
    if (quantity <= 0) {
        JOptionPane.showMessageDialog(null, "Please enter a valid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
         System.out.println("1");
    // Append order to file
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Order.txt", true))) {
        bw.write(orderID + ";" + customerID + ";" + menuItem + ";" + quantity + ";" + date + ";" + status);
        bw.newLine();
        System.out.println(orderID + ";" + customerID + ";" + menuItem + ";" + quantity + ";" + date + ";" + status);
        JOptionPane.showMessageDialog(null, "Order added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing to Order.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
     //=========================================================================================================================================================
     private static String generateOrderID() {
    File file = new File("Order.txt");
    int lastOrderID = 500; 

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length > 0) {
                    lastOrderID = Math.max(lastOrderID, Integer.parseInt(data[0].trim()));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error reading Order.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    return String.valueOf(lastOrderID + 1); 
}
//=========================================================================================================================================================
     public static void saveTransaction(String customerID, double amount, String type) {
    String transactionID = generateTransactionID(); 
    String date = LocalDate.now().toString(); 

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Transactions.txt", true))) {
        bw.write(transactionID + ";" + customerID + ";" + amount + ";" + type + ";" + date);
        bw.newLine();
        JOptionPane.showMessageDialog(null, "Transaction saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing to Transactions.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
//=========================================================================================================================================================
     public static void setBalance(String customerID, double newBalance) {
    File file = new File("CustomerBalance.txt");
    File tempFile = new File("TempBalance.txt");

    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 2 && data[0].trim().equals(customerID)) {
                bw.write(customerID + ";" + newBalance);
                found = true;
            } else {
                bw.write(line);
            }
            bw.newLine();
        }

        // If the customer ID was not found, add a new entry
        if (!found) {
            bw.write(customerID + ";" + newBalance);
            bw.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating balance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Replace the original file with the updated file
    file.delete();
    tempFile.renameTo(file);
}
   //=========================================================================================================================================================
     public static double getBalance(String customerID) {
    File file = new File("CustomerBalance.txt");

    if (!file.exists()) {
        return 0.0; // Return 0 if the file does not exist
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 2 && data[0].trim().equals(customerID)) {
                return Double.parseDouble(data[1].trim()); // Return balance
            }
        }
    } catch (IOException | NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error reading balance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return 0.0; // Default to 0 if no record is found
}
  //=========================================================================================================================================================
     private static String generateTransactionID() {
    File file = new File("Transactions.txt");
    int lastTransactionID = 1000; // Starting transaction ID

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length > 0) {
                    lastTransactionID = Math.max(lastTransactionID, Integer.parseInt(data[0].trim()));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error reading Transactions.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    return String.valueOf(lastTransactionID + 1);
}
 
//=============================================================================================================================================
     public static void saveNotification(String customerID, String message) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Notifications.txt", true))) {
        bw.write(customerID + ";" + message + ";Unread");
        bw.newLine();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error saving notification: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
//=============================================================================================================================================
     public static void markNotificationsAsRead(String customerID) {
    File file = new File("Notifications.txt");
    File tempFile = new File("TempNotifications.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(file));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 3 && data[0].trim().equals(customerID) && data[2].trim().equals("Unread")) {
                bw.write(data[0] + ";" + data[1] + ";Read"); // Mark as read
            } else {
                bw.write(line);
            }
            bw.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Replace original file
    file.delete();
    tempFile.renameTo(file);
}
//=====================================================================================================================
     public static String getNotifications(String customerID,  DefaultTableModel tableModel) {
    tableModel.setRowCount(0);
    File file = new File("Notifications.txt");
    StringBuilder notifications = new StringBuilder();
    if (!file.exists()) {
        return "No notifications.";
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 3 && data[0].trim().equals(customerID) && data[2].trim().equals("Unread")) {
               String[] parts = {data[1]};
               tableModel.addRow(parts);
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return notifications.length() > 0 ? notifications.toString() : "No new notifications.";
}
//================================================================================================
    public static void displayRatings(String videoTitle, DefaultTableModel model, JLabel averageRatingLabel) {
        File file = new File("Rating.txt");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No ratings available", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        model.setRowCount(0); // Clear existing data
        int totalRating = 0, count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3 && data[0].trim().equalsIgnoreCase(videoTitle)) {
                    String rating = data[2].trim();
                    String comment = data[3].trim();
                    model.addRow(new Object[]{rating + " ★", comment});
                    try {
                        totalRating += Integer.parseInt(rating);
                        count++;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading ratings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (count > 0) {
            double averageRating = (double) totalRating / count;
           averageRatingLabel.setText("Average Rating: " + String.format("%.2f", averageRating) + " ★");
        } else {
            averageRatingLabel.setText("Average Rating: N/A");
        }
    }
//================================================================================================================== 
    public void loadTopupHistory(JTable tblTopupHistory) {
        String currentUserID = UserSession.getUserID(); 

        DefaultTableModel model = (DefaultTableModel) tblTopupHistory.getModel();
        model.setRowCount(0); // Clear existing data

        Map<String, String[]> requestMap = new HashMap<>(); // Stores {topupID → [dateSent, status]}

        // Read TopUpRequest.txt to get dateSent & status for the current user
        try (BufferedReader br = new BufferedReader(new FileReader("TopUpRequest.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[1].trim().equals(currentUserID)) { 
                    String topupID = data[0].trim();
                    String dateSent = data[4].trim();
                    String status = data[5].trim();
                    requestMap.put(topupID, new String[]{dateSent, status});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading TopUpRequest.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read TopUpReceipt.txt and match entries using topupID
        try (BufferedReader br = new BufferedReader(new FileReader("TopUpReceipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[1].trim().equals(currentUserID)) { // Ensure correct column for customerID
                    String topupID = data[0].trim();
                    String amount = data[2].trim();
                    String oldBalance = data[3].trim();
                    String newBalance = data[4].trim();
                    String dateCompleted = data[5].trim();

                    // Retrieve request details
                    String dateSent = requestMap.containsKey(topupID) ? requestMap.get(topupID)[0] : "N/A";
                    String status = requestMap.containsKey(topupID) ? requestMap.get(topupID)[1] : "Unknown";

                    // Add row to table
                    model.addRow(new Object[]{topupID, amount, status, dateSent, dateCompleted});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading TopUpReceipt.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//=============================================================================================================================================================    
    public void loadUserDetails(JTextField txtID, JTextField txtUsername, JTextField txtPassword, 
                JTextField txtName, JTextField txtEmail, JTextField txtPhone, JTextField txtAddress) {
            String currentUserID = UserSession.getUserID(); // Get logged-in user ID

            try (BufferedReader br = new BufferedReader(new FileReader("User.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length >= 5 && data[0].trim().equals(currentUserID)) { // Match UserID
                        txtID.setText(data[0].trim());   
                        txtUsername.setText(data[1].trim());
                        txtPassword.setText(data[2].trim());
                        txtName.setText(data[3].trim());      
                        txtEmail.setText(data[4].trim());      
                        txtPhone.setText(data[5].trim());
                        txtAddress.setText(data[6].trim());
                        return; // Exit loop once found
                    }
                } br.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading user details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } 
        }
 //====================================================================================================================================================  
    public void loadOrderHistory(JTable tblHistory) {
        try {
            List<String[]> orders = new ArrayList<>();
            String userID = UserSession.getUserID();
            BufferedReader orderReader = new BufferedReader(new FileReader("Order.txt"));
            String line;
            while ((line = orderReader.readLine()) != null) {
                String[] order = line.split(";");
                if (order[9].equals("Complete") && order[2].equals(userID)) {
                    orders.add(order);
                }
            }
            orderReader.close();

            BufferedReader menuReader = new BufferedReader(new FileReader("Menu.txt"));
            List<String[]> menuList = new ArrayList<>();
            while ((line = menuReader.readLine()) != null) {
                menuList.add(line.split(";"));
            }
            menuReader.close();

            DefaultTableModel model = (DefaultTableModel) tblHistory.getModel();
            model.setRowCount(0);

            for (String[] order : orders) {
                String vendorID = order[1];
                String itemID = order[3];
                String date = order[4];
                String totalAmount = order[9];

                String itemName = "Unknown Item";
                for (String[] menuItem : menuList) {
                    if (menuItem[1].equals(itemID)) {
                        itemName = menuItem[2];
                        break;
                    }
                }

                String restaurantName = "Vendor " + vendorID;
                BufferedReader userReader = new BufferedReader(new FileReader("User.txt"));
                while ((line = userReader.readLine()) != null) {
                    String[] user = line.split(";");
                    if (user[0].equals(vendorID) && user[7].equals("Vendor")) {
                        restaurantName = user[3];
                        break;
                    }
                }
                userReader.close();

                int numberOfItems = (int) orders.stream().filter(o -> o[3].equals(itemID) && o[2].equals(userID)).count();
                model.addRow(new Object[]{restaurantName, itemName, numberOfItems, totalAmount, date});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//=============================================================================================================================================
    public void updateUserDetails(JTextField txtID, JTextField txtUsername, JTextField txtPassword, 
                              JTextField txtName, JTextField txtEmail, JTextField txtPhone, JTextField txtAddress) {
    String currentUserID = UserSession.getUserID(); // Get logged-in user ID
    File inputFile = new File("User.txt");
    File tempFile = new File("User_temp.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

        String line;
        boolean updated = false;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length >= 7 && data[0].trim().equals(currentUserID)) { // Match UserID
                // Update the line with new user details
                String updatedLine = String.join(";",
                    txtID.getText().trim(),
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    txtName.getText().trim(),
                    txtEmail.getText().trim(),
                    txtPhone.getText().trim(),
                    txtAddress.getText().trim()
                );
                bw.write(updatedLine);
                updated = true;
            } else {
                // Write the original line
                bw.write(line);
            }
            bw.newLine();
        }

        if (!updated) {
            JOptionPane.showMessageDialog(null, "User ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating user details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Replace original file with updated file
    if (!inputFile.delete()) {
        JOptionPane.showMessageDialog(null, "Error replacing the old file!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!tempFile.renameTo(inputFile)) {
        JOptionPane.showMessageDialog(null, "Error renaming the new file!", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "User details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
//=====================================================================================================================
public void loadReviews(DefaultTableModel tableModel, String userID) {
    tableModel.setRowCount(0); // Clear table before loading new data

    try (BufferedReader reader = new BufferedReader(new FileReader("Reviews.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts[0].equals(userID)) { // Match user ID
                tableModel.addRow(new Object[]{parts[2], parts[3]});  // {Restaurant, Review}
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error loading reviews.");
    }
}
//=====================================================================================================================
  public void saveReview(String userID, String orderID, String restaurant, String reviewText, int rating, int vendorRating) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Reviews.txt", true))) {
        writer.write(userID + ";" + orderID + ";" + restaurant + ";" + reviewText + ";" + rating);
        writer.newLine();
        JOptionPane.showMessageDialog(null, "Review submitted successfully!");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saving review.");
    }
}
//======================================================================================================================
public void loadComplaints(DefaultTableModel tableModel, String userID) {
    tableModel.setRowCount(0); // Clear table before adding new data

    try (BufferedReader reader = new BufferedReader(new FileReader("Complaints.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts[0].equals(userID)) { // Check if complaint belongs to the user
                tableModel.addRow(new Object[]{parts[1], parts[2]}); // Complainee-ID, Complaint
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error loading complaints.");
    }
}
//=====================================================================================================================
public void saveComplaint(String userID, String complaineeID, String complaintText) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Complaints.txt", true))) {
        writer.write(userID + ";" + complaineeID + ";" + complaintText);
        writer.newLine();
        JOptionPane.showMessageDialog(null, "Complaint submitted successfully!");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error saving complaint.");
    }
}
//=====================================================================================================================

    //  Method to get selected rating for Rider
    public int getRiderRating(JRadioButton awful_rider, JRadioButton poor_rider, JRadioButton average_rider, JRadioButton good_rider, JRadioButton excellent_rider) {
        if (awful_rider.isSelected()) return 1;
        if (poor_rider.isSelected()) return 2;
        if (average_rider.isSelected()) return 3;
        if (good_rider.isSelected()) return 4;
        if (excellent_rider.isSelected()) return 5;
        return -1; // No selection
    }

    //  Method to get selected rating for Vendor
    public int getVendorRating(JRadioButton awful_vendor, JRadioButton poor_vendor, JRadioButton average_vendor, JRadioButton good_vendor, JRadioButton excellent_vendor) {
        if (awful_vendor.isSelected()) return 1;
        if (poor_vendor.isSelected()) return 2;
        if (average_vendor.isSelected()) return 3;
        if (good_vendor.isSelected()) return 4;
        if (excellent_vendor.isSelected()) return 5;
        return -1; // No selection
    }

//=====================================================================================================================
  public static void addVendorRating(String vendorID, int rating, String feedback) {
    String filePath = "VendorRatings.txt";

    // Validate rating input (ensure it's between 1 and 5)
    if (rating < 1 || rating > 5) {
        JOptionPane.showMessageDialog(null, "Rating must be between 1 and 5.", "Invalid Rating", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
        // Append new rating entry
        bw.write(vendorID + ";" + rating + ";" + feedback);
        bw.newLine();
        JOptionPane.showMessageDialog(null, "Rating added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
    }
}

//=====================================================================================================================
  public void updateMyProfile(String userID, JTextField txtName, JTextField txtUsername, JTextField txtEmail, 
                            JTextField txtPhone, JTextField txtAddress, JTextField txtPassword, String c) {
    
    File inputFile = new File("User.txt");
    File tempFile = new File("User_temp.txt");
    
    boolean userUpdated = false;

    try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");

            if (data.length >= 7 && data[0].trim().equals(userID)) { 

                // Update line with new user details
                line = userID + ";" + 
                       txtUsername.getText().trim() + ";" + 
                       txtPassword.getText().trim() + ";" + 
                       txtName.getText().trim() + ";" + 
                       txtEmail.getText().trim() + ";" + 
                       txtPhone.getText().trim() + ";" + 
                       txtAddress.getText().trim() + ";" + 
                       UserSession.getRole(); // Ensure role is preserved as Customer
                
                userUpdated = true;
            }

            bw.write(line + System.lineSeparator());
        }

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Replace old file with updated file
    if (inputFile.delete() && tempFile.renameTo(inputFile)) {
        if (userUpdated) {
            JOptionPane.showMessageDialog(null, "Profile Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Error replacing the file.", "Error", JOptionPane.ERROR_MESSAGE);
    }

}

//=====================================================================================================================
//=====================================================================================================================
//=====================================================================================================================
//=====================================================================================================================
//=====================================================================================================================
//=====================================================================================================================

}


