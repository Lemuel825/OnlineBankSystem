package com.example.onlinebanksystem.GcashApp;

import com.example.onlinebanksystem.ControlManager;
import com.example.onlinebanksystem.DatabaseManager;

public class CashIn extends DatabaseManager {

    public static boolean cashIn(int id, float amount){
        return setDataBalance(id,amount);
    }
}
