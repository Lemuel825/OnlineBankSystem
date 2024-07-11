package com.example.onlinebanksystem.GcashApp;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

import static com.example.onlinebanksystem.DatabaseManager.getTransactions;

public class Transaction {
    private SimpleIntegerProperty transactionID;
    private SimpleStringProperty transactionAmount;
    private SimpleStringProperty transactionName;
    private SimpleIntegerProperty transactionAccountID;
    private SimpleStringProperty transactionDate;
    private SimpleIntegerProperty transactionTransferToID;
    private SimpleIntegerProperty transactionTransferFromID;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", transactionAmount=" + transactionAmount +
                ", transactionName=" + transactionName +
                ", transactionAccountID=" + transactionAccountID +
                ", transactionDate=" + transactionDate +
                ", transactionTransferToID=" + transactionTransferToID +
                ", transactionTransferFromID=" + transactionTransferFromID +
                '}';
    }

    public Transaction(int transactionID, String amount,
                       String name, int accountID, String date, int colTransferToID, int colTransferFromID) {

        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.transactionAmount = new SimpleStringProperty(amount);
        this.transactionName = new SimpleStringProperty(name);
        this.transactionAccountID = new SimpleIntegerProperty(accountID);
        this.transactionDate = new SimpleStringProperty(date) ;
        this.transactionTransferToID = new SimpleIntegerProperty(colTransferToID);
        this.transactionTransferFromID = new SimpleIntegerProperty(colTransferFromID);
    }

    public int getTransactionID() {
        return transactionID.get();
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = new SimpleIntegerProperty(transactionID);
    }

    public String getTransactionAmount() {
        return transactionAmount.get();
    }

    public void setTransactionAmount(String amount) {
        this.transactionAmount = new SimpleStringProperty(amount);
    }

    public String getTransactionName() {
        return transactionName.get();
    }

    public void setTransactionName(String name) {
        this.transactionName = new SimpleStringProperty(name);
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public void setTransactionDate(String date) {
        this.transactionDate = new SimpleStringProperty(date);
    }

    public int getTransactionTransferToID() {
        return transactionTransferToID.get();
    }

    public void setTransactionTransferToID(int colTransferToID) {
        this.transactionTransferToID = new SimpleIntegerProperty(colTransferToID);
    }

    public int getTransactionTransferFromID() {
        return transactionTransferFromID.get();
    }

    public void setTransactionTransferFromID(int colTransferFromID) {
        this.transactionTransferFromID = new SimpleIntegerProperty(colTransferFromID);
    }

    public static ArrayList<Transaction> getTransactionHistory(int currentid,int id,int selection){
        return getTransactions(currentid,id,selection);
    }
}
