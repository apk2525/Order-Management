package com.furniture.orders.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PessimisticLockingExperimentTest {

    @Autowired
    private OrderCreationService orderCreationService;

    @Test
    void shouldMakeSecondTransactionWaitForRowLock() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            long startTime = System.currentTimeMillis();

            Future<?> requestA = executor.submit(
                    () -> orderCreationService.updateQuantityWithLock(1L, 40)
            );

            Future<?> requestB = executor.submit(
                    () -> orderCreationService.updateQuantityWithLock(1L, 50)
            );

            requestA.get(20, TimeUnit.SECONDS);
            requestB.get(20, TimeUnit.SECONDS);

            long elapsed = System.currentTimeMillis() - startTime;

            System.out.println("Total elapsed time: " + elapsed + " ms");

            assertTrue(elapsed >= 9000,
                    "Expected transactions to execute sequentially due to row locking");

        } finally {
            executor.shutdownNow();
        }
    }
}