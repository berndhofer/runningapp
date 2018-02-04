package com.example.ddb.runningapp.service;

import android.content.Context;

public class ServiceInstantiator {
    private static RouteService instance = null;

    public static RouteService getInstance(Context context) {
        if(instance == null) {
            instance = new RouteService(context);
        }
        return instance;
    }
}