package com.alexecollins.performancequiz;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author: alexec (alex.e.c@gmail.com)
 */
public class Bootstrap {
    public static final Logger LOGGER = Logger.getLogger(Bootstrap.class);
    public static void main(String[] args) throws Exception {

        LOGGER.info("starting up...");

        ExecutorService executorService = Executors.newFixedThreadPool(16);

        Callable<Customer> callable = new Callable<Customer>() {
            private final CustomerFactory customerFactory = new CustomerFactory();

            public Customer call() {
                return customerFactory.createCustomer();
            }
        };

        Set<Customer> customers = new HashSet<Customer>();

        for (int j = 0; j < 50; j++) {
            long startTime = System.currentTimeMillis();

            int n = 50000;
            List<Future<Customer>> futures = new ArrayList<Future<Customer>>();
            for (int i = 0; i < n; i++) {
                futures.add(executorService.submit(callable));

                int x = futures.size();

                if (x  % (n / 10) == 0) {
                    LOGGER.info(x * 100 / n + "% created");
                }
            }

            File tempFile = File.createTempFile("customers", ".ser");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));

            LOGGER.info("writing to <" + tempFile + ">");

            int x = 0;
            for (Future<Customer> f : futures) {
                Customer customer = f.get();
                customers.add(customer);
                out.writeObject(customer);
                x++;

                if (x  % (n / 10) == 0) {
                    LOGGER.info(x * 100 / n + "% written");
                }
            }

            out.close();

            LOGGER.info("iteration " + j +", took " + (System.currentTimeMillis() - startTime) + "ms");
        }

        executorService.shutdown();

    }
}
