/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author kulmu
 */
public class VendorCustomerReview {
    private static final String FILE_NAME = "Review.txt";

    public static List<String[]> loadReviews() {
        List<String[]> reviews = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(";");
                if (data.length >= 4) {
                    reviews.add(data);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return reviews;
    }
}