package com.example.onlinebanksystem.GcashApp;

import com.example.onlinebanksystem.DatabaseManager;

public class CheckBalance extends DatabaseManager {
    private int ID;
    private float amount;
    private int user_ID;

    public CheckBalance(int ID, float amount, int user_ID) {
        this.ID = ID;
        this.amount = amount;
        this.user_ID = user_ID;
    }

    public CheckBalance(int id) {
        super();
    }

    public int getID() {return ID;}

    public void setID(int ID) {this.ID = ID;}

    public float getAmount() {return amount;}

    public void setAmount(float amount) {this.amount = amount;}

    public int getUser_ID() {return user_ID;}

    public void setUser_ID(int user_ID) {this.user_ID = user_ID;}

    public static float checkBalance(int id){
        return getBalance(id);
    }

//    public static void cashIn(int id, String amount){
//        setDataBalance(id,amount);
//    }
}
