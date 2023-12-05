package ru.netology;

import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {

                String text = generateText("abaca", 100);
                int aFound = (int) IntStream
                        .range(0, text.length())
                        .filter((c) -> {
                            return 'a' == text.charAt(c);
                        }).count();

                synchronized (sizeToFreq) {
                    int aCount = sizeToFreq.getOrDefault(aFound, 0);
                    sizeToFreq.put(aFound, aCount + 1);
                }
            });
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            thread.join();
        }

        System.out.println("Самая частая частота " + maxFreq(sizeToFreq, 100)
                + " встретилась " + sizeToFreq.get(maxFreq(sizeToFreq, 100)) + " раз.");


        IntStream.range(1, 100).filter(sizeToFreq::containsKey)
                .mapToObj(c -> "Частота " + c + " повторяется " + sizeToFreq.get(c) + " раз.")
                .forEach(System.out::println);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxFreq(Map<Integer, Integer> valueMap, int lenght) {

        int key = 1;
        int maxValue = 0;
        for (int i = 1; i < lenght; i++) {
            if (valueMap.containsKey(i)) {
                if (valueMap.get(i) > maxValue) {
                    maxValue = valueMap.get(i);
                    key = i;
                }
            }
        }

        return key;
    }

}
