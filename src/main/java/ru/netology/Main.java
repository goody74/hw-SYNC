package ru.netology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static int aFound;
    private static int aCount;

    public static void main(String[] args) throws InterruptedException {
        IntStream.range(0, 1000).forEach(i -> new Thread(() -> {

            String text = generateText("abaca", 100);
            aFound = (int) IntStream
                    .range(0, text.length())
                    .filter((c) -> {
                        return 'a' == text.charAt(c);
                    }).count();

            synchronized (sizeToFreq) {
                aCount = sizeToFreq.getOrDefault(aFound, 0);
                sizeToFreq.notify();
            }

            synchronized (sizeToFreq) {
                sizeToFreq.put(aFound, aCount + 1);
                sizeToFreq.notify();
            }
        }).start());

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
