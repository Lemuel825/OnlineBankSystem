package com.example.onlinebanksystem.GcashApp;

import com.example.onlinebanksystem.DatabaseManager;

public class UserAuthentication extends DatabaseManager {

    private int userId;
    private String Name;
    private String Email;
    private String Number;
    private String PIN;

    public UserAuthentication(int userId, String name, String email, String number, String PIN) {
        this.userId = userId;
        Name = name;
        Email = email;
        Number = number;
        this.PIN = PIN;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getNumber() {
        return Number;
    }
    public void setNumber(String number) {
        Number = number;
    }
    public String getPIN() {
        return PIN;
    }
    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public static UserAuthentication Login(String number, String pin) {
        return  checkUser(number,pin);
    }
    
    public static boolean Register(String name, String email, String phone, String pin){
        return registerUser(name,email,phone,pin);
    }

    public static UserAuthentication SearchNumberAccount(String mobileNumber){
        return checkIfMobileNumberExists(mobileNumber);
    }

    //public static UserAuthentication Search()
}
