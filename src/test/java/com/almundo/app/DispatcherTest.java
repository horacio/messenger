package com.almundo.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for Dispatcher class.
 */
 public class DispatcherTest extends TestCase {
   public DispatcherTest(String testName) {
     super(testName);
   }

   public static Test suite() {
     return new TestSuite(DispatcherTest.class);
   }

   public void testMultithreadedDispatchCall() {
        List<Integer> threadCounts = Arrays.asList(20);

        for (final int threadCount : threadCounts) {
            System.out.println("Going with: " + threadCount + " threads.");

            Dispatcher dispatcher = new Dispatcher(5, 3, 2);

            Callable<Boolean> phoneCall = () -> {
                final Call call = new Call("A love call");

                dispatcher.dispatchCall(call);

                return true;
            };

            List<Callable<Boolean>> phoneCalls = Collections.nCopies(25, phoneCall);
            ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(threadCount);

            try {
                pool.invokeAll(phoneCalls);

                dispatcher.shutdown();
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("Call interrupted", e);
            }
            finally {
                pool.shutdownNow();
            }

            System.out.println("Ended with: " + threadCount + " threads.");
        }
    }
 }
