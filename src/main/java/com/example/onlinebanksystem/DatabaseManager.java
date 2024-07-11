package com.example.onlinebanksystem;

import com.example.onlinebanksystem.GcashApp.CashTransfer;
import com.example.onlinebanksystem.GcashApp.Transaction;
import com.example.onlinebanksystem.GcashApp.UserAuthentication;
import javafx.scene.control.Alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static String url= "jdbc:mysql://localhost:3306/bankingsystem";
    private static String dbuser ="root";
    private static String dbpw ="";
    private static String users ="select * from users";
    private static String balance ="select * from balance";
    private static String transaction = "select * from transactions";

    private static UserAuthentication currentUser;
    //users DBReference {1-userID(int), 2-Name(String), 3-Email(String), 4-Number(String), 5-PIN(String)}
    //balance DBreference {1- BalanceID(int), 2-Amount(String), 3-UserID(int)}

    public static UserAuthentication checkUser(String input_mNumber, String input_Pin) {
        currentUser = null;
        boolean found = false;
        try {
            Connection con= DriverManager.getConnection(url,dbuser,dbpw);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(users);
            while(rs.next()){
               // System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5));
                if (rs.getString(4).equals(input_mNumber)){
                    if (rs.getString(5).equals(input_Pin)){
                        UserAuthentication userAuthentication = new UserAuthentication(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5));
                                currentUser = userAuthentication;
                                found = true;
                                break;
                        }
                        else{//if mobile number has been found but Pin number is incorrect
                                ControlManager controlManager = new ControlManager();
                                controlManager.notif("PIN is Incorrect!", Alert.AlertType.ERROR);
                                found = true;
                                break;
                        }
                    }
                }

            if(!found) {//if mobile number has not been found
                ControlManager controlManager = new ControlManager();
                controlManager.notif("Mobile Number is Not Registered!", Alert.AlertType.ERROR);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    return currentUser;
    }

    public static void changeUserPin(int id, String newPIN){
        try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(users)) {

            while (rs.next()){
                if(rs.getInt(1) == id){
                    rs.updateString(5,newPIN);
                    rs.updateRow();
                    break;
                }
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean registerUser(String name, String email, String phone, String pin) {
        boolean Registered =false;
        if(checkIfMobileNumberExists(phone) == null){

            try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
                 Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = stmt.executeQuery(users)) {
                System.out.println("tyow");
                rs.moveToInsertRow();
                rs.updateString(2, name);
                rs.updateString(3, email);
                rs.updateString(4, phone);
                rs.updateString(5, pin);
                rs.insertRow();

                rs.last();
                int newUserID = rs.getInt(1);
                createUserBalance(newUserID);
                Registered = true;
                con.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else{
            ControlManager.notif("Phone Number already Exist!", Alert.AlertType.ERROR);
        }

        return Registered;
    }

    private static void createUserBalance(int newUserID) {
        try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(balance)) {

            rs.moveToInsertRow();
            float amount = 0.0f;
            rs.updateString(2, String.valueOf(amount));
            rs.updateInt(3, newUserID);
            rs.insertRow();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean CashTransfer(CashTransfer cashTransfer){
        //transaction DBreference {1-ID(int), 2- amount(float),3- name(String),
        // 4- account_ID(int),date {String}, int transferToID, int transferFromID}

        int currentID = cashTransfer.getTransferFromID();
        float tAmount = cashTransfer.getAmount();
        int transferID = cashTransfer.getTransferToID();
        boolean TransactionStatus = false;

    if(setDataBalance(currentID,-tAmount) &&  setDataBalance(transferID,tAmount)) {
        try {
            Connection con = DriverManager.getConnection(url, dbuser, dbpw);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(transaction);

            rs.moveToInsertRow();
            rs.updateString(2, String.valueOf("- " + cashTransfer.getAmount()));
            rs.updateString(3, String.valueOf(cashTransfer.getName()));
            rs.updateInt(4, cashTransfer.getAccount_ID());
            rs.updateString(5, cashTransfer.getDate());
            rs.updateInt(6, cashTransfer.getTransferToID());
            rs.updateInt(7, cashTransfer.getTransferFromID());

            rs.insertRow();

            rs.moveToInsertRow();
            rs.updateString(2, String.valueOf("+ " + cashTransfer.getAmount()));
            rs.updateString(3, String.valueOf(cashTransfer.getName()));
            rs.updateInt(4, cashTransfer.getTransferToID());
            rs.updateString(5, cashTransfer.getDate());
            rs.updateInt(6, cashTransfer.getTransferToID());
            rs.updateInt(7, cashTransfer.getTransferFromID());

            rs.insertRow();
            TransactionStatus = true;

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    else {
        TransactionStatus = false;
    }

        return TransactionStatus;
    }

    public static ArrayList<Transaction> getTransactions(int currentId ,int searchId,int selection){
        ArrayList<Transaction> userHistory = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(transaction)) {

                switch (selection){
                    case 1:
                        System.out.println("CASE 1");

                        while (rs.next()) {
                            if (rs.getInt(4) == currentId) {
                                System.out.println("CASE 1 " + rs.getString(2));
                                Transaction cashTransfer = new Transaction(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getInt(4),
                                        rs.getString(5),
                                        rs.getInt(6),
                                        rs.getInt(7));
                                userHistory.add(cashTransfer);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("CASE 2");

                        while (rs.next()) {
                            if (rs.getInt(4) == currentId) {
                                if(rs.getInt(7) == searchId){
                                    Transaction cashTransfer = new Transaction(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getInt(4),
                                        rs.getString(5),
                                        rs.getInt(6),
                                        rs.getInt(7));
                                userHistory.add(cashTransfer);
                                }
                            }
                        }

                        break;
                    case 3:

                        while (rs.next()) {
                            if (rs.getInt(4) == currentId) {
                                if(rs.getInt(1) == searchId){
                                    Transaction cashTransfer = new Transaction(
                                            rs.getInt(1),
                                            rs.getString(2),
                                            rs.getString(3),
                                            rs.getInt(4),
                                            rs.getString(5),
                                            rs.getInt(6),
                                            rs.getInt(7));
                                    userHistory.add(cashTransfer);
                                }
                            }
                        }
                        break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return userHistory;
    }

    public static float getBalance(int id){
        float bal = 0.0f;
        try {
            Connection con = DriverManager.getConnection(url, dbuser, dbpw);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(balance);
            while (rs.next()) {
                if (rs.getString(3).equals(String.valueOf(id))) {
                    bal = Float.parseFloat(rs.getString(2));
                    break;
                }
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    return bal;
    }

    public static boolean setDataBalance(int id, float amount) {
        boolean exceed = false;
        try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(balance)) {

            while (rs.next()) {
                if (rs.getInt(3) == id) {

                    BigDecimal dataAmount = new BigDecimal(rs.getString(2));
                    BigDecimal newAmount = dataAmount.add(BigDecimal.valueOf(amount));
                    System.out.println("amount1 " + amount);
                    System.out.println("newAmount " + newAmount);
                    System.out.println("amount2 " + amount);
                    // Check if the new amount exceeds the limit
                    if (isAmountExceedingLimit(newAmount.floatValue())) {
                        System.out.println("test1 ");
                        exceed = false;
                        break;
                    } else {
                        // Format the new amount to 2 digits after the decimal
                        BigDecimal formattedAmount = newAmount.setScale(2, RoundingMode.HALF_UP);
                        rs.updateString(2, formattedAmount.toString());
                        rs.updateRow();
                        System.out.println("test2 ");

                        exceed = true;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return exceed;
    }

    public static UserAuthentication checkIfMobileNumberExists(String mobileNumber) {

        UserAuthentication currentUser1 = null;
        try {
            Connection con = DriverManager.getConnection(url, dbuser, dbpw);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(users);
            while (rs.next()) {
                if (rs.getString(4).equals(mobileNumber)) {
                    System.out.println("gg");
                    UserAuthentication userAuthentication = new UserAuthentication(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5));
                    currentUser1 = userAuthentication;
                            break;
                }
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return currentUser1;
    }

    private static boolean isAmountExceedingLimit(float amount) {
        boolean exceeding = false;
        String[] parts = String.valueOf(amount).split("\\.");
        if (parts[0].length() > 6) {
            System.out.println("womp womp");
             exceeding = true;
        }
        if (parts.length > 1 && parts[1].length() > 2) {
            System.out.println("yonk yonk");
             exceeding = true;
        }

        return exceeding;
    }

    static void showAllStudent() {
        displayResults(users, 5);
        displayResults(balance, 3);
    }

    private static void displayResults(String query, int columnCount) {
        try (Connection con = DriverManager.getConnection(url, dbuser, dbpw);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                StringBuilder output = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    output.append(rs.getString(i)).append("\t");
                }
                // Remove the last tab character
                System.out.println(output.substring(0, output.length() - 1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    private static String formatAmount(float amount) {
//        String[] parts = String.valueOf(amount).split("\\.");
//        if (parts.length > 0 && parts[0].length() > 6) {
//            parts[0] = parts[0].substring(0, 6);
//        }
//        if (parts.length > 1 && parts[1].length() > 2) {
//            parts[1] = parts[1].substring(0, 2);
//        }
//        return parts.length > 1 ? String.join(".", parts) : parts[0];
//    }
}
