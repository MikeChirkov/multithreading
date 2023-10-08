package ru.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        List<Future<Integer>> futureList = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(25);

        long startTs = System.currentTimeMillis(); // start time

        for (String text : texts) {
            Callable<Integer> myCallable = new MyCallable(text);
            Future<Integer> newFuture = threadPool.submit(myCallable);
            futureList.add(newFuture);
        }

        List<Integer> listOfMax = new ArrayList<>();
        for (Future<Integer> future : futureList) {
            listOfMax.add(future.get());
        }

        threadPool.shutdown();

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Max: " + Collections.max(listOfMax));
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}