package huongbien.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static int randomNumber(int min, int max) {
        // Ensures bounds are valid
        if (min > max) {
            throw new IllegalArgumentException("Min must be less than or equal to max");
        }

        // ThreadLocalRandom provides better randomization than Math.random()
        // and performs better in concurrent environments
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static byte[] readImage(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] suggestMoneyReceived(int totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total Amount must be greater than 0 dong");
        }

        int[] denominations = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};

        ArrayList<Integer> suggestions = new ArrayList<>();

        if (totalAmount >= 1000) {
            suggestions.add((int) Math.ceil(totalAmount / 1000.0) * 1000);
        }

        int currentAmount = totalAmount;

        for (int denomination : denominations) {
            if (currentAmount < denomination) {
                suggestions.add(denomination);
                continue;
            }

            currentAmount = ((totalAmount + denomination - 1) / denomination) * denomination;

            if (suggestions.contains(currentAmount)) continue;

            suggestions.add(currentAmount);
        }

        return suggestions.stream().mapToInt(Integer::intValue).toArray();
    }
}
