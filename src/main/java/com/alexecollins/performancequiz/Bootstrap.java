package com.alexecollins.performancequiz;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: alexec (alex.e.c@gmail.com)
 */
public class Bootstrap {
    public static final Logger LOGGER = Logger.getLogger(Bootstrap.class);
    public static void main(String[] args) throws Exception {

        LOGGER.info("starting up...");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);

        Callable<Customer> callable = new Callable<Customer>() {
            private final CustomerFactory customerFactory = new CustomerFactory();

            public Customer call() {
                return customerFactory.createCustomer();
            }
        };

        while (true) {
            int n = 500000;
            List<Future<Customer>> futures = new ArrayList<Future<Customer>>();
            for (int i = 0; i < n; i++) {
                futures.add(executorService.submit(callable));

                int x = futures.size();

                if (x  % (n / 10) == 0) {
                    LOGGER.info(x * 100 / n + "% created");
                }
            }

            LOGGER.info("awaiting completion...");


            File tempFile = File.createTempFile("customers", ".ser");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tempFile));

            LOGGER.info("writing to <" + tempFile + ">");

            int x = 0;
            for (Future<Customer> f : futures) {
                out.writeObject(f.get());
                x++;

                if (x  % (n / 10) == 0) {
                    LOGGER.info(x * 100 / n + "% written");
                }
            }

            out.close();

            LOGGER.info("done");
        }
    }
}
