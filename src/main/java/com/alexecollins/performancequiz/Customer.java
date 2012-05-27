package com.alexecollins.performancequiz;

import java.io.Serializable;

/**
 * @author: alexec (alex.e.c@gmail.com)
 */
public class Customer implements Serializable {

    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
