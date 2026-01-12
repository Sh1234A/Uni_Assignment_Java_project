/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kulmu
 */
public class VendorOrderHistory {
    private static final String FILE_NAME = "Order.txt";
    
    public static List<String[]> loadOrders() {
        List<String[]> orders = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILE_NAME));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                orders.add(line.split(";"));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("not found");
        }
        return orders;
    }
    public static void updateOrderStatus(String orderId, String newStatus) {
        List<String[]> orders = loadOrders();
        boolean orderFound = false;

        try {
            PrintWriter writer = new PrintWriter(FILE_NAME);
            for (String[] order : orders) {
                if (order.length > 9 && order[0].equals(orderId)) {
                    order[9] = newStatus;
                    orderFound = true;
                }
                writer.println(String.join(";", order));
            }
            writer.close();

            if (orderFound) {
                System.out.println("The order status has been updated");
            } else {
                System.out.println("An order with this ID was not found");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error"+ e.getMessage());
        }
    }
}
