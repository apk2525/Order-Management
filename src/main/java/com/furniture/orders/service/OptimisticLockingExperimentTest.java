package com.furniture.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OptimisticLockingExperimentTest {
    @Autowired
    private OptimisticLockingExperimentService experimentService;

    @Test
    void shouldDetectOptimisticLockingConflict() throws Exception{
        CountDownLatch bothTransactionsReady = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<?> requestA = executorService.submit(
                () -> experimentService.updateQuantity(
                        1L, 25, bothTransactionsReady)
        );

        Future<?> requestB = executorService.submit(
                () -> experimentService.updateQuantity(
                        1L, 30, bothTransactionsReady)
        );

        try {
            int successCount = 0;
            int conflictCount = 0;

            for (Future<?> request : new Future<?>[]{requestA, requestB}) {
                try {
                    request.get(10, TimeUnit.SECONDS);
                    successCount++;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                        conflictCount++;
                    } else {
                        throw e;
                    }
                }
            }

            assertEquals(1, successCount);
            assertEquals(1, conflictCount);

        } finally {
            executorService.shutdownNow();
        }

    }
}
