/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_assignment;

/**
 *
 * @author user
 */
public class UserSession {
    private static String userID;
    private static String username;
    private static String name;
    private static String role;
    private static String email;
    private static String phone;
    private static String address;

    // Set user session details
    public static void setUserSession(String userID, String username, String name, String email, String phone, String address, String role) {
        UserSession.userID = userID;
        UserSession.username = username;
        UserSession.name = name;
        UserSession.email = email;
        UserSession.phone = phone;
        UserSession.address = address;
        UserSession.role = role;
    }
    
    // Getters for session details
    public static String getUserID(){
        return userID;
    }

    public static String getUsername() {
        return username;
    }

    public static String getName(){
        return name;
    }

    public static String getRole() {
        return role;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getAddress() {
        return address;
    }

    // Clear session on logout
    public static void clearSession() {
        userID = null;
        username = null;
        name = null;
        role = null;
        email = null;
        phone = null;
        address = null;
    }
}

