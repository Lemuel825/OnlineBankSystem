package com.example.onlinebanksystem.GcashApp;

import com.example.onlinebanksystem.DatabaseManager;

public class CashTransfer extends DatabaseManager {
    private float amount;
    private String name;
    private int account_ID;
    private String date;
    private int transferToID;
    private int transferFromID;

    public CashTransfer(float amount, String name, int account_ID, String date, int transferToID, int transferFromID) {
        this.amount = amount;
        this.name = name;
        this.account_ID = account_ID;
        this.date = date;
        this.transferToID = transferToID;
        this.transferFromID = transferFromID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccount_ID() {
        return account_ID;
    }

    public void setAccount_ID(int account_ID) {
        this.account_ID = account_ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTransferToID() {
        return transferToID;
    }

    public void setTransferToID(int transferToID) {
        this.transferToID = transferToID;
    }

    public int getTransferFromID() {
        return transferFromID;
    }

    public void setTransferFromID(int transferFromID) {
        this.transferFromID = transferFromID;
    }

    public static boolean transferCash(CashTransfer cashTransfer){
        return DatabaseManager.CashTransfer(cashTransfer);
    }
}
