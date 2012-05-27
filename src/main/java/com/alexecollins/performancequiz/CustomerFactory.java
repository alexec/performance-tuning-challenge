package com.alexecollins.performancequiz;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * @author: alexec (alex.e.c@gmail.com)
 */
public class CustomerFactory {

    public static final Logger LOGGER = Logger.getLogger(CustomerFactory.class);

    public static final Random RANDOM = new Random();

    public Customer createCustomer() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            name.append(RANDOM.nextInt(8));
        }

        Customer customer = new Customer(name.toString());

        LOGGER.debug("created " + customer);

        return customer;
    }
}
