package com.example.arjunchopra.service;

/**
 * Created by arjunchopra on 01/01/18.
 */

public class Name {
    private String name;
    private int status;

    public Name(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
